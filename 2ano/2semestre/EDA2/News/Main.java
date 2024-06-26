import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        int numberOfEmployees = Integer.parseInt(s.readLine());
        String[] friends;
        Organisation o = new Organisation(numberOfEmployees);
        for(int i=0; i<numberOfEmployees; i++) {
            friends = s.readLine().split(" ");
            for(int j=1; j<friends.length; j++) {
                o.addFriend(i, Integer.parseInt(friends[j]));
            }
        }
        int numberOfTests = Integer.parseInt(s.readLine());
        for(int i=0; i<numberOfTests; i++)
            o.solve(Integer.parseInt(s.readLine()));
    }
}

class Organisation {
    // bfs variables
    public static final int infinity = Integer.MAX_VALUE;
    public static final int none = -1;

    enum Colour {
        WHITE,
        GREY,
        BLACK
    }
    //end here
    int employees;
    List<Integer>[] adjacents;

    @SuppressWarnings("unchecked")
    public Organisation(int employees) {
        this.employees = employees;
        adjacents = new List[employees];
        for(int i=0; i<employees; i++)
            adjacents[i] = new LinkedList<>();
    }

    public void addFriend(int employee, int friend) {
        adjacents[employee].add(friend);
    }

    public int[] bfs(int employee) {
        Colour[] colour = new Colour[employees];
        int[] d = new int[employees];
        int[] p = new int[employees];

        for(int i=0; i<employees; i++) {
            colour[i] = Colour.WHITE;
            d[i] = infinity;
            p[i] = none;
        }

        colour[employee] = Colour.GREY;
        d[employee] = 0;
        Queue<Integer> Q = new LinkedList<>();
        Q.add(employee);
        int[] days = new int[employees];

        while(!Q.isEmpty()) {
            int u = Q.remove();

            for(int v : adjacents[u]) {
                if(colour[v] == Colour.WHITE) {
                    colour[v] = Colour.GREY;
                    d[v] = d[u] + 1;
                    p[v] = u;
                    Q.add(v);
                    days[d[u]]++;
                }
            }
            colour[u] = Colour.BLACK;
        }

        return days;
    }

    public void solve(int source) {
        int[] spread = bfs(source);
        int maxDay = -1;
        int maxBoom = -1;
        if(adjacents[source].size() == 0) {
            System.out.println(0);
            return;
        }
        for(int i=0; i<spread.length; i++) {
            if(spread[i] > maxBoom) {
                maxBoom = spread[i];
                maxDay = i + 1;
            }
        }
        System.out.println(maxBoom + " " + maxDay);
    }
}