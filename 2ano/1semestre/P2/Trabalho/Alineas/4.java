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
