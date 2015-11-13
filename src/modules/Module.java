package modules;
import java.util.ArrayList;

/** 
 * Classe visant � factoriser tout ce qui est commun aux diff�rents modules.
 * @author Maxime Ansquer 
 */

public class Module implements IModule{
	
	//---Variables d'instance---
	
	private ArrayList<Module> predecesseurs; 
	private ArrayList<Module> successeurs; 
	private String nom;
	protected boolean cree;
	protected boolean coherent;
	protected boolean disponible;
	
	//---Constructeurs---
	
	public Module(String nom) {
		this.predecesseurs=new ArrayList<>();
		this.successeurs=new ArrayList<>();
		this.nom=nom ;
	}
	
	//---Getters et setters---
	
	@Override
	public boolean estCoherent() {
		return this.coherent;
	}

	@Override
	public boolean estCree() {
		return this.cree;
	}

	@Override
	public boolean estDisponible() {
		return this.disponible;
	}

	@Override
	public String getNom() {
		return this.nom;
	} 

}
