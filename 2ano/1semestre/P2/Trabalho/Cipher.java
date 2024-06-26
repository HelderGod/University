import java.text.Normalizer;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Cipher {

    private static String normalize(String naturalText) {                               //alínea 1
        String noAccentString =                                                         //retira os acentos da string
            Normalizer
                .normalize(naturalText, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        String noSpaceString = noAccentString.replaceAll("\\s+", "");                   //retira os espaços entre as palavras da string
        String noPunctString = noSpaceString.replaceAll("\\p{Punct}", "");              //retira os sinais de pontuação da string
        String normalString = noPunctString.toLowerCase();                              //coloca todas as letras da string minúsculas
        return normalString;
    }
    
    public static String encode(String plainText, int cols) {                           //alínea 2
        String normalize = Cipher.normalized(plainText);
        boolean c = normalize.length() % cols != 0;
        String randomText = "";
        StringBuilder code = new StringBuilder();
        if(c) {
            Random x = new Random();
            for(int i=normalize.length()%cols; i!=cols; i++) randomText += (char)(x.nextInt(26) + 'a');
        }
        normalize += randomText;
        int i = 0;
        int column = 0;
        while(column < cols) {
            if(i < normalize.length()) {
                code.append(normalize.charAt(i));
                i += cols;
            }
            else {
                column++;
                i = column;
            }
        }
        return code.toString();
    }
    
    public static List<Integer> findDividers(int n) {               //alínea 3
        List<Integer> divs = new ArrayList<Integer>();              //cria uma lista para os divisores da variável n
        for(int i=1; i<=n; i++) {                                   //neste ciclo for, a variável i percorre todos os inteiros até n (inclusivé)
            if(n % i == 0)                                          // se a divisão de n por i der resto 0, então i é divisor de n
                divs.add(i);
        }
        return divs;                                                //retorna a lista de inteiros já com os divisores de n
    }
}

public static List<String> findWords(String candidate, List<String> words) {
        List<String> children = new ArrayList<String>();
        List<String> result = new ArrayList<String>();
        for(String word : words) {
            if(candidate.startsWith(word)) {
                String suffix = candidate.substring(word.length());
                children = findWords(suffix, words);
                for(String child : children) {
                    String solution = word+" "+child;
                    result.add(solution);
                }
            }
        }
        if(result.isEmpty()) result.add("");
        return result;
    }

    public static List<String> breakCipher(String cipherText, List<String> words) {
        List<String> decode = new ArrayList<String>();
        List<Integer> divs = findDividers(cipherText.length());
        List<String> result = new ArrayList<String>();
        List<String> aux = new ArrayList<String>();
        for(int i : divs) {
            decode.add(encode(cipherText, i));
        }
        boolean stringExists = false;
        for(String d : decode) {
            aux = findWords(d, words);
            for(String a : aux) {
                for(String s : result) {
                    if(a.equals(s)) stringExists = true;
                }
                if(!stringExists && !a.isEmpty()) result.add(a);
            }
        }
        return result;
    }
