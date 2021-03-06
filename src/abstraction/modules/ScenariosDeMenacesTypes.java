package abstraction.modules;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;

import abstraction.Etude;
import abstraction.autres.Bien;
import abstraction.autres.Critere;
import abstraction.autres.ScenarioGenerique;
import abstraction.autres.ScenarioType;
import abstraction.autres.SourceDeMenace;
import abstraction.autres.TypeBien;

public class ScenariosDeMenacesTypes extends Module {

	// Represente la bdc
	private static ArrayList<ScenarioType> bdcScenariosMenacesTypes;

	// Variable d'instance
	ArrayList<ScenarioType> tableau;
	private ScenarioType scenarioTypeCourant ;
	// On liste les critères de sécurité retenus dans le module Scénarios de menaces génériques
	private ArrayList<String> nomColonneSup ;
	private Hashtable<String, Bien> biensRetenus ;
	private Hashtable<String, Bien> biensModifies ;

	public ScenariosDeMenacesTypes(Etude etude) {
		super("ScenariosDeMenacesTypes");
		this.etude=etude;
		this.biensRetenus=new Hashtable<String,Bien>();
		this.biensModifies= new Hashtable<String,Bien>();
		
		this.tableau = new ArrayList<ScenarioType>();
		this.nomColonneSup=new ArrayList<String>();
		
		this.predecesseurs.add(this.getEtude().getModule("ScenariosDeMenacesGeneriques"));
		this.predecesseurs.add(this.getEtude().getModule("BiensSupports"));
		this.predecesseurs.add(this.getEtude().getModule("Metriques"));
		this.predecesseurs.add(this.getEtude().getModule("SourcesDeMenaces"));
		
		// this.successeurs.add(this.getEtude().getModule("AnalyseDesRisques"));
		this.scenarioTypeCourant=new ScenarioType();
		this.cree = false;
		this.coherent = false;
		this.disponible = false;
		this.importerBDC();
	}
	
	public int getSize(){
		return this.tableau.size();
	}
	
	public ScenarioType getScenarioTypeCourant() {
		return scenarioTypeCourant;
	}

	public void setScenarioTypeCourant(ScenarioType scenarioTypeCourant) {
		this.scenarioTypeCourant = scenarioTypeCourant;
		this.setChanged();         // PAC
		this.notifyObservers();    // PAC
	}

	public ArrayList<String> getNomColonneSup() {
		return nomColonneSup;
	}

	public void setNomColonneSup(ArrayList<String> nomColonneSup) {
		this.nomColonneSup = nomColonneSup;
	}

	public ArrayList<ScenarioType> getTableau() {
		return tableau;
	}

	public void setTableau(ArrayList<ScenarioType> tableau) {
		this.tableau = tableau;
	}
	
	public Hashtable<String, Bien> getBiensRetenus() {
		return biensRetenus;
	}

	public void setBiensRetenus(Hashtable<String, Bien> biensRetenus) {
		this.biensRetenus = biensRetenus;
	}
	
	public ScenarioType getScenarioType (int i){
		return this.tableau.get(i);
	}
	
	public void addScenarioType(ScenarioType scenario,int indiceInsertion){
		this.tableau.add(indiceInsertion,scenario);
	}
	
	public void removeScenariosTypes(Integer index){
		this.tableau.remove(index);
	}

	public static ArrayList<ScenarioType> getBDC() {
		return bdcScenariosMenacesTypes;
	}
	
	public ArrayList<ScenarioType> getScenariosTypesRetenus() {
		ArrayList<ScenarioType> resultat = new ArrayList<ScenarioType>();
		for (ScenarioType scenario : this.getTableau()) {
			if (scenario.isRetenu()) {
				resultat.add(scenario);
			}
		}
		return resultat;
	}
	
	public Hashtable<String,Boolean> getCriteres(ScenarioGenerique sGene){
		Hashtable<String,Boolean> criteres = new Hashtable<String, Boolean>();
		for (String intitule : sGene.getCriteresSup().keySet()){
			criteres.put(intitule, sGene.getCriteresSup().get(intitule));
		}
		return criteres ;
	}
	
	public ArrayList<SourceDeMenace> getSourcesMenaces(){
		SourcesDeMenaces sourcesDeMenaces = (SourcesDeMenaces) this.etude.getModule("SourcesDeMenaces");
		ArrayList<SourceDeMenace> menaces = new ArrayList<SourceDeMenace>();
		for (SourceDeMenace source : sourcesDeMenaces.getSourcesDeMenacesRetenues() ){
			menaces.add(source);
		}
		return menaces;
	}
	
