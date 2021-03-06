import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/*
 * BFS algorithm :
 * given a matrix we can travel on.
 * start point & end point 
 * 
 * BFS can be used to find single source shortest path in an unweighed graph, 
 * because in BFS, we reach a vertex with minimum number of edges from a source vertex.
 * 
 * Each point can have at most 4 neighbors (left, right, up, down)
 * Nodes are not visited twice.
 * 
 * Extracting neighbors = O(1) , using the interface.
 * Looping on all nodes = O(V) (worst case scenario) , nodes are not inserted twice-> inserting only those who are not visited yet.
 * 
 * Collection.reverse complexity is O(V/2) at worst case (from one edge to another).
 */
public class BFS {

	// start position 
	Point start;
	// end position
	Point end;
	// our graph
	IGraph graph;

	// queue for bfs, holds the next nodes we need to reach
	private Queue<Point> queue;
	// holds the nodes that we already visited
	private HashSet<Point> blackend;
	// each node we visited - we mark his parent in order to extract the path
	private	HashMap<Point, Point> previous;
	// will hold our moves , from the bfs
	private ArrayList<Point> path;


	BFS(Point start, Point end, IGraph graph) {
		this.start = start;
		this.end = end;
		this.graph= graph;

		// initializing
		this.queue = new LinkedList<>();
		this.blackend = new HashSet<>();
		this.previous = new HashMap<>();
		this.path=new ArrayList<Point>();
	}

	public ArrayList<Point> performBFS() {
		clearBFS(); // each bfs we clear our list

		queue.add(start);
		blackend.add(start);

		// as long as we have more nodes to reach :
		while (!queue.isEmpty()) {

			// getting the current node we want to work on
			Point current = queue.poll();

			// reached the destination
			if (current == end) {
				path.add(end);

				while (previous.containsKey(current)) {
					current = previous.get(current);
					path.add(current);
				}

				Collections.reverse(path);
				break;

			} else {
				// extracting our neighbors : 
				HashMap<String, Point> neighbors = graph.neighbors(current);
				// going over each neighbor
				for (Point p : neighbors.values()) {
					if (blackend.contains(p)) {
						continue;
					}
					queue.add(p);
					blackend.add(p);
					previous.put(p, current);
				}
			}
		}
		return path;
	}



	//Cleaning the content before we use this for bfs function
	private void clearBFS() {
		queue.clear();
		blackend.clear();
		previous.clear();
		path.clear();
	}
}
