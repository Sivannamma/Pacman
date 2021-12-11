import java.util.HashMap;

public interface IGraph<T, V> {
	HashMap<T , V> neighbors (V node); // function that recieves hashmap of neighbors
	V[][] getGraph(); // getting the graph by getting the V matrix we can go over
}
