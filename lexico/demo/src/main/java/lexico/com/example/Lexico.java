package lexico.com.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class Lexico {
    private String caminhoArquivo;
    private String nomeArquivo;
    private int c;
    BufferedReader br;
    private ArrayList<String> reservedWords = new ArrayList<String>(Arrays.asList(
        "and", "array", "begin", "case", "const", "div",
        "do", "downto", "else", "end", "file", "for", 
        "function", "goto", "if", "in", "label", "mod",
        "nil", "not", "of", "or", "packed", "procedure",
        "program", "record", "repeat", "set", "then",
        "to", "type", "until", "var", "while", "with"
    ));

    public Lexico(String nomeArquivo) {
        this.caminhoArquivo = Paths.get(nomeArquivo).toAbsolutePath().toString();
        this.nomeArquivo = nomeArquivo;

        try {
            this.br = new BufferedReader(new FileReader(caminhoArquivo, StandardCharsets.UTF_8));
            this.c = this.br.read();
        } catch (IOException err) {
            System.err.println("Não foi possível abrir o arquivo ou ler do arquivo: " + this.nomeArquivo);
            err.printStackTrace();
        }
    }

    public Token getToken(int linha, int coluna) {
        int e;
        StringBuilder lexema = new StringBuilder("");
        char caractere;
        Token token = new Token();

        try {

            while (c != -1) { // -1 fim da stream
                caractere = (char) c;
                if (!(c == 13 || c == 10)) {
                    if (caractere == ' ') {
                        while (caractere == ' ') {
                            c = this.br.read();
                            coluna++;
                            caractere = (char) c;
                        }
                    } else if (Character.isLetter(caractere)) {
                        while (Character.isLetter(caractere) || Character.isDigit(caractere)) {
                            lexema.append(caractere);
                            c = this.br.read();
                            coluna++;
                            caractere = (char) c;   
                        }
                        Valor valor = new Valor();
                        if(returnIfIsReservedWord(lexema.toString())){
                            token.setClasse(Classe.cPalRes);
                        } else {
                            token.setClasse(Classe.cId);
                        }
                        token.setColuna(coluna);
                        token.setLinha(linha);
                        valor.setValorIdentificador(lexema.toString());
                        token.setValor(valor);
                        return token;
                    } else if (Character.isDigit(caractere)) {
                        int numberOfPoints = 0;
                        while (Character.isDigit(caractere) || caractere=='.') {
                            if(caractere=='.'){
                                numberOfPoints++;
                            }
                            lexema.append(caractere);
                            c = this.br.read();
                            coluna++;
                            caractere = (char) c;
                        }
                        if(numberOfPoints<=1){
                            float numberConverted =  Float.parseFloat(lexema.toString());
                            Valor valor = new Valor();
                            if(numberConverted-Math.round(numberConverted)==0){
                                token.setClasse(Classe.cInt);
                                valor.setvalorInteiro(Integer.parseInt(lexema.toString()));
                            }
                            else{
                                token.setClasse(Classe.cReal);
                                valor.setValorDecimal(numberConverted);
                            }
                            token.setColuna(coluna);
                            token.setLinha(linha);
                            token.setValor(valor);
                            return token;
                        }
                    } else {
                        if(caractere==':'){
                            c = this.br.read();
                            caractere = (char) c;
                            if(caractere=='='){
                                token.setClasse(Classe.cAtribuicao);
                            }else{
                                token.setClasse(Classe.cDoisPontos);
                            }
                        }else if(caractere=='+'){
                            token.setClasse(Classe.cMais);
                        }else if(caractere=='-'){
                            token.setClasse(Classe.cMais);
                        }else if(caractere=='/'){
                            token.setClasse(Classe.cMais);
                        }else if(caractere=='*'){
                            token.setClasse(Classe.cMais);
                        }else if(caractere=='>'){
                            c = this.br.read();
                            caractere = (char) c;
                            if(caractere=='='){
                                token.setClasse(Classe.cMaiorIgual);
                            }else{
                                token.setClasse(Classe.cMaior);
                            }
                        }else if(caractere=='<'){
                            c = this.br.read();
                            caractere = (char) c;
                            if(caractere=='='){
                                token.setClasse(Classe.cMenorIgual);
                            }else if(caractere=='>'){
                                token.setClasse(Classe.cDiferente);
                            }else{
                                token.setClasse(Classe.cMenor);
                            }
                        }else if(caractere=='='){
                            token.setClasse(Classe.cIgual);
                        }else if(caractere==','){
                            token.setClasse(Classe.cVirgula);
                        }else if(caractere==';'){
                            token.setClasse(Classe.cPontoVirgula);
                        }else if(caractere=='.'){
                            token.setClasse(Classe.cPonto);
                        }else if(caractere=='('){
                            token.setClasse(Classe.cParEsq);
                        }else if(caractere==')'){
                            token.setClasse(Classe.cParDir);
                        }else{
                            token.setClasse(Classe.cEOF);
                        }
                        
                        token.setColuna(coluna);
                        token.setLinha(linha);
                        token.setValor(null);
                        c = this.br.read();
                        return token;
                    }
                }else{
                    c = this.br.read();
                    linha++;
                    coluna=0;
                }
            }

            token.setClasse(Classe.cEOF);
            return token;
        } catch (IOException err) {
            System.err.println("Não foi possível abrir o arquivo ou ler do arquivo: " + this.nomeArquivo);
            err.printStackTrace();
        }
        return null;
    }

    boolean returnIfIsReservedWord(String word){
        return this.reservedWords.contains(word);
    }
}