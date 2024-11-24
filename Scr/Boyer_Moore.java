package Scr;

public class Boyer_Moore {

    public static int buscar(String padrao, String texto) {
        int N = texto.length();
        int M = padrao.length();
        int comparacoes = 0;
        int deslocamento = 0;
        int[] badChar = caracterRuim(padrao, M);
        int[] sufixo_bom = sufixo_bom(padrao, M);

        long tempoInicial = System.currentTimeMillis();

        int s = 0;

        // Procuta até que o tamanho do texto restante seja menor que o tamanho do padrão
        while (s <= (N - M)) {
            int j = M - 1;

            comparacoes++;
            // Contagem de letras iguais ao do padrão
            while (j >= 0 && padrao.charAt(j) == texto.charAt(s + j)) {
                j--;
                comparacoes++;
            }

            // Se j for igual a -1, então encontrou
            if (j < 0) {
                System.out.println("\nNúmero de comparações: " + comparacoes);
                System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial) + "ms");
                return s;
            } 

            // Se não encontrou, calcula o deslocamento
            else {
                deslocamento = badChar[texto.charAt(s + j)] > sufixo_bom[j] ? badChar[texto.charAt(s + j)] : sufixo_bom[j];
                s += Math.max(1, j - deslocamento);         // desloca
            }
        }
        System.out.println("\nNúmero de comparações: " + comparacoes);
        System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial) + "ms");
        return -1;
    }

    public static int[] caracterRuim(String padrao, int M) {
        int[] badChar = new int[256];

        // Inicializa qualque caracter da tabela ascii com -1
        for (int i = 0; i < 256; i++) {
            badChar[i] = -1;
        }

        // Preenche o vetor com a posição do último caractere do padrão
        for (int i = 0; i < M; i++) {
            badChar[padrao.charAt(i)] = i;
        }
        return badChar;
    }

    public static int[] sufixo_bom(String padrao, int M) {
    
        int[] sufixo = new int[M];
        int f = 0;
        int g;
        sufixo[M - 1] = M;
        g = M - 1;

        // Preenche o vetor sufixo 
        for (int i = M - 2; i >= 0; i--) {

            
            if (i > g && sufixo[i + M - 1 - f] < i - g) {
                sufixo[i] = sufixo[i + M - 1 - f];      
            } else {
                if (i < g) {
                    g = i;
                }
                f = i;
                while (g >= 0 && padrao.charAt(g) == padrao.charAt(g + M - 1 - f)) {
                    g--;
                }
                sufixo[i] = f - g;
            }
        }
        return sufixo;
    }


    // Funcao pra testes
    public static void main(String[] args) {
        String texto = "AAAAAAAAAAAAAAAABCAABDES";
        String padrao = "AABCAAB";
        System.out.println(buscar(padrao, texto));
    }
}
