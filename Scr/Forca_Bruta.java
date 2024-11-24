package Scr;

public class Forca_Bruta {

    public static void main(String[] args) {
        String texto = "AAAAAAAAAAAAAAAABCAABDES";
        String padrao = "AABCAAB";
        System.out.println(Busca(padrao, texto));
    }

    // função que recebe um texto e um padrão e retorna a posição da primeira ocorrência do padrão no texto
    public static int Busca(String padrao, String texto) {
        int N = texto.length();
        int M = padrao.length();
        int comparacoes = 0;

        long tempoInicial = System.currentTimeMillis();

        // Procuta até que o tamanho do texto restante seja menor que o tamanho do padrão
        for (int i = 0; i < N - M; i++) {
            int j = 0;

            // Contagem de letras iguais ao do padrão
            comparacoes++;
            while (j < M && texto.charAt(i + j) == padrao.charAt(j)) {
                j++;
                comparacoes++;
            }

            // Se j for igual ao tamanho do padrão, então encontrou
            if (j == M) {
                System.out.println("\nNúmero de comparações: " + comparacoes);
                System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial) + "ms");
                return i;   // retorna posição no texto
            }
        }
        
        System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial) + "ms");
        return -1;  // não encontrou
    }
    
}
