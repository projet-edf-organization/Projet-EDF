package presentation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import abstraction.Etude;
import abstraction.autres.Critere;
import abstraction.autres.Evenement;
import abstraction.autres.NiveauDeMetrique;
import abstraction.modules.CriteresDeSecurite;
import abstraction.modules.EvenementsRedoutes;
import abstraction.modules.Metriques;
import presentation.FenetreCriteresDeSecurite.ModeleDynamiqueObjet;

public class FenetreEvenementsRedoutes extends JPanel{
	/*Cette classe fait appel aux variables d'instance suivantes pour afficher le tableau des Evenements Redout�s
	 * 
	 */
	
	
	
	private ModeleEvenementsRedoutes modele;
	private Metriques metriques;
	private EvenementsRedoutes evenements;
	private JTableX tableau;
	private Etude etude;
	private CriteresDeSecurite criteres;
	private ArrayList<TableCellEditor> editors=new ArrayList<TableCellEditor>();
	private JTextArea zoneDescription;
	private JFrame petiteFenetre;
	private JTextArea textAreaPetiteFenetre; 

	
	FenetreEvenementsRedoutes(EvenementsRedoutes evenements){
		 super(new GridLayout(1,0));
		 /* On recr�e un module pour �tre sur que les donn�es sont � jour*/
		 
		
		this.modele=new ModeleEvenementsRedoutes(evenements);
		
		this.etude=evenements.getEtude();
		
		this.metriques=(Metriques)this.etude.getModule("Metriques");
		
		this.criteres=(CriteresDeSecurite) this.etude.getModule("CriteresDeSecurite");
		
		this.evenements=evenements;
		
		
		this.setUpComBo();
		
		this.tableau.addMouseListener(new MouseListener(){
		
		public void mousePressed(MouseEvent e) {
			
			
			petiteFenetre.setVisible(false);
			if(SwingUtilities.isRightMouseButton(e)){
				selectionnerLaLigne(e);
				setPetiteFenetre();
			}
		}
		public void mouseReleased(MouseEvent e) {
			
		}
		public void mouseClicked(MouseEvent arg0) {
			
			
		}
		public void mouseEntered(MouseEvent arg0) {
			
			
		}
		public void mouseExited(MouseEvent arg0) {
			
			
		}});
		tableau.setPreferredScrollableViewportSize(new Dimension(500, 70));
        tableau.setFillsViewportHeight(true);
		
        JScrollPane scrollPane = new JScrollPane(tableau);
		this.tableau.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
		
		this.add(scrollPane,BorderLayout.WEST);

		
		CellRendererEv renderer=new CellRendererEv();
		
		this.tableau.getColumnModel().getColumn(tableau.getColumnCount()-1).setCellRenderer( renderer);
		this.tableau.getColumnModel().getColumn(tableau.getColumnCount()-2).setCellRenderer( renderer);
		this.tableau.getColumnModel().getColumn(tableau.getColumnCount()-5).setCellRenderer( renderer);
		this.tableau.setRowHeight(50);
		
		tableau.setFont(new Font("Arial", Font.PLAIN, 15));
		
		this.creerZoneDescription();
		
		
		this.setVisible(true);
		
		this.evenements.setCree(true);
		
		
		
	    
		
		
		
	}
	
	
	/*Methode qui permet de construire le tableau ainsi que les Jcombobox associ�es � la Gravit� et � l'Exigence*/
	
	public void setUpComBo() {
 
		
		this.tableau=new JTableX(modele);
		
		this.creerPetiteFenetre();
		
		tableau.setRowSelectionAllowed(false);
        tableau.setColumnSelectionAllowed(false);
		
		RowEditorModel rm = new RowEditorModel();
		
		tableau.setRowEditorModel(rm);
	
	/*On construit la JCombobox associ�e � l'exigence de chaque crit�re du tableau*/
		
		int u=this.criteres.getCriteresRetenus().size();
		
		
		for(int i=0;i<u;i++){
			
			ArrayList<Critere> tableaucriteres=new ArrayList<Critere>(this.criteres.getCriteresRetenus());
			String nomcritere=tableaucriteres.get(i).getIntitule();	
			
			
			int b=((Metriques)this.etude.getModule("Metriques")).getMetrique(nomcritere).nombreDeNiveaux();
			String[] listebis=new String[b];
	    
			for(int j=1;j<=b;j++){
	    	
	    	
			listebis[j-1]=""+j;
		}
	    JComboBox comboBoxex=new JComboBox(listebis);
	    DefaultCellEditor ed = new DefaultCellEditor(comboBoxex);
	    for (int k=0;k<modele.getRowCount();k++){
	    	if(evenements.getEvenementsRedoutes().get(k).getNomCritere()==nomcritere){
	    		rm.addEditorForRow(k,ed);
	    	}
	    }
	    
		
		}
		
		this.tableau.getColumnModel().getColumn(modele.getColumnCount()-2).setPreferredWidth(100);
		this.tableau.getColumnModel().getColumn(modele.getColumnCount()-3).setPreferredWidth(150);
		this.tableau.getColumnModel().getColumn(modele.getColumnCount()-4).setPreferredWidth(150);
		this.tableau.getColumnModel().getColumn(modele.getColumnCount()-5).setPreferredWidth(250);
		
		/*Puis on construit la JCombobox associ�e � la Gravit�*/
		
       int a=((Metriques) this.etude.getModule("Metriques")).getMetrique("Gravite").nombreDeNiveaux();
		
		String[] liste=new String[a];
		
		for(int i=1;i<=a;i++){
			
			liste[i-1]=""+i;
		}
        
	
	TableColumn gravColumn =this.tableau.getColumnModel().getColumn(modele.getColumnCount()-1);
	final JComboBox comboBoxgrav=new JComboBox(liste);
	
	gravColumn.setCellEditor(new DefaultCellEditor(comboBoxgrav));
	
	
	gravColumn.setPreferredWidth(100);
	gravColumn.setMaxWidth(250);
	
	/*TableColumn nameColumn =this.tableau.getColumnModel().getColumn(modele.getColumnCount()-5);
	 DefaultTableCellRenderer renderer =
             new DefaultTableCellRenderer();
     renderer.setToolTipText("Appuyez sur entr�e pour confirmer le nom de l'�v�nement une fois qu'il a �t� �crit");
     nameColumn.setCellRenderer(renderer);*/
	
	}
	
