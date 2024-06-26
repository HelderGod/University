import java.util.List;
import java.io.*;
import java.util.ArrayList;

public class SimpleFileProvider extends AbstractProvider {
    private String file;
    List<String> words;

    public SimpleFileProvider(String file) throws IOException{
        this.file = file;
        words = new ArrayList<String>();
    }

    public List<String> getWords() {
        try {
            MemoryProvider p = new MemoryProvider();
            BufferedReader f = new BufferedReader(new FileReader(file));
            String line = new String();
            while((line = f.readLine()) != null)
                p.addWord(line);
            f.close();
            words = p.getWords();
            return words;
        } catch (Exception e) {
            return null;
        }
    }
}
