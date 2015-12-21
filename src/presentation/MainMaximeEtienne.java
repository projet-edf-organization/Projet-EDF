package presentation;

import abstraction.Etude;
import abstraction.modules.BiensEssentiels;
import abstraction.modules.CriteresDeSecurite;
import abstraction.modules.EvenementsRedoutes;
import abstraction.modules.Metriques;
import abstraction.modules.SourcesDeMenaces;

public class MainMaximeEtienne {
	
	public static Etude etude;

	public static void main(String[] args){
	etude = new Etude();
	etude.addModule(new CriteresDeSecurite());
	etude.addModule(new BiensEssentiels());
	etude.addModule(new Metriques(etude));
	etude.addModule(new SourcesDeMenaces());
	etude.addModule(new EvenementsRedoutes(etude));
	EvenementsRedoutes ev=new EvenementsRedoutes(etude);
	
	FenetreEvenementsRedoutes f= new FenetreEvenementsRedoutes(ev);
}
	
}