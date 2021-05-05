import java.util.Scanner;

public class Cliente {
    Cliente() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.println("Inicializando cliente ...\n");

//            System.out.print("IP do servidor: ");
            System.out.print("IP do servidor: 127.0.0.1");
//            String ip = in.nextLine();
            String ip = "127.0.0.1";

//            System.out.print("Porta do servidor: ");
            System.out.print("\nPorta do servidor: 16000");
//            int porta = in.nextInt();
            int porta = 16000;

            System.out.println("\nUse '!sair' para desconectar-se e '!ranking' para visualizar o ranking.");

            System.out.print("\nInsira o seu nome de jogador: ");
            String nomeJogador = in.next();

            GerenciadorConexao gConexao = new GerenciadorConexao(ip, porta, nomeJogador);
            new Thread(gConexao).start();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }
}
