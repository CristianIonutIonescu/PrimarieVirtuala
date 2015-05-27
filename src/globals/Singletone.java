package globals;

public class Singletone {
	private static Singletone instance = null;
	
	protected Singletone(){
		// Only to defeat instantiaton
	}
	
	public Singletone getInstance(){
		if(instance == null){
			instance = new Singletone();
		}
		
		return instance;
	}
}
