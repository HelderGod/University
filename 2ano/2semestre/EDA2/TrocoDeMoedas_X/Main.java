package EDA2.TrocoDeMoedas_X;

import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        int numberOfCoins = Integer.parseInt(s.readLine());
        String[] coinsValue = s.readLine().split(" ");
        int numberOfQuestions = Integer.parseInt(s.readLine());
        Coins c = new Coins(numberOfCoins);
        c.setValues(coinsValue);
        int[] coinsNeeded = new int[1];
        int amount;
        for(; numberOfQuestions>0; numberOfQuestions--) {
            amount = Integer.parseInt(s.readLine());
            coinsNeeded = c.change(amount);
            System.out.print(amount + ": [" + coinsNeeded.length + "]");
            for(int i=0; i<coinsNeeded.length; i++)
                System.out.print(" " + coinsNeeded[i]);
            System.out.println("");
        }
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

    public int[] change(int amount) {
        int[] min = new int[amount + 1];
        int aux = 0;
        min[0] = 0;
        int[] coinsGiven = new int[amount + 1];
        for(int i=1; i<=amount; i++)
            min[i] = Integer.MAX_VALUE;
        for(int i=1; i<=amount; i++) {
            for(int coin : coinsValue) {
                if(coin <= i) {
                    aux = min[i - coin];
                    if(aux != Integer.MAX_VALUE && aux + 1 < min[i]) {
                        min[i] = aux + 1;
                        coinsGiven[i] = coin;
                    }

                }
            }
        }
        int subAmount = amount;
        int[] coinsForAmount = new int[min[amount]];
        for(int i=0; subAmount>0 && i<coinsForAmount.length; i++) {
            coinsForAmount[i] = coinsGiven[subAmount];
            subAmount = subAmount - coinsForAmount[i];
        }
        return coinsForAmount;
    }
}