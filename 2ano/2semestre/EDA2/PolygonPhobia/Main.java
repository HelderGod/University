import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        int numberOfSegments = Integer.parseInt(s.readLine());
        System.out.println(numberOfSegments);
        Polygon p = new Polygon(numberOfSegments);
        for(int i=0; i<numberOfSegments; i++) {
            String[] segments = s.readLine().split(" ");
            p.checkVertex(new Vertex(Integer.parseInt(segments[0]), Integer.parseInt(segments[1])));
            p.checkVertex(new Vertex(Integer.parseInt(segments[2]), Integer.parseInt(segments[3])));
        }
        p.addToArray();
        System.out.println("olá");
        p.printArray();
    }
}

class Edge {
    Vertex destiny;
}

class Vertex {
    List<Edge> destination;
    public int x;
    public int y;

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Polygon {
    Vertex[] v;
    public int numberOfSegments;
    public boolean[][] exists;
    private int numberOfVertexes;
    private Queue<Vertex> Q;

    public Polygon(int numberOfSegments) {
        this.numberOfSegments = numberOfSegments;
        exists = new boolean[1000][1000];
        for(int i=0; i<1000; i++) {
            for(int j=0; j<1000; j++)
                exists[i][j] = false;
        }
        numberOfVertexes = 0;
        Q = new LinkedList<>();
    }

    public void checkVertex(Vertex u) {
        if(exists[u.x][u.y] == false) {
            exists[u.x][u.y] = true;
            numberOfVertexes++;
            Q.add(u);
        }
    }

    public void addToArray() {
        for(int i=0; !Q.isEmpty(); i++)
            v[i] = Q.poll();
    }

    public void printArray() {
        for(int i=0; i<v.length; i++)
            System.out.println("(" + v[i].x + "; " + v[i].y + ")");
        System.out.println("numberOfVertexes: " + numberOfVertexes);
    }
}