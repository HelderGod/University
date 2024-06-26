package EDA2.TrocoDeMoedas;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        int numberOfCoins = Integer.parseInt(s.readLine());
        String[] coinsValue = s.readLine().split(" ");
        int numberOfQuestions = Integer.parseInt(s.readLine());
        Coins c = new Coins(numberOfCoins);
        c.setValues(coinsValue);
        int[] minCoinsNeeded = new int[numberOfQuestions];
        int[] amount = new int[numberOfQuestions];
        int i = 0;
        for(; numberOfQuestions>0; numberOfQuestions--) {
            amount[i] = Integer.parseInt(s.readLine());
            minCoinsNeeded[i] = c.change(amount[i]);
            i++;
        }
        for(i=0; i<minCoinsNeeded.length; i++)
            System.out.println(amount[i] + ": [" + minCoinsNeeded[i] + "]");
    }
}

class Coins {
    private int[] coinsValue;

    public Coins(int coins) {
        coinsValue = new int[coins];
    }

    public void setValues(String[] values) {
        coinsValue = Arrays.stream(values).mapToInt(Integer::parseInt).toArray();
    }

    public int change(int amount) {
       int min[] = new int[amount + 1];
       int aux = 0;
       min[0] = 0;
       for(int i=1; i<=amount; i++)
           min[i] = Integer.MAX_VALUE;
        for(int i=1; i<=amount; i++) {
            for(int coin : coinsValue) {
                if(coin <= i) {
                    aux = min[i - coin];
                    if(aux != Integer.MAX_VALUE && aux + 1 < min[i])
                        min[i] = aux + 1;
                }
            }
        }
        return min[amount];
    }
}