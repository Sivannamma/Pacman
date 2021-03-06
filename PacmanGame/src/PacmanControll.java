import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;

/*
 * This class will handle the player movement, if the player can walk or not.
 */
public class PacmanControll extends KeyAdapter implements Runnable{

	// movement:
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	// jumping cell :
	private int cellSize;
	// coordinates :
	private int corX;
	private int corY;
	private MovementConnecter pacmanBoard;
	private PacmanGraph graph;
	private Point[][] points;
	
	// constructor : 
	public PacmanControll(int corX, int corY , int cellSize, MovementConnecter pacmanBoard, Point[][] graph) { // the coordinates of the player in the beggining
		this.corX= corX;
		this.corY=corY;		
		this.cellSize=cellSize;		
		this.pacmanBoard = pacmanBoard;		
		this.points = graph;
		this.graph = new PacmanGraph(graph);
	}

	// checking movement of the player
	@Override
	public void run() {
		try {
			while (true) {
				
				// we get the current location
				int locX =(this.corX-7)/this.cellSize;
				int locY =(this.corY-7)/this.cellSize;
				
				// we check the neighbors in order to determine which direction we can move
				HashMap<String, Point> hm = graph.neighbors(points[locY][locX]);
				
				
				
				// checking if valid move
				if(!hm.containsKey("LEFT"))
					left = false;
				if(!hm.containsKey("RIGHT"))
					right = false;
				if(!hm.containsKey("UP"))
					up = false;
				if(!hm.containsKey("DOWN"))
					down = false;


				if (left && up) {
					corY -= cellSize;
					pacmanBoard.setCoordinates(corX, corY);	// setting the coordinates of the pacman
				}

				else if (left && down) {
					corY += cellSize;
					pacmanBoard.setCoordinates(corX, corY);	// setting the coordinates of the pacman
				}

				else if (right && up) {
					corY -= cellSize;
					pacmanBoard.setCoordinates(corX, corY);	// setting the coordinates of the pacman
				}

				else if (right && down) {
					corX += cellSize;
					pacmanBoard.setCoordinates(corX, corY);	// setting the coordinates of the pacman
				}

				else if (left) {
					corX -= cellSize;
					pacmanBoard.setCoordinates(corX, corY);	// setting the coordinates of the pacman
				}

				else if (up) {
					corY -= cellSize;
					pacmanBoard.setCoordinates(corX, corY);	// setting the coordinates of the pacman
				}

				else if (right) {
					corX += cellSize;
					pacmanBoard.setCoordinates(corX, corY);	// setting the coordinates of the pacman
				}

				else if (down) {
					corY += cellSize;
					pacmanBoard.setCoordinates(corX, corY);	// setting the coordinates of the pacman
				}
				// setting the right coordinates of the Pacman, inside the ghost class
				ChasingBFS.setCoordinates(corX, corY);
				ChasingNearBy.setCoordinates(corX, corY);
				Thread.sleep(100);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(0);
		}
	}

	//To handle key pressing : 
	@Override
	public void keyPressed(KeyEvent e) {
		
		// checking which key is pressed
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT) left = true;
		if (key == KeyEvent.VK_RIGHT) right = true;
		if (key == KeyEvent.VK_UP) up = true;
		if (key == KeyEvent.VK_DOWN) down = true;

	}

	// To handle key releasing : 
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down = false;
		}
	}
}
