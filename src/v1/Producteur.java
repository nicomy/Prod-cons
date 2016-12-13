package v1;

import java.text.SimpleDateFormat;
import java.util.Date;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur{
	
	private int nbMessageafaire ; 
	private ProdCons buffer ; 
	private Aleatoire alea ;  
	

	protected Producteur(ProdCons buf, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, int nbm) throws ControlException {
		
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		// On g�n�re al�toirement le nombre de messages � faire 
		nbMessageafaire = nbm ; 
		
		//on d�finit un objet al�toire pour indiquer quand envoyer un message.
		alea = new Aleatoire(moyenneTempsDeTraitement,deviationTempsDeTraitement);
		buffer = buf ; 
		
		
	}

	@Override
	public int nombreDeMessages() {
		return nbMessageafaire;
	}

	public void run(){
		
		buffer.nouveau_prod();
		for(int i = 0 ; i < nbMessageafaire ; i++  ){
			MessageX m = new MessageX(i,"contenu");
			
			
			int temp= alea.next();
			
			try {
				sleep(temp);
			} catch (InterruptedException e1) {
				System.out.println("impossible de dormir");
				e1.printStackTrace();
			}
			
			try {
				
				buffer.put(this, m);
				if(TestProdCons.outputs)blabla(m);
					
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
		System.out.println(time +": Je suis le produceur d'id "+ identification() + "j'envoi le message"+ m.get_id()+"\n" );
		
		
	}
}