	private Evenement getEvenementSelectionne(){
		Evenement e;
		try{
			e = evenements.getEvenementsRedoutes().get(tableau.getSelectedRow());
		}
		catch(ArrayIndexOutOfBoundsException h){
			e = evenements.getEvenementsRedoutes().get(0);
		}
		return e;
	}
		
		
	
	 
	public void creerZoneDescription(){
		JLabel label = new JLabel("");
		this.zoneDescription= new JTextArea("Cliquer sur un critère dont vous souhaitez afficher la description de l'exigence et la gravité associés aux niveaux sélectionnés");
		
		Font font=new Font("Verdana",Font.BOLD,12);
		
		this.zoneDescription.setFont(font);
		
		this.zoneDescription.setLineWrap(true); // On passe à la ligne 
		this.zoneDescription.setWrapStyleWord(true);
		
		tableau.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e) {
			    
			      JTableX target = (JTableX)e.getSource();
			      System.out.println("bonjour");
			      int row = target.getSelectedRow();
			      int column = target.getSelectedColumn();
			      if(column==target.getColumnCount()-3){
			    	  
			    	  
			    	 
			    	 String intitulecritere= (String)tableau.getValueAt(row, target.getColumnCount()-3);
			    	
			     
			    	
			    	 NiveauDeMetrique niveaugravite=metriques.getMetrique("Gravite").getNiveau((Integer)tableau.getValueAt(row, column+2)-1);
			    	 
			    	 NiveauDeMetrique niveaucourant=metriques.getMetrique(intitulecritere).getNiveau((Integer)tableau.getValueAt(row, column+1)-1);
			    	 
			      zoneDescription.setText("Description du niveau d'exigence associé au critère sélectionné: "+niveaucourant.getDescription()+System.getProperty("line.separator")+"Description du niveau de gravité: "+niveaugravite.getDescription());}
			    
			  }
			});
		
		JScrollPane areaScrollPane = new JScrollPane(this.zoneDescription);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(400, 150));
		areaScrollPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Sélectionnez un critère pour afficher la description de l'exigence et de la gravité associés au critère"),
								BorderFactory.createEmptyBorder(5,5,5,5)),
								areaScrollPane.getBorder()));
		
		//this.add(label);
		this.add(areaScrollPane,BorderLayout.CENTER);
	}
	
	protected void selectionnerLaLigne(MouseEvent e) {
		// get the coordinates of the mouse click
		Point p = e.getPoint();

		// get the row and column indexes that contains that coordinate
		int rowNumber = tableau.rowAtPoint(p);
		int colNumber = tableau.columnAtPoint(p);

		tableau.changeSelection(rowNumber, colNumber, false, true);

	}

	private void creerPetiteFenetre() {
		this.petiteFenetre = new JFrame("Détails de la cellule");
		this.creerTextAreaPetiteFenetre();
		petiteFenetre.add(textAreaPetiteFenetre);	
		petiteFenetre.setMaximumSize(new Dimension(1000,1000));
		petiteFenetre.setMinimumSize(new Dimension(300,0));		
	}

	private void creerTextAreaPetiteFenetre() {
		this.textAreaPetiteFenetre = new JTextArea("Laul");		
		textAreaPetiteFenetre.setEditable(false);
		textAreaPetiteFenetre.setFont(new Font("Arial", Font.PLAIN, 17));
		textAreaPetiteFenetre.setLineWrap(true);
		textAreaPetiteFenetre.setWrapStyleWord(true);		
	}

	protected void setPetiteFenetre() {
		int row = this.tableau.getSelectedRow();
		int col = this.tableau.getSelectedColumn();
		if(row != -1 && col != -1){
			ModeleEvenementsRedoutes modele = (ModeleEvenementsRedoutes) tableau.getModel();
			String contenuCellule = modele.getValueAt(row, col).toString();
			System.out.println(contenuCellule);
			this.textAreaPetiteFenetre.setText(contenuCellule);
			petiteFenetre.pack();
			petiteFenetre.pack();
			Point positionSouris = MouseInfo.getPointerInfo().getLocation();
			int xSouris = (int) positionSouris.getX();
			int ySouris = (int) positionSouris.getY();
			Point positionDeLaFenetre = new Point(xSouris - 1, ySouris + 1);
			petiteFenetre.setLocation(positionDeLaFenetre);
			petiteFenetre.setVisible(true);	
		}			
	}

	
}

