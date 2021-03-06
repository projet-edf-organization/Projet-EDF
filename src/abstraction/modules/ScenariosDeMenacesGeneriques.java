package abstraction.modules;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import abstraction.Etude;
import abstraction.autres.ScenarioGenerique;
import abstraction.autres.TypeBien;

public class ScenariosDeMenacesGeneriques extends Module {
	
	// Represente la bdc
	private static ArrayList<ScenarioGenerique> bdcScenariosMenacesGeneriques;

	// Variable d'instance
	private ArrayList<ScenarioGenerique> tableau ;
	private ScenarioGenerique scenarioCourant ;
	private ArrayList<String> nomColonneSup;
	
	private ArrayList<ScenarioGenerique> scenariosSupprimes ;
	
	public ScenariosDeMenacesGeneriques(Etude etude){
		super("ScenariosDeMenacesGeneriques");
		this.tableau= new ArrayList<ScenarioGenerique>();
		
		this.scenariosSupprimes=new ArrayList<ScenarioGenerique>();
		
		this.etude=etude;
		this.predecesseurs.add(this.getEtude().getModule("TypologieDesBiensSupports"));
		this.predecesseurs.add(this.getEtude().getModule("CriteresDeSecurite"));
		//this.successeurs.add(this.getEtude().getModule("BiensSupports"));
		//this.successeurs.add(this.getEtude().getModule("ScenariosDeMenacesTypes"));
		this.scenarioCourant= new ScenarioGenerique();
		this.nomColonneSup = new ArrayList<String>();
		this.cree = false;
		this.coherent = false;
		this.disponible = false;
		this.importerBDC();
		this.tableau = ScenariosDeMenacesGeneriques.getBDC();
	}
	
	// ---Getters et setters---
	
	public int getSize(){
		return this.tableau.size();
	}
	
	public ScenarioGenerique getScenarioCourant(){
		return this.scenarioCourant;
	}
	
	public void setScenarioCourant(ScenarioGenerique scenarioCourant){
		this.scenarioCourant=scenarioCourant;
		this.setChanged();         // PAC
		this.notifyObservers();    // PAC
	}
	
	public ArrayList<String> getNomColonneSup() {
		return nomColonneSup;
	}

	public void setNomColonneSup(ArrayList<String> nomColonneSup) {
		this.nomColonneSup = nomColonneSup;
	}

	public ArrayList<ScenarioGenerique> getScenariosSupprimes() {
		return scenariosSupprimes;
	}

	public void setScenariosSupprimes(ArrayList<ScenarioGenerique> scenariosSupprimes) {
		this.scenariosSupprimes = scenariosSupprimes;
	}
	
	public ArrayList<ScenarioGenerique> getTableau() {
		return tableau;
	}

	public void setTableau(ArrayList<ScenarioGenerique> tableau) {
		this.tableau = tableau;
	}
	
	public ScenarioGenerique getScenarioGenerique(int i) {
		return this.tableau.get(i);
	}
	
	public static ArrayList<ScenarioGenerique> getBDC(){
		return bdcScenariosMenacesGeneriques;
	}
	
	// ---Services---
	
	/*
	// Un TypeBien est nouveau dans le cas o� il n'est pas contenu dans la bdc de cette classe
	// et qu'il n'est pas dans la bdc de la classe TypeBien
	public boolean estNouveauTypeBien(TypeBien type) {
		boolean b = ((TypologieDesBiensSupports) this.getEtude().getModule(
				"Typologie des biens supports")).estNouveauTypeBien(type);
		if (b) {
			for (ScenarioGenerique scenario : bdcScenariosMenacesGeneriques) {
				b = b
						&& (scenario.getTypeBienSupport() != type
								.getIntitule());
			}
		}
		return b;
	}
	*/
	
	// L'ajout d'une ligne dans le tableau correspond ici
	// � un type de bien support r�f�renc�
	
	public void addScenarioGenerique(ScenarioGenerique scenario, int index) {
		if (this.nomColonneSup!=null){
			for(String nomCritere : this.nomColonneSup){
				scenario.getCriteresSup().put(nomCritere,false);
			}
		}
		this.tableau.add(index,scenario);
		this.setChanged();                           // PAC
		this.notifyObservers();                      // PAC
	}
	
	public void removeScenarioGenerique(ScenarioGenerique scenario){
		if (this.nomColonneSup!=null){
			for(String nomCritere : this.nomColonneSup){
				scenario.getCriteresSup().remove(nomCritere);
			}
		}
		this.tableau.remove(scenario);
		this.setChanged();                           // PAC
		this.notifyObservers();                      // PAC
	}
	
