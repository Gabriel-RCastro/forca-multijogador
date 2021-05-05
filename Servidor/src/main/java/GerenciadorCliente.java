import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class GerenciadorCliente implements Runnable {

    Socket cliente;
    ObjectInputStream mensagemEntrada;
    ObjectOutputStream mensagemSaida;
    String palavra, letrasAdivinhadas;
    List<String> letrasPalpitadas = new ArrayList<>();
    int tentativasErradas = 0;
    Mensagem mensagem;
    List<Jogador> jogadores = new ArrayList<>();

    public GerenciadorCliente(Socket socket) throws Exception {
        try {
            cliente = socket;
            mensagemEntrada = new ObjectInputStream(socket.getInputStream());
            mensagemSaida = new ObjectOutputStream(cliente.getOutputStream());
        } catch (Exception e) {
            System.out.println("Exception: construtor GC - " + e.getMessage());
        }
    }

    @Override
    public void run() {

        while (!cliente.isClosed()) {

            if (mensagemEntrada == null) {
                System.out.println("*** MensagemEntrada == null");
                break;
            }

            try {
                mensagem = (Mensagem) mensagemEntrada.readObject();
                System.out.println("\n* Mensagem situacaoJogo: " + mensagem.situacaoJogo);

                if (mensagem.situacaoJogo == MensagemStatus.NOVO_JOGO) {
                    if (jogadores.size() == 0) {
                        jogadores.add(new Jogador(mensagem.nomeJogador, 0));
                    }

                    letrasPalpitadas = new ArrayList<>();
                    gerarNovaPalavra();
                    enviarNovoJogo();
                    System.out.println("\n* Aguardando resposta do cliente ...");
                    continue;
                } else if (mensagem.situacaoJogo == MensagemStatus.CONEXAO_ENCERRADA) {
                    mensagemEntrada.close();
                    mensagemSaida.close();
                    cliente.close();
                    continue;
                } else if (mensagem.situacaoJogo == MensagemStatus.RANKING) {
                    getRanking();
                    continue;
                }

                if (mensagem.letraTentativa.length() == 1) {
                    System.out.println("* Palpite do cliente: " + mensagem.letraTentativa);
                    boolean palpitado = false;

                    if(letrasPalpitadas.contains(mensagem.letraTentativa)) {
                        letraJaPalpitada();
                        palpitado = true;
                    }

                    if(mensagem.letraTentativa != null) {
                        letrasPalpitadas.add(mensagem.letraTentativa);

                        if (palavra.contains(mensagem.letraTentativa) && !palpitado) {
                            adicionarLetrasAdivinhadas(mensagem.letraTentativa);

                            if (!letrasAdivinhadas.contains("-")) {
                                enviarVitoria();
                            } else {
                                enviarPalpiteCerto();
                            }
                        } else {
                            enviarPalpiteErrado();
                        }
                    }

                } else {
                    if (palavra.equals(mensagem.letraTentativa)) {
                        enviarVitoria();
                    } else {
                        enviarPalpiteErrado();
                    }
                }

                if(tentativasErradas == 6) {
                    for (Jogador jogador: jogadores) {
                        jogador.setPontos(jogador.getPontos() - 5);
                    }
                    tentativasErradas = 0;
                }
            } catch (Exception e) {
                System.out.println("\nException: método run: " + e.getMessage());
                break;
            }
        }
        if (Servidor.mostrarInformacao) {
            System.out.printf("\nCliente %s desconectou-se.\n",
                    cliente.getInetAddress().toString().replaceFirst("/", ""));
        }
    }

    private void gerarNovaPalavra() throws Exception {
        File file = new File("src/main/resources/palavras.txt");
        BufferedReader bf = new BufferedReader(new FileReader(file));

        int lines = 0;
        while (bf.readLine() != null) lines++;

        bf = new BufferedReader(new FileReader(file));

        for (int i = 0; i < new Random().nextInt(lines); i++) {
            bf.readLine();
        }

        palavra = bf.readLine();
        letrasAdivinhadas = new String(new char[palavra.length()]).replace('\0', '-');
        letrasPalpitadas = new ArrayList<>();

        bf.close();
        System.out.println("\n* Palavra gerada: " + palavra);
    }

    private void adicionarLetrasAdivinhadas(String letras) {
        if (letras.length() != 1) {
            return;
        }

        char caractere = letras.charAt(0);
        boolean[] novasLetras = new boolean[palavra.length()];

        for (int i = 0; i < palavra.length(); i++) {
            if (palavra.charAt(i) == caractere) {
                novasLetras[i] = true;
            }
        }

        char[] caracteres = letrasAdivinhadas.toCharArray();

        for (int i = 0; i < novasLetras.length; i++) {
            if (novasLetras[i]) {
                caracteres[i] = caractere;
            }
        }

        letrasAdivinhadas = new String(caracteres);
    }

    private void enviarMensagem() {
        try {
            mensagemSaida.writeObject(mensagem);
            mensagemSaida.flush();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void enviarNovoJogo() {
        Jogador cliente = new Jogador();

        for(Jogador jogador: jogadores) {
            if(jogador.getNomeUsuario().equals(mensagem.nomeJogador))
                cliente = jogador;
        }

        mensagem = new Mensagem(MensagemStatus.NOVO_JOGO, cliente.getPontos(), null, letrasAdivinhadas);
        if (Servidor.mostrarInformacao) {
            System.out.printf("Cliente %s começou um novo jogo! \n",
                    cliente.getNomeUsuario());
        }
        enviarMensagem();
    }

    private void enviarVitoria() {
        for (Jogador jogador: jogadores) {
            jogador.setPontos(jogador.getPontos() + 5);
        }

        Jogador cliente = getJogador(0);
        mensagem = new Mensagem(MensagemStatus.VITORIA, cliente.getPontos(), palavra, null);

        if (Servidor.mostrarInformacao) {
            System.out.printf("Cliente %s descobriu a palavra! \n",
                    cliente.getNomeUsuario());
        }

        enviarMensagem();
    }

    private void enviarPalpiteCerto() {
        Jogador jogador = this.getJogador(1);
        mensagem = new Mensagem(MensagemStatus.PALPITE_CERTO, jogador.getPontos(), null, letrasAdivinhadas, letrasPalpitadas.toString());

        if (Servidor.mostrarInformacao) {
            System.out.printf("Cliente %s acertou o palpite!  %s \n",
                    jogador.getNomeUsuario(), letrasAdivinhadas);
        }

        enviarMensagem();
    }

    private void enviarPalpiteErrado() {
        Jogador jogador = this.getJogador(-3);
        mensagem = new Mensagem(MensagemStatus.PALPITE_ERRADO, jogador.getPontos(), null, letrasAdivinhadas, letrasPalpitadas.toString());

        if (Servidor.mostrarInformacao) {
            System.out.printf("Cliente %s errou o palpite!  %s \n",
                    mensagem.nomeJogador, letrasAdivinhadas);
        }

        tentativasErradas++;
        enviarMensagem();
    }

    private void letraJaPalpitada() {
        Jogador jogador = this.getJogador(-1);
        mensagem = new Mensagem(MensagemStatus.PALPITE_ERRADO, jogador.getPontos(), null, letrasAdivinhadas, letrasPalpitadas.toString());

        if (Servidor.mostrarInformacao) {
            System.out.printf("Cliente %s errou o palpite!  %s \n",
                    mensagem.nomeJogador, letrasAdivinhadas);
        }

        tentativasErradas++;
        enviarMensagem();
    }

    private void getRanking() {
        StringBuilder ranking = new StringBuilder();
        Collections.sort(jogadores, new Jogador());
        int count = 0;

        ranking.append("==========================\n");
        for (Jogador jogador: jogadores) {
            count++;
            String jogadorString = count + ". " + jogador.getNomeUsuario() + ": " + jogador.getPontos() + "\n";
            ranking.append(jogadorString);
        }
        ranking.append("==========================");

        mensagem = new Mensagem(MensagemStatus.RANKING, this.getJogador(0).getPontos(), null, letrasAdivinhadas, letrasPalpitadas.toString(), ranking.toString());
        enviarMensagem();
    }

    private Jogador getJogador(int pontosGanhos) {
        for (Jogador jogador : jogadores) {
            if (jogador.getNomeUsuario().equals(mensagem.nomeJogador)) {
                jogador.setPontos(jogador.getPontos() + pontosGanhos);
                return jogador;
            }
        }
        System.out.println("*** Erro getJogador ---------------------");
        return null;
    }

}
