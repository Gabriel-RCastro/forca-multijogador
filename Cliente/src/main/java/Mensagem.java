import java.io.Serializable;

public class Mensagem implements Serializable {
    static final long serialVersionUID = -7588980448693010399L;

    public MensagemStatus situacaoJogo;
    public int pontuacao, tentativasPermitidas;
    public String palavra, letrasAdivinhadas;
    public String letraTentativa;
    String letrasPalpitadas;
    String nomeJogador, ranking;

    Mensagem(MensagemStatus situacaoJogo) {
        this.situacaoJogo = situacaoJogo;
    }

    Mensagem(MensagemStatus situacaoJogo, String nomeJogador) {
        this.situacaoJogo = situacaoJogo;
        this.nomeJogador = nomeJogador;
    }

    Mensagem(MensagemStatus situacaoJogo, String nomeJogador, String letra) {
        this.situacaoJogo = situacaoJogo;
        this.nomeJogador = nomeJogador;
        this.letraTentativa = letra;
    }

    Mensagem(MensagemStatus situacaoJogo, int pontuacao, int tentativasPermitidas, String palavra, String letrasAdivinhadas) {
        this.situacaoJogo = situacaoJogo;
        this.pontuacao = pontuacao;
        this.tentativasPermitidas = tentativasPermitidas;
        this.palavra = palavra;
        this.letrasAdivinhadas = letrasAdivinhadas;
    }
}
