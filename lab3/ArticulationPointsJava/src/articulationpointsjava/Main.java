package articulationpointsjava;

import java.util.LinkedList;

public class Main {
    static int operations = 0;


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
      int start = 500000;
      int step  = 100000;
      int end = 120000000;

      int last_size = 0;
      int edges = 0;
      Graph g = new Graph();
      LinkedList<Integer> lastVertexes = new LinkedList<Integer>();

      for (int vertexes = start; vertexes < end; vertexes += step) {
        long start_time;
        
        for (int i = last_size; i < vertexes; i++) {
          g.initVertex(i);

          edges += lastVertexes.size();
          for (int number: lastVertexes) {
            g.connect(i, number);
          }

          // Keep last
          lastVertexes.addLast(i);
          while (lastVertexes.size() > 2) {
            lastVertexes.removeFirst();
          }
        }
        last_size = vertexes;

        int times = 10;
        long time = 0;
        for (int i = 0; i < times; i++) {
          operations = 0;
          g.reset();

          System.out.print(i);
          System.out.flush();
          start_time = System.currentTimeMillis();
          g.findArt(0);
          time += System.currentTimeMillis() - start_time;
          System.out.print(". ");
          System.out.flush();
        }

        System.out.println(String.format(
                "%10d %10d %10d %8.3f",
                vertexes, edges,
                operations,
                (float)time / times / 1000
                ));
        System.out.flush();
      }
//      Graph g = new Graph();
//      g.connect(0, 1);
//      g.connect(0, 2);
//      g.connect(3, 1);
//      g.connect(3, 2);
//      g.connect(3, 4);
//      g.connect(2, 5);
//      g.connect(2, 6);
//      g.connect(5, 6);
//
//      System.out.println(g.toString());
//
//      ArrayList<Vertex> points = g.findArt(0);
//      System.out.println(points.toString());
    }
}
