# Pokédex - Projeto de Algoritmos e Estruturas de Dados III

Este repositório contém a implementação completa de uma Pokédex em Java, desenvolvida como projeto prático da disciplina **Algoritmos e Estruturas de Dados III (PUC Minas)**. O sistema implementa funcionalidades avançadas como CRUD em arquivos binários, indexação, compressão, casamento de padrões e criptografia.

## 📦 Entregas
Os arquivos compactados das cinco etapas estão localizados na pasta Entregas:

TP01.zip — CRUD

TP02.zip — Indexação

TP03.zip — Compressão

TP04.zip — Casamento de Padrões

TP05.zip — Criptografia

## 🎥 Vídeos
Os links dos vídeos demonstrativos estão listados no arquivo VIDEOS.txt, na raiz do projeto.

## 🧩 Etapas do Projeto
### ✅ Etapa 1 — CRUD com Arquivo Sequencial
Implementação das operações de criação, leitura, atualização e remoção lógica em um arquivo binário.

Controle de espaços reaproveitáveis e cabeçalhos com informações úteis.

### ✅ Etapa 2 — Indexação
Geração de três tipos de índices:

Índice Direto (por ID)

Índice Indireto (por nome)

Índice Multilista (por tipo)

Acelera buscas sem varrer o arquivo inteiro.

### ✅ Etapa 3 — Compressão
Aplicação do algoritmo LZW para compactar os dados antes do armazenamento.

Redução do tamanho dos registros no disco.

### ✅ Etapa 4 — Casamento de Padrões
Busca de padrões nos nomes e descrições utilizando:

Força Bruta

KMP

Boyer-Moore

Rabin-Karp

### ✅ Etapa 5 — Criptografia
Implementação de algoritmo de cifragem simples com chave.

Os dados são cifrados antes de serem salvos e decifrados na leitura.


## 📁 Estrutura de Pastas

```plaintext
AEDs 3\Trabalhos\Pokedex
│   Pokemons.csv            → Base de dados original dos Pokémon
│   VIDEOS.txt              → Links dos vídeos demonstrativos de cada entrega
│
│
├───data                    → Arquivos binários gerados pelos índices e CRUD
│       indice_id.db
│       indice_multilista.db
│       indice_nomes.db
│       Pokemons_dados.db
│
├───Entregas                → Zips com os códigos e vídeos de cada etapa
│       TP01.zip
│       TP02.zip
│       TP03.zip
│       TP04.zip
│       TP05.zip
│
└───Scr                     → Códigos-fonte Java
        Boyer_Moore.java
        Cifragem.java
        CRUD.java
        Data.java
        DirectIndex.java
        Forca_Bruta.java
        KMP.java
        LZWCompression.java
        Main.java
        MultiList.java
        NameIndex.java
        Pokemon.java
        Rabin_karp.java
