package presentation;


import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import controle.ScenariosDeMenacesTypes.ControlJButtonAjoutLigne;
import controle.ScenariosDeMenacesTypes.ControlJTable;
import abstraction.modules.ScenariosDeMenacesTypes;

public class FenetreScenarioDeMenacesTypes extends JPanel {
	private ScenariosDeMenacesTypes moduleCourant;
	private ModeleScenarioDeMenacesTypes modeleTableau;
	private JTable tableau ;
	private JComboBox comboBoxIntrinseque ;
	private JButton ajoutLigne ;

	public FenetreScenarioDeMenacesTypes(ScenariosDeMenacesTypes module) {
		
		this.moduleCourant=module;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.creerTableau();
		this.creerComboBox();
		this.creerBoutonsBas();
	}
	
	public void creerTableau() {
		this.modeleTableau= new ModeleScenarioDeMenacesTypes(this.moduleCourant);
		// this.moduleCourant=this.modeleTableau.getModuleCourant();
		this.tableau = new JTable(this.modeleTableau);
		
		ControlJTable controlTableau = new ControlJTable(modeleTableau, tableau);
		this.tableau.addMouseListener(controlTableau);
		
		this.add(tableau.getTableHeader());
        this.add(new JScrollPane(tableau));
	}
	
	public void creerComboBox(){
		
		// Création du JcomboBox pour chaque case de la colonne Vraisemblance intrinsèque
		
		TableColumn colonneIntrinseque = this.tableau.getColumnModel().getColumn(this.modeleTableau.getColumnCount()-3);
		int nbNiveauxIntrinseque = this.modeleTableau.getMetriques().getMetrique("Vraisemblance").nombreDeNiveaux();
		String[] niveauxIntrinseque = new String[nbNiveauxIntrinseque];
		for (int i = 1 ; i<=nbNiveauxIntrinseque ; i++){
			niveauxIntrinseque[i-1]=""+ i;
		}
		this.comboBoxIntrinseque = new JComboBox(niveauxIntrinseque);
		// On gère la couleur des JcomboBox
		CellRendererEv renderer = new CellRendererEv();
		colonneIntrinseque.setCellRenderer(renderer);
		colonneIntrinseque.setCellEditor(new DefaultCellEditor(comboBoxIntrinseque));
		
		// Création du JcomboBox pour chaque case de la colonne Vraisemblance réelle
		
		TableColumn colonneReelle = this.tableau.getColumnModel().getColumn(this.modeleTableau.getColumnCount()-2);
		int nbNiveauxReelle = this.modeleTableau.getMetriques().getMetrique("Vraisemblance").nombreDeNiveaux();
		String[] niveauxReelle = new String[nbNiveauxReelle];
		for (int i = 1 ; i<=nbNiveauxReelle ; i++){
			niveauxReelle[i-1]=""+ i;
		}
		this.comboBoxIntrinseque = new JComboBox(niveauxReelle);
		// On gère la couleur des JcomboBox
		colonneReelle.setCellRenderer(renderer);
		colonneReelle.setCellEditor(new DefaultCellEditor(comboBoxIntrinseque));
		
	}
	
	public void creerBoutonsBas() {
		JLabel label = new JLabel("");
		this.add(label);
		
		this.ajoutLigne=new JButton("Particulariser un scénario générique");
		
		ControlJButtonAjoutLigne controlAjoutLigne = new ControlJButtonAjoutLigne(modeleTableau, tableau, ajoutLigne);
		this.moduleCourant.addObserver(controlAjoutLigne);
		this.ajoutLigne.addActionListener(controlAjoutLigne);
		
		this.add(ajoutLigne);
	}
}
