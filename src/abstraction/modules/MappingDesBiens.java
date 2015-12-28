package abstraction.modules;
import java.util.ArrayList;
import java.util.Hashtable;

import abstraction.autres.*;

/**
 * Cette classe permet d'associer les biens supports et les biens
 * essentiels entre eux, sachant qu'un bien support peut avoir plusieurs biens 
 * essentiels associ�s et de m�me pour un bien essentiel
 * 
 * @author Francois Adam
 */

public class MappingDesBiens extends Module{
	//Variables d'instance
	private ArrayList<MappingBien> mappingDesBiens; // table qui relie chaque bien essentiel a son mapping
	private BiensSupports biensSupports;
	private BiensEssentiels biensEssentiels;
	
	//Constructeur
	public MappingDesBiens(BiensSupports biensSupports, BiensEssentiels biensEssentiels) {
		super("MappingDesBiens");
		this.biensSupports = new BiensSupports();
		this.mappingDesBiens=new ArrayList<MappingBien>(this.biensSupports.nombreDeBiens());
		this.biensEssentiels = new BiensEssentiels();
		for (int i=0; i<this.biensSupports.nombreDeBiens(); i++){
			mappingDesBiens.add(new MappingBien(biensSupports,biensEssentiels.getBien(i)));
		}
		//TODO Decomenter quand les autres parties seront OK
		/*
		this.successeurs.add(AnalyseDesRisques.getInstance());
		this.predecesseurs.add(BiensEssentiels.getInstance());
		this.predecesseurs.add(BiensSupports.getInstance());
		this.biensSupports = BiensSupports.getInstance();
		this.biensEssentiels = BiensEssentiels.getInstance();
		*/
		this.cree = false;
		this.coherent = false;
		this.disponible = false;
	}
	
	//Getters et Setters
	public ArrayList<MappingBien> getMappingDesBiens(){
		return this.mappingDesBiens;
	}
	
	public  void setMappingDesBiens(ArrayList<MappingBien> mapping){
		this.mappingDesBiens=mapping;
	}
	
	public BiensSupports getBiensSupports(){
		return this.biensSupports;
	}
	
	public void setBiensSupports(BiensSupports biensSupports){
		this.biensSupports = biensSupports;
	}
	
	public BiensEssentiels getBiensEssentiels(){
		return this.biensEssentiels;
	}
	
	public void setBiensEssentiels(BiensEssentiels biensEssentiels){
		this.biensEssentiels=biensEssentiels;
	}
	
	public int getIndexBienSupport(Bien bienSupport) throws Exception{
		int index=0;
		while (index<this.mappingDesBiens.get(0).getBiensSupports().nombreDeBiens() 
				&& this.getMappingDesBiens().get(0).getBiensSupports().getBien(index)==bienSupport){
			index++;
		}
		if (index!=this.mappingDesBiens.get(0).getBiensSupports().nombreDeBiens()){
			return index;
		}
		else {
			throw new Exception("bien support pas present");
		}
	}
	
	public ArrayList<Bien> getBiensEssentielsCorrespondant(Bien bienSupport) throws Exception{
		ArrayList<Bien> biens = new ArrayList<Bien>();
		int indexBien = this.getIndexBienSupport(bienSupport);
		for (int i=0; i<this.mappingDesBiens.size(); i++){
			if (this.getMappingDesBiens().get(i).getMappingBien().get(indexBien).equals("x")){
				biens.add(this.getMappingDesBiens().get(i).getBienEssentiel());
			}
		}
		return biens;
	}
	
	public String toString(){
		return "Mapping Des Biens";
	}
}
