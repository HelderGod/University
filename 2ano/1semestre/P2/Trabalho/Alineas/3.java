public static List<Integer> findDividers(int n) {               //alínea 3
        List<Integer> divs = new ArrayList<Integer>();              //cria uma lista para os divisores da variável n
        for(int i=1; i<=n; i++) {                                   //neste ciclo for, a variável i percorre todos os inteiros até n (inclusivé)
            if(n % i == 0)                                          // se a divisão de n por i der resto 0, então i é divisor de n
                divs.add(i);
        }
        return divs;                                                //retorna a lista de inteiros já com os divisores de n
}
