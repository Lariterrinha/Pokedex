package Scr;

import java.util.ArrayList;

public class KMP {

    
    // TESTE
    public static void main(String[] args) {
        String texto = "AAAAAAAAAAAAAAAABCAABDES";
        String padrao = "AABCAAB";      // (Ta retornando -1 0 0 1 0 0 0  (?))
        System.out.println(Busca(padrao, texto));
    }


    /**
     * Função de busca de padrão em texto
     * @param padrao 
     * @param texto 
     * @return  Retorna a posição da primeira ocorrência do padrão no texto
     */
    public static int Busca(String padrao, String texto) { 
        int comparacoes = 0;

        int texto_lenght = texto.length();
        int padrao_lenght = padrao.length();
        int[] funcao = funcao_prefixo(padrao, padrao.length());
        int i = 0, j = 0;

        long tempoInicial;
        tempoInicial = System.currentTimeMillis();

        while (i < texto_lenght) {          // Percorrer o texto todo

            comparacoes++;
            if (texto.charAt(i) == padrao.charAt(j)) {      // Encontrou um casamento de letra?

                j++;
                i++;
                if (j == padrao_lenght) {                   // Encontrou o padrão todo?

                    System.out.println("\nNúmero de comparações: " + comparacoes);
                    System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial) + "ms");
                    return (i - padrao_lenght);                     // Encontrou todo o padrão

                    //j = funcao[j];                                // Caso fosse retornar todas as ocorrencias
                }

            } else {         // Não encontrou  ainda                 
                j = funcao[j];
                if (j < 0) { 
                    i++;
                    j++;
                }
            }

        }

        System.out.println("\nNúmero de comparações: " + comparacoes);
        System.out.println("Tempo de execução: " + (System.currentTimeMillis() - tempoInicial) + "ms");

        return -1;    // Não encontrou nada
    }

    /**
     * Função que retorna a função de prefixo de um padrão
     * @param padrao 
     * @param M tamanho do padrão
     * @return Retorna um vetor de prefixo pi
     */
    public static int[] funcao_prefixo(String padrao, int M) {

        int funcao[] = new int[M + 1];

        funcao[0] = -1;         // primeiro valor é sempre -1

        int j = -1;

        for (int i = 0; i < M; i++) {

            // Repete até não ter mais falha (volta pro prefixo anterior)
            while (j >= 0 && padrao.charAt(i) != padrao.charAt(j)) {
                j = funcao[j];
            }

            j++;
            funcao[i + 1] = j;          // Salva o prefixo
        }

        /* 
        //TESTE
        for (int i = 0; i < M; i++) {
            System.out.print(funcao[i] + " ");
        }
        */

        return funcao;
    }   

}