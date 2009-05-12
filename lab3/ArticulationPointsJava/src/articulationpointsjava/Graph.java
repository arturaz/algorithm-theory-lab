package articulationpointsjava;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
  HashMap<Integer,Vertex> vertexes = new HashMap<Integer,Vertex>();

  void initVertex(int number) {
    if (! vertexes.containsKey(number))
      vertexes.put(number, new Vertex(number));
  }

  void connect(int source, int target) {
    this.connect(source, target, true);
  }

  void connect(int source, int target, boolean bidirectional) {
//    this.initVertex(source);
//    this.initVertex(target);
    vertexes.get(source).connect(vertexes.get(target), bidirectional);
  }

  void reset() {
    for (Vertex v: vertexes.values()) {
      v.visited = false;
      v.num = -1;
      v.low = -1;
    }
  }

  private int counter;
  private ArrayList<Vertex> apoints;
  private Vertex root;
  private ArrayList<Vertex> root_children;

  ArrayList<Vertex> findArt(int number) {
    Vertex vertex = vertexes.get(number);

    counter = 0;
    apoints = new ArrayList<Vertex>();
    root = vertex;
    root_children = new ArrayList<Vertex>();
    findArtRecursive(vertex);
    if (root_children.size() >= 2)
      apoints.add(root);
    return apoints;
  }

  void findArtRecursive(Vertex vertex) {
    Main.operations += 3;
    vertex.visited = true;
    counter += 1;
    vertex.low = vertex.num = counter;

    // O(n)
    for (Vertex neighbor: vertex.neighbors) {
      if (! neighbor.visited) { // Forward edge
        Main.operations += 4;
        neighbor.parent = vertex;
        if (vertex == root) {
          Main.operations += 1;
          root_children.add(neighbor);
        }

        findArtRecursive(neighbor);

        Main.operations += 2;
        if (vertex != root && neighbor.low >= vertex.num)
          apoints.add(vertex);
        vertex.low = Math.min(vertex.low, neighbor.low); // Rule 3
      }
      else if (vertex.parent != neighbor) { // Back edge
        Main.operations += 2;
        vertex.low = Math.min(vertex.low, neighbor.num); // Rule 2
      }
    }
  }

  @Override
  public String toString() {
    String s = String.format("Graph of %d vertexes:\n", vertexes.size());

    for (Vertex v: vertexes.values()) {
      s += v.toString() + "\n";
    }
    return s;
  }
}
