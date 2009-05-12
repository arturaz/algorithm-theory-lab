package articulationpointsjava;

import java.util.ArrayList;

public class Vertex {
  public int number = 0;
  public ArrayList<Vertex> neighbors = new ArrayList<Vertex>();
  public boolean visited = false;
  public int num = -1;
  public int low = -1;
  public Vertex parent = null;

  public Vertex(int number) {
    this.number = number;
  }

  public void connect(Vertex vertex) {
    this.connect(vertex, true);
  }

  public void connect(Vertex vertex, boolean bidirectional) {
    if (! neighbors.contains(vertex)) {
      neighbors.add(vertex);
      if (bidirectional)
        vertex.connect(this, false);
    }
  }

  @Override
  public String toString() {
    String s = String.format(" %-4d: ", this.number);
    for (Vertex v: neighbors) {
      s += (new Integer(v.number)).toString() + ", ";
    }
    s += String.format("(visited: %s, num: %d, low: %d)",
            this.visited, this.num, this.low);
    return s;
  }
}
