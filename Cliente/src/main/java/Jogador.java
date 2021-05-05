import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Jogador {
    private String nomeUsuario;
    private int pontos;
}
