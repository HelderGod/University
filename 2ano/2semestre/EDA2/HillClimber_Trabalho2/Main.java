import java.io.*;
import java.util.*;
import java.lang.*;

class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        String[] info = s.readLine().split(" ");                                    //array que irá receber as coordenadas
        int numberOfPoints = Integer.parseInt(info[0]);                             //número de coordenadas
        int maxHeight = Integer.parseInt(info[1]);                                  //altura que o Hill tem que alcançar (altura máxima)
        int numberOfTests = Integer.parseInt(info[2]);                              //número de testes que serão executados
        Climb c = new Climb(numberOfPoints, maxHeight);
        for(int i=0; i<numberOfPoints; i++) {                                       //coloca todas as coordenadas na Queue
            info = s.readLine().split(" ");
            c.addToQueue(Integer.parseInt(info[0]), Integer.parseInt(info[1]));
        }
        int[] tests = new int[numberOfTests];                                       //array que recebe todos os testes a serem executados
        int maxTest = Integer.MIN_VALUE;                                            //variável para guardar o maior salto que o Hill irá fazer                            
        for(int i = 0; i<numberOfTests; i++){                                       //ciclo para colocar todos os testes no array e descobrir o maior salto                      
            tests[i] = Integer.parseInt(s.readLine());
            if(maxTest < tests[i])
                maxTest = tests[i];
        }
        c.addToArray();
        c.defineMaxTest(maxTest);
        c.build();
        for(int i = 0; i<numberOfTests; i++){

            int result = c.bestPath(tests[i]);                                      //recebe a distância para chegar ao ponto final
            if(result == Integer.MAX_VALUE)                                         //se result receber o valor MAX_VALUE significa que não é possível chegar ao final com o salto utilizado
                System.out.println("unreachable");
            else                                                                    //caso contrário significa que chegou a esse ponto e imprime esse mesmo resultado
                System.out.println(result-1);
        }
    }
}

class Edge {                                                                        //classe que recebe o vértice que posso chegar a partir de determinado vértice e a distância para lá chegar
    public Vertex destiny;                                                          //vértice aonde é possível chegar a partir de determinado vértice
    public int distance;                                                            //distância entre o vértice em questão e o vértice destino

    public Edge(Vertex destiny, int distance) {
        this.destiny = destiny;
        this.distance = distance;
    }
}

class Vertex implements Comparable<Vertex> {                    //classe que recebe as coordenadas dos pontos
    List<Edge> destination;                                     //lista que alberga todos os vértices aos quais determinado ponto se pode ligar
    public int x;
    public int y;
    public int d;                                               

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
        destination = new LinkedList<>();
    }

    public int calcDistance(Vertex a) {                         //calcula a distância entre dois vértices
        double d = 0.0;
        if(y == 0 || a.y == Climb.maxHeight)                    //se pelo menos um dos vértices for o vértice final ou inicial, apenas interessa a distância entre os y dos dois vértices
            d = Math.abs(y - a.y);
        else                                                    //caso contrário, se nenhum dos pontos envolvidos for o final ou inicial, calcula a distância entre os vértices normalmente
            d = Math.sqrt(Math.pow(x - a.x, 2) + Math.pow(y - a.y, 2));
        return (int)Math.ceil(d);
    }

    @Override
    public int compareTo(Vertex a) {                            //através deste método, a priority Queue presente na classe Climb ordena os vértices do y menor para o y maior
        if(y > a.y)
            return 1;
        else if(y < a.y)
            return -1;
        else
            return 0;
    }
}
class Climb {                                               //classe que recebe todos os dados necessários, constrói o grafo e devolve o melhor caminho
    public int numberOfPoints;                              //número de coordenadas presentes
    public static int maxHeight;
    private Queue<Vertex> Q;
    public Vertex[] v;
    private int maxTest;

    public Climb(int numberOfPoints, int maxHeight) {
        this.numberOfPoints = numberOfPoints + 2;                   //colocam-se mais 2 coordenadas no grafo: o ponto 0 e o ponto onde o Hill tem que chegar
        Climb.maxHeight = maxHeight;
        v = new Vertex[this.numberOfPoints];
        Q = new PriorityQueue<>();
        Q.add(new Vertex(0, 0));                                    //adiciona à Queue o ponto inicial de onde o Hill tem que partir
        Q.add(new Vertex(0, maxHeight));                            //adiciona à Queue o ponto final onde o Hill tem que chegar
    }

    public void defineMaxTest(int maxTest) {                        //define o salto máximo que o Hill pode realizar
        this.maxTest = maxTest;
    }

    public void addToQueue(int x, int y) {                          //adiciona os vértices à priority Queue de forma a ordená-los do y menor para o y maior
        Q.add(new Vertex(x, y));
    }

    public void addToArray() {                                      //retira os vértices da Queue, já ordenados, e coloca-os no array
        int i = 0;
        while(!Q.isEmpty()) {
            v[i] = Q.poll();
            i++;
        }
    }

    public void build() {                                           //constrói o grafo em si, isto é, a lista que guarda os vértices a que um determinado vértice se pode ligar
        int distance = 0;
        for(int i=0; i<v.length-1; i++) {
            for(int j=i+1; j<v.length; j++) {
                if(Math.abs(v[j].y - v[i].y) <= maxTest) {          //se a distância entre os y de dois vértices for inferior ou igual ao salto máximo, então são elegíveis para serem adicionados à lista de cada um     
                    distance = v[i].calcDistance(v[j]);
                    if(distance <= maxTest){                       //se a distância entre os dois vértices for inferior ou igual ao salto máximo, então adiciona à lista de cada um dos dois vértices   
                        v[i].destination.add(new Edge(v[j], distance));
                        v[j].destination.add(new Edge(v[i], distance));
                    }
                }
                else                                               //quando a distância entre os y de dois vértices for superior ao salto máximo, então não vale a pena comparar aos restantes vértices
                    break;
            }
        }
       
    }

    public int bestPath(int test) {                                 //método que calcula o melhor caminho até ao topo utilizando bfs
        for(Vertex u : v)                                           //inicializa-se as distâncias para chegar a cada vértice a MAX_VALUE 
            u.d = Integer.MAX_VALUE;

        v[0].d = 0;
        Queue<Vertex> aux = new LinkedList<>();
        aux.add(v[0]);

        while(!aux.isEmpty()) {
            Vertex u = aux.poll();
            for(Edge a : u.destination) {                           //ciclo que coloca o melhor caminho possível para alcançar cada vértice
                if(a.distance <= test) {
                    if((u.d + 1) < a.destiny.d) {               
                        a.destiny.d = u.d + 1;
                        aux.add(a.destiny);
                    }
                }
            }
        }
        return v[numberOfPoints-1].d;                               //retorna a distância ao ponto final (altura máxima)
    }
}