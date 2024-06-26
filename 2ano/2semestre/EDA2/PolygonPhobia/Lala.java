public class Lala {
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


