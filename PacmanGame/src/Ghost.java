import java.util.ArrayList;

public class Ghost implements Runnable{

	// ghost coordinates
	private int corX;
	private int corY;

	private static int pacX;
	private static int pacY;
	
	private ChaseBehaviour chase; // the behavior this ghost received (which function to perform)
	private  IGraph graph; 
	private ArrayList<Point> path; // the list will represent the path to the player
	private Point [][] points; // our points array (represent the game area)
	private int speed; // speed of the monster

	Ghost(int corX, int corY, ChaseBehaviour chase, IGraph graph, int speed){
		this.corX = corX;
		this.corY = corY;
		this.chase = chase;
		this.graph = graph;
		this.speed=speed;

		points = (Point[][]) graph.getGraph();
	}

	@Override
	public void run() {
		try {
			while (true) {
				isOnPlayer();
				Point ghost = points[(corY-7)/PacmanBoard.BLOCK_SIZE][(corX-7)/PacmanBoard.BLOCK_SIZE];
				path = chase.chase(ghost, graph);			
				move();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}

	}

	public int getCorX() {
		return corX;
	}

	public int getCorY() {
		return corY;
	}

	private void move() throws InterruptedException {
		
		if(path==null)
			return;
		
		for (int i = 0; i < path.size(); i++) {
			this.corY = path.get(i).getIndRow() * PacmanBoard.BLOCK_SIZE + 7;
			this.corX = path.get(i).getIndCol() * PacmanBoard.BLOCK_SIZE + 7;
			isOnPlayer();
			Thread.sleep(speed);
		}
	}
	
	// getting the exact coordinates of the Pacman
	public static void setCoordinates(int corX, int corY) {
		pacX = corX;
		pacY = corY;
	}

	
	private void isOnPlayer() {
		if(corX==pacX && corY==pacY)
			PacmanBoard.removeHeart();
	}
}