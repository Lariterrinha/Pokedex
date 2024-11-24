package Scr;
import java.util.Scanner;


public class KMP {

    // TESTE
    public static void main(String[] args) {
        String texto = "AAAAAAAAAAAAAAAABCAABDES";
        String padrao = "AABCAAB";      // (Ta retornando -1 0 0 1 0 0 0  (?))
        System.out.println(Busca(padrao, texto));
    }

    // função que recebe um texto e um padrão e retorna a posição da primeira ocorrência do padrão no texto
    // Função do slide 
    public static int Busca(String padrao, String texto) {
        int prefixo[] = funcaoPrefixo(padrao);
        int N = texto.length();
        int M = padrao.length();

        int comparacoes = 0;
        long tempoInicial = System.currentTimeMillis();

        for(int i = 0; i < M; i++){
            System.out.print(prefixo[i] + " ");
        }

        // Procuta até que o tamanho do texto restante seja menor que o tamanho do padrão
        for(int i = 0, j =0; i < N && j< M; i++, j++){

            comparacoes++;

            // Contagem de letras iguais ao do padrão
            while(j >= 0 && texto.charAt(i) != padrao.charAt(j)){
                j = prefixo[j];
                comparacoes++;

                if(j == M){
                    System.out.println("\nNúmero de comparações: " + comparacoes);
                    System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial) + "ms");
                    return i - M;
                }else{
                    System.out.println("\nNúmero de comparações: " + comparacoes);
                    System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial) + "ms");
                    return i;
                }
            }
        } 

        
        
        
        System.out.println("\nNúmero de comparações: " + comparacoes);
        System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial) + "ms");
        return 0;
    }
       
    // Função do slide (index out of bonds exception)
    private static int[] funcaoPrefixo(String padrao){

        int M = padrao.length();
        int prefixo[] = new int[M];
        prefixo[0] = -1;

        for(int i = 1, j = -1 ; i < M; i++, j++){

            if(j>0 && j<M){
                prefixo[i] = (padrao.charAt(i) == padrao.charAt(j)) ? prefixo[j] : j ;

            while(j>=0 && padrao.charAt(i) != padrao.charAt(j)){
                j = prefixo[j];
            }
            
            }
        }

        return prefixo;
    }
    
}