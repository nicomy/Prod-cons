package v4;

import java.text.SimpleDateFormat;
import java.util.Date;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;
import v3.MessageX;

public class Producteur extends Acteur implements _Producteur{
	
	private int nbMessageafaire ; 
	private ProdCons buffer ; 
	private Aleatoire alea ;  
	private Observateur Ob; 
	

	public Producteur(ProdCons buf, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, int nbm) throws ControlException {
		
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		// On g�n�re al�toirement le nombre de messages � faire 
		nbMessageafaire = nbm ; 
		
		Ob = observateur ;
		//on d�finit un objet al�toire pour indiquer quand envoyer un message.
		alea = new Aleatoire(moyenneTempsDeTraitement,deviationTempsDeTraitement);
		buffer = buf ; 
		
		
	}

	public int nombreDeMessages() {
		return nbMessageafaire;
	}

	public void run(){
		
		buffer.nouveau_prod();
		for(int i = 0 ; i < nbMessageafaire ; i++  ){
			MessageX m = new MessageX(this.identification()*100+i,"contenu du message ");
			int temps= alea.next();
			// if(TestProdCons.outputs) System.out.println("temps = "+ temps);
			try {
				Ob.productionMessage(this, m, temps);
			} catch (ControlException e2) {
				System.out.println("erreur lors de l'observaiton du message");
				e2.printStackTrace();
			}
			
			
			try {
				sleep(temps);
			} catch (InterruptedException e1) {
				System.out.println("impossible de dormir");
				e1.printStackTrace();
			}
			
			try {
					if(TestProdCons.outputs) blabla(m);
					buffer.put(this, m);
										
			} catch (InterruptedException e) {
				System.out.println("erreur de mise dans le tampon");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("erreur de mise dans le tampon");
				e.printStackTrace();
			}
			
		}
		buffer.fin_prod();
	}
	
	
	public void blabla(MessageX m ){
		String time = new SimpleDateFormat("mm:ss:S").format(new Date());
		System.out.println(time +": Je suis le produceur d'id "+ this.identification() + "j'envoi le message"+ m.get_id()+"\n" );
		
		
	}
}
