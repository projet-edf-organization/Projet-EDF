package presentation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

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
import javax.swing.table.AbstractTableModel;

import controle.ControlJButtonAjoutTypeBien;
import controle.ControlJButtonModifierDescription;
import controle.ControlJButtonSuppressionTypeBien;
import controle.ControlJTable;
import controle.ControlJTextArea;
import abstraction.modules.TypologieDesBiensSupports;

public class FenetreTypologieBiensSupports extends JFrame {

	private TypologieDesBiensSupports moduleCourant;
	private ModeleTypologieBiensSupports modeleTableau ;
	private JTable tableau ;
	private JTextArea zoneDescription ;
	private JButton modifierDescription ;
	private JButton supprimerLigne ;
	private JButton ajouterLigne ;

	public FenetreTypologieBiensSupports() {
		super("Typologie des Biens Supports");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		
		this.creerTableau();
		this.creerZoneDescription();
		this.creerBoutonsBas();
		this.pack();
	}

	public void creerTableau() {
		this.modeleTableau=new ModeleTypologieBiensSupports();
		this.moduleCourant=this.modeleTableau.getModuleCourant();
		this.tableau = new JTable(this.modeleTableau);
		
		ControlJTable control = new ControlJTable(modeleTableau, tableau);
		this.tableau.addMouseListener(control);
		
		//tableau.getColumnModel().getColumn(2).setPreferredWidth(1000);
		this.getContentPane().add(tableau.getTableHeader());
        this.getContentPane().add(new JScrollPane(tableau));
	}
	
	public void creerZoneDescription(){
		this.zoneDescription= new JTextArea("Cliquer sur la description que vous souhaitez afficher");
		this.zoneDescription.setLineWrap(true); // On passe à la ligne 
		this.zoneDescription.setWrapStyleWord(true);
		
		ControlJTextArea controlTextArea = new ControlJTextArea(modeleTableau, tableau, zoneDescription);
		this.moduleCourant.addObserver(controlTextArea);
		
		JScrollPane areaScrollPane = new JScrollPane(this.zoneDescription);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(400, 150));
		areaScrollPane.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createCompoundBorder(
								BorderFactory.createTitledBorder("Description du type de bien support"),
								BorderFactory.createEmptyBorder(5,5,5,5)),
								areaScrollPane.getBorder()));
		
		this.getContentPane().add(areaScrollPane,BoxLayout.Y_AXIS);
	}
	
	public void creerBoutonsBas() {
		JPanel panelBas = new JPanel() ;
		panelBas.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		this.ajouterLigne = new JButton("Ajouter un type de bien support");
		
		ControlJButtonAjoutTypeBien ControlAjoutTypeBien = new ControlJButtonAjoutTypeBien(this.modeleTableau,this);
		this.ajouterLigne.addActionListener(ControlAjoutTypeBien );
		
		this.supprimerLigne = new JButton("Supprimer un type de bien support"); 
		
		ControlJButtonSuppressionTypeBien controlSuppressionTypeBien = new ControlJButtonSuppressionTypeBien(
				modeleTableau, tableau, this.supprimerLigne);                                                 // PAC
		this.moduleCourant.addObserver(controlSuppressionTypeBien);                                           // PAC
		this.supprimerLigne.addActionListener(controlSuppressionTypeBien);                                    // PAC
		
		this.modifierDescription = new JButton("Modifier la description");
		
		ControlJButtonModifierDescription controlModifierDescription = new ControlJButtonModifierDescription(
				this.modeleTableau, tableau,this.zoneDescription, modifierDescription);
		this.moduleCourant.addObserver(controlModifierDescription);
		this.modifierDescription.addActionListener(controlModifierDescription);
		
		panelBas.add(ajouterLigne);
		panelBas.add(supprimerLigne);
		panelBas.add(modifierDescription);
		this.getContentPane().add(panelBas);
	}
}
