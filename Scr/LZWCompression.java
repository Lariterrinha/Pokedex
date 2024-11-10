package Scr;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

public class LZWCompression{

    ArrayList<String> dicionario; 
    RandomAccessFile arq;
    String caminho_arq;
    final int TAMANHO_DICIONARIO = 256;

    LZWCompression(String caminho_arq){
        try {
            dicionario = new ArrayList<String>();
            this.arq = new RandomAccessFile(caminho_arq, "rw");
            this.caminho_arq = caminho_arq;
            inicializeDicionario();

        } catch (IOException e) {
            e.printStackTrace();
        }  
    }

    public void inicializeDicionario(){
        
        // Adiciona todos os caracteres da tabela ASCII (0 - 255)
        
        for(int i = 0; i < TAMANHO_DICIONARIO; i++){
            if(!dicionario.contains((char)i + ""))
                dicionario.add((char)i + "");
        }
        
        /*
        // Pega todos os caracteres presentes no arquivo
        String caracter_lido;
        try{

            arq.seek(0);
            while(arq.getFilePointer() < arq.length()){

                caracter_lido = (char)arq.read() + "";
                if(!dicionario.contains(caracter_lido)){
                    dicionario.add(caracter_lido);
                }
            }

        }catch(IOException e){
            e.printStackTrace();
        }
         */
        
    }

    /**
     * Comprime o arquivo de entrada desta instância
     */
    public void compress(String novo_caminho){
        int ultima_pesquisa_bem_sucedida = 0;
        int int_byte_lido;
        String caracter_lido;
        String combinado = "";

        try{
            RandomAccessFile arq_compactado = new RandomAccessFile(novo_caminho + ".lzw", "rw");
            arq.seek(0);
            long tamanho_original = arq.length();

            caracter_lido = (char)arq.readByte() + "";
            combinado = caracter_lido;
            ultima_pesquisa_bem_sucedida = dicionario.indexOf(caracter_lido);

            // Compressão até o final do arquivo
            while(arq.getFilePointer() < arq.length()){

                // Lê um caracter e concatena ao combinado (caracter do dicionario)
                int_byte_lido = arq.readByte() & (int)0x0FF ;
                caracter_lido = (char)(int_byte_lido) + "";
                combinado += caracter_lido; 

                // Caso contenha o catacter combinado no dicionario continua concatenando e salva a ultima pesquisa bem sucedida
                if(dicionario.contains(combinado)){
                    ultima_pesquisa_bem_sucedida = dicionario.indexOf(combinado);
                }
                //  Caso não, adiociona e escreve a posição da ultima pesquisa valida no arq compactado 
                else{

                    if(dicionario.size() < 32767){      // Congelamento do dicionario em 2 bytes 
                        dicionario.add(combinado);
                    }

                    arq_compactado.writeShort(ultima_pesquisa_bem_sucedida);
                    combinado = caracter_lido;                     // Reinicia String combinada
                    ultima_pesquisa_bem_sucedida = dicionario.indexOf(caracter_lido); // Atualiza ultima pesquisa bem sucedida
                }
            }
            if(combinado.equals(caracter_lido))    // Verifica se combinado não está vazio e adiciona o restante que falta
                arq_compactado.writeShort(ultima_pesquisa_bem_sucedida);


            System.out.print("Economia arquivo " + novo_caminho.split("/")[novo_caminho.split("/").length-1] + ": \t\t"+ (tamanho_original - arq_compactado.length()) + " bytes\t || ");
            System.out.printf("%.2f%% do tamanho original\n",(arq_compactado.length()/(float)tamanho_original) * 100);
            
            arq_compactado.close();
            arq.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public void decompress(String novo_caminho){

        try{
            // Cria um novo arquivo para descompactar
            RandomAccessFile arq_descompactado = new RandomAccessFile(novo_caminho , "rw");

            this.arq.seek(0);

            // Lê o primeiro código do arquivo compactado
            short codigo_lido = arq.readShort();
            String sequencia_decodificada = dicionario.get((int)codigo_lido);
            String concatenada = sequencia_decodificada;

            // Como só tem um caracter inicialmente, escreve ele
            arq_descompactado.writeByte((byte)sequencia_decodificada.charAt(0));

            // Anda até o final do arquivo compactado (Enquanto não for o ultimo "short" continua)
            while(arq.getFilePointer() < arq.length()){ 
                codigo_lido = arq.readShort();

                if(codigo_lido >= dicionario.size()){
                    concatenada += concatenada.charAt(0);
                    sequencia_decodificada = concatenada;
                }else{
                    sequencia_decodificada = dicionario.get(codigo_lido);
                    concatenada+= sequencia_decodificada.charAt(0);
                }
                // Add no dicionario a string concatenada
                if(dicionario.size() < 32767){ // Congelamento do dicionario em 2 bytes
                    dicionario.add(concatenada);
                }

                concatenada = sequencia_decodificada; // Reinicia a string concatenada

                // Escreve todos os caracteres da sequência decodificada no arquivo
                for(char c : sequencia_decodificada.toCharArray()){
                    arq_descompactado.writeByte((byte)c);
                }
            
            }

            arq.close();
            arq_descompactado.close();
            
        }catch(Exception e){
                e.printStackTrace();
        }
    }
        

}