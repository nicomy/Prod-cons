package v4;

import java.text.SimpleDateFormat;
import java.util.Date;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {
	private int nbMessagelu ; 
	private ProdCons buffer ; 
	private Aleatoire gen_temps ; 
	private int idConsommateur ; 
	private Observateur Ob; 
	
	protected Consommateur(int id , Observateur observateur, int moyenneTempsDeTraitement,
		int deviationTempsDeTraitement,ProdCons buf) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		nbMessagelu = 0 ; 
		gen_temps = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		buffer = buf ;
		idConsommateur  =id ; 
		Ob = observateur;
	}

	@Override
	public int nombreDeMessages() {
		return nbMessagelu;
	}
	
	public int get_id(){
		return idConsommateur ;
	}
	
	public void run(){
		
		MessageX m = null ; 
		int temps; 
		// On simule un temps de calcul une fois le message lu.
		Aleatoire alea = new Aleatoire(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
		
		System.out.println("Consomateur "+ idConsommateur+ " rentre dans la game");
		
		while(!buffer.fin()){
			
			try {
					m = buffer.lire(this);
					nbMessagelu++ ; 
			
			} catch (Exception e) {
				System.out.println(" probleme recuperation du message k"+ e);
				e.printStackTrace();
			} 
			temps = gen_temps.next();
			try {
				Ob.consommationMessage(this, m, temps);
			} catch (ControlException e1) {
				System.out.println("Erreur Observation consomation message");
				e1.printStackTrace();
			}
			try {
				sleep(temps );
			} catch (InterruptedException e) {
				System.out.println("consomateur ne dort pas");
				e.printStackTrace();
			}
			
		}
		
	System.out.println("Consomateur "+ idConsommateur+ " sort de la game");
	}
	
	// cette fonction permet de dire que fait les consomateur
	public void blabla(MessageX m ){
		String time = new SimpleDateFormat("mm:ss:S").format(new Date());
		System.out.println(time +": Je suis le consomateur d'id "+ idConsommateur + ". Je lis le message"+ m.get_id()+"\n" );
	}	
			
}
