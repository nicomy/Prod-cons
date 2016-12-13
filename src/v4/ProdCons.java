package v4;


import java.util.ArrayList;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {
	private int N, enAttente ; // N est le nombre maximum de messages que peut contenir le buffer. 
	//enAttente correspond au nombre de message que le buffer doit lire.
	private int in = 0 , out = 0,nbProdAlive = 0, NbExplaireRestant = 0,NbMesCosomme  ; 
	private Semaphore mutex ; 
	private Semaphore RessourceALire ; 
	private Semaphore Place ; 
	private Semaphore[] ProdEnAttente;
	private Semaphore[] ConsEnAttent ;
	
	private ArrayList<Consommateur> ConsBloque ; 
	private Observateur Ob ; 
	private Message[] buffer ;
	
	public ProdCons( int n, Observateur observateur, int NbP,int NbCons ){
		N = n ;
		enAttente = 0 ; //nombre de message à lire; 
		buffer = new Message[N];
		mutex = new Semaphore(1);
		RessourceALire = new Semaphore(0);
		Place = new Semaphore(N);
		ProdEnAttente = new Semaphore[NbP];
		for(int i = 0 ; i < NbP ; i++){
			ProdEnAttente[i] = new Semaphore(0);
		}
		
		ConsEnAttent = new Semaphore[NbCons];
		for(int i = 0 ; i < NbCons ; i++){
			ConsEnAttent[i] = new Semaphore(0);
		}
		
		ConsBloque = new ArrayList<>();
		
		Ob = observateur ; 
		
	}


	
	
//	// je pense pas qu'un synchronise soit necessaire car il est fait à l'appel de la fonction
//	public  void ecrire(Producteur p, MessageX m) throws InterruptedException, Exception{
//		put (p,m);
//		System.out.println("le producteur " + p.get_id() + " à posé son message et va attendre") ;
//		ProdEnAttente.P();
//		System.out.println("le producteur " + p.get_id() + " a fini d'attendre") ;
//	}
//	
	
	// fonction permettant de retirer une ressource dans le tampon. 
//	public  MessageX get(_Consommateur c) throws Exception, InterruptedException {
//		
//		RessourceALire.P() ; 
//		MessageX m;
//		
//		mutex.P();
//			m = (MessageX) buffer[out];
//			m.lecture();
//			Ob.retraitMessage(c, m);
//			((Consommateur) c).blabla(m);
//		mutex.V();
//		
//		if(m.est_consomme()) {
//			Place.V();
//		}
//		return m;
//	}
//	
//	public  void put(_Producteur p, Message m) throws Exception, InterruptedException {
//		
//		Place.P() ;  
//		
//		mutex.P();
//			Ob.depotMessage(p, m);
//			buffer[in] = m ;
//			in = (in +1) %N;
//			enAttente++ ;
//			((Producteur) p).blabla((MessageX) m);
//		mutex.V();
//		
//		
//		for(int i = 0 ; i < ((MessageX) m).get_NbExemplaire() ; i ++ ){
//			RessourceALire.V();
//		}
//	}
//	
	
//	public  MessageX lire(Consommateur c) throws InterruptedException, Exception{
//	
//		MessageX m = null ;
//		ConsServie.add(c);
//	
//		m = (MessageX) get(c);
//		
//		
//		if(m.est_consomme()){
//			
//			for(int i = 0 ; i < ConsServie.size() ; i ++ ){
//				ConsEnAttent.V();
//			}
//			ConsServie.clear();
//			
//			out = (out+ 1) % N ;
//
//			ProdEnAttente.V();
//			enAttente-- ; 
//		}
//		
//		while(!m.est_consomme()){
//			ConsEnAttent.P();
//		}
//		
//		return m ; 
//	
//	}
	public  Message get(_Consommateur c) throws Exception, InterruptedException {
		
		Consommateur cons = (Consommateur ) c ; 
		
		//test pour savoir s'il y'a des messages à lire
		RessourceALire.P() ; 
		Message m;
			
		if(!fin()){
			// gestion du buffer protï¿½gï¿½ par les mutex
			mutex.P();
				m = buffer[out];
				((MessageX) m).lecture();
				enAttente-- ; 
				cons.blabla((MessageX) m);
			mutex.V();
			
			if (((MessageX) m).est_consomme()){
				
				mutex.P();
				//On libère son producteur
				ProdEnAttente[((MessageX) m).get_idProd()].V();
				
				//on libère tous les consomateurs blolqué ; 
				for (Consommateur ctmp : ConsBloque){
					ConsEnAttent[ctmp.get_id()].V(); 
				}
				ConsBloque.clear();
				
				//indique qu'on a libéré une place dans le buffeur pour un Thread Producteur.
				Place.V();
			}
			else{// On bloque le consommmateur. 
				ConsBloque.add(cons );
				ConsEnAttent[cons.get_id()].P() ;
			}
		}else{
			m=null;
		}	
		
		System.out.println("sortie par "+cons.get_id() +" du get ");
		return m;
		
	}
	
	public  void put(_Producteur p, Message m) throws Exception, InterruptedException {
		
		
		Place.P();
		
		mutex.P();
		
			Ob.depotMessage(p, m);
			buffer[in] = m ;
			in = (in +1) %N;
			NbExplaireRestant = ((MessageX) m).get_NbExemplaire() ;
			enAttente += NbExplaireRestant ; 
			((Producteur) p).blabla((MessageX) m);
			
		mutex.V();
		
		//indique qu'il y a un Certains nombre de ressource à prendre 
		for(int i = 0 ; i < ((MessageX) m).get_NbExemplaire() ; i ++ ){
			RessourceALire.V();
		}
		
		//On bloque le Prodcteur associé. 
		ProdEnAttente[((Producteur) p).get_id()].P();

		
	}
	public synchronized int enAttente() {
		return enAttente;
	}

	public int taille() {
		return N;
	}

	public synchronized void nouveau_prod(){
		System.out.println("le poducteur "+ nbProdAlive+" rentre dans le game");
		nbProdAlive++;
		
	}
	public synchronized void fin_prod(){
		
		nbProdAlive-- ; 
		System.out.println("le poducteur "+ nbProdAlive+" sort de la game");
	}
	
	// return vrai si il n'y a plus de pproducteur et que le bufer est vide
	public synchronized boolean fin() {
		boolean resultat = ((nbProdAlive == 0) && ( enAttente == 0 ));
		//System.out.println("resultat fin = "+ resultat);
		
		//On s'assure qu'il n'y pas de nouveau producteur cree. 
		if(resultat){
			notifyAll();
		}
		return (nbProdAlive == 0) && ( enAttente == 0 );
	} 
}
