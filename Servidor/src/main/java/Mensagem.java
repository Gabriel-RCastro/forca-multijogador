import java.io.Serializable;

public class Mensagem implements Serializable {

    private static final long serialVersionUID = -7588980448693010399L;

    public MensagemStatus situacaoJogo;
    int pontuacao;
    public String palavra, letrasAdivinhadas;
    public String letraTentativa;
    String letrasPalpitadas;
    String nomeJogador, ranking;

    public Mensagem(MensagemStatus situacaoJogo, int pontuacao, String palavra, String letrasAdivinhadas) {
        this.situacaoJogo = situacaoJogo;
        this.pontuacao = pontuacao;
        this.palavra = palavra;
        this.letrasAdivinhadas = letrasAdivinhadas;
    }

    public Mensagem(MensagemStatus situacaoJogo, int pontuacao, String palavra, String letrasAdivinhadas, String letrasPalpitadas) {
        this.situacaoJogo = situacaoJogo;
        this.pontuacao = pontuacao;
        this.palavra = palavra;
        this.letrasAdivinhadas = letrasAdivinhadas;
        this.letrasPalpitadas = letrasPalpitadas;
    }

    public Mensagem(MensagemStatus situacaoJogo, int pontuacao, String palavra, String letrasAdivinhadas, String letrasPalpitadas, String ranking) {
        this.situacaoJogo = situacaoJogo;
        this.pontuacao = pontuacao;
        this.palavra = palavra;
        this.letrasAdivinhadas = letrasAdivinhadas;
        this.letrasPalpitadas = letrasPalpitadas;
        this.ranking = ranking;
    }
}
