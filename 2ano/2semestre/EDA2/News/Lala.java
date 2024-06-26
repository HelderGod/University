import java.io.*;
import java.util.*;

public class Lala {
    public static void main(String[] args) throws IOException{
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        int numberOfEmployees = Integer.parseInt(s.readLine());
        Organisation o = new Organisation(numberOfEmployees);
        String[] line;
        for(int i=0; i<numberOfEmployees; i++) {
            line = s.readLine().split(" ");
            for(int j=1; j<line.length; j++)
                o.addFriend(i, Integer.parseInt(line[j]));
        }
        int numberOfTests = Integer.parseInt(s.readLine());
        for(int i=0; i<numberOfTests; i++) {
            o.solve(Integer.parseInt(s.readLine()));
        }
    }
}

class Organisation {
    public static final int infinity = Integer.MAX_VALUE;
    public static final int none = -1;

    private static enum Colour {
        WHITE,
        GREY,
        BLACK
    };

    private int employees;
    List<Integer>[] adjacents;

    @SuppressWarnings("unchecked")
    public Organisation(int employees) {
        this.employees = employees;
        adjacents = new List[employees];
        for(int i=0; i<employees; ++i)
            adjacents[i] = new LinkedList<>();
    }

    public void addFriend(int employee, int friend) {
        adjacents[employee].add(friend);
    }

    public int[] bfs(int employee) {
        Colour[] colour = new Colour[employees];
        int[] d = new int[employees];   //array que guarda a distância de employee
        int[] p = new int[employees];   //array que guarda o pai do nó
        int[] days = new int[employees];

        for(int i=0; i<employees; ++i) {
            colour[i] = Colour.WHITE;
            d[i] = infinity;
            p[i] = none;                //este array serve para guardar os grafos já percorridos
        }
        colour[employee] = Colour.GREY;
        d[employee] = 0;
        Queue<Integer> Q = new LinkedList<>();
        Q.add(employee);
        while(!Q.isEmpty()) {
            int u = Q.remove();
            for(int v : adjacents[u]) {
                if(colour[v] == Colour.WHITE) {
                    colour[v] = Colour.GREY;
                    d[v] = d[u] + 1;
                    p[v] = u;
                    days[d[u]]++;
                    Q.add(v);
                }
            }
            colour[u] = Colour.BLACK;
        }
        return days;
    }

    public void solve(int source) {
        int[] spread = bfs(source);
        int maxDay = -1;
        int boom = -1; 
 
        if(adjacents[source].size() == 0){
            System.out.println(0);
            return;
        }
        
        for(int i = 0; i < spread.length; ++i){
            if(spread[i] > boom){
                boom = spread[i];
                maxDay = i + 1;
            }
        }
        System.out.println(boom + " " + maxDay);
    }
}