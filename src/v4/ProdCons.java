package v4;

import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.Message;
import v3.*;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons extends v3.ProdCons {
	protected int exemplairesRestant[] ; // N est le nombre maximum de messages que peut contenir le buffer. 
	protected Semaphore mutexE ;
	protected Aleatoire aleaExemplaires ;
	
	public ProdCons( int n, Observateur observateur,int nbExemplaires, int deviationExemplaires){
		super(n ,observateur);
		aleaExemplaires= new Aleatoire(nbExemplaires, deviationExemplaires);
		
		mutexE=new Semaphore(1);
		
	}

	// fonction permettant de retirer une ressource dans le tampon. 
	public Message get(_Consommateur c) throws Exception, InterruptedException {
		System.out.println("Hello");
		//test pour savoir s'il y'a des messages � lire
		RessourceALire.P(); 
		Message m;
			
		// gestion du buffer prot�g� par les mutex
		
		if(enAttente()>0){
			
			mutexE.P();
			m = buffer[out];
			Ob.retraitMessage(c, m);
			if(v3.TestProdCons.outputs) System.out.println("nb d'exemplaires restants");
			if(--(exemplairesRestant[out])<=0){
				mutexE.V();
				out = (out+ 1) % N ;
				
				enAttente-- ; 
				//indique qu'on a lib�r� une place dans le buffeur pour les un Thread Producteur.
				Place.V();
				m.notifyAll();
			}else{
				mutexE.V();
				RessourceALire.V(); 
				if(v3.TestProdCons.outputs) System.out.println("Consommateur "+c.identification()+
						" retire un exemplaire d'un message");
				m.wait();
			}
			
				
		}else{
			m=null;	//plus de message à produire ou dans le buffer. 
		}
		
		
		
		return m;
	}
	
	// fonction permettant de d�poser une ressource dans le tampon. 
	public void put(_Producteur p, Message m) throws Exception, InterruptedException {
		
		// on s'assure qu'il y a de la place pour y palcer une ressource 
		Place.P() ;  
		
		//section critique proti�g� par les mutex
		Ob.depotMessage(p, m);
		mutex.P();
		
		buffer[in] = m;
		exemplairesRestant[in] = aleaExemplaires.next();
		in = (in +1) %N;
		
		mutex.V();
		
		enAttente++ ;
		
		
		//indique qu'une ressource est disponible pour reveiller 
		RessourceALire.V();
		
		m.wait();
	}

}
