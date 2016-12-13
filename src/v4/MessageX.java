package v4;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	private String contenu ; 
	private int idmessage ;
	private int idProd ;
	private int NbExemplaire ;  // nombre de fois que le messsage doit être lu
	private int NbCons ; // nombre de consomateurs ayant lu le message
	
	public MessageX(int id, String c, int nbE,int idP ){
		contenu = c ; 
		idmessage = id;
		NbExemplaire = nbE;
		NbCons = 0 ; 
		idProd = idP ; 
	}
	
	public boolean est_consomme(){
		return NbCons == NbExemplaire ;
	}
	
	public int get_idProd(){
		return idProd ;
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
	
	public int get_NbExemplaire(){
		return NbExemplaire ;
	}
}
