package abstraction.autres;

import abstraction.modules.ScenariosDeMenacesTypes;

public class Risque {
	/*Cette classe donne une ligne du tableau AnalyseDesRisques en faisant correspondre divers �l�ments d�finis plus t�t
	 * et en leur allouant un intitul� pour d�signer chaque risque.
	 */
	
	
	private String intitule;
	private Evenement evenementredoute;
	private int niveaugravite;
	private Bien biensupport;
	private ScenariosDeMenacesTypes scenarioconcret;
	private int niveauvraisemblance;
	
	
	
	public Risque(String intitule,Evenement evenementredoute,int niveaugravite,Bien biensupport,ScenariosDeMenacesTypes scenarioconcret,int niveauvraisemblance){
		this.intitule=intitule;
		this.evenementredoute=evenementredoute;
		this.niveaugravite=niveaugravite;
		this.biensupport=biensupport;
		this.scenarioconcret=scenarioconcret;
		this.niveauvraisemblance=niveauvraisemblance;
		
	}
	
	
	public String getIntitule(){
		return this.intitule;
	}
	
	public void setIntitule(String intitule){
		this.intitule=intitule;
	}
	
	public int getNiveauGravite(){
		return this.niveaugravite;
		
		
	}
	public int getNiveauVraisemblance(){
		return this.niveauvraisemblance;
	}

}