	public boolean isImpacteCritere(String nomCritere){
		ArrayList<Boolean> critere = new ArrayList<Boolean>();
		for (ScenarioType scenario : this.tableau){
			if (!scenario.getCriteresTypes().get(nomCritere)){
				critere.add(scenario.getCriteresTypes().get(nomCritere));
			}
		}
		return !(critere.size()==this.tableau.size()) ;
	}
	
	// Vérifie si le Bien b est présent dans le tableau
	public Boolean contientBien(Bien b){
		Boolean resultat = false ;
		for (ScenarioType scenario : this.tableau) {
			if (scenario.getBienSupport().getIntitule().equals(b.getIntitule())) {
				resultat=true;
			}
		}
		return resultat ;
	}
	
	// Vérifie si le Bien b est retenu dans le module BiensEssentiels
	public Boolean estRetenuBien (Bien b){
		Boolean res = false ;
		BiensSupports biensSupports = (BiensSupports) this.etude.getModule("BiensSupports");
		for (Bien bien : biensSupports.getBiensRetenus()){
			if (bien.getIntitule().equals(b.getIntitule())){
				res=true;
			}
		}
		return res;
	}
	
	// Vérifie si l'Id du scénario typé provient d'un scénario générique retenu dans le module Scenarios de menaces génériques
	public Boolean estRetenuScenarioGene (ScenarioType scenario){
		Boolean res = false ;
		ScenariosDeMenacesGeneriques moduleScenarioGene = (ScenariosDeMenacesGeneriques) this.etude
				.getModule("ScenariosDeMenacesGeneriques");
		for (ScenarioGenerique sGene : moduleScenarioGene.getScenariosGeneriquesRetenus()){
			if (sGene.getId().equals(scenario.getId())){
				res=true;
			}
		}
		return res;
	}
	
	// Retourne les indices du tableau où les scenarios
	// types ont pour bien b
	public ArrayList<Integer> getIndicesBien (Bien b){
		ArrayList<Integer> listeIndices = new ArrayList<Integer>();
		System.out.println(this.tableau.size());
		for (int i = 0 ; i<this.tableau.size() ; i++){
			if (this.tableau.get(i).getBienSupport().equals(b)){
				listeIndices.add(i);
			}
		}
		return listeIndices ;
	}
	
	// Suppression des scenarios types dont le Bien Support n'est plus retenu
	// Suppression des scenarios types dont les scénarios génériques ont été supprimé
	public void supprimerScenariosTypes() {
		int j = 0;
		while (j < this.tableau.size() && this.tableau.get(j) != null) {
			while (j < this.tableau.size() && !estRetenuBien(this.tableau.get(j).getBienSupport())) {
				ScenarioType scenario = this.tableau.get(j);
				this.biensRetenus.remove(this.tableau.get(j).getBienSupport().getIntitule());
				this.tableau.remove(scenario);
			}
			j++;
		}
		j=0;
		while (j < this.tableau.size() && this.tableau.get(j) != null) {
			while (j < this.tableau.size() && !estRetenuScenarioGene(this.tableau.get(j))) {
				ScenarioType scenario = this.tableau.get(j);
				this.tableau.remove(scenario);
			}
			j++;
		}
	}
	
	// Suppression des scenarios types dont le type de Bien dans l'onglet Bien
	// support n'est pas celui du scénario générique
	public void coherenceTypologie() {
		int j = 0;
		while (j < this.tableau.size() && this.tableau.get(j) != null) {
			while (j < this.tableau.size()
					&& !this.tableau
							.get(j)
							.getTypeBienSupport()
							.getIntitule().contains(
									this.tableau.get(j).getBienSupport()
											.getType())) {
				this.tableau.remove(this.tableau.get(j));
			}
			j++;
		}
	}
	
	public void biensNonCoherent() {
		for (ScenarioType scenario : this.tableau) {
			if (!scenario.getTypeBienSupport().getIntitule().contains(
					scenario.getBienSupport().getType())) {
				this.biensModifies.put(scenario.getBienSupport().getType(),scenario.getBienSupport());
			}
		}
	}
	
