import java.util.Scanner;

public class InterfaceTextual {
    public GerenciadorConexao gerenciadorConexao;
    Scanner in = new Scanner(System.in);

    public void vitoria(Mensagem msg) {
        System.out.println("=============================");
        System.out.println("Pontuacao: " + msg.pontuacao);
        System.out.println("Resposta: " + msg.palavra);
        System.out.println("Parabéns! Você descobriu a palavra!");
        System.out.println("=============================");
    }

    public String palpite() {
        System.out.print("Insira seu palpite ou comando: ");
        String palpite = in.nextLine();
        System.out.println("\n.............................");

        return palpite;
    }

    public void atualizarInformacao(Mensagem msg) {
//        System.out.println("\nTentativas restantes: " + msg.tentativasPermitidas);
        System.out.println(".............................");
        System.out.println("\nLetras adivinhadas: " + msg.letrasAdivinhadas);
        System.out.println("Seus pontos: " + msg.pontuacao);

        if (msg.letrasPalpitadas != null)
            System.out.println("Letras palpitadas: " + msg.letrasPalpitadas);
    }

    public void aceitar() {
        System.out.println("\nPressione enter para continuar ...");
        in.nextLine();
    }
}