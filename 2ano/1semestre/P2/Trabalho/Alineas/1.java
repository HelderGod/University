public static String normalized(String naturalText) {                                    //alínea 1
        String noAccentString =                                                         //retira os acentos da string
            Normalizer
                .normalize(naturalText, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        String noSpaceString = noAccentString.replaceAll("\\s+", "");                   //retira os espaços entre as palavras da string
        String noPunctString = noSpaceString.replaceAll("\\p{Punct}", "");              //retira os sinais de pontuação da string
        String normalString = noPunctString.toLowerCase();                              //coloca todas as letras da string minúsculas
        return normalString;
}