	public void actualisationSourcesMenaces(ScenarioType sType){
		
		SourcesDeMenaces sourcesDeMenaces = (SourcesDeMenaces) this.etude.getModule("SourcesDeMenaces");
		
		// on réinitialise les sources de menaces
		sType.setMenaces(sourcesDeMenaces.getSourcesDeMenacesRetenues());
		
		// Ne pas oubliez les sources de menaces de la checkbox !
		// if (this.listeSourcesDeMenaces.size()>=1){
		if (sourcesDeMenaces.getSourcesDeMenacesRetenues().size()< sType.getMenaces().size()){
			System.out.println("SUCCESS");
			for (SourceDeMenace source : sType.getMenacesSuppl()) {
				if (!sType.getMenaces().contains(source)) {
					sType.getMenaces().add(source);
				}
			}
		}
		/*
		 * for (SourceDeMenace source :
		 * sourcesDeMenaces.getSourcesDeMenacesRetenues()){ if
		 * (!sType.getMenaces().contains(source)){
		 * sType.getMenaces().add(source); } }
		 */
		
	}
	
	public void actualiserDonnees() {

		ScenariosDeMenacesGeneriques moduleScenarioGene = (ScenariosDeMenacesGeneriques) this.etude
				.getModule("ScenariosDeMenacesGeneriques");
		
		SourcesDeMenaces SourcesDeMenaces = (SourcesDeMenaces) this.etude.getModule("SourcesDeMenaces");
		
		// Actualisation des types de biens supports que l'on modifie dans biens supports
		this.biensNonCoherent();
		this.coherenceTypologie();
		for (Bien b : this.biensModifies.values()) {
			for (ScenarioGenerique sGene : moduleScenarioGene.getTableau()) {
				ScenarioType scenario = new ScenarioType(
						sGene.getTypeBienSupport(), sGene.getId(),
						sGene.getIntitule(), this.getCriteres(sGene),
						SourcesDeMenaces.getSourcesDeMenacesRetenues(), null,
						true);
				if (sGene.getTypeBienSupport().getIntitule().contains(b.getType())
						&& scenario.getBienSupport() == null) {
					scenario.setBienSupport(b);
					this.tableau.add(scenario);
				}
			}
		}
		// On réinitialise la liste des biens modifiés
		this.biensModifies= new Hashtable<String, Bien>();
		
		// On actualise les sources de menaces retenus dans l'onglet correspondant
		for (ScenarioType scenario : this.tableau){
			this.actualisationSourcesMenaces(scenario);
		}
	}
	
	// On extrait de l'ID du scénario son numéro (ou position)
	public static int extractInt (String id){
		String s = "";
		for (int j=0 ; j<id.length() ; j++){
			if(Character.isDigit(id.charAt(j))){
				s+=id.charAt(j);
			}
		}
		return Integer.parseInt(s);
	}
	
	// On actualise par rapport aux changements du module
	// "Scénarios de menaces génériques"
	// CAS : AJOUT de scénarios
	public void actualiserScenarios() {
		BiensSupports biensSupports = (BiensSupports) this.etude.getModule("BiensSupports");
		ScenariosDeMenacesGeneriques scenariosGeneriques = (ScenariosDeMenacesGeneriques) this.etude.getModule("ScenariosDeMenacesGeneriques");
		for (Bien b : biensSupports.getBiensRetenus()){
			ArrayList<Integer> listeIndices = this.getIndicesBien(b);
			
			TypeBien typeScenario = this.tableau.get(listeIndices.get(0)).getTypeBienSupport();
			ArrayList<ScenarioGenerique> listeSGene = scenariosGeneriques.getScenariosGeneriques(typeScenario);
			
			if (listeIndices.size() < listeSGene.size()){
				// System.out.println("succeed");
				
				///*
				for (int i=listeIndices.size() ; i<listeSGene.size() ; i++){
					ScenarioGenerique sGene = listeSGene.get(i);
					
					ScenarioType scenario = new ScenarioType(
							sGene.getTypeBienSupport(), sGene.getId(),
							sGene.getIntitule(),
							this.getCriteres(sGene),
							this.getSourcesMenaces(),
							b, true);
					
					// On place le scénario au bon endroit
					// "j+1" correspond au numéro d'indice de l'ID. EX : M1 -> indice 0
					int j = 0 ;
					String id = sGene.getId();
					// System.out.println(id.charAt(id.length()-1));
					while(j<listeIndices.size() && (j+1)!=extractInt(id)){
						j++ ;
					}
					if (j==listeIndices.size()){
						this.tableau.add(listeIndices.get(j-1)+1, scenario);
					}
					else{
						this.tableau.add(listeIndices.get(j), scenario);
					}
				}
				//*/
			}
		}
	}
	
