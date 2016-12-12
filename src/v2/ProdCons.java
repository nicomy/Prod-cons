package v2;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {
	private int N, enAttente ; // N est le nombre maximum de messages que peut contenir le buffer. 
	 
	private int in = 0 , out = 0,nbProd = 0 ; 
	private Semaphore mutex ; 
	private Semaphore RessourceALire ; 
	private Semaphore Place ; 
	
	private Message[] buffer ;
	
	
	public ProdCons( int n ){
		N = n ;
		enAttente = 0 ; 
		buffer = new Message[N];
		mutex = new Semaphore(1);
		RessourceALire = new Semaphore(0);
		Place = new Semaphore(N);
	}

	// fonction permettant de retirer une ressource dans le tampon. 
	public Message get(_Consommateur c) throws Exception, InterruptedException {
		
		//test pour savoir s'il y'a des messages à lire
		RessourceALire.P() ; 
		Message m;
			
		// gestion du buffer protégé par les mutex
//		mutex.P();
		synchronized (this) {
			
			m = buffer[out];
			out = (out+ 1) % N ;
			enAttente-- ; 
//		mutex.V();
		}
		
		//indique qu'on a libéré une place dans le buffeur pour les un Thread Producteur.
		Place.V();
		
		return m;
	}
	
	// fonction permettant de dï¿½poser une ressource dans le tampon. 
	public synchronized void put(_Producteur p, Message m) throws Exception, InterruptedException {
		
		// on s'assure qu'il y a de la place pour y palcer une ressource 
		Place.P() ;  
		
		//section critique protiégé par les mutex
//		mutex.P();
		synchronized (this) {
			
			buffer[in] = m ;
			in = (in +1) %N;
			enAttente++ ;
		}
//		mutex.V();
		
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
		System.out.println("le poducteur "+ nbProd+" rentre dans le game");
		nbProd++;
//		System.out.println(nbProd);
		
	}
	public synchronized void fin_prod(){
		
		nbProd-- ; 
		System.out.println("le poducteur "+ nbProd+" sort de la game");
	}
	
	// return vrai si il n'y a plus de pproducteur et que le bufer est vide
	public synchronized boolean fin() {
		boolean resultat = ((nbProd == 0) && ( enAttente == 0 ));
		//System.out.println("resultat fin = "+ resultat);
		
		//On s'assure qu'il n'y pas de nouveau producteur cree. 
		if(resultat){
			notifyAll();
		}
		return (nbProd == 0) && ( enAttente == 0 );
	} 
}
