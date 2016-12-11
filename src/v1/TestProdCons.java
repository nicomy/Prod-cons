package v1;
import java.util.ArrayList;

import jus.poc.prodcons.*;

public class TestProdCons extends Simulateur {
	private Observateur Ob ; 
	private ProdCons buffer ;
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
	
	
	//On remplie dans cette classe les deux listes lcons et lprod par le nb voulue
	public TestProdCons(Observateur observateur){
		super(observateur);
		Ob = observateur;
		init("options.xml");
		
		buffer = new ProdCons(nbBuffer);
		
		lprod = new ArrayList<>();
		for(int i = 0 ; i< nbProd ; i ++ ){
			try {
				lprod.add(new Producteur( buffer,
						Ob,tempsMoyenProduction, deviationNombreMoyenDeProduction,
						nombreMoyenDeProduction, deviationNombreMoyenDeProduction));
			} catch (ControlException e) {
				System.out.println("erreur à la création de Producteur");
				e.printStackTrace();
			}
			
		}
		
		lcons = new ArrayList<>();
		for(int i = 0 ; i< nbCons ; i ++ ){
			try {
				lcons.add(new Consommateur(Ob, tempsMoyenConsommation,
						deviationTempsMoyenConsommation, buffer));
			} catch (ControlException e) {
				System.out.println("erreur création consomateur");
				e.printStackTrace();
			}
		}
		
	}
	
	
	// On lance chaqu'un des consomateurs dans lcons et producteur dans lprod  
	protected void run() throws Exception{
		
		for (int i=0;i<nbProd;i++){
//			if (lprod.get(i)==null) System.out.println("on peut pas acceder à un Prdo\n");
//			else System.out.println(lprod.get(i).toString());
			lprod.get(i).start();
		}
		for (int i=0;i<nbCons;i++){
//			System.out.println(c.get(i).toString());
			lcons.get(i).start();
		}	
		
		
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
		System.out.println("nbCons = "+ nbCons);
		nbBuffer = option.get("nbBuffer");b
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
