package v1;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {
	// nbplein correspond au nombre de ressource dans le buffer.
	private int nbplein, N ; // N est le nombre maximum de messages que peut contenir le buffer. 
	 
	private int in = 0 , out = 0,nbProd = 0 ; 
	
	
	private Message[] buffer ;
	
	
	public ProdCons( int n ){
		N = n ;
		nbplein = 0 ;  
		buffer = new Message[N]; 
	}

	
	public synchronized int enAttente() {
		return nbplein;
	}
	
	// fonction permettant de retirer une ressource dans le tampon. 
	public Message get(_Consommateur c) throws Exception, InterruptedException {
		// tant qu'il n'y a rien a lire le processus attend. 
		Message m;
		while (nbplein == 0 ) wait() ; 
		
		// gestion du buffer
//		synchronized(this){
			m= buffer[out];
			out = (out+1 ) % N ;
//		}
		
		//on d�cremente le nombre de ressource dispo
		nbplein -- ;
		
		notifyAll();
		return m;
	}
	
	// fonction permettant de disposer une ressource dans le tampon. 
	public  void put(_Producteur p, Message m) throws Exception, InterruptedException {
		//si le buffer est plein on attend : 
		while(nbplein == N ) wait() ;
		
		//mettre a jour le buffer 
		
			buffer[in] = m ;
			in = (in +1) %N;

			//on incremente le nombre de ressource dispo 
		nbplein ++ ; 
		
		notifyAll();
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
		boolean resultat = ((nbProd == 0) && ( nbplein == 0 ));
		//System.out.println("resultat fin = "+ resultat);
		
		
		
		//On s'assure qu'il n'y pas de nouveau producteur crée. 
		if(resultat){
			notifyAll();
		}
		
		return (nbProd == 0) && ( nbplein == 0 );
	} 
}
