package presentation;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.TableColumn;

import presentation.FenetreBiensSupports.Renderer;
import controle.TypologieBiensSupports.ControlJButtonAide;
import controle.TypologieBiensSupports.ControlJButtonAjoutTypeBien;
import controle.TypologieBiensSupports.ControlJButtonSuppressionTypeBien;
import controle.TypologieBiensSupports.ControlJTable;
import controle.TypologieBiensSupports.ControlJTextArea;
import abstraction.modules.TypologieDesBiensSupports;

public class FenetreTypologieBiensSupports extends JPanel {

	private TypologieDesBiensSupports moduleCourant;
	private ModeleTypologieBiensSupports modeleTableau ;
	private CellRendererTypesBiens rendererTypeBien ;
	private JTable tableau ;
	private JTextArea zoneDescription ;
	private JButton supprimerLigne ;
	private JButton ajouterLigne ;
	private JButton aide ;
	private JFrame petiteFenetre;
	private JTextArea textAreaPetiteFenetre;

	public FenetreTypologieBiensSupports(TypologieDesBiensSupports module) {
		
		this.moduleCourant=module;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.creerPetiteFenetre();
		this.creerTableau();
		//this.creerCouleurs();
		this.creerZoneDescription();
		this.creerBoutonsBas();
	}
	
	public void creerPetiteFenetre(){
		this.petiteFenetre = new JFrame("Détail de la cellule");
		this.creerTextAreaPetiteFenetre();
		this.petiteFenetre.add(textAreaPetiteFenetre);
		petiteFenetre.setMaximumSize(new Dimension(1000,1000));
		petiteFenetre.setMinimumSize(new Dimension(300,0));
		this.petiteFenetre.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.petiteFenetre.pack();
	}
	
	public void creerTextAreaPetiteFenetre() {
		this.textAreaPetiteFenetre = new JTextArea("");		
		textAreaPetiteFenetre.setEditable(false);
		textAreaPetiteFenetre.setFont(new Font("Arial", Font.PLAIN, 17));
		textAreaPetiteFenetre.setLineWrap(true);
		textAreaPetiteFenetre.setWrapStyleWord(true);
	}

	public void creerTableau() {
		this.modeleTableau=new ModeleTypologieBiensSupports(this.moduleCourant);
		this.tableau = new JTable(this.modeleTableau);
		
		this.rendererTypeBien=new CellRendererTypesBiens(this.moduleCourant,modeleTableau);
		this.tableau.setDefaultRenderer(Object.class, rendererTypeBien);
		
		ControlJTable control = new ControlJTable(modeleTableau, tableau, petiteFenetre); // PAC
		this.tableau.addMouseListener(control);                            // PAC
		this.tableau.addKeyListener(control);                              // PAC
	
		tableau.getColumnModel().getColumn(tableau.getColumnModel().getColumnCount()-1).setMaxWidth(50);
		this.tableau.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableau.setFont(new Font("Arial", Font.PLAIN, 15));
		tableau.setRowHeight(50);
		
		//tableau.getColumnModel().getColumn(2).setPreferredWidth(1000);
		this.add(tableau.getTableHeader());
        this.add(new JScrollPane(tableau));
	}
	
	public void creerCouleurs(){
		for(int i =0 ; i< this.modeleTableau.getColumnCount() - 1 ; i++){
			TableColumn colonneId = this.tableau.getColumnModel().getColumn(i);
			colonneId.setCellRenderer(this.rendererTypeBien);
		}
		
	}
	
	public void creerZoneDescription(){
		JLabel label = new JLabel("");
		this.zoneDescription= new JTextArea("Cliquer sur la description que vous souhaitez afficher");
		this.zoneDescription.setLineWrap(true); // On passe à la ligne 
		this.zoneDescription.setWrapStyleWord(true);
		
		ControlJTextArea controlTextArea = new ControlJTextArea(modeleTableau, tableau, zoneDescription); // PAC
		this.moduleCourant.addObserver(controlTextArea);                                                  // PAC
		this.zoneDescription.addKeyListener(controlTextArea);
		
		JScrollPane areaScrollPane = new JScrollPane(this.zoneDescription);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(400, 150));
		areaScrollPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Description du type de bien support"),
								BorderFactory.createEmptyBorder(5,5,5,5)),
								areaScrollPane.getBorder()));
		
		this.add(label);
		this.add(areaScrollPane);
	}
	
	public void creerBoutonsBas() {
		JPanel panelBas = new JPanel() ;
		panelBas.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		this.ajouterLigne = new JButton("Ajouter un type de bien support");
		
		ControlJButtonAjoutTypeBien ControlAjoutTypeBien = new ControlJButtonAjoutTypeBien(this.modeleTableau,null);
		this.ajouterLigne.addActionListener(ControlAjoutTypeBien );
		
		this.supprimerLigne = new JButton("Supprimer un type de bien support"); 
		
		ControlJButtonSuppressionTypeBien controlSuppressionTypeBien = new ControlJButtonSuppressionTypeBien(
				modeleTableau, tableau, this.supprimerLigne);                                                 // PAC
		this.moduleCourant.addObserver(controlSuppressionTypeBien);                                           // PAC
		this.supprimerLigne.addActionListener(controlSuppressionTypeBien);                                    // PAC
		
		this.aide = new JButton("?");
		
		ControlJButtonAide controlAide = new ControlJButtonAide(
				this.aide);
		this.aide.addActionListener(controlAide);
		
		panelBas.add(ajouterLigne);
		panelBas.add(supprimerLigne);
		panelBas.add(aide);
		this.add(panelBas);
	}
}
