import java.util.ArrayList;
import java.util.List;

public class MemoryProvider extends AbstractProvider {
    List<String> words;

    public MemoryProvider() {
        words = new ArrayList<String>();
    }

    public void addWord(String word) {
        words.add(Cipher.normalized(word));
    }

    public List<String> getWords() {
        List<String> aux = new ArrayList<String>();
        for(String s : words) {
            if(!aux.contains(s))
                aux.add(s);
        }
        words = aux;
        String temp = new String();
        for(int i=0; i<words.size(); i++) {
            for(int j=i+1; j<words.size(); j++) {
                if(words.get(i).compareTo(words.get(j)) > 0) {
                    temp = words.get(i);
                    words.set(i, words.get(j));
                    words.set(j, temp);
                }
            }
        }
        return words;
    }
}
