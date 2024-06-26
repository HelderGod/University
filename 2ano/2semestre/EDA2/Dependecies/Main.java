import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        String[] tasksAndRules = s.readLine().split(" ");
        int numberOfTasks = Integer.parseInt(tasksAndRules[0]);
        int numberOfRules = Integer.parseInt(tasksAndRules[1]);
        Project p = new Project(numberOfTasks);
        for(int i=0; i<numberOfRules; i++) {
            String[] rules = s.readLine().split(" ");
            int numberOfDependencies = Integer.parseInt(rules[1]);
            for(int j=2; j<rules.length; j++)
                p.addDependency(Integer.parseInt(rules[0]), Integer.parseInt(rules[j]), numberOfDependencies);
        }
        p.addZeroDep();
        int[] result = p.computeOrder();
        System.out.print(result[0]);
        for(int i=1; i<result.length; i++)
            System.out.print(" " + result[i]);
        System.out.println();
    }
}

class Project {
    // inicialização do grafo
    private int tasks;
    List<Integer>[] adjacents;
    int[] numOfDep;
    Queue<Integer> Q;
    
    @SuppressWarnings("unchecked")
    public Project(int tasks) {
        this.tasks = tasks + 1;
        adjacents = new List[this.tasks];
        for(int i=0; i<this.tasks; ++i)
            adjacents[i] = new LinkedList<>();
        numOfDep = new int[this.tasks];
        numOfDep[0] = -1;
        Q = new PriorityQueue<>();
    }

    public void addDependency(int task, int precedent, int numberOfDependencies) {
        adjacents[precedent].add(task);
        numOfDep[task] = numberOfDependencies;
    }
    // fim grafo

    public void addZeroDep() {
        for(int i=1; i<numOfDep.length; i++) {
            if(numOfDep[i] == 0)
                Q.add(i);
        }
    }

    private static enum Colour {
        WHITE,
        GREY,
        BLACK
    };

    public int[] computeOrder() {
        List<Integer> aux = new ArrayList<>();
        Colour[] colour = new Colour[tasks];

        for(int i=0; i<tasks; i++)
            colour[i] = Colour.WHITE;
        int temp = -1;
        while(!Q.isEmpty()) {
            temp = Q.remove();
            for(int v : adjacents[temp]) {
                if(colour[v] == Colour.WHITE)
                    numOfDep[v] = numOfDep[v] - 1;
                if(numOfDep[v] == 0) {
                    Q.add(v);
                    colour[v] = Colour.GREY;
                }
            }
            aux.add(temp);
        }
        int[] result = new int[aux.size()];
        for(int i=0; i<aux.size(); i++) {
            result[i] = aux.get(i);
        }
        return result;
    }
}