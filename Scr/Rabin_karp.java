package Scr;

public class Rabin_karp {

	public final static int d = 256;        // tamanho da tabela ascii
    public final static int q = 101;        // Numero que vai fazer o hashing

	public static int Busca(String padrao, String txt){
		int M = padrao.length();
		int N = txt.length();
		int i, j;
		int p = 0;           // valor hash do padrao
		int t = 0;           // valor hash do texto
		int h = 1;			 

		long tempoInicial = System.currentTimeMillis();
		int comparacoes_caracter = 0;
		int comparacoes_hash = 0;


		// Calcula h = d^(M-1) % q
		for (i = 0; i < M - 1; i++)
			h = (h * d) % q;

        // Calcula o valor hash do padrão e da primeira partezinha do texto
		for (i = 0; i < M; i++) {
			p = (d * p + padrao.charAt(i)) % q;
			t = (d * t + txt.charAt(i)) % q;
		}

        // Comparando os valores hash e deslizando o padrão sobre o texto
		for (i = 0; i <= N - M; i++) {

			comparacoes_hash++;
            // Se os valores hash forem iguais, compara os caracteres um a um
			if (p == t) {

                // Se os caracteres forem iguais, compara o próximo 		
				for (j = 0; j < M; j++) {
					comparacoes_caracter++;			
					if (txt.charAt(i + j) != padrao.charAt(j))
						break;					// j vai ser até que ponto o padrão é igual ao texto
				}

				// Se j for igual a M, então encontrou
				if (j == M){
					System.out.println("\nNúmero de comparações dos caracteres: " + comparacoes_caracter);
					System.out.println("Número de comparações dos valores hash: " + comparacoes_hash);
        			System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial) + "ms");
					return i;
                }
			}

            // Calcula o valor hash para a próxima janela do texto (remove o primeiro digito e adiciona o próximo)
			if (i < N - M) {
				t = (d * (t - txt.charAt(i) * h) + txt.charAt(i + M)) % q;

                // Se o valor de t for negativo, adiciona q para que fique positivo (será que precisa?) // não! remover
				if (t < 0)
					t = (t + q);
			}
		}

        return -1;
	}

    // Funcao pra testes
    public static void main(String[] args) {
        String texto = "AAAAAABCAABAAAAAABAADBDCBAAAAAAABABAAAA";
        String padrao;
        padrao = "ABAB";    
        System.out.println(Busca(padrao, texto));
    }
}

