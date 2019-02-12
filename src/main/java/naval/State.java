package naval;

public class State{
	private String next;
	private boolean hasEnded;	

	public State(String turn){
		this.next = turn;
		this.hasEnded = false;

	}
	
	public State(String turn, boolean b){
		this(turn);
		this.hasEnded = b;
	}

	public void setNext(String next){
		this.next = next;
	}
	
	public void stop(){
		hasEnded = true;
	}
	
	public boolean getHasEnded(){
		return hasEnded;
	}
	
	
	public String getNext(){
		return next;
	}
}

