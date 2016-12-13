package v3;

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
	private Observateur Ob; 
	
	public Consommateur(Observateur observateur, int moyenneTempsDeTraitement,
		int deviationTempsDeTraitement,ProdCons buf) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		nbMessagelu = 0 ; 
		gen_temps = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		buffer = buf ;
		Ob = observateur;
	}

	@Override
	public int nombreDeMessages() {
		return nbMessagelu;
	}
	
	public int get_id(){
		return identification() ;
	}
	
	public void run(){
		
		MessageX m = null ; 
		int temps;
		
		if(TestProdCons.outputs) System.out.println("Consomateur "+ identification()+ " rentre dans la game");
		
		while(!buffer.fin()){
			
			try {
				 			
				m = (MessageX) buffer.get(this);
				if(TestProdCons.outputs) blabla(m);
				if(m!=null)nbMessagelu++ ;
				
			} catch (Exception e) {
				System.out.println(" probleme recuperation du message");
				e.printStackTrace();
			} 
			// On simule un temps de calcul une fois le message lu.
			temps = gen_temps.next();
			try {
				if(m!=null) Ob.consommationMessage(this, m, temps);
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
		
		if(TestProdCons.outputs) System.out.println("Consomateur "+ identification()+ " sort de la game");
	}
	
	// cette fonction permet de dire que fait les consomateur
	public void blabla(MessageX m ){
		String time = new SimpleDateFormat("mm:ss:S").format(new Date());
		if(m!=null){
			System.out.println(time +": Je suis le consomateur d'id "+ identification() + ". Je lis le message"+ m.get_id()+"\n" );
		}else{
			System.out.println(time +": Je suis le consomateur d'id "+ identification() + ". IL n'y a plus de message pour moi\n" );
		}
	}	
			
}
