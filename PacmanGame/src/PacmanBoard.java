import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/*
 * This class is used for creating the actual board of the Pacman
 * We are reading the Pacman matrix from file and then initializing the board accordingly.
 * 
 * The matrix values : 
 * 0 -> blocks inside the Pacman area.
 * 1 -> the outline's of the game (the board lines)
 * 2 -> where the player can walk (also where we can place points to collect)
 */
public class PacmanBoard extends JPanel implements ActionListener, MovementConnecter {

	private static final long serialVersionUID = 1L;

	// offset for the borders
	private final int bourderOffset = 25;
	// variable for the block size (how big each cell is)
	public static int BLOCK_SIZE;
	// variable for the matrix size
	public static int MATRIX_SIZE;
	// variable for the screen size
	private static int SCREEN_SIZE;
	// to place every line in an offset from the corner of the screen
	private int pointsOffset = 5;

	private Image pacman;

	private Image [] ghost = new Image [2];
	private Ghost [] ghostClass = new Ghost [2];

	private int corX, corY;
	private Timer timer;
	private int DELAY = 80;

	private PacmanGraph graph;

	// points matrix
	private Point[][] points;

	// score for the pacman (how many points collected)
	private JLabel score;
	private int scoreCounter;

	// hearts :
	private static ArrayList<Image> hearts;
	private static int lives;

	private final short board[] = {
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,

	};

	//	private final short board[] = {
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//			2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2,
	//			2, 2, 2, 2,
	//
	//	};

	// constructor :
	PacmanBoard(int blockSize, int matrixSize) {
		// initializing :
		BLOCK_SIZE = blockSize;
		MATRIX_SIZE = matrixSize;

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

		// pacman
		PacmanControll pac = new PacmanControll(corX, corY, BLOCK_SIZE, this, points);
		Thread thread = new Thread(pac);
		thread.start();
		// listening to events by the pacman
		addKeyListener(pac);

		// score label
		setScoreLabel();

		// setting screen
		setBackground(Color.black);
		setFocusable(true);
		setPreferredSize(new Dimension(SCREEN_SIZE+ BLOCK_SIZE, SCREEN_SIZE + BLOCK_SIZE*2));
	}

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
	// pacman image
	private void loadPacman() {
		ImageIcon iid = new ImageIcon("Images/pacman.png");
		pacman = iid.getImage();
		pacman = pacman.getScaledInstance(BLOCK_SIZE, BLOCK_SIZE, Image.SCALE_DEFAULT);
	}


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
	// ghosts image and threads
	private void loadGhosts() {
		ImageIcon iid = new ImageIcon("Images/ghost.png");
		ghost[0] = iid.getImage();
		ghost[0] = ghost[0].getScaledInstance(BLOCK_SIZE, BLOCK_SIZE, Image.SCALE_DEFAULT);

		ghostClass[0] = new Ghost(76, 175, new ChasingBFS(), graph, 200);
		Thread thread = new Thread(ghostClass[0]);
		thread.start();

		ImageIcon iid2 = new ImageIcon("Images/pinkGhost.png");
		ghost[1] = iid2.getImage();
		ghost[1] = ghost[1].getScaledInstance(BLOCK_SIZE, BLOCK_SIZE, Image.SCALE_DEFAULT);

		ghostClass[1] = new Ghost(127, 151, new Patrol(), graph, 200);
		Thread thread2 = new Thread(ghostClass[1]);
		thread2.start();
	}

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
					g2d.drawLine(x + bourderOffset + pointsOffset, y + pointsOffset, x + bourderOffset + pointsOffset,
							y + BLOCK_SIZE - 1 + pointsOffset);
				}
				// drawing lines horizontal lines :
				if (y == 0) {
					g2d.drawLine(x + pointsOffset, y + pointsOffset, x + BLOCK_SIZE - 1 + pointsOffset,
							y + pointsOffset);
				}
				// drawing lines horizontal lines :
				if (y == SCREEN_SIZE - BLOCK_SIZE) {
					g2d.drawLine(x + pointsOffset, y + bourderOffset + pointsOffset, x + BLOCK_SIZE - 1 + pointsOffset,
							y + bourderOffset + pointsOffset);
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
		drawBoard(graphicComponent);
		//drawing the pacman
		g.drawImage(pacman, corX, corY, this);

		// drawing the score and checking if there is a point
		int locX = (this.corX - 7) / BLOCK_SIZE;
		int locY = (this.corY - 7) / BLOCK_SIZE;
		if(board[locY * MATRIX_SIZE + locX] == 2) {
			board[locY * MATRIX_SIZE + locX] = 5; // symbolize as eaten

			score.setText("SCORE: " + scoreCounter++);
		}
		
		// drawing ghosts:
		g.drawImage(ghost[0], ghostClass[0].getCorX(), ghostClass[0].getCorY(), this);
		g.drawImage(ghost[1], ghostClass[1].getCorX(), ghostClass[1].getCorY(), this);
		
		// drawing hears
		int heartOffset =pointsOffset*3;
		for (int i = 0; i < lives; i++) {
			g.drawImage(hearts.get(i),(i+7) *BLOCK_SIZE, SCREEN_SIZE+ heartOffset, this);
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
	
	public static void removeHeart() {
		lives--;
		if(hearts.size()>0)
		hearts.remove(hearts.size()-1);
	}

}
