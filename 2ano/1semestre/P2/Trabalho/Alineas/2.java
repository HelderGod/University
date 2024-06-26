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
