import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        String[] peopleAndRelations = s.readLine().split(" ");
        int numberOfPeople = Integer.parseInt(peopleAndRelations[0]);
        int numberOfRelations = Integer.parseInt(peopleAndRelations[1]);
        SocialNetwork n = new SocialNetwork(numberOfPeople);
        for(int i=0; i<numberOfRelations; i++) {
            String[] line = s.readLine().split(" ");
            n.addRelation(Integer.parseInt(line[0]), Integer.parseInt(line[1]), Integer.parseInt(line[2]));
        }
        System.out.println(n.prim());
    }
}

class Edge implements Comparable<Edge>{
    public int destination;
    public int weight;

    public Edge(int destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public int compareTo(Edge node1, Edge node2) {
        if()
    }
}

class SocialNetwork {
    private int nPersons;
    List<Edge>[] adjacents;
    int[][] daysInSN;
    Queue<Integer> Q;

    @SuppressWarnings("unchecked")
    public SocialNetwork(int nPersons) {
        this.nPersons = nPersons + 1;
        adjacents = new List[this.nPersons];
    }
    
    public void addRelation(int person1, int person2, int days) {
        E
    }
    
    private static enum Colour {
        WHITE,
        GREY,
        BLACK
    };

    public int prim() {
        Colour[] colour = new Colour[nPersons];
        int result = 0;
        int max = Integer.MIN_VALUE;
        int maxIndex = 1;
        for(int i=0; i<nPersons; i++)
            colour[i] = Colour.WHITE;
        for(int i=1; i<nPersons-1; i++) {
            for(int j=1; j<nPersons; j++) {
                if(daysInSN[i][j] != Integer.MIN_VALUE) {
                    if(daysInSN[i][j] > max) {
                        maxIndex = j;
                        max = daysInSN[i][maxIndex];
                    }
                }
            }
            result += max;
            System.out.println(result);
        }
        return result;
    }
}