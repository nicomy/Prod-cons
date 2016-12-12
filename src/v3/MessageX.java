package v3;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	private String contenu ; 
	int idmessage ;
	
	
	public MessageX(int id, String c){
		contenu = c ; 
		idmessage = id;
	}
	
	public String get_contenu(){
		return contenu;
	}
	
	public int get_id(){
		return idmessage; 
	}
}
