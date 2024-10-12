package Scr;

import java.util.Scanner;

/*
 * TP01 - Algoritmos e Estrutura de Dados 3
 * Sistema de Armazenamento de dados de Pokemons
 * CRUD - Create, Read, Update e Delete  
 */

public class Main{


    static String clear = "\033[H\033[2J";          // clear no windowns

    public static void main(String[] args){
        
         // Variaveis auxiliares
        int selecao = -1;      
        int ids[];    
        
        Scanner sc = new Scanner(System.in);
        
        System.out.print(clear);          // Limpa tela (windows)

        //MENU
        do{
                
            System.out.println();
            System.out.println("=====================================");
            System.out.println("          Sistema de Cadastro        ");
            System.out.println("=====================================");
            System.out.println("1. Carregar base de dados");
            System.out.println("2. Criar registro");
            System.out.println("3. Ler registro");
            System.out.println("4. Ler conjunto de registros");
            System.out.println("5. Atualizar registro");
            System.out.println("6. Deletar registro");
            System.out.println("7. Pesquisar pelo arquivo de indice");
            System.out.println("0. Sair do Sistema");
            System.out.println("=====================================");
            
            System.out.print("Escolha uma opção: ");


            // Leitura da seleção
            try{
                selecao = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
                selecao = 99999; // Erro de leitura - Buffer cheio, repete operação
            }
            

            // MENU
            switch(selecao) {
                
                case 99999:
                    // ERRO DE LEITURA
                    break;

                case 0:
                    // Fim programa
                    System.out.println("Saindo... Até logo!");
                    break;

                case 1:
                    // Carregar .csv para arquivo binario
                    CRUD.carregarBaseDados();
                    break;

                case 2:
                    // Inserir um registro no arquivo
                    CRUD.criarRegistro();
                    break;

                case 3:
                    // Procurar um unico registro
                    System.out.print("Qual o Id do registro a ser lido? ");
                    System.out.println((CRUD.lerRegistro(Integer.parseInt(sc.nextLine()))).toString()); // Imprime pokemon achado
                    break;

                case 4:
                    // Procurar um conjunto de "n" registros
                    System.out.print("Quantos Pokemons você deseja procurar? ");
                    ids = new int[ Integer.parseInt(sc.nextLine())];
                    for(int i = 0; i<ids.length; i++){
                        System.out.print("Qual o id do " + (i+1) + "º pokemon? ");
                        ids[i] =  Integer.parseInt(sc.nextLine()); 
                    }
                    CRUD.lerConjuntoRegistros(ids);
                    break;

                case 5:
                    // Atualizar algum registro
                    System.out.print("Qual o Id do registro a ser atualizado? ");
                    CRUD.atualizarRegistro( Integer.parseInt(sc.nextLine()));
                    break;

                case 6:                
                    // Deletar regitro (marcar lapide)
                    System.out.print("Qual o Id do registro a ser deletado?");
                    CRUD.deletarRegistro( Integer.parseInt(sc.nextLine()));
                     
                    break;

                case 7:
                    // Procurar um unico registro usando arquivo de indice
                    System.out.print("Qual o Id do registro a ser lido? ");

                    // Imprime o to string do pokemon com id lido
                    System.out.println((CRUD.readByIndex(Integer.parseInt(sc.nextLine()))).toString()); // Imprime pokemon achado
                     
                    break;    
                
                

                default:
                    System.out.println("\nOpção inválida. Por favor, escolha um número entre as opcoes.");
            }
            

            System.out.println("\n\nPessione qualquer tecla para continuar");
            sc.nextLine();
            System.out.print(clear);          // Limpa tela (windows)

        }while(selecao != 0);

        sc.close();     
    }


}

