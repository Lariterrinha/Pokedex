package Scr;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
        Pokemon pokemon_tmp; 
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
            System.out.println("7. Pesquisar pelo arquivo de ids (indice direto)");
            System.out.println("8. Pesquisar pelo arquivo de nomes (indice indireto)");
            System.out.println("9. Ver pokemons de uma geração especifica (multilista)");
            System.out.println("10. Opções de Backup (LZW)");
            System.out.println("11. Opções de casamento de padões (Força Bruta/KMP/Boyer-Moore)");
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
                    CRUD.createPokemon();
                    break;

                case 3:
                    // Procurar um unico registro
                    System.out.print("Qual o Id do registro a ser lido? ");
                    
                    // Imprime o to string do pokemon com id lido
                    pokemon_tmp = (CRUD.readPokemon(Integer.parseInt(sc.nextLine())));
                    
                    // Verifica se achou o pokemon para imprimir
                    if(pokemon_tmp != null){           
                        System.out.println(pokemon_tmp.toString()); // Imprime pokemon achado
                    }else{
                        System.out.println("Pokemon não encontrado");
                    }

                    break;

                case 4:
                    // Procurar um conjunto de "n" registros
                    System.out.print("Quantos Pokemons você deseja procurar? ");
                    ids = new int[ Integer.parseInt(sc.nextLine())];
                    for(int i = 0; i<ids.length; i++){
                        System.out.print("Qual o id do " + (i+1) + "º pokemon? ");
                        ids[i] =  Integer.parseInt(sc.nextLine()); 
                    }
                    CRUD.readPokemons(ids);
                    break;

                case 5:
                    // Atualizar algum registro
                    System.out.print("Qual o Id do registro a ser atualizado? ");
                    CRUD.updatePokemon( Integer.parseInt(sc.nextLine()));
                    break;

                case 6:                
                    // Deletar regitro (marcar lapide)
                    System.out.print("Qual o Id do registro a ser deletado?");
                    CRUD.deletePokemon( Integer.parseInt(sc.nextLine()));
                     
                    break;

                case 7:
                    // Procurar um unico registro usando arquivo de ids
                    System.out.print("Qual o Id do pokemon a ser lido? ");

                    // Imprime o to string do pokemon com id lido
                    pokemon_tmp = DirectIndex.readPokemon(Integer.parseInt(sc.nextLine()));
                    
                    // Verifica se achou o pokemon para imprimir
                    if(pokemon_tmp.getId_pokedex() != 0){           
                        System.out.println(pokemon_tmp.toString()); // Imprime pokemon achado
                    }else{
                        System.out.println("Pokemon não encontrado");
                    }
                     
                    break;    
                
                case 8:
                    // Procurar um unico registro usando arquivo de nomes
                    System.out.print("Qual o nome do pokemon a ser lido? ");

                    // Imprime o to string do pokemon com id lido
                    pokemon_tmp = NameIndex.readPokemon((sc.nextLine()));
                    
                    // Verifica se achou um pokemon para imprimir
                    if(pokemon_tmp.getId_pokedex() != 0){           
                        System.out.println(pokemon_tmp.toString()); // Imprime pokemon achado
                    }else{
                        System.out.println("Pokemon não encontrado");
                    }
                    break;

                case 9:
                    System.out.print("Qual a você geração você deseja ver? ");
                    MultiList.printList(Byte.parseByte(sc.nextLine()));

                    break;

                case 10:
                    gerencia_de_backups();
                    break;
                case 11:
                    casamento_de_padroes();
                    break;
                default:
                    System.out.println("\nOpção inválida. Por favor, escolha um número entre as opcoes.");
            }
            
            System.out.println("\nPessione qualquer tecla para continuar");
            sc.nextLine();
            System.out.print(clear);          // Limpa tela (windows)

        }while(selecao != 0);

        sc.close();     
    }

    static public void gerencia_de_backups(){
        Scanner sc = new Scanner(System.in);
        int selecao = -1;

        System.out.print(clear);          // Limpa tela (windows)

        do{
            System.out.println();
            System.out.println("=====================================");
            System.out.println("          Opções de Backup           ");
            System.out.println("=====================================");
            System.out.println("1. Gerar um backup do sistema");
            System.out.println("2. Recuperar um backup");
            System.out.println("0. Voltar ao menu principal");
            System.out.println("=====================================");
            
            System.out.print("Escolha uma opção: ");

            // Leitura da seleção
            try{
                selecao = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
                selecao = 99999; // Erro de leitura - Buffer cheio, repete operação
            }

            switch(selecao) {
                case 99999:
                    // ERRO DE LEITURA
                    break;

                case 0:
                    // Fim programa
                    return;

                case 1:
                    // Gerar backup do sistema atual
                    gerar_backups();
                    break;

                case 2:
                    // Descomprimir arquivos
                    recuperar_backup();
                    break;

                default:
                    System.out.println("\nOpção inválida. Por favor, escolha um número entre as opcoes.");
            }
            
            System.out.println("\nPessione qualquer tecla para continuar");
            sc.nextLine();
            System.out.print(clear);          // Limpa tela (windows)

        }while(selecao != 0);

        //sc.close();    // Não fechar o scanner pois da erro  

    }

    /**
     * Recupera um backup da pasta de backups (escolhido pelo usuario)
     */
    public static void recuperar_backup(){
        Scanner sc = new Scanner(System.in);
        int selecao = -1;

        // Lista de backups disponiveis na pasta de backups
        File pasta_backups = new File("backups/");
        File [] arquivos = pasta_backups.listFiles();

        do{
            System.out.print(clear);          // Limpa tela (windows)

            System.out.println();
            System.out.println("=====================================");
            System.out.println("     Qual backup deseja recuperar?");
            System.out.println("=====================================");
            
            for(int i = 0; i < arquivos.length; i++){
                System.out.println((i+1) + ". " + arquivos[i].getName());
            }

            System.out.println("=====================================");

            System.out.print("Escolha uma opção válida: ");

            // Leitura da seleção
            try{
                selecao = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
                selecao = 99999; // Erro de leitura - Buffer cheio, repete operação
            }
            
            pasta_backups = new File(arquivos[selecao-1].getAbsolutePath());
            
        }while(selecao < 1 || selecao > arquivos.length);

        // Lista de arquivos na pasta de backup escolhida
        arquivos = pasta_backups.listFiles();
        String novo_nome;

        // Comprime todos os arquivos na pasta data e poe na pasta backup
        for(int i = 0; i < arquivos.length; i++){
            LZWCompression lzw = new LZWCompression(pasta_backups.getAbsolutePath() + "/"+ arquivos[i].getName());     // arquivo a ser compactado
            novo_nome = arquivos[i].getName();
            novo_nome = novo_nome.substring(0, novo_nome.length() - 4);
            lzw.decompress("data/" + novo_nome);  // arquivo compactado
            
        }
        System.out.println("Operação terminada");
            
    }

    /**
     * Gera um backup do sistema atual
     */
    public static void gerar_backups(){
        
        long versao = System.currentTimeMillis();                                       // Pra colocar no nome da pasta
        
        // Pasta com arquivos originais
        File pasta_data = new File("data/");
        
        // Criação da pasta backup
        File pasta_backup = new File("backups/backup_" + versao + "/");    
        if (pasta_backup.mkdir()) {
            System.out.println("Novo diretório com backups criado com sucesso\n");
        } else {
            System.out.println("Falha ao criar o diretório");
            return;
        }

        // Lista de arquivos na pasta data
        File [] arquivos = pasta_data.listFiles();

        // Comprime todos os arquivos na pasta data e poe na pasta backup
        for(int i = 0; i < arquivos.length; i++){
                LZWCompression lzw = new LZWCompression(arquivos[i].getAbsolutePath());     // arquivo a ser compactado
                lzw.compress("backups/backup_" + versao + "/"+ arquivos[i].getName());      // arquivo compactado
                
        }

    }

    public static void casamento_de_padroes(){
        Scanner sc = new Scanner(System.in);
        int selecao = -1;

        System.out.print(clear);          // Limpa tela (windows)

        // Cria o texto para ser usado nos algoritmos
        String texto = "";
        ArrayList<Pokemon> pokemons = CRUD.Pokemons_dados();
        for(int i= 0; i < pokemons.size(); i++){
            texto += pokemons.get(i).toString2(); 
        }

        // Solicita padrão ao usuario
        System.out.println("\nQual o seu padrao? " );
        String padrao =  sc.nextLine();

        int posicao;
        do{
            System.out.println();
            System.out.println("=====================================");
            System.out.println("     Opções de casamento de padrões  ");
            System.out.println("=====================================");
            System.out.println("1. Força Bruta");
            System.out.println("2. KMP");
            System.out.println("3. Boyer-Moore");
            System.out.println("4. Vizualizar texto");
            System.out.println("0. Voltar ao menu principal");
            System.out.println("=====================================");
            
            System.out.print("Escolha uma opção: ");

            // Leitura da seleção
            try{
                selecao = Integer.parseInt(sc.nextLine());
            }
            catch(Exception e){
                selecao = 99999; // Erro de leitura - Buffer cheio, repete operação
            }

            switch(selecao) {
                case 99999:
                    // ERRO DE LEITURA
                    break;

                case 0:
                    // Fim programa
                    return;

                case 1:
                    // Força Bruta
                    posicao = Forca_Bruta.Busca(padrao, texto);
                    System.out.println("Seu padrão foi encontrado na posição: " + posicao);
                    break;

                case 2:
                    // KMP
                    posicao = KMP.Busca(padrao, texto);
                    System.out.println("Seu padrão foi encontrado na posição: " + posicao);
                    break;

                case 3:
                    // Boyer-Moore
                    posicao = Boyer_Moore.buscar(padrao, texto);
                    System.out.println("Seu padrão foi encontrado na posição: " + posicao);
                    break;
                
                case 4:
                    // Ver texto
                    System.out.println("\n"+texto);
                    break;

                default:
                    System.out.println("\nOpção inválida. Por favor, escolha um número entre as opcoes.");
            }
            
            
            System.out.println("\nPessione qualquer tecla para continuar");
            sc.nextLine();
            System.out.print(clear);          // Limpa tela (windows)

        }while(selecao != 0);

        //sc.close();    // Não fechar o scanner pois da erro
    }

}

