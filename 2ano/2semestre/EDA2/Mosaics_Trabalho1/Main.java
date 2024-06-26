import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException{
        BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
        String[] mosaicSize = s.readLine().split(" ");
        int mosaicLines = Integer.parseInt(mosaicSize[0]);
        int mosaicRows = Integer.parseInt(mosaicSize[1]);
        int[] blocks = {1, 2, 3, 4, 6, 8, 10, 12, 16};                  //array que guarda todos os tamanhos de blocos disponíveis
        String[][] mosaic = new String[mosaicLines][mosaicRows];
        String mosaicBuilt;
        for(int line=0; line<mosaicLines; line++) {                     //nestes dois for's colocamos o mosaico numa matriz
            mosaicBuilt = s.readLine();
            for(int row=0; row<mosaicRows; row++)
                mosaic[line][row] = String.valueOf(mosaicBuilt.charAt(row));        //coloca na matriz a cor ou . no index correspondente
        }
        Mosaics m = new Mosaics(mosaic, blocks);
        long result = m.getSequence(mosaicLines, mosaicRows);
        System.out.println(result);
    }
}

class Mosaics {
    private String[][] mosaic;
    private int[] blocks;

    
    public Mosaics(String[][] mosaic, int[] blocks) {
        this.mosaic = mosaic;
        this.blocks = blocks;
    }

    //função para todas as sequências de cada cor do mosaico
    public long getSequence(int mosaicLines, int mosaicRows) {
        long result = 1;
        int sequence = 1;
        for(int line=0; line<mosaicLines; line++) {
            for(int row=0; row<mosaicRows-1; row++) {
                if(!mosaic[line][row].equals(".")) {                                  
                    if(mosaic[line][row].equals(mosaic[line][row+1]))       
                    sequence++;                                             //se o carater atual e o proximo forem iguais, incrementamos a sequencia
                    else {                                                  //se nao for, acumula o resultado multiplicando as combinações com a sequencia que calculou                                        
                        result = result * combinations(sequence);           //acumula todas as combinações possíveis para cada sequência presente no mosaico
                        sequence = 1;
                    }
                    if(row == mosaicRows-2)                                 //depois de verificarmos a penultima coluna com a ultima, acumulamos o resultado salvaguardando qualquer situação
                        result = result * combinations(sequence);
                }
                else
                    sequence = 1;                                       //se for ".", colocamos a variavel pronta para uma nova sequencia
            }
            sequence = 1;                                     //coloca a variavel pronta para uma nova sequencia
        }
        return result;              
    }

    //função que permite guardar as combinações das sequencias de blocos da mesma cor
    public long combinations(int sequence) {
        long[] table = new long[sequence + 1];  //tabela que vai guardar os valores para cada sequência
        long aux = 0;
        table[0] = 1;                           //caso base

        for(int i=1; i<=sequence; i++) {        
            for(int block : blocks) {
                if(block <= i) {
                    aux += table[i - block];    //variavel auxiliar que acumula os resultados das combinaçoes anteriores com os blocos possiveis
                }
                table[i] = aux;                 
            }
            aux = 0;                            
        }
        return table[sequence];                 //retorna todas as combinações possíveis para a sequência desejada
    }
}
