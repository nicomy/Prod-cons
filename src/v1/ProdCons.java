package v1;

import java.util.ArrayList;
import java.util.Date;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {
	// nbplein correspond au nombre de ressource dans le buffer.
	private int nbplein, nbvide, N ; // N est le nombre maximum de messages que peut contenir le buffer. 
	//nbvide et nbplein, il est nécessaire d'avoir 2 variables différentes pour désynchroniser les consommateurs et producteurs.
	 
	private int in = 0 , out = 0,nbProd = 0 ; 
	private Object mp;		// Moniteur producteurs
	private Object mc;		// Moniteur consommateurs
	
	private Message[] buffer ;
	private ArrayList<MessageX> l_mes ; 
	
	
	public ProdCons( int n ){
		N = n ;
		nbplein = 0 ;
		nbvide = N ;
		buffer = new Message[N]; 
		mc = new Object();
		mp = new Object();
		
		l_mes = new ArrayList<>(); 
	}

	
	public synchronized int enAttente() {
		return nbplein;
	}
	
	// fonction permettant de retirer une ressource dans le tampon. 
	public Message get(_Consommateur c) throws Exception, InterruptedException {

		Message m;
		synchronized (mc){
			while (nbplein == 0 && !fin()) mc.wait() ; //s'il n'y pas de message, cons attend
			
			if(nbplein>0){
				m= buffer[out];
				out = (out+1 ) % N ;
				nbplein -- ;
				
			}else{ 		
				m=null;	//plus de message à produire ou dans le buffer. 
			}
			
		}
		synchronized(mp){nbvide++; mp.notifyAll();}
		return m;
	}
	
	// fonction permettant de disposer une ressource dans le tampon. 
	public  void put(_Producteur p, Message m) throws Exception, InterruptedException {
		//si le buffer est plein on attend : 
		synchronized(mp){
			while(nbvide == 0 ) mp.wait() ;
			
			
			l_mes.add((MessageX) m);
			//mettre a jour le buffer 
			buffer[in] = m ;
			in = (in +1) %N;
	
				//on incremente le nombre de ressource dispo 
			nbvide --;
		}
		synchronized(mc){nbplein++; mc.notifyAll();}
	}
	

	public int taille() {
		return N;
	}

	public synchronized void nouveau_prod(){
		if(TestProdCons.outputs) System.out.println("le poducteur "+ nbProd+" rentre dans le game");
		nbProd++;
		
	}
	public synchronized void fin_prod(){
		nbProd-- ; 
		if(TestProdCons.outputs) System.out.println("le poducteur "+ nbProd+" sort de la game");
	}
	
	// return vrai si il n'y a plus de producteur et que le bufer est vide
	public boolean fin() {
		boolean resultat = ((nbProd == 0) && ( nbplein == 0 ));
		
		if(resultat){
			synchronized(mc){mc.notify();} //libère les consommateurs en attente d'un message qui ne viendra plus. 
		}
		
		return (nbProd == 0) && ( nbplein == 0 );
	} 
	
}