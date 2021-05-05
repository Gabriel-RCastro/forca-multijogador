public enum MensagemStatus {

    PALPITE(0),
    NOVO_JOGO(-1),
    VITORIA(-2),
    DERROTA(-3),
    PALPITE_CERTO(-4),
    PALPITE_ERRADO(-5),
    CONEXAO_ENCERRADA(-6),
    NOVA_CONEXAO(-7),
    RANKING(-8);

    int situacaoJogo;

    MensagemStatus(int situacaoJogo) {
        this.situacaoJogo = situacaoJogo;
    }
}
