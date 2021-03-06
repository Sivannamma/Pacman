import java.util.HashMap;

public class PacmanGraph implements IGraph<String, Point> {
	Point [][] graph;
	
	// constructor  : 
	public PacmanGraph(Point[][] mat) {
		graph = mat ; // shallow copy because want to change the changes to appear if happens
	}
	
	
	/*
	 * This function is receiving a certain node, and returning hashmap with the neighbors it can go to.
	 * for example if we are at (0,0) we can't go left and up, therefore hm.size() will be 2.
	 */
	@Override
	public HashMap<String, Point> neighbors(Point node) {
		HashMap<String, Point> nei = new HashMap<String, Point>();
		
		// UP
		if(node.getIndRow()-1 >= 0 && graph[node.getIndRow()-1][node.getIndCol()].isWalkable()) // it means we can walk up
			nei.put("UP", graph[node.getIndRow()-1][node.getIndCol()]);
		// DOWN
		if(node.getIndRow()+1 < graph.length && graph[node.getIndRow()+1][node.getIndCol()].isWalkable()) // it means we can down down
			nei.put("DOWN", graph[node.getIndRow()+1][node.getIndCol()]);
		// LEFT
		if(node.getIndCol()-1 >= 0 && graph[node.getIndRow()][node.getIndCol()-1].isWalkable()) // it means we can walk left
			nei.put("LEFT", graph[node.getIndRow()][node.getIndCol()-1]);
		// RIGHT
		if(node.getIndCol()+1 < graph.length && graph[node.getIndRow()][node.getIndCol()+1].isWalkable()) // it means we can walk right
			nei.put("RIGHT", graph[node.getIndRow()][node.getIndCol()+1]);

		return nei;
	}


	@Override
	public Point[][] getGraph() {
		return graph;
	}

}