	// Ajout d'une colonne
	public void addCritere (String nomCritere){
		this.nomColonneSup.add(nomCritere);
		for (int i=0 ; i<this.tableau.size() ; i++){
			if (!this.getScenarioGenerique(i).getCriteresSup().containsKey(nomCritere)){
				this.getScenarioGenerique(i).getCriteresSup().put(nomCritere, false);
			}
		}
		this.setChanged();                           // PAC
		this.notifyObservers();                      // PAC
	}
	
	//Suppression d'une colonne
	public void removeCritere (String nomCritere){
		this.nomColonneSup.remove(nomCritere);
		/*
		for (int i=0 ; i<this.tableau.size() ; i++){
			this.getScenarioGenerique(i).getCriteresSup().remove(nomCritere);
		}
		//*/
		this.setChanged();                           // PAC
		this.notifyObservers();                      // PAC
	}
	
	public ArrayList<ScenarioGenerique> getScenariosGeneriquesRetenus() {
		ArrayList<ScenarioGenerique> resultat = new ArrayList<ScenarioGenerique>();
		for (ScenarioGenerique scenario : this.getTableau()) {
			if (scenario.isRetenu()) {
				resultat.add(scenario);
			}
		}
		return resultat;
	}
	
	public boolean isImpacteCritere(String nomCritere){
		ArrayList<Boolean> critere = new ArrayList<Boolean>();
		for (ScenarioGenerique scenario : this.tableau){
			if (!scenario.getCriteresSup().get(nomCritere)){
				critere.add(scenario.getCriteresSup().get(nomCritere));
			}
		}
		return !(critere.size()==this.tableau.size()) ;
	}
	
	public void actualiserScenarios(){
		TypologieDesBiensSupports typesBiens = (TypologieDesBiensSupports) this.etude.getModule("TypologieDesBiensSupports");
		String idsTypesSupprimes = typesBiens.getIdTypesSupprimes();
		String idsTypesNonRetenus = typesBiens.getIdTypesNonRetenus();
		
		// System.out.println(idsTypesSupprimes);
		if (typesBiens.getTypesSupprimes().size()!=0){
			int j = 0;
			while (j < this.tableau.size() && this.tableau.get(j) != null) {
				while (j < this.tableau.size()
						&& idsTypesSupprimes.contains(tableau.get(j).getTypeBienSupport().getId())){
					this.tableau.remove(this.tableau.get(j));
				}
				j++;
			}
		}
		
		// Suppression des scénarios génériques dont les types de bien supports n'existent plus
		// On les stocke quelque part
		if (typesBiens.getTypeBiensRetenus().size()<typesBiens.getTableau().size()){
			int j = 0;
			while (j < this.tableau.size() && this.tableau.get(j) != null) {
				while (j < this.tableau.size()
						&& idsTypesNonRetenus.contains(tableau.get(j).getTypeBienSupport().getId())){
					this.scenariosSupprimes.add(this.tableau.get(j));
					this.tableau.remove(this.tableau.get(j));
				}
				j++;
			}
		}
		else{
			if (this.scenariosSupprimes.size()>=1){
				ArrayList<ScenarioGenerique> newTab = new ArrayList<ScenarioGenerique>();
				
				// On ajoute d'abord les scénarios qui sont passés du statut de "non retenu" à "retenu"
				for (ScenarioGenerique sg : this.scenariosSupprimes){
					newTab.add(sg);
				}
				
				for (ScenarioGenerique sg : this.tableau){
					newTab.add(sg);
				}
				
				this.tableau=newTab;
			}
		}
	}
	
	// Rend une liste composée de tous les scénarios génériques de type de bien "type"
	public ArrayList<ScenarioGenerique> getScenariosGeneriques(TypeBien type){
		ArrayList<ScenarioGenerique> l = new ArrayList<ScenarioGenerique>();
		int j=0 ; 
		while(j<this.tableau.size() && !this.tableau.get(j).getTypeBienSupport().equals(type)){
			j++;
		}
		if (j!=this.tableau.size()){
			while(j<this.tableau.size() && this.tableau.get(j).getTypeBienSupport().equals(type)){
				l.add(this.tableau.get(j));
				j++;
			}
		}
		return l ;
	}
	
