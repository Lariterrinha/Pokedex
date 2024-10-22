package Scr;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class MultiList {
    
    static String arq_multilista = "indice_multilista.db";  // Arquivo de dados em binario
    static String arq_ids = "indice_id.db";         // Arquivo de indices com Ids e endereços
    static String arq_dados = "Pokemons_dados.db";  // Arquivo de dados em binario
    
    public static boolean chain(Pokemon pokemon){
       
       try{
            RandomAccessFile arq_mult = new RandomAccessFile(arq_multilista, "rw");
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids, "rw");
            
            long position_id = DirectIndex.getPosition(pokemon.getId_pokedex());
            
            arq_mult.seek(0);

            byte gen_lido;
            int qtd_lido;
            long start_id;
            long position_qtd;

            // Roda até o ultimo registro da multilista
            while(arq_mult.getFilePointer() <= (arq_mult.length() - 1 - 4 - 8)){

                // Leitura de registro da multilista
                gen_lido = arq_mult.readByte();
                position_qtd = arq_mult.getFilePointer();                   // Posição de onde está a quatidade de registros
                qtd_lido = arq_mult.readInt();
                start_id = arq_mult.readLong();

                

                
                if(gen_lido == pokemon.getGeneration()){
                    
                    addToEnd(start_id, position_id);  // Encadeia a nova posição na lista que começa no registro lido

                    // Atualiza quantidade de itens na multilista
                    arq_mult.seek(position_qtd);
                    qtd_lido += 1;
                    arq_mult.writeInt(qtd_lido);

                    // fecha arquivos e retorna operação bem sucedida
                    arq_mult.close();
                    arq_id.close();
                    return true;                    // Operação bem sucedida
                }
            }

            // Se chegou até siginifica que não achou (não entrou no if generation == generation)
            // Adiciona ao final do arquivo essa nova geração
            arq_mult.write(pokemon.getGeneration());
            arq_mult.writeInt(1);
            arq_mult.writeLong(position_id);
            

            arq_id.close();
            arq_mult.close();


        }catch (EOFException e) {
            return false;

        }catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void addToEnd(long start, long new_item){
        try{
            RandomAccessFile arq_bin = new RandomAccessFile(arq_dados, "rw");
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids, "rw");
            
            byte[] b;
            byte lapide;
            int tam_reg;
            Pokemon pokemon = new Pokemon();
            pokemon.setnext(start);
            
            // Leitura de arquivo do primeiro item da lista
            do{
                arq_id.seek(pokemon.getnext() + 4 + 1);         // posiciona no arq indices pula lapide e id

                arq_bin.seek(arq_id.readLong());                // Posiciona no arquivo de dados

                lapide = arq_bin.readByte();
                tam_reg = arq_bin.readInt();

                if(lapide == 0x00){
                    b = new byte[tam_reg];
                    arq_bin.readFully(b);
                    pokemon.fromByteArray(b);
                }else{
                    arq_bin.skipBytes(tam_reg);
                }
            }while(pokemon.getnext() != -1);
            
            // Volta até onde vai subrescrever o registro com o novo encadeamento e o escreve
            arq_bin.seek(arq_bin.getFilePointer() - tam_reg);
            pokemon.setnext(new_item);
            arq_bin.write(pokemon.toByteArray());

            arq_id.close();
            arq_bin.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printList(Byte generation){
        try{
            RandomAccessFile arq_mult = new RandomAccessFile(arq_multilista, "rw");
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids, "rw");
            RandomAccessFile arq_bin = new RandomAccessFile(arq_dados, "rw");

            // Leitura no arquivo multilista
            byte gen_lido;
            int qtd_lido;
            long start_id;

            while(arq_mult.getFilePointer() <= (arq_mult.length() - 1 - 4 - 8)){

                // Leitura de registro da multilista
                gen_lido = arq_mult.readByte();
                qtd_lido = arq_mult.readInt();
                start_id = arq_mult.readLong();

                // Se encontrar imprime pokemons da geração requerida
                if(gen_lido == generation){

                    printList(start_id, qtd_lido);
                    arq_mult.close();
                    arq_id.close();
                    arq_bin.close();
                    return;
                }
            }

            // Se não encontrar imprime mensagem de que não achou
            System.out.println("\nGeração não encontrada");

            arq_mult.close();
            arq_id.close();
            arq_bin.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static void printList(long start, int quantidade){
         

        try{
            RandomAccessFile arq_mult = new RandomAccessFile(arq_multilista, "rw");
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids, "rw");
            RandomAccessFile arq_bin = new RandomAccessFile(arq_dados, "rw");


            // Leitura no arquivo de dados
            byte[] b;
            byte lapide;
            int tam_reg;
            Pokemon pokemon = new Pokemon();
            pokemon.setnext(start);

            // Percorre o arquivo de dados imprimindo os registro do start_id selecionado;
            for(int i = 0; i < quantidade; i++){

                arq_id.seek(pokemon.getnext() + 4 + 1);         // posiciona no arq indices pula lapide e id
                arq_bin.seek(arq_id.readLong());                // Posiciona no arquivo de dados

                lapide = arq_bin.readByte();
                tam_reg = arq_bin.readInt();

                // Check de segurança
                if(lapide == 0x00){
                    // Lê o pokemone o imprime no console
                    b = new byte[tam_reg];
                    arq_bin.readFully(b);
                    pokemon.fromByteArray(b);
                    System.out.println(pokemon.toString());

                }else{
                    arq_bin.skipBytes(tam_reg);
                }
            }
            arq_mult.close();
            arq_id.close();
            arq_bin.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getfirst(byte generation){
        long end = 0;
        
        try{
            RandomAccessFile arq_mult = new RandomAccessFile(arq_multilista, "rw");
            
            arq_mult.seek(0);

            byte gen_lido;
            long multilist_start;

            while(arq_mult.getFilePointer() <= (arq_mult.length() - 1 - 4 - 8)){

                multilist_start = arq_mult.getFilePointer();

                // Leitura de registro da multilista
                gen_lido = arq_mult.readByte();
                arq_mult.getFilePointer();                   // Posição de onde está a quatidade de registros
                arq_mult.readInt();
                arq_mult.readLong();

                
                if(gen_lido == generation){
                    return multilist_start;                    // Operação bem sucedida
                }
            }

            arq_mult.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return end;

    }
    public static void unchain(int id){
        //try{
        try{
            RandomAccessFile arq_mult = new RandomAccessFile(arq_multilista, "rw");
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids, "rw");
            RandomAccessFile arq_bin = new RandomAccessFile(arq_dados, "rw");


            // Lê o registro a ser excluído e obtém sua geração
            Pokemon registro = DirectIndex.readPokemon(id);
            byte generation = registro.getGeneration();

            // Obtém a posição do primeiro registro dessa geração na multilista
            long multilist_start = getfirst(generation);

            // Leitura no arquivo de dados
            byte[] b;
            byte lapide;
            int tam_reg;
            Pokemon pokemon_atual = new Pokemon();
            Pokemon pokemon_anterior = new Pokemon();
            long posicao_anterior_ids = -1 , posicao_atual_ids = multilist_start;

            pokemon_atual.setnext(multilist_start);

            // Percorre o arquivo de dados até o poekemon requerido
            while(pokemon_atual.getId_pokedex() != id){

                
                // atualiza pokemon_anterior e posicao_anterior
                pokemon_anterior = pokemon_atual.clone();
                posicao_anterior_ids = posicao_atual_ids;
                
                posicao_atual_ids = pokemon_atual.getnext(); 
                arq_id.seek(posicao_atual_ids + 4 + 1);               // Posiciona no arq indices pula lapide e id
                arq_bin.seek(arq_id.readLong());                      // Posiciona no arquivo de dados
                
                lapide = arq_bin.readByte();
                tam_reg = arq_bin.readInt();

                // Lê o pokemone_atual
                b = new byte[tam_reg];
                arq_bin.readFully(b);
                pokemon_atual.fromByteArray(b);
                
            }
            
            // Se o registro a ser excluído for o primeiro da lista atualiza no arquivo da multilista
            if(multilist_start == posicao_atual_ids){
                arq_mult.seek(multilist_start + 1 + 4);
                arq_mult.writeLong(pokemon_atual.getnext());
            }else{
            //Se não desencadeia o pokemon requerido   
                pokemon_anterior.setnext(pokemon_atual.getnext());      // atualiza pra onde o arquivo anterior deve apontar 
                
                // Obtem posição e atualiza registro "anterior"
                arq_id.seek(posicao_anterior_ids + 4 + 1);              
                arq_bin.seek(arq_id.readLong() + 1 + 4);
                arq_bin.write(pokemon_anterior.toByteArray());
            }

            // Atualiza a quantidade de itens na multilista
            arq_mult.seek(multilist_start + 1);
            int qtd = arq_mult.readInt();
            arq_mult.seek(arq_mult.getFilePointer() - 4);
            arq_mult.writeInt(qtd -1);

            arq_mult.close();
            arq_id.close();
            arq_bin.close();

        }catch (EOFException e) {
            System.out.println("(EOF)");
            
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