	public void importerDonnees() {
		System.out.println("ENTREE DANS LA METHODE importDonnees");
		
		BiensSupports biensSupports = (BiensSupports) this.etude.getModule("BiensSupports");
		ScenariosDeMenacesGeneriques scenarioDeMenacesGeneriques = (ScenariosDeMenacesGeneriques) this.etude
				.getModule("ScenariosDeMenacesGeneriques");
		
		// Cas où on remplit le tableau pour la première fois
		if (this.tableau.size() == 0) {
			System.out.println("ENTREE DANS Le 1er cas");
			this.importerBDC();
		}
		// On actualise les valeurs du tableau
		else {
			if (this.biensRetenus.size() < biensSupports
					.getBiensRetenus().size()) {
				System.out.println("Cas intermediaire");
				for (Bien b : biensSupports.getBiensRetenus()) {
					if (!this.contientBien(b)) {
						for (ScenarioGenerique sGene : scenarioDeMenacesGeneriques
								.getScenariosGeneriquesRetenus()) {
							ScenarioType scenario = new ScenarioType(
									sGene.getTypeBienSupport(), sGene.getId(),
									sGene.getIntitule(),
									this.getCriteres(sGene),
									this.getSourcesMenaces(),
									null, true);
							if (sGene.getTypeBienSupport().getIntitule()
									.contains(b.getType())
									&& scenario.getBienSupport() == null) {
								scenario.setBienSupport(b);
								//scenario.setTypeBienSupport(b.getType());
								this.biensRetenus.put(b.getIntitule(), b);
								this.tableau.add(scenario);
								System.out.println("scénario ajouté");
								//System.out.println(tableau.size());
							}
						}
					}
				}
			}
			// Sinon on supprime les scénarios correspondants aux biens qui 
			// ne sont plus retenus ou aux scénarios non retenus
			else {
				this.supprimerScenariosTypes();
				System.out.println("Dernier cas !");
			}
		}
	}

	public void importerBDC() {
		bdcScenariosMenacesTypes = new ArrayList<ScenarioType>();
		
		BiensSupports biensSupports = (BiensSupports) this.etude.getModule("BiensSupports");
		
		ScenariosDeMenacesGeneriques moduleScenarioGene = (ScenariosDeMenacesGeneriques) this.etude
				.getModule("ScenariosDeMenacesGeneriques");
		
		for (Bien b : biensSupports.getBiensRetenus()) {
			for (ScenarioGenerique sGene : moduleScenarioGene.getTableau()) {
				ScenarioType scenario = new ScenarioType(
						sGene.getTypeBienSupport(), sGene.getId(),
						sGene.getIntitule(), this.getCriteres(sGene),
						this.getSourcesMenaces(), null,
						true);
				if (sGene.getTypeBienSupport().getIntitule().contains(b.getType())
						&& scenario.getBienSupport() == null) {
					scenario.setBienSupport(b);
					this.biensRetenus.put(b.getIntitule(), b);
					bdcScenariosMenacesTypes.add(scenario);
				}
			}
		}
		this.tableau=bdcScenariosMenacesTypes;
	}
	
	public boolean estCoherent() {
		boolean resultat = true ;
		this.erreurs = new ArrayList<String>();
		for (ScenarioType scenario : this.tableau) {
			if (scenario.isIncompleteType()) {
				String s = "Scenario type \" " + scenario.getIntitule()
						+ " \" incomplet";
				this.erreurs.add(s);
				resultat = false;
			}
		}
		if (this.getScenariosTypesRetenus().size() < 1) {
			String s = "Aucun scenario type retenu";
			this.erreurs.add(s);
			resultat = false;
		}
		for (String nomCritere : this.nomColonneSup){
			if (!this.isImpacteCritere(nomCritere)) {
				String s = "Le critere de securite \" " + nomCritere
						+ " \" n'est retenu dans aucun scenario type";
				this.erreurs.add(s);
				resultat = false;
			}
		}
		return resultat;
	}
	
	public String toString(){
		return "Scenarios de menaces types";
	}
}
