import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Servidor {

    private static int porta = 16000;
    private static int numeroMaximoClientes = 10;
    public static final boolean mostrarInformacao = true;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket;

            if (args.length == 1)
                porta = Integer.parseInt(args[0]);
            else if (args.length == 2) {
                porta = Integer.parseInt(args[0]);
                numeroMaximoClientes = Integer.parseInt(args[1]);
            }

            serverSocket = new ServerSocket(porta);
            Executor executor = Executors.newFixedThreadPool(numeroMaximoClientes);

            if (mostrarInformacao) {
                System.out.printf("Inicialização na porta: %s. \nNúmero máximo de clientes: %s.",
                        porta, numeroMaximoClientes);
            }

            while (true) {
                try {
                    Socket cliente = serverSocket.accept();

                    if (mostrarInformacao) {
                        System.out.printf("\nNova conexão aceita: %s",
                                cliente.getInetAddress().toString().replaceFirst("/", ""));
                    }

                    executor.execute(new GerenciadorCliente(cliente));

                } catch (Exception e) {
                    System.out.println("\nException: main servidor - " + e.getMessage());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
