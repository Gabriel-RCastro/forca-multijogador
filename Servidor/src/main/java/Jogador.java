import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Jogador implements Comparator<Jogador> {
    private String nomeUsuario;
    private int pontos;


    @Override
    public int compare(Jogador o1, Jogador o2) {
        if(o1.getPontos() > o2.getPontos())
            return 1;
        if(o1.getPontos() < o2.getPontos())
            return -1;
        return 0;
    }

}
