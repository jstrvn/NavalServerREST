package naval;

public class Player{
	private boolean[][] board;
	private Player next;
	private int usedCells;
	
	public void setNext(Player p){
		this.next = p;
	}
	
	public Player getNext(){
		return next;
	}

	public Player(int width, int height){
		board = new boolean[width][height];
	}	
	
	public void occupyCell(int x, int y){
		board[x][y] = true;
		usedCells++;
	}
	
	public void destroyCell(int x, int y){
		board[x][y] = false;
		usedCells--;
	}
	
	public boolean isDefeated(){
		if (usedCells <= 0){
			return true;
		}
		return false;
	}

}

