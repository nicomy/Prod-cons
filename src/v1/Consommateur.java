package v1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {
	private int nbMessagelu ; 
	private ProdCons buffer ; 
	private Aleatoire gen_temps ; 
	
	protected Consommateur( Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement,ProdCons buf) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		nbMessagelu = 0 ; 
		gen_temps = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		buffer = buf ; 
	}

	@Override
	public int nombreDeMessages() {
		return nbMessagelu;
	}
	
	public void run(){
		
		MessageX m ; 
		int temps; 
		// On simule un temps de calcul une fois le message lu.
		Aleatoire alea = new Aleatoire(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
		
		while(buffer.fin()){
			
			try {
				m = (MessageX) buffer.get(this);
				nbMessagelu++ ; 
			} catch (Exception e) {
				System.out.println(" problème récupération du message");
				e.printStackTrace();
			}
			temps = gen_temps.next();
			
			try {
				sleep(temps );
			} catch (InterruptedException e) {
				System.out.println("consomateur ne dort pas");
				e.printStackTrace();
			}
			
		}
		
		
	}
	
}
