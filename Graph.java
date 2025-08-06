package cs3110.hw3;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class Graph<T> {
	
	private Map<T, List<T>> adj;
	private int edgeCount;

    /* This class must have a default constructor. */
    public Graph() {
    	this.adj = new HashMap<>();
    	this.edgeCount = 0;
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param u
     * @return False if vertex u was already in the graph, True otherwise.
     */
    public boolean addVertex(T u) {
    	if(adj.containsKey(u))
			return false;
		adj.put(u, new ArrayList<>());
		return true;
    }

    /**
     * Adds edge (u,v) to the graph.
     *
     * @param u
     * @param v
     * @return Returns false if the edge was already present, true otherwise.
     */
    public boolean addEdge(T u, T v) {
    	addVertex(u);
    	addVertex(v);
    	// make sure u and v are already in the graph
    	
    	List<T> neighbors = adj.get(u);
    	
    	if(neighbors.contains(v))
    		return false;
    	//checks to see if the neighbor is there if it isnt return false add it and +1 edgeCount
    	neighbors.add(v);
    	edgeCount++;
    	return true;
    	
    }

    /**
     * @return |V|
     */
    public int getVertexCount() {
        return adj.size();
    }

    /**
     * @param v
     * @return True if vertex v is present in the graph, false otherwise.
     */
    public boolean hasVertex(T v) {
    	return adj.containsKey(v);
    }

    /**
     * Provides access to every vertex in the graph.
     *
     * @return An object that iterates over V.
     */
    public Iterable<T> getVertices() {
        return adj.keySet();
    }

    /**
     * @return |E|
     */
    public int getEdgeCount() {
        return edgeCount;
    }

    /**
     * @param u One endpoint of the edge.
     * @param v The other endpoint of the edge.
     * @return True if edge (u,v) is present in the graph, false otherwise.
     */
    public boolean hasEdge(T u, T v) {
    	if (!adj.containsKey(u)) {
            return false;
        }
        return adj.get(u).contains(v);
    }

    /**
     * Returns all neighbors of vertex u.
     *
     * @param u A vertex.
     * @return The neighbors of u.
     */
    public Iterable<T> getNeighbors(T u) {
        return adj.get(u);
    }


    /**
     * @param u
     * @param v
     * @return True if v is a neighbor of u.
     */
    public boolean isNeighbor(T u, T v) {
        return hasEdge(u,v);
    }

    /**
     * Finds the length of the shortest path from vertex s to all other vertices in the graph.
     *
     * @param s The source vertex.
     * @return A map of shortest path distances. Every reachable vertex v should be present as a key mapped to the length of the shortest s->v path. 
     */
    public Map<T, Long> getShortestPaths(T s) {
        Map<T, Long> dist = new HashMap<>();
        
        //if s isnt in the graph just return 0s hashmap
        if(!adj.containsKey(s))
        	return dist;
        
        Queue<T> queue = new LinkedList<>();
        dist.put(s, (long)0);
        queue.add(s);
        
        //enter the first vertex into the hashmap
        while(!queue.isEmpty()) {
        	T u = queue.poll();
        	Long distanceU = dist.get(u);
        	//for every vertex that hasnt been seen we look up distance and add 1
        	for(T v : adj.get(u)) {
        		if(!dist.containsKey(v)) {
        			dist.put(v, distanceU + 1);
        			queue.add(v);
        		}
        	}
        }
        return dist;
    }
    
    
    
    public static void main(String[] args) {
    	Graph<String> g = new Graph<>();
        g.addEdge("A", "B");
        g.addEdge("A", "C");
        g.addEdge("B", "C");
        g.addVertex("D");

        /*				A
         * 			   / \
         * 			  v   v
         * 			  B -> C
         *           
         *           D
         */
        System.out.println("Vertices 4:" + g.getVertexCount());
        System.out.println("Edges 3:" + g.getEdgeCount());

        // presence checks
        System.out.println("Has vertex D TRUE:     " + g.hasVertex("D"));      // true
        System.out.println("Has edge A to B TRUE:      " + g.hasEdge("A", "B"));   // true
        System.out.println("Has edge C to A FALSE:      " + g.hasEdge("C", "A"));   // false

        System.out.print("All vertices:       ");
        for (String v : g.getVertices()) {
            System.out.print(v + " ");
        }
        System.out.println();

        System.out.print("Neighbors of A:");
        for (String nbr : g.getNeighbors("A")) {
            System.out.print(nbr + " ");
        }
        System.out.println();
        System.out.println("Distances from A:");
        Map<String, Long> dist = g.getShortestPaths("A");
        for (Map.Entry<String, Long> e : dist.entrySet()) {
            System.out.printf("  %s -> %d%n", e.getKey(), e.getValue());
        }
    }
}
