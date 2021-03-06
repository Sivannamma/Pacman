import java.util.ArrayList;

/*
 * ChasingBFS class is used by the ghost chasing the Pacman
 * start -> position of the ghost
 * destination() -> position of the Pacman
 */
public class ChasingBFS implements ChaseBehaviour {
	private Point [][] points; // our points array (represent the game area)
	private static int pacX;
	private static int pacY;
	private IGraph graph;

	@Override
	public ArrayList<Point> chase(Point start, IGraph graph) {
		this.graph = graph;
		return new BFS(start , destination() , graph).performBFS();
	}


	//The destination for chasingBFS is the Pacman position
	@Override
	public Point destination() {
		points = (Point[][]) graph.getGraph();
		Point des = points[(pacY-7)/PacmanBoard.BLOCK_SIZE][(pacX-7)/PacmanBoard.BLOCK_SIZE];
		return des;
	}

	// getting the exact coordinates of the pacman
	public static void setCoordinates(int corX, int corY) {
		pacX = corX;
		pacY = corY;
	}

}
