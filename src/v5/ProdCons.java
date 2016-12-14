package v5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {
	private int N, enAttente ; // N est le nombre maximum de messages que peut contenir le buffer. 

	private int in = 0 , out = 0,nbProd = 0 ; 
	private Observateur Ob ; 
	private Message[] buffer ;
	final Lock lock = new ReentrantLock();
	final Condition notFull  = lock.newCondition(); 
	final Condition notEmpty = lock.newCondition(); 

	
	
	public ProdCons( int n, Observateur observateur ){
		N = n ;
		enAttente = 0 ; 
		buffer = new Message[N];
		Ob = observateur ; 
		
	}

	// fonction permettant de retirer une ressource dans le tampon. 
	public Message get(_Consommateur c) throws Exception, InterruptedException {
		Message m;
		
		//test pour savoir s'il y'a des messages a lire
		lock.lock();
		try {
		       while (enAttente == 0 && !fin())
		         notEmpty.await();
		       if(enAttente()>0){
					m = buffer[out];
					Ob.retraitMessage(c, m);
					out = (out+ 1) % N ;
					enAttente-- ; 
		       }else{
		    	   m=null;	//plus de message à produire ou dans le buffer. 
		       }
		       
		       notFull.signal();
		       return m;
		  } finally {
		       lock.unlock();
		  }
	}
	
	// fonction permettant de deposer une ressource dans le tampon. 
	public void put(_Producteur p, Message m) throws Exception, InterruptedException {
		lock.lock();
	     try {
	    	 
	       //tant que le buffer est plein ; 
    	   while(enAttente == N ) 
	         notFull.await();
    	   Ob.depotMessage(p, m);
   			buffer[in] = m ;
   			in = (in +1) %N;
   			enAttente++ ;
	       notEmpty.signal();
	     } finally {
	       lock.unlock();
	     }
		
		
	}
	
	public int enAttente() {
		return enAttente;
	}

	public int taille() {
		return N;
	}

	public  void nouveau_prod(){
		lock.lock();
		if(TestProdCons.outputs) System.out.println("le poducteur "+ nbProd+" rentre dans le game");
		nbProd++;
		lock.unlock();
		
	}
	public void fin_prod(){
		lock.lock();
		nbProd-- ; 
		if(TestProdCons.outputs) System.out.println("le poducteur "+ nbProd+" sort de la game");
		lock.unlock();
	}
	
	// return vrai si il n'y a plus de pproducteur et que le bufer est vide
	public boolean fin() {
		boolean resultat = ((nbProd == 0) && ( enAttente == 0 ));
		if (resultat){
			lock.lock();
			notEmpty.signalAll();
			lock.unlock();
		}
		//if(TestProdCons.outPuts)  System.out.println("resultat fin = "+ resultat);
		return (nbProd == 0) && ( enAttente == 0 );
	} 
}
