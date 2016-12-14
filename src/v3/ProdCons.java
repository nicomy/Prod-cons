package v3;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {
	protected int N, enAttente ; // N est le nombre maximum de messages que peut contenir le buffer. 
	 
	protected int in = 0 , out = 0,nbProd = 0 ; 
	protected Semaphore mutex ; 
	protected Semaphore RessourceALire ; 
	protected Semaphore Place ; 
	protected Observateur Ob ; 
	protected Message[] buffer ;
	
	
	public ProdCons( int n, Observateur observateur ){
		N = n ;
		enAttente = 0 ; 
		buffer = new Message[N];
		mutex = new Semaphore(1);
		RessourceALire = new Semaphore(0);
		Place = new Semaphore(N);
		Ob = observateur ; 
		
	}

	// fonction permettant de retirer une ressource dans le tampon. 
	public Message get(_Consommateur c) throws Exception, InterruptedException {
		
		//test pour savoir s'il y'a des messages � lire
		RessourceALire.P() ; 
		Message m;
			
		// gestion du buffer prot�g� par les mutex
		
		if(enAttente>0){
			mutex.P();
				m = buffer[out];
				Ob.retraitMessage(c, m);
				out = (out+ 1) % N ;
				enAttente-- ; 
			mutex.V();
			//indique qu'on a lib�r� une place dans le buffeur pour les un Thread Producteur.
			Place.V();
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
		mutex.P();
			Ob.depotMessage(p, m);
			buffer[in] = m ;
			in = (in +1) %N;
			enAttente++ ;
		mutex.V();
		
		//indique qu'une ressource est disponible pour reveiller 
		RessourceALire.V();
	}
	
	public synchronized int enAttente() {
		return enAttente;
	}

	public int taille() {
		return N;
	}

	public synchronized void nouveau_prod(){
		nbProd++;
		if(TestProdCons.outputs) System.out.println("le poducteur "+ nbProd +" rentre dans le game");
//		System.out.println(nbProd);
		
	}
	public synchronized void fin_prod(){
		
		if(TestProdCons.outputs) System.out.println("le poducteur "+ nbProd+" sort de la game");
		nbProd-- ; 
	}
	
	// return vrai si il n'y a plus de pproducteur et que le bufer est vide
	public synchronized boolean fin() {
		boolean resultat = ((nbProd == 0) && ( enAttente == 0 ));

		if(resultat){
			RessourceALire.V(); //libère les consommateurs en attente d'un message qui ne viendra plus. 
		}
		return (nbProd == 0) && ( enAttente == 0 );
	} 
}
