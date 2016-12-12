package v4;

import java.util.ArrayList;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {
	private int N, enAttente ; // N est le nombre maximum de messages que peut contenir le buffer. 
	 
	private int in = 0 , out = 0,nbProd = 0 ; 
	private Semaphore mutex ; 
	private Semaphore RessourceALire ; 
	private Semaphore Place ; 
	private Semaphore ProdEnAttente;
	private Semaphore ConsEnAttent ;
	private Observateur Ob ; 
	private Message[] buffer ;
	private ArrayList<Consommateur> ConsServie ;
	private ArrayList<Producteur> Prod;
	
	public ProdCons( int n, Observateur observateur ){
		N = n ;
		enAttente = 0 ; 
		buffer = new Message[N];
		mutex = new Semaphore(1);
		RessourceALire = new Semaphore(0);
		Place = new Semaphore(N);
		ProdEnAttente = new Semaphore(0);
		ConsEnAttent = new Semaphore(0 );
		Ob = observateur ; 
		ConsServie =new ArrayList<>() ;
		
	}

	//gère la lecture mutliple du même message. 
	public  MessageX lire(Consommateur c) throws InterruptedException, Exception{
//		System.out.println(" Cons " + ((Consommateur) c).get_id() + " est au debut ");
	
		MessageX m = null ;
		ConsServie.add(c);
	
		m = (MessageX) get(c);
		
		
		//une fois le nombre de consomateur atteint on les réveillent pour qu'ils puissent 
		//continuer leurs activités
		if(m.est_consomme()){
//			notifyAll();
			
			//On libère tous les consomateurs
			for(int i = 0 ; i < ConsServie.size() ; i ++ ){
				ConsEnAttent.V();
			}
			ConsServie.clear();
			out = (out+ 1) % N ;
			//on bloque les consomateurs
			ProdEnAttente.V();
			enAttente-- ; 
		}
		
		//un consomateur ne peut pas poursuivre son activité tant que tous les messages n'ont pas été lu.
		while(!m.est_consomme()){
			//wait();
			ConsEnAttent.P();
		}
		
		return m ; 
	
	}
	
	
	// je pense pas qu'un synchronise soit necessaire car il est fait à l'appel de la fonction
	public  void ecrire(Producteur p, MessageX m) throws InterruptedException, Exception{
		put (p,m);
		System.out.println("le producteur " + p.get_id() + " à posé son message et va attendre") ;
		ProdEnAttente.P();
		System.out.println("le producteur " + p.get_id() + " a fini d'attendre") ;
	}
	
	
	// fonction permettant de retirer une ressource dans le tampon. 
	public  MessageX get(_Consommateur c) throws Exception, InterruptedException {
//		System.out.println(" Cons " + ((Consommateur) c).get_id() + " est au debut ");
		
		//test pour savoir s'il y'a des messages à lire
		RessourceALire.P() ; 
		MessageX m;
		
		// gestion du buffer protégé par les mutex
		mutex.P();
			m = (MessageX) buffer[out];
			m.lecture();
			Ob.retraitMessage(c, m);
			((Consommateur) c).blabla(m);
		mutex.V();
		
		//indique si le message est entièrement consommé qu'on a libéré une place dans le buffeur pour les un Thread Producteur.
		if(m.est_consomme()) {
			Place.V();
		}
//		System.out.println(" Cons " + ((Consommateur) c).get_id() + "sort de get");
		return m;
	}
	
	// fonction permettant de dï¿½poser une ressource dans le tampon. 
	public  void put(_Producteur p, Message m) throws Exception, InterruptedException {
		
		// on s'assure qu'il y a de la place pour y palcer une ressource 
		Place.P() ;  
		
		//section critique protiégé par les mutex
		mutex.P();
			Ob.depotMessage(p, m);
			buffer[in] = m ;
			in = (in +1) %N;
			enAttente++ ;
			((Producteur) p).blabla((MessageX) m);
		mutex.V();
		
		
		//indique qu'une ressource est disponible et va reveiller si besoin les 
		for(int i = 0 ; i < ((MessageX) m).get_NbExemplaire() ; i ++ ){
			RessourceALire.V();
		}
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
