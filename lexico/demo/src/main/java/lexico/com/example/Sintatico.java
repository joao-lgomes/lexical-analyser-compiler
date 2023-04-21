package lexico.com.example;

public class Sintatico {
    private Lexico lexico;
    private Token token;


    public Sintatico(String nomeArquivo){
        this.lexico = new Lexico(nomeArquivo);
        token = lexico.getToken(0, 0);
    }

    public void analisar(){
        programa();
    }

    public void programa(){
        if(token.getClasse() == Classe.cPalRes &&
            token.getValor().getValorIdentificador().equals("program")){
                token = lexico.getToken(0, 0);
                // id();
                //(A1)
                // corpo();
                if(token.getClasse() == Classe.cPonto){
                    token = lexico.getToken(0, 0);
                    //A31
                }else{
                    System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+"Faltou um ponto final no PROGRAM");
                }
            }else{
                    System.out.println("Faltou começar por PROGRAM");
            }
    }

    public void id(){
        if(token.getClasse()==Classe.cId){
            
        }else{
            System.out.println("Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+"Faltou o id após o program");
        }
    }
}
