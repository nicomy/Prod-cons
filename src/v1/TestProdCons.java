package v1;
import jus.poc.prodcons.*;

public class TestProdCons extends Simulateur {
	
	
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
	
	}
//	Properties option = new Properties("/jus.poc.prodcons/src/options/"+file);
//	<option> = option.getProperty("option");
	
	
	public static void main(String[] args){new TestProdCons(new Observateur()).start();}
}
