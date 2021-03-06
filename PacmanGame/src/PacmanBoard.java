import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;


/*
 * This class is used for creating the actual board of the Pacman
 * We received Pacman matrix from file and now we initialize the board accordingly.
 * 
 * The matrix values : 
 * 0 -> blocks inside the Pacman area.
 * 2 -> where the player can walk (also where we can place points to collect)
 */
public class PacmanBoard extends JPanel implements ActionListener, MovementConnecter {

	private static final long serialVersionUID = 1L;
	// offset for the borders
	private final int borderOffset = 25;
	// variable for the block size (how big each cell is)
	public static int BLOCK_SIZE;
	// variable for the matrix size
	public static int MATRIX_SIZE;
	// variable for the screen size
	private static int SCREEN_SIZE;
	// to place every line in an offset from the corner of the screen
	private int pointsOffset = 5;

	private Image pacman;

	// ghosts
	private ImageIcon iid [] = new ImageIcon[5];
	private Image [] ghost = new Image [5];
	private Ghost [] ghostClass = new Ghost [5];

	// Pacman coordinates 
	private volatile int corX, corY; // volatile in order that the class that responsible for the collision will be able to see the variables and the changes 

	// variable in order to make the actionPerformed that calls repaint to work
	private Timer timer;
	private int DELAY = 80;

	private PacmanGraph graph;

	// points matrix
	private Point[][] points;

	// score for the Pacman (how many points collected)
	private JLabel score;
	private int scoreCounter;

	// hearts :
	private static ArrayList<Image> hearts;
	private int lives;

	private final int board[];

	// constructor :
	PacmanBoard(int blockSize, int matrixSize, int[] board) {
		// initializing :
		BLOCK_SIZE = blockSize;
		MATRIX_SIZE = matrixSize;
		this.board=board;
		// (valued as size of the matrix multiply by cell size)
		SCREEN_SIZE = BLOCK_SIZE * MATRIX_SIZE;

		// initializing the points matrix to our matrix size.
		points = new Point[MATRIX_SIZE][MATRIX_SIZE];

		// starting position : +2 in order to not be on the edge
		corX = pointsOffset + 2;
		corY = pointsOffset + 2;


		initPoints();
		initBoard();

		// setting the repaint to be called
		timer = new Timer(DELAY, this); // starting the timer which calls actionPerformed until it stops
		timer.start();

	}

	private void initBoard() {
		loadPacman();
		loadGhosts();
		loadHearts();
		loadCeckCollision();

		// Pacman
		PacmanControll pac = new PacmanControll(corX, corY, BLOCK_SIZE, this, points);
		Thread thread = new Thread(pac);
		thread.start();
		// listening to events by the Pacman
		addKeyListener(pac);

		// score label
		setScoreLabel();

		// setting screen
		setBackground(Color.black);
		setFocusable(true);
		setPreferredSize(new Dimension(SCREEN_SIZE+ BLOCK_SIZE, SCREEN_SIZE + BLOCK_SIZE*2));
	}
	
	// load hearts (set the heart images, adding to the array and setting amount of lives)
	private void loadHearts() {
		hearts = new ArrayList<Image>();
		lives =3;
		Image heart;
		ImageIcon h = new ImageIcon("Images/heart.png");
		heart= h.getImage();
		heart = heart.getScaledInstance(BLOCK_SIZE, BLOCK_SIZE, Image.SCALE_DEFAULT);	
		for (int i = 0; i <lives; i++) {
			hearts.add(heart);
		}

	}
	
	// Pacman image
	private void loadPacman() {
		ImageIcon player = new ImageIcon("Images/pacman.png");
		pacman = player.getImage();
		pacman = pacman.getScaledInstance(BLOCK_SIZE, BLOCK_SIZE, Image.SCALE_DEFAULT);
	}


	// initializing the score label - the number of points the Pacman collect will appear in the level
	private void setScoreLabel() {
		scoreCounter=0;
		this.setLayout(null); // in order to control the layout location

		score = new JLabel("SCORE");
		score.setForeground(Color.RED);
		score.setSize(40, 40);
		score.setBounds(7, SCREEN_SIZE+ pointsOffset*2, 70, 40);

		// adding the label to the panel
		this.add(score);
	}
	

	// function that returns a random valid position on the screen (in order to place the ghosts)
	public Point randomPos() {
		// random points for the patrol.
		int x =0;
		int y=0;
		Point random = points[y][x];
		do {
			x = (int) (Math.random() * PacmanBoard.MATRIX_SIZE);
			y = (int) (Math.random() * PacmanBoard.MATRIX_SIZE);
			random = points[x][y];
		} while(points[x][y].isWalkable()== false); // continue random until we receive a valid position
		
		return random;
	}


	// ghosts image & threads
	private void loadGhosts() {

		// which image to load
		String [] names = {"blueGhost.png", "blueGhost.png", "pinkGhost.png","blueGhost.png","pinkGhost.png"};
		// which behavior each ghost will receive
		ChaseBehaviour [] chase = {new ChasingNearBy(), new Patrol(), new Patrol(),new ChasingBFS(),new ChasingNearBy()};
		for (int i = 0; i < iid.length; i++) {
			iid[i] = new ImageIcon("Images/"+names[i]);
			ghost[i] = iid[i].getImage();
			ghost[i] = ghost[i].getScaledInstance(BLOCK_SIZE, BLOCK_SIZE, Image.SCALE_DEFAULT);

			Point pos =randomPos();
			ghostClass[i] = new Ghost(pos.getIndCol()*BLOCK_SIZE +7 , pos.getIndRow()*BLOCK_SIZE +7 , chase[i], graph, 200);
			Thread thread = new Thread(ghostClass[i]);
			thread.start();
		}
	}


