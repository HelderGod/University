import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        int numberOfTests = Integer.parseInt(s.readLine());
        for(int i=0; i<numberOfTests; i++) {
            int size = Integer.parseInt(s.readLine());
            String[] start = s.readLine().split(" ");
            String[] end = s.readLine().split(" ");
            City c = new City(size);
            int numberOfBlocks = Integer.parseInt(s.readLine());
            for(int j=0; j<numberOfBlocks; j++) {
                String[] block = s.readLine().split(" ");
                c.addBlockage(Integer.parseInt(block[0]), Integer.parseInt(block[1]), block[2]);
            }
            System.out.println(c.numberOfWays(Integer.parseInt(start[0]), Integer.parseInt(start[1]), Integer.parseInt(end[0]), Integer.parseInt(end[1])));
        }
    }
}

class City {
    private int size;
    private int[][] northBlocks;
    private int[][] eastBlocks;

    public City(int size) {
        this.size = size + 2;
        northBlocks = new int[this.size][this.size];
        eastBlocks = new int[this.size][this.size];
        for(int i=1; i<size; i++) {
            for(int j=1; j<size; j++) {
                northBlocks[i][j] = 0;
                eastBlocks[i][j] = 0;
            }
        }
    }

    public void addBlockage(int x, int y, String direction) {
        if(direction.equals("N"))
            northBlocks[x][y+1] = 1;
        else if(direction.equals("E"))
            eastBlocks[x+1][y] = 1;
        else if(direction.equals("S"))
            northBlocks[x][y] = 1;
        else if(direction.equals("W"))
            eastBlocks[x][y] = 1;
    }

    public boolean existsBlockage(int x, int y, String direction) {
        if(direction.equals("N")) {
            return northBlocks[x][y+1] == 1;
        }
        else if(direction.equals("E")) {
            return eastBlocks[x+1][y] == 1;
        }
        return false;
    }

    public long numberOfWays(int sx, int sy, int ex, int ey) {
        long[][] ways = new long[size][size];
        for(int i=1; i<size; i++) {
            for(int j=1; j<size; j++)
                ways[i][j] = 0;
        }
        ways[ex][ey] = 1;
        for(int i=ex; i>=sx; i--) {
            for(int j=ey; j>=sy; j--) {
                if(j < ey && !existsBlockage(i, j, "N"))
                    ways[i][j] = ways[i][j] + ways[i][j+1];
                if(i < ex && !existsBlockage(i, j, "E"))
                    ways[i][j] = ways[i][j] + ways[i+1][j];
            }
        }
        return ways[sx][sy];
    }
}