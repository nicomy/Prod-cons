package v1;

import java.util.Date;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	private String contenu ; 
	int idmessage ;
	private Date creation; 
	private Date ecriture ;
	private Date lecture ;
	private Date consomation; 
	
	
	public MessageX(int id, String c, Date Crea){
		contenu = c ; 
		idmessage = id;
		this.creation = Crea ; 
	}
	
	public String get_contenu(){
		return contenu;
	}
	
	public int get_id(){
		return idmessage; 
	}
}
