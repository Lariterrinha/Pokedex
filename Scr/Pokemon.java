package Scr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class Pokemon{
    
    private int id_pokedex;             // Identificador unico
    private String name;                // Nome do pokemon
    private int[] attributes;           // Attaque, defesa e HP
    private String classfication;       // Tipo do animal pokemon (rato, passaro, etc)
    private byte generation;            // Geração (1-7) 
    private long next;                  // Proximo com a mesma geração
    private Data date;                  // Data aleatoria baseda no ano da geração 
    private String[] abilities;         // Habilidades do pokemon
   

    
    /***************************** CONSTRUTORES **********************************/
    Pokemon(){
        this.next = -1;
    }


    public Pokemon(int id_pokedex, String name, int[] attributes, String classfication, byte generation, Data date,
            String []abilities) {
        this.id_pokedex = id_pokedex;
        this.name = name;
        this.attributes = attributes;
        this.classfication = classfication;
        this.generation = generation;
        this.date = date;
        this.abilities = abilities;
        this.next = -1;
    }


    /***************************** GETTERS e SETTERS **********************************/

    public long getnext() {
        return next;
    }


    public void setnext(long next_gen) {
        this.next = next_gen;
    }

    public int getId_pokedex() {
        return id_pokedex;
    }


    public void setId_pokedex(int id_pokedex) {
        this.id_pokedex = id_pokedex;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int[] getAttributes() {
        return attributes;
    }


    public void setAttributes(int[] attributes) {
        this.attributes = attributes;
    }

    public void setAttributes(int attack, int defense, int hp) {
        // Alocação de espaço tamanho 3
        int []a = new int[3];
        this.attributes = a;
        
        // alteração de valores
        this.attributes[0] = attack;
        this.attributes[1] = defense;
        this.attributes[2] = hp;
    }

    public String getClassfication() {
        return classfication;
    }


    public void setClassfication(String classfication) {
        this.classfication = classfication;
    }


    public byte getGeneration() {
        return generation;
    }


    public void setGeneration(byte generation) {
        this.generation = generation;
    }


    public Data getDate() {
        return date;
    }


    public void setDate(Data date) {
        this.date = date;
    }

    public void setDate(int dd, int mm, int aaaa) {
        this.date = new Data(dd,mm,aaaa);
    }
    
    public void setDate(int dias) {
        this.date = new Data(dias);
    }

    public String[] getAbilities() {
        return abilities;
    }


    public void setAbilities(String []abilities) {
        this.abilities = abilities;
    }


    @Override
    public String toString() {
        return "Pokemon [ id_pokedex = " + id_pokedex + ", name = " + name + ", classfication = " + classfication
                + ", generation = " + generation + ", Data = " +  this.date.toString()  +" ]";
    }

    // Cria um clone deste objeto
    public Pokemon clone() {

        Pokemon clone = new Pokemon(this.id_pokedex, this.name, this.attributes, this.classfication,
        this.generation, this.date, this.abilities);
        
        return clone;
    }
    
    /***************************** MANIPULAÇÂO DE ARQUIVOS **********************************/

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(this.id_pokedex);
        dos.writeUTF(this.name);
        dos.writeInt(this.attributes[0]);
        dos.writeInt(this.attributes[1]);
        dos.writeInt(this.attributes[2]);
        dos.writeUTF(this.classfication);
        dos.writeByte(this.generation);
        dos.writeLong(this.next);
        dos.writeInt(date.tempo_dias);

        dos.writeShort(abilities.length);
        for (int i= 0; i < abilities.length; i++){
            dos.writeUTF(abilities[i]);
        }
        
        return baos.toByteArray();
    }

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        // Leitura de dados
        this.id_pokedex = dis.readInt();
        this.name = dis.readUTF();
        this.attributes =  new int[3];
        this.attributes[0] = dis.readInt();
        this.attributes[1] = dis.readInt();
        this.attributes[2] = dis.readInt();
        this.classfication = dis.readUTF();
        this.generation = dis.readByte();
        this.next = dis.readLong();
        this.date = new Data(dis.readInt());

        // Array de strings (habilidades)
        Short len = dis.readShort();
        this.abilities = new String[len];
        for (int i= 0; i < len; i++){
            abilities[i] = dis.readUTF();
        }

    }


    /************** Atualiza manualmente um Pokemon ******************/
    public void alterarPokemon(){

        int opcao;

        Scanner sc = new Scanner (System.in);
        System.out.print("\033[H\033[2J");          // Limpa tela (windows)

        System.out.println("Alterar pokemon: " + this.toString());

        /* MENU */
        //  Pergunta pro usuario oq ele quer mudar e altera
        do {
            System.out.println("\nEscolha o atributo que deseja alterar:");
            System.out.println("1. Nome");
            System.out.println("2. Atributos (Ataque, Defesa, HP)");
            System.out.println("3. Classificação");
            System.out.println("4. Geração");
            System.out.println("5. Data");
            System.out.println("6. Habilidades");
            System.out.println("7. Aplicar Mudanças");
            System.out.print("Opção: ");

            opcao =  Integer.parseInt(sc.nextLine());

            switch (opcao) {

                // Atualização nome
                case 1:
                    System.out.print("Novo Nome: ");
                    String new_name = sc.nextLine();
                    NameIndex.update(this.name, new_name);
                    this.setName(new_name);
                    break;

                // Atualização atributos (ataque, defesa, HP)
                case 2:
                    int []attributes = new int[3];
                    System.out.print("Novo Ataque: ");
                    attributes[0] =  Integer.parseInt(sc.nextLine());
                    System.out.print("Nova Defesa: ");
                    attributes[1] =  Integer.parseInt(sc.nextLine());
                    System.out.print("Novo HP: ");
                    attributes[2] =  Integer.parseInt(sc.nextLine());

                    this.setAttributes(attributes);
                    break;

                // Atualização Classificação
                case 3:
                    System.out.print("Nova Classificação: ");
                    this.setClassfication(sc.next());
                    break;

                // Atualização geração
                case 4:
                    System.out.print("Nova Geração: ");
                    byte geracao = Byte.parseByte(sc.nextLine());

                    // Atualiza multilista e Pokemon com nova geração
                    MultiList.unchain(this.id_pokedex);
                    this.setGeneration(geracao);
                    MultiList.chain(this);
                    break;

                // Atualização data
                case 5:
                    int []aux = new int[3];
                    System.out.println("Nova data: ");

                    // Dia
                    System.out.print("Dia: ");
                    aux[0] =  Integer.parseInt(sc.nextLine());

                    // Mês
                    do{
                    System.out.print("Mês (1/12): ");
                    aux[1] =  Integer.parseInt(sc.nextLine());
                    }while(aux[1] > 12);

                    // Ano
                    do{
                    System.out.print("Ano (Depois de 1970): ");
                        aux[2] =  Integer.parseInt(sc.nextLine());
                    } while(aux[2] < 1970);

                    this.setDate(aux[0], aux[1], aux[2]);

                    break;
                
                // Atualização vetor de strings de habilidades
                case 6:
                    System.out.print("Quantas habilidades deseja ter? ");
                    int numAbilities =  Integer.parseInt(sc.nextLine());
                    String[] abilities = new String[numAbilities];
                    for (int i = 0; i < numAbilities; i++) {
                        System.out.print("Habilidade " + (i + 1) + ": ");
                        abilities[i] = sc.next();
                    }                
                    this.setAbilities(abilities);
                    break;
                
                // Aplicar alterações
                case 7:
                    break;

                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }

            System.out.print("\033[H\033[2J");

        } while (opcao != 7);
        //sc.close();
    }

}