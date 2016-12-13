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

	public  Message get(_Consommateur c) throws Exception, InterruptedException {
		
		Consommateur cons = (Consommateur ) c ; 
		
		
		//test pour savoir s'il y'a des messages à lire
		RessourceALire.P() ; 
		Message m;
		
		
		if(enAttente>0){
			// gestion du buffer protï¿½gï¿½ par les mutex
			mutex.P();
				m = buffer[out];
				((MessageX) m).lecture();
				enAttente-- ; 
				cons.blabla((MessageX) m);
			mutex.V();
			
			if (((MessageX) m).est_consomme()){
				
				mutex.P();
				
				out= (out+1)  %N ;
				//On libère son producteur
				ProdEnAttente[((MessageX) m).get_idProd()].V();
				//on libère tous les consomateurs bloqué ; 
				for (Consommateur ctmp : ConsBloque){
					ConsEnAttent[ctmp.get_id()].V(); 
				}
				ConsBloque.clear();
				
				//indique qu'on a libéré une place dans le buffeur pour un Thread Producteur.
				Place.V();
				
				mutex.V();
			}
			else{
				// On bloque le consommmateur. 
				ConsBloque.add(cons );
				ConsEnAttent[cons.get_id()].P() ;
			}
		}else{
			m=null;
		}	
		
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
			System.out.println(NbExplaireRestant);
			((Producteur) p).blabla((MessageX) m);
			
			//indique qu'il y a un Certains nombre de ressource à prendre 
			for(int i = 0 ; i < ((MessageX) m).get_NbExemplaire() ; i ++ ){
				RessourceALire.V();
			}
		mutex.V();
		
		
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
