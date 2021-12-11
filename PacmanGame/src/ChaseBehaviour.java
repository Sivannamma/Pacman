import java.util.ArrayList;

/*
 * This interface will handle each ghost.
 * The ghost will implement this interface, and each ghost will have different behavior.
 */
public interface ChaseBehaviour { 
 ArrayList<Point> chase(Point start, IGraph<?, ?> graph);
 Point destination(); // destination to the chase function
}
