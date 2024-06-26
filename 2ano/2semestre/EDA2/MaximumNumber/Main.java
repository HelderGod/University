package EDA2.MaximumNumber;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        int numChildren = Integer.parseInt(s.readLine());
        String[] SticksNum;
        int biggerStickNum = Integer.MIN_VALUE;
        int aux = Integer.MIN_VALUE;
        for(int i=1; i<=numChildren; i++) {
            SticksNum = s.readLine().split(" ");
            for(int j=1; j<SticksNum.length; j++) {
                aux = Integer.parseInt(SticksNum[j]);
                if(aux > biggerStickNum)
                    biggerStickNum = aux;
            }
        }
        System.out.println(biggerStickNum);
    }
}