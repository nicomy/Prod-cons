package v1;
import jus.poc.prodcons.*;

public class TestProdCons extends Simulateur {
	public TestProdCons(Observateur observateur){super(observateur);}
	protected void run() throws Exception{
		
		
	}
	public static void main(String[] args){new TestProdCons(new Observateur()).start();}
}
