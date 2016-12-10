package v1;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur{
	
	private int nbMessageafaire ; 
	private ProdCons buffer ; 
	private Aleatoire alea ; 
	

	protected Producteur(int type,ProdCons buf, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, int moyenne, int deviation) throws ControlException {
		
		super(type, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		// On génère alétoirement le nombre de messages à faire 
		nbMessageafaire = new Aleatoire(moyenne, deviation).next() ;
		
		//on définit un objet alétoire pour indiquer quand envoyer un message.
		alea = new Aleatoire(moyenneTempsDeTraitement,deviationTempsDeTraitement);
		buffer = buf ; 
		
		
	}

	@Override
	public int nombreDeMessages() {
		return nbMessageafaire;
	}

	public void run(){
		
		for(int i = 0 ; i < nbMessageafaire ; i++  ){
			MessageX m = new MessageX("contenu");
			
			int temp= alea.valeur(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
			
			try {
				sleep(temp);
			} catch (InterruptedException e1) {
				System.out.println("impossible de dormir");
				e1.printStackTrace();
			}
			
			try {
				buffer.put(this, m);
			} catch (InterruptedException e) {
				System.out.println("erreur de mise dans le tampon");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("erreur de mise dans le tampon");
				e.printStackTrace();
			}
			
			
			
			
		}
		
	}
}
