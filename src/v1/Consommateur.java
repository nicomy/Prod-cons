package v1;

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
	
	protected Consommateur(int id , Observateur observateur, int moyenneTempsDeTraitement,
		int deviationTempsDeTraitement,ProdCons buf) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement, deviationTempsDeTraitement);
		
		nbMessagelu = 0 ; 
		gen_temps = new Aleatoire(moyenneTempsDeTraitement, deviationTempsDeTraitement);
		buffer = buf ;
		idConsommateur  =id ; 
	}

	@Override
	public int nombreDeMessages() {
		return nbMessagelu;
	}
	
	public int get_id(){
		return idConsommateur ;
	}
	
	public void run(){
		
		MessageX m ; 
		int temps; 
		// On simule un temps de calcul une fois le message lu.
		Aleatoire alea = new Aleatoire(moyenneTempsDeTraitement(), deviationTempsDeTraitement());
		
		if(TestProdCons.outputs) System.out.println("Consomateur "+ idConsommateur+ " rentre dans la game");
		
		while(!buffer.fin()){
			
			try {
				
				// le synchronise sert pour afficher dans le bon odre les �l�ments. 
				m = (MessageX) buffer.get(this);
				if(TestProdCons.outputs) blabla(m);
				if(m!=null) {
					nbMessagelu++ ;
					((MessageX) m).setConsomation(new Date());
				}
				
				 
			} catch (Exception e) {
				System.out.println(" probleme recuperation du message");
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
		
		if(TestProdCons.outputs) System.out.println("Consomateur "+ idConsommateur+ " sort de la game");
	}
	
	// cette fonction permet de dire que fait les consomateur
	public void blabla(MessageX m ){
		String time = new SimpleDateFormat("mm:ss:S").format(new Date());
		if(m!=null){
			System.out.println(time +": Je suis le consomateur d'id "+ idConsommateur + ". Je lis le message"+ m.get_id()+"\n" );
		}else{
			System.out.println(time +": Je suis le consomateur d'id "+ idConsommateur + ". IL n'y a plus de message pour moi\n" );
		}
			
	}	
			
}