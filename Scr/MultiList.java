package Scr;

import java.io.EOFException;
import java.io.RandomAccessFile;

public class MultiList {
    
    static String arq_multilista = "indice_multilista.db";  // Arquivo de dados em binario
    static String arq_ids = "indice_id.db";         // Arquivo de indices com Ids e endereços
    static String arq_dados = "Pokemons_dados.db";  // Arquivo de dados em binario
    
    public static boolean linkTogether(Pokemon pokemon, long position_id){
       
       try{
            RandomAccessFile arq_mult = new RandomAccessFile(arq_multilista, "rw");
            RandomAccessFile arq_id = new RandomAccessFile(arq_ids, "rw");
            
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

            arq_bin.close();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