	private void importerBDC() {
		
		bdcScenariosMenacesGeneriques = new ArrayList<ScenarioGenerique>();
		
		/*
		 * Etape 1 : recuperation d'une instance de la classe "DocumentBuilderFactory"
		 */
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			/*
			 * Etape 2 : creation d'un parseur
			 */
			final DocumentBuilder builder = factory.newDocumentBuilder();
			
			/*
			 * Etape 3 : creation d'un Document
			 */
			final Document document= builder.parse(new File("bdc.xml"));
			
			/*
			 * Etape 4 : recuperation de l'Element racine
			 */
			final Element racine = document.getDocumentElement();
			
			/*
			 * Etape 5 : recuperation du noeud " ScenariosMenacesGeneriques "
			 */
			final Element scenariosDeMenacesGeneriques = (Element) racine.getElementsByTagName("ScenariosMenacesGeneriques").item(0);
			final NodeList listeScenarios = scenariosDeMenacesGeneriques.getChildNodes();
			final int nbScenarios = listeScenarios.getLength();
			
			for (int i = 0; i<nbScenarios; i++) {
				if(listeScenarios.item(i).getNodeType() == Node.ELEMENT_NODE) {
					final Element scenarioGenerique = (Element) listeScenarios.item(i);
					
					/*
					 * Construction d'un scenario
					 */
					
					String intituleTypeBien = scenarioGenerique.getElementsByTagName("TypeBien").item(0).getTextContent();
					String id = scenarioGenerique.getElementsByTagName("Id").item(0).getTextContent();
					String intituleScenario = scenarioGenerique.getElementsByTagName("Intitule").item(0).getTextContent(); 
					
					Hashtable<String, Boolean> CriteresSup = new Hashtable<String, Boolean>();
					
					final Element criteres = (Element) scenarioGenerique.getElementsByTagName("CriteresRetenus").item(0);
					final NodeList listeCriteres = criteres.getChildNodes();
					final int nbCriteres = listeCriteres.getLength();					
					
					for (int j = 0; j < nbCriteres; j++){
						if(listeCriteres.item(j).getNodeType() == Node.ELEMENT_NODE) {
							final Element critere = (Element) listeCriteres.item(j);
							String intituleCritere = critere.getElementsByTagName("IntituleCritere").item(0).getTextContent();
							Boolean retenu = Boolean.parseBoolean(critere.getElementsByTagName("Retenu").item(0).getTextContent());
							
							CriteresSup.put(intituleCritere,retenu);
						}
					}					
					TypologieDesBiensSupports typologie = (TypologieDesBiensSupports) this.etude.getModule("TypologieDesBiensSupports");
					ScenarioGenerique scenario = new ScenarioGenerique(typologie.getTypeBien(intituleTypeBien), id, intituleScenario, CriteresSup, true);
					
					/*
					 * Ajout du scenario à la bdc dans le cas où le type de bien est retenu dans la fenêtre "Typologie des biens supports"
					 */
					
					bdcScenariosMenacesGeneriques.add(scenario);
				}
			}
			
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (final SAXException e) {
			e.printStackTrace();
		}
		catch (final IOException e) {
			e.printStackTrace();
		}
	}
	
	public String toString(){
		return "Scenarios de menaces generiques";
	}
	
	public boolean estCoherent() {
		boolean resultat = true;
		this.erreurs = new ArrayList<String>();
		for (ScenarioGenerique scenario : this.tableau) {
			if (scenario.isIncomplete()) {
				String s = "Scenario generique \" " + scenario.getIntitule()
						+ " \" incomplet";
				this.erreurs.add(s);
				resultat = false;
			}
			if (!scenario.getCriteresSup().values().contains(true)){
				String s = "Le scenario generique \" " + scenario.getIntitule()
						+ " \" n'a pas de critère retenu";
				this.erreurs.add(s);
				resultat = false;
			}
		}
		if (this.getScenariosGeneriquesRetenus().size() < 1) {
			String s = "Aucun scenario generique retenu";
			this.erreurs.add(s);
			resultat = false;
		}
		if (this.nomColonneSup==null || this.nomColonneSup.size()==0){
			String s = "Aucun critere de securite ajoute";
			this.erreurs.add(s);
			resultat = false;
		}
		for (String nomCritere : this.nomColonneSup) {
			if (!this.isImpacteCritere(nomCritere)) {
				String s = "Le critere de securite \" " + nomCritere
						+ " \" n'est retenu dans aucun scenario";
				this.erreurs.add(s);
				resultat = false;
			}
		}
		return resultat;
	}
}