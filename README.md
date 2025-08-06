Graph.java (README):
Implements a Graph<T> backed by a Map<T, List<T>>, offering methods to add vertices/edges and compute shortest-path distances via BFS.

NetworkAnalyzer.java (README):
Uses Graph<String> to build a directed host network from simplex and duplex links, then computes the worst-case delivery time with repeated BFS and infers reconnection needs by counting strongly connected components.
