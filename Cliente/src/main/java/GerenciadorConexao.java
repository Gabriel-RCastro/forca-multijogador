import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GerenciadorConexao implements Runnable {
    String nomeJogador;
    int pontuacao = 0, tentativasPermitidas, porta;
    String palavra, palavrasAdivinhadas, ip;
    Mensagem msg;
    Socket socket;
    ObjectInputStream mensagemEntrada;
    ObjectOutputStream mensagemSaida;

    InterfaceTextual interfaceTextual = new InterfaceTextual();

    GerenciadorConexao(String ip, int porta, String nomeJogador) {
        this.ip = ip;
        this.porta = porta;
        this.nomeJogador = nomeJogador;
    }

    public void enviarMensagem(Mensagem msg) {
        try {
            mensagemSaida.writeObject(msg);
            mensagemSaida.flush();

        } catch (Exception ex) {
            System.out.println("Não foi possível se comunicar com o servidor!");
        }
    }

    @Override
    public void run() {
        if (!conectar()) return;

        enviarMensagem(new Mensagem(MensagemStatus.NOVO_JOGO, nomeJogador));

        while (!socket.isClosed()) {
            try {
                Mensagem mensagem = (Mensagem) mensagemEntrada.readObject();
                if (mensagem != null) {
                    if (mensagem.situacaoJogo == MensagemStatus.VITORIA) {
                        interfaceTextual.vitoria(mensagem);
                        enviarMensagem(new Mensagem(MensagemStatus.NOVO_JOGO, nomeJogador));

                    } else if (mensagem.situacaoJogo == MensagemStatus.PALPITE_CERTO || mensagem.situacaoJogo == MensagemStatus.PALPITE_ERRADO || mensagem.situacaoJogo == MensagemStatus.NOVO_JOGO) {
                        interfaceTextual.atualizarInformacao(mensagem);
                        String palpite = interfaceTextual.palpite();

                        if (palpite.equals("!sair")) {
                            desconectar();
                        } else if (palpite.equals("!ranking")) {
                            enviarMensagem(new Mensagem(MensagemStatus.RANKING, nomeJogador));
                        } else {
                            enviarMensagem(new Mensagem(MensagemStatus.PALPITE, nomeJogador, palpite));
                        }
                    } else if (mensagem.situacaoJogo == MensagemStatus.CONEXAO_ENCERRADA) {
                        desconectar();
                    } else if (mensagem.situacaoJogo == MensagemStatus.RANKING) {
                        System.out.println(mensagem.ranking);
                        interfaceTextual.aceitar();

                        interfaceTextual.atualizarInformacao(mensagem);
                        String palpite = interfaceTextual.palpite();

                        if (palpite.equals("!sair")) {
                            desconectar();
                        } else if (palpite.equals("!ranking")) {
                            enviarMensagem(new Mensagem(MensagemStatus.RANKING, nomeJogador));
                        } else {
                            enviarMensagem(new Mensagem(MensagemStatus.PALPITE, nomeJogador, palpite));
                        }
                    }
                } else Thread.yield();
            } catch (Exception ex) {
                System.out.println("Exceção método run GerenciadorConexao: " + ex.getMessage());
                return;
            }
        }
    }

    public boolean conectar() {
        try {
            socket = new Socket(ip, porta);
            mensagemSaida = new ObjectOutputStream(socket.getOutputStream());
            mensagemEntrada = new ObjectInputStream(socket.getInputStream());

            return true;
        } catch (Exception e) {
            System.out.println("Não foi possível se comunicar com o servidor!");
            return false;
        }
    }

    public void desconectar() {
        try {
            enviarMensagem(new Mensagem(MensagemStatus.CONEXAO_ENCERRADA));
            mensagemSaida.close();
            mensagemEntrada.close();
            socket.close();
            mensagemSaida = null;
            mensagemEntrada = null;

            System.out.println("Conexão encerrada!");
        } catch (Exception ex) {
            System.out.println("Não foi possível terminar a conexão!");
        }
    }
}