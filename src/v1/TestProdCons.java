package v1;
import java.util.ArrayList;

import jus.poc.prodcons.*;

public class TestProdCons extends Simulateur {
	private Observateur Ob ; 
	private ProdCons buffeur ;
	private ArrayList<Producteur> lprod ;
	private ArrayList<Consommateur> lcons ;
	
	
	// liste des options
	protected static int nbProd;
	protected static int nbCons;
	protected static int nbBuffer;
	protected static int tempsMoyenProduction;
	protected static int deviationTempsMoyenProduction;
	protected static int tempsMoyenConsommation;
	protected static int deviationTempsMoyenConsommation;
	protected static int nombreMoyenDeProduction;
	protected static int deviationNombreMoyenDeProduction;
	protected static int nombreMoyenNbExemplaire;
	protected static int deviationNombreMoyenNbExemplaire;
	
	public TestProdCons(Observateur observateur){
		super(observateur);
		init("options.xml");
		
		
	}
	
	
	
	protected void run() throws Exception{
			
	}	
	
//	protected <type> option;
	
	/**
	* Retreave the parameters of the application.
	* @param file the final name of the file containing the options.
	*/
	protected static void init(String file) {
	// retreave the parameters of the application
	final class Properties extends java.util.Properties {
	private static final long serialVersionUID = 1L;
	public int get(String key){return Integer.parseInt(getProperty(key));}
		public Properties(String file) {
			try{
				loadFromXML(ClassLoader.getSystemResourceAsStream(file));
			}catch(Exception e){e.printStackTrace();}
			
			}
		}
		Properties option = new Properties("options/"+file);
		nbProd = option.get("nbProd");
		System.out.println("nb_prod =" + nbProd);
		nbCons = option.get("nbCons");
		nbBuffer = option.get("nbBuffer");
		tempsMoyenProduction = option.get("tempsMoyenProduction");
		deviationTempsMoyenProduction = option.get("deviationTempsMoyenProduction");
		tempsMoyenConsommation = option.get("tempsMoyenConsommation");
		deviationTempsMoyenConsommation = option.get("deviationTempsMoyenConsommation");
		nombreMoyenDeProduction = option.get("nombreMoyenDeProduction");
		deviationNombreMoyenDeProduction = option.get("deviationNombreMoyenDeProduction");
		nombreMoyenNbExemplaire = option.get("nombreMoyenNbExemplaire");
		deviationNombreMoyenNbExemplaire = option.get("deviationNombreMoyenNbExemplaire");
	}
//	
//	<option> = option.getProperty("option");
	
	
	public static void main(String[] args){new TestProdCons(new Observateur()).start();}
}
