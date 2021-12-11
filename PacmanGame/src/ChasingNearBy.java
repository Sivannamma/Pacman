import java.util.ArrayList;
/*
 * This class is used by ghost who's chasing the Pacman by certain radios.
 * If the Pacman is not within the radios , then the ghost is static at certain position -> the start point we give.
 * If the Pacman within the radios , then the path is the result from the bfs algorithm to the Pacman.
 */
public class ChasingNearBy implements ChaseBehaviour {
	private Point [][] points; // our points array (represent the game area)
	private IGraph graph;
	private Point staticPosition;

	private Point des ;
	private Point start;

	private Boolean firstEntrence;


	// Pacman position :
	private static int pacX;
	private static int pacY;
	private int chaseRadius;

	// constructor : 
	ChasingNearBy(){
		firstEntrence = true;
	}

	// getting the exact coordinates of the Pacman
	// (when the Pacman moves, it's calling this function in order to set the coordination)
	public static void setCoordinates(int corX, int corY) {
		pacX = corX;
		pacY = corY;
	}


	@Override
	public ArrayList<Point> chase(Point start, IGraph graph) {
		this.graph = graph;
		this.start = start;
		chaseRadius= 2;
		return new BFS(start , destination() , graph).performBFS();
	}
	

	/*
	 * Algorithm to check the radius from the ghost to the Pacman
	 * 
	 * 
	 * (x-1, y-1)		(x-1, y)			(x-1,y+1)
	 * 
	 * (x, y-1)			(x, y)				(x, y+1)
	 * 
	 * (x+1, y-1)		(x+1, y)			(x+1, y+1)	
	 * 
	 * (This radius is for offSet 1)
	 * We can understand by this that we want to travel 
	 * From : x - offSet until x + offSet
	 * From y - offSet until y + offSet
	 * 
	 */
	private boolean pacmanInRadius() {

		// Extracting Pacman position
		Point pac = points[(pacY-7)/PacmanBoard.BLOCK_SIZE][(pacX-7)/PacmanBoard.BLOCK_SIZE];

		for (int i = start.getIndRow() - chaseRadius; i <= start.getIndRow() +chaseRadius ; i++) {
			for (int j = start.getIndCol() - chaseRadius; j <= start.getIndCol() +chaseRadius ; j++) {
				if(i< points.length && j < points.length)
				{
					// if Pacman is equal to the point, it means its in the radius
					if(pac.getIndRow()== i && pac.getIndCol()==j) {
						return true;
					}
				}
			}
		}
		return false;
	}


	// function that is used for setting the right destination for the bfs algorithm
	@Override
	public Point destination() {

		points = (Point[][]) graph.getGraph();

		// checking the radius of the Pacman in order to determine which state should the ghost perform.
		if (pacmanInRadius())
			des = points[(pacY-7)/PacmanBoard.BLOCK_SIZE][(pacX-7)/PacmanBoard.BLOCK_SIZE];

		// if its not in the radius -> stay at static position
		else {
			des = setGhostCoordinate();
		}

		return des;
	}

	// function that is used to determine which coordinates for the patrol needed : 
	private Point setGhostCoordinate() {
		if(firstEntrence) { // we check in order to save the static position of the ghost
			firstEntrence = false;
			staticPosition = start;

		}
		// if the ghost reaches the destination, then we set its path to go to the start
		else {
			des = staticPosition; // if the ghost went on bfs run, then it should return to the static position
		}
		return des;
	}

}
