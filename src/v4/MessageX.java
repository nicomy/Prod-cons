package v4;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	private String contenu ; 
	int idmessage ;
	int NbExemplaire ;  // nombre de fois que le messsage doit être lu
	int NbCons ; // nombre de consomateurs ayant lu le message
	
	public MessageX(int id, String c, int nbE ){
		contenu = c ; 
		idmessage = id;
		NbExemplaire = nbE;
		NbCons = 0 ; 
		
	}
	
	public boolean est_consomme(){
		return NbCons == NbExemplaire ;
	}
	
	public synchronized void lecture(){
		NbCons ++ ; 
	}
	
	public String get_contenu(){
		return contenu;
	}
	
	public int get_id(){
		return idmessage; 
	}
}
