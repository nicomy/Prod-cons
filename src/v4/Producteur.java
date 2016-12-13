package v4;

import java.text.SimpleDateFormat;
import java.util.Date;

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
	private Aleatoire nbMAlea ;
	private int idProducteur ; 
	private Observateur Ob; 
	

	protected Producteur(int id, ProdCons buf, Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, int nbm, int MoyNb, int devNb) throws ControlException {
		
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		// On gï¿½nï¿½re alï¿½toirement le nombre de messages ï¿½ faire 
		nbMessageafaire = nbm ; 
		
		idProducteur= id ;
		Ob = observateur ;
		//on définit un objet aléatoire pour simuler un temps de production des messages 
		alea = new Aleatoire(moyenneTempsDeTraitement,deviationTempsDeTraitement);
		nbMAlea = new Aleatoire(MoyNb,devNb);
		buffer = buf ; 
		
		
	}

	@Override
	public int nombreDeMessages() {
		return nbMessageafaire;
	}
	
	public int get_id(){
		return idProducteur;
	}

	public void run(){
		int temps, NbInteration ; 
		buffer.nouveau_prod();
		for(int i = 0 ; i < nbMessageafaire ; i++  ){
			temps= alea.next();
			NbInteration = nbMAlea.next();
			MessageX m = new MessageX(get_id()*100 + i,"contenu du message ",NbInteration, get_id());
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
		System.out.println(time +": Je suis le produceur d'id "+ idProducteur + "j'envoi le message"+ m.get_id()+"\n" );
		
		
	}
}
