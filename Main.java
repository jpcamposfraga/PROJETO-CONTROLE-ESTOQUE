
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Estoque estoque1 = new Estoque(100, LocalDateTime.now());
        Produtos produto1 = new Produtos(1, 0, LocalDate.of(2026, 01, 01), LocalDateTime.now(), "Litros", "leite", "laticineos", "piracanjuba", "Leite desnatado 1 litro", 5.99);
        estoque1.adicionarProduto(produto1);
        System.out.println(estoque1.listarProdutos());

    }
}
