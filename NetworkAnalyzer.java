import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class NetworkAnalyzer {
	
	//be able to use the graph class that was made
	private Graph<String> graph;
	
    public NetworkAnalyzer(List<String> hosts, Map<String,String> simplexConnections, Map<String,String> duplexConnections) {
        this.graph = new Graph<>();
        //put all of the hosts into a graph
        for(String h : hosts)
        	graph.addVertex(h);
        
        //wire up the connections that go one way in the directed graph
        for(Map.Entry<String,String> sConnect : simplexConnections.entrySet()) {
        	String u = sConnect.getKey();
        	String v = sConnect.getValue();
        	graph.addEdge(u, v);
        }
        
        //wire up the connections that go both ways in the directed graph
        for(Map.Entry<String,String> dConnect : duplexConnections.entrySet()) {
        	String u = dConnect.getKey();
        	String v = dConnect.getValue();
        	graph.addEdge(u, v);
        	graph.addEdge(v, u);
        }
    }

    public long findMaxDeliveryTime() {
        long max = (long) 0;
        
        for( String host : graph.getVertices()) {
        	Map<String, Long> dist = graph.getShortestPaths(host);
        	//update max with whatever is greatest of the optimal routes
        	for( long d : dist.values()) {
        		if(d > max)
        			max = d;
        	}
        }
        return max;
    }
    
    
    // Kosaraju’s algorithm method here
    public int countMinToReconnect(Map<String,String> simplexConnectionsToRemove) {
    	// duplicate the graph and remove specified simplex edges
        Graph<String> temp = new Graph<>();
        for (String u : graph.getVertices()) {
            temp.addVertex(u);
        }
        for (String u : graph.getVertices()) {
            for (String v : graph.getNeighbors(u)) {
                if (!simplexConnectionsToRemove.containsKey(u) ||
                    !simplexConnectionsToRemove.get(u).equals(v)) {
                    temp.addEdge(u, v);
                }
            }
        }
        
     // build reverse adjacency for temp
        
        Map<String, List<String>> revAdj = new HashMap<>();
        for (String u : temp.getVertices()) {
            revAdj.put(u, new ArrayList<>());
        }
        for (String u : temp.getVertices()) {
            for (String v : temp.getNeighbors(u)) {
                revAdj.get(v).add(u);
            }
        }
        
     // DFS to get finishing order
        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        for (String u : temp.getVertices()) {
            if (!visited.contains(u)) {
                dfs1(u, visited, stack, temp);
            }
        }

        // DFS on reversed graph to collect strong connected components
        visited.clear();
        List<List<String>> sccs = new ArrayList<>();
        while (!stack.isEmpty()) {
            String u = stack.pop();
            if (!visited.contains(u)) {
                List<String> comp = new ArrayList<>();
                dfs2(u, visited, comp, revAdj);
                sccs.add(comp);
            }
        }

        int n = sccs.size();
        if (n == 1) {
            return 0;
        }

        // Map each vertex to its SCC index
        Map<String, Integer> compId = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (String u : sccs.get(i)) {
                compId.put(u, i);
            }
        }

        // count incoming and outgoing edges between components
        
        boolean[] hasIn = new boolean[n];
        boolean[] hasOut = new boolean[n];
        for (String u : temp.getVertices()) {
            for (String v : temp.getNeighbors(u)) {
                int cu = compId.get(u), cv = compId.get(v);
                if (cu != cv) {
                    hasOut[cu] = true;
                    hasIn[cv] = true;
                }
            }
        }

        // count source beginning points and end points
        int sources = 0, ends = 0;
        for (int i = 0; i < n; i++) {
            if (!hasIn[i]) sources++;
            if (!hasOut[i]) ends++;
        }

        // Minimum dually connected links needed
        return Math.max(sources, ends); 
    }
    
 // dfs 1st iteration
    private void dfs1(String u, Set<String> visited, Deque<String> stack, Graph<String> temp) {
        visited.add(u);
        for (String v : temp.getNeighbors(u)) {
            if (!visited.contains(v)) {
                dfs1(v, visited, stack, temp);
            }
        }
        stack.push(u);
    }

 //dfs 2nd iteration
    private void dfs2(String u, Set<String> visited, List<String> comp, Map<String, List<String>> revAdj) {
        visited.add(u);
        comp.add(u);
        for (String v : revAdj.get(u)) {
            if (!visited.contains(v)) {
                dfs2(v, visited, comp, revAdj);
            }
        }
    }
}
