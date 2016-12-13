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
	
	public String getContenu() {
		return contenu;
	}

	public int getIdmessage() {
		return idmessage;
	}

	public Date getCreation() {
		return creation;
	}

	public Date getEcriture() {
		return ecriture;
	}

	public Date getLecture() {
		return lecture;
	}

	public Date getConsomation() {
		return consomation;
	}

	public void setContenu(String contenu) {
		this.contenu = contenu;
	}

	public void setIdmessage(int idmessage) {
		this.idmessage = idmessage;
	}

	public void setCreation(Date creation) {
		this.creation = creation;
	}

	public void setEcriture(Date ecriture) {
		this.ecriture = ecriture;
	}

	public void setLecture(Date lecture) {
		this.lecture = lecture;
	}

	public void setConsomation(Date consomation) {
		this.consomation = consomation;
	}

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
