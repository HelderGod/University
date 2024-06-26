import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        String[] info = s.readLine().split(" ");
        int minProms = Integer.parseInt(info[0]);
        int maxProms = Integer.parseInt(info[1]);
        int numOfEmployees = Integer.parseInt(info[2]);
        int numOfPrecedences = Integer.parseInt(info[3]);
        Organisation o = new Organisation(numOfEmployees, minProms, maxProms);
        for(int i=0; i<numOfPrecedences; i++) {
            String[] precedences = s.readLine().split(" ");
            int x = Integer.parseInt(precedences[0]);
            int y = Integer.parseInt(precedences[1]);
            o.addPrecedences(x, y);
        }
        int[] proms = o.checkProms();
        System.out.println(Arrays.toString(proms));
    }
}

class Organisation {
    List<Integer>[] adj;
    private int minProms;
    private int maxProms;
    private int numOfEmployees;
    private int[] precedences;

    private static enum Colour {
        WHITE,
        GREY,
        BLACK
    };

    @SuppressWarnings("unchecked")
    public Organisation(int numOfEmployees, int minProms, int maxProms) {
        this.minProms = minProms;
        this.maxProms = maxProms;
        this.numOfEmployees = numOfEmployees;
        precedences = new int[numOfEmployees];
        adj = new List[this.numOfEmployees];
        for(int i=0; i<this.numOfEmployees; i++)
            adj[i] = new LinkedList<>();
    }

    public void addPrecedences(int x, int y) {
        adj[y].add(x);
        precedences[y]++;
    }

    private void visitedNode(int v, Colour[] color, Queue<Integer> Q) {
        color[v] = Colour.BLACK;

        

        Q.add(v);
    }

    public int[] checkProms() {
        Queue<Integer> Q = new LinkedList<>();
        int count = 0;
        int[] proms = new int[3];

        Colour[] color = new Colour[numOfEmployees];

        for(int i=0; i<numOfEmployees; i++) {
            color[i] = Colour.WHITE;
            if(i < 3)
                proms[i] = -1;
        }

        for(int i=0; i<numOfEmployees; i++) {
            if(color[i] == Colour.WHITE) {
                visitedNode(i, color, Q);
            }
            for(int j=0; j<numOfEmployees; j++) {
                if(precedences[j] == 0) {
                    System.out.println(j);
                    count++;
                }
            }
            if(count <= minProms) {
                proms[0] = count;
                proms[1] = count;
            }
            else if(count <= maxProms && count > proms[1])
                proms[1] = count;
            //count = 0;
        }

        proms[2] = numOfEmployees - proms[1];
        return proms;
    }
}