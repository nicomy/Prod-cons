package v3;

public class Semaphore {
	private int n ;
	
	public Semaphore( int res){
		n = res ; 
	}
	
	public synchronized void P(){
		n-- ; 
		
		// si on veut prendre plus de ressources que nécessaire : 
		if (n < 0 ){
			
			try {
				wait();
			} catch (InterruptedException e) {
				System.out.println("erreur");
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void V() {
		n++ ;
		
		//s'il ya des processus en file d'attente. 
		if(n<= 0 ){
			notify() ;
		}
		
	}
}
