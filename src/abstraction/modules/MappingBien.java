package abstraction.modules;
import java.util.ArrayList;

import abstraction.autres.*;

/**
 * Cette classe permet d'associer un bien essentiel et les biens
 * supports entre eux
 * 
 * @author Francois Adam
 */

public class MappingBien {
	//Variables d'instance
	private ArrayList<String> mappingBien; // table qui relie un bien essentiel aux biens supports
	private BiensSupports biensSupports;
	private Bien bienEssentiel;
	
	//Constructeur
	public MappingBien(BiensSupports biensSupports, Bien bienEssentiel){
		this.biensSupports = biensSupports;
		this.bienEssentiel = bienEssentiel;
		this.mappingBien = new ArrayList<String>(this.biensSupports.getLesBiens().size());
		for (int i=0; i<this.biensSupports.nombreDeBiens(); i++){
			this.mappingBien.add("");
		}
	}
	
	//Getters et Setters
	public BiensSupports getBiensSupports(){
		return this.biensSupports;
	}
	
	public void setBiensSupports(BiensSupports biensSupports){
		this.biensSupports = biensSupports;
	}
	
	public Bien getBienEssentiel(){
		return bienEssentiel;
	}
	
	public void setBienEssentiel(Bien bienEssentiel){
		this.bienEssentiel=bienEssentiel;
	}
	
	public ArrayList<String> getMappingBien(){
		return this.mappingBien;
	}
	
	public String getValueAt(int index){
		return mappingBien.get(index);
	}
	
	public void setValueAt(String value, int index){
		if (value.equals("") || value.equals("x") || value.equals("o")){
			mappingBien.set(index, value);
		}
	}
	
	public void setMappingBien(ArrayList<String> mappingBien){
		this.mappingBien=mappingBien;
	}
	
	public boolean estComplet(){
		int nombreCasesVides = 0;
		int index = 0;
		while (nombreCasesVides<this.getMappingBien().size() && index<this.getMappingBien().size()){
			if (this.getMappingBien().get(index).equals("")){
				nombreCasesVides++;
			}
			index++;
		}
		return nombreCasesVides<this.getMappingBien().size();
	}
	
	public String toString(){
		return "{Mapping Bien : intitul� = "+this.getBienEssentiel().getIntitule()+"}";
	}
}
