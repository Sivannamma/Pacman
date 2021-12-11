import javax.swing.JFrame;

/*
 * This class is intended to open our GUI window
 */

public class Gui  extends JFrame {

	private static final long serialVersionUID = 1L;

	// constructor : 
	Gui() {
		
		initWindow(); // calling the initWindow(); function in order to start our window.
	}

	private void initWindow() {
		ReadFromFile g = new ReadFromFile("file.txt");
		int [] board = g.convertFileToIntArray();
		int cellSize = 24;
		int matSize = (int)(Math.sqrt(board.length));
		add(new PacmanBoard(cellSize,matSize, board)); // calling the PacmanBoard class

		setResizable(false); // setting to be un-resizeable 
		pack(); // The pack() method is defined in Window class in Java and it sizes the frame so that all its contents are at or above their preferred sizes.

		setTitle("Pacman"); // setting the window title
		setLocationRelativeTo(null); // setting window in the middle of the display
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // finishing the thread when quiting the game

	}

}
