package v4;

import v3.*;
import java.util.ArrayList;

import jus.poc.prodcons.*;

public class TestProdCons extends v3.TestProdCons {
	private Observateur Ob ; 
	private ProdCons buffer ;
	private ArrayList<Producteur> lprod ;
	private ArrayList<Consommateur> lcons ;
	
	
	// liste des options
	protected static int nombreMoyenNbExemplaire;
	protected static int deviationNombreMoyenNbExemplaire;
	
	
	//On remplie dans cette classe les deux listes lcons et lprod par le nb voulue
	public TestProdCons(Observateur observateur){
		super(observateur);
		Ob = observateur;
		v3.TestProdCons.outputs=true;
		init("options.4.xml");
		
		if((nombreMoyenNbExemplaire+deviationNombreMoyenNbExemplaire)>nbCons)
			try {
				throw new Exception("Risque d'avoir plus d'exemplaires que de consommateurs");
			} catch (Exception e2) {
				e2.printStackTrace();
				System.out.println(e2.getMessage());
			}
		
		Aleatoire alea = new Aleatoire(nombreMoyenDeProduction, deviationNombreMoyenDeProduction) ;
		try {
			Ob.init(nbProd, nbBuffer, nbBuffer);
		} catch (ControlException e1) {
			System.out.println("probl�me initialisation Observateur");
			e1.printStackTrace();
		}
		
		buffer = new ProdCons(nbBuffer, Ob, nombreMoyenNbExemplaire, deviationNombreMoyenNbExemplaire);
		
		//creation des porducteurs
		lprod = new ArrayList<>();
		for(int i = 0 ; i< nbProd ; i ++ ){
			try {
				Producteur p = new Producteur(i, buffer,
						Ob,tempsMoyenProduction, deviationNombreMoyenDeProduction,alea.next() );
				lprod.add(p);
				Ob.newProducteur(p);
			} catch (ControlException e) {
				System.out.println("erreur a la creation de Producteur");
				e.printStackTrace();
			}
			
		}
		
		//mise en place des consomateurs
		lcons = new ArrayList<>();
		for(int i = 0 ; i< nbCons ; i ++ ){
			try {
				Consommateur c = new Consommateur(i,Ob, tempsMoyenConsommation,
						deviationTempsMoyenConsommation, buffer) ;
				lcons.add(c);
				Ob.newConsommateur(c);
			} catch (ControlException e) {
				System.out.println("erreur creation consomateur");
				e.printStackTrace();
			}
		}
		
	}
	
	
	public static void main(String[] args){new TestProdCons(new Observateur()).start();}
}
