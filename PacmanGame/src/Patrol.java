import java.util.ArrayList;

public class Patrol implements ChaseBehaviour {
	private Point [][] points; // our points array (represent the game area)
	private IGraph graph;
	private Point startPosition;
	private Point endPosition;
	private Boolean firstEntrence;
	
	Patrol(){
		firstEntrence = true;
	}
	
	@Override
	public ArrayList<Point> chase(Point start, IGraph graph) {
		points = (Point[][]) graph.getGraph();
		// only first entrence because we wan't to random only once the destination position
		if(firstEntrence) {
			firstEntrence = false;
			startPosition = start;
			endPosition = destination();
		}
		// if the ghost reaches the destination, then we set its path to go to the start
		if(start==endPosition) {
			endPosition = startPosition;
			startPosition = start;
		}
		return new BFS(start , endPosition , graph).performBFS();
	}

	@Override
    public Point destination() {
		// random points for the patrol.
        int endX = (int) (Math.random() * PacmanBoard.MATRIX_SIZE);
        int startX = (int) (Math.random() * PacmanBoard.MATRIX_SIZE);
        Point end = points[startX][endX];
        return end;
    }


}