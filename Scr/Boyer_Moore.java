package Scr;

public class Boyer_Moore {

    public static int Busca(String padrao, String texto) {
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
                deslocamento = badChar[texto.charAt(s + j)] > sufixo_bom[j] ? badChar[texto.charAt(s + j)] : sufixo_bom[j]; //pega o maior dos deslocamentos
                s += Math.max(1, j - deslocamento);         // desloca
            }
        }
        System.out.println("\nNúmero de comparações: " + comparacoes);
        System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial) + "ms");
        return -1;
    }

    public static int[] caracterRuim(String padrao, int M) {
        int[] caracter_ruim = new int[256];

        // Inicializa qualque caracter da tabela ascii com -1
        for (int i = 0; i < 256; i++) {
            caracter_ruim[i] = -1;
        }

        // Preenche o vetor com a posição do último caractere do padrão
        for (int i = 0; i < M - 1; i++) {       // (Vai até o penultimo caracter)
            caracter_ruim[padrao.charAt(i)] = i;
        }
        return caracter_ruim;
    }

    public static int[] sufixo_bom(String padrao, int M) {
        int[] sufixo = new int[M];
        int[] prefixo = new int[M];
        sufixo[M - 1] = M;                          // ultimo sufixo é o tamanho do padrão

        // Preenche o vetor sufixo e prefixo em um único loop
        for (int i = 0; i < M - 1; i++) {
            sufixo[i] = M; // Inicializa com o tamanho do padrão
            prefixo[i] = 0; // Inicializa prefixo com 0
        }

        int j = 0;
        for (int i = M - 2; i >= 0; i--) {
            if (padrao.charAt(i) == padrao.charAt(M - 1)) {
                sufixo[j] = i + 1;
                j++;
            }

            // subtexto é sufixo do meu padrão?
            if (padrao.substring(0, i + 1).equals(padrao.substring(M - 1 - i))) {
                prefixo[i] = i + 1;
            }
        }

        // Ajusta o vetor sufixo usando o vetor prefixo
        for (int i = 0; i < M - 1; i++) {
            if (sufixo[i] == M) {
                sufixo[i] = prefixo[i] == 0 ? M : prefixo[i];
            }
        }

        /*
        System.out.print("Sufixo bom: ");
        for (int i = 0; i < M; i++) {
            System.out.print(sufixo[i] + " ");
        }
        */
        return sufixo;
    }


    // Funcao pra testes
    public static void main(String[] args) {
        String texto = "AAAAAABCAABAAAAAABAADBDCBAAAAAAABABAAAA";
        String padrao;
        padrao = "ABAB";    // Referencia: 2 2 4 1
        System.out.println(Busca(padrao, texto)); // OK
    }
}