	// initializing the inner class in order to check our Pacman and ghost collision
	private void loadCeckCollision() {
		Thread thread = new Thread(new collisonCheck());
		thread.start();
	}

	// responsible for drawing the Pacman board
	private void drawBoard(Graphics2D g2d) {
		// variable for the index of the matrix board
		short i = 0;
		// variables for looping over the matrix we want to draw on the panel
		int x, y;

		// looping my size in order to draw the board :
		for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
			for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

				g2d.setColor(new Color(0, 72, 251));
				g2d.setStroke(new BasicStroke(5));

				// drawing lines vertical lines :
				if (x == 0) {
					g2d.drawLine(x + pointsOffset, y + pointsOffset, x + pointsOffset,
							y + BLOCK_SIZE - 1 + pointsOffset);
				}
				// drawing lines vertical lines :
				if (x == SCREEN_SIZE - BLOCK_SIZE) {
					g2d.drawLine(x + borderOffset + pointsOffset, y + pointsOffset, x + borderOffset + pointsOffset,
							y + BLOCK_SIZE - 1 + pointsOffset);
				}
				// drawing lines horizontal lines :
				if (y == 0) {
					g2d.drawLine(x + pointsOffset, y + pointsOffset, x + BLOCK_SIZE - 1 + pointsOffset,
							y + pointsOffset);
				}
				// drawing lines horizontal lines :
				if (y == SCREEN_SIZE - BLOCK_SIZE) {
					g2d.drawLine(x + pointsOffset, y + borderOffset + pointsOffset, x + BLOCK_SIZE - 1 + pointsOffset,
							y + borderOffset + pointsOffset);
				}
				// we need to fill block ( where the player cannot walk)
				if (board[i] == 0) {
					g2d.fillRect(x + pointsOffset, y + pointsOffset, BLOCK_SIZE, BLOCK_SIZE);
				}

				// we want to fill the dots where the player can eat and walk
				if (board[i] == 2) {
					// color white :
					g2d.setColor(new Color(255, 255, 255));
					g2d.fillOval(x + (BLOCK_SIZE / 2), y + (BLOCK_SIZE / 2), 6, 6); // BLOCK_SIZE/2 because we want to
					// place in the middle
				}

				i++;
			}
		}
	}

	// initializing our points matrix according to 0\2 (isWalkable), and converting to 2D-array
	private void initPoints() {

		for (int row = 0; row < points.length; row++) {
			for (int column = 0; column < points.length; column++) {
				// if equals to 0 then its not walkable because it is a block.
				boolean isWalkable = board[row * MATRIX_SIZE + column] == 0 ? false : true;
				points[row][column] = new Point(row, column, isWalkable);
			}
		}
		graph = new PacmanGraph(points);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D graphicComponent = (Graphics2D) g;

		// drawing the board
		drawBoard(graphicComponent);

		// drawing the pacman
		g.drawImage(pacman, corX, corY, this);

		// drawing the score and checking if there is a point to collect
		drawPoints(g);

		// drawing ghosts
		drawGhost(g);

		// drawing hearts
		drawHearts(g);
	}


	private void drawGhost(Graphics g) {
		for (int i = 0; i < ghost.length; i++) {
			g.drawImage(ghost[i], ghostClass[i].getCorX(), ghostClass[i].getCorY(), this);
		}
	}

	private void drawHearts(Graphics g) {
		int heartOffset =pointsOffset*3;

		for (int i = 0; i < hearts.size(); i++) {
			g.drawImage(hearts.get(i),(i+7) *BLOCK_SIZE, SCREEN_SIZE+ heartOffset, this);
		}
	}

	private void drawPoints(Graphics g) {
		// drawing the score and checking if there is a point
		int locX = (this.corX - 7) / BLOCK_SIZE;
		int locY = (this.corY - 7) / BLOCK_SIZE;
		if(board[locY * MATRIX_SIZE + locX] == 2) {
			board[locY * MATRIX_SIZE + locX] = 5; // symbolize as eaten

			score.setText("SCORE: " + scoreCounter++);
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	// function of the interface that is connecting between pacmanControll and
	// pacmanBoard
	@Override
	public void setCoordinates(int x, int y) {
		this.corX = x;
		this.corY = y;
		repaint();
	}

	// if we have more lives, we remove one, otherwise, game is over
	public void removeHeart() {
		if(hearts.size()>0)
			hearts.remove(hearts.size()-1);
	}


	/*
	 * Inner class that is responsible for catching the collision between the ghosts and the Pacman player.
	 * It implements runnable because we want the thread to always check the position of all ghosts vs the Pacman and detect a collision
	 */
	private class collisonCheck implements Runnable{

		@Override
		public void run() {
			while(true) {
				for (int i = 0; i < ghostClass.length; i++) {
					//System.out.println("");
					if (ghostClass[i].getCorX() == corX && ghostClass[i].getCorY()==corY)
					{
						lives--;
						removeHeart();
						try {
							Thread.sleep(300); // small sleep in order not to the detect the same position
						}
						catch(Exception ex) {
							System.out.println("exception");
						}	
					}
				}
			}	
		}
	}
}
