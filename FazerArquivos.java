package src.main.java;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FazerArquivos {

    public static void main(String[] args) {
        int N;
        String nome;
        String extensao;

        Scanner sc = new Scanner(System.in);

        System.out.print("Quantidade de arquivos: ");
        N = Integer.parseInt(sc.nextLine());

        System.out.print("Nome comum dos arquivos: ");
        nome = sc.nextLine();

        System.out.println("Extensão (sem ponto): ");
        extensao = sc.nextLine();

        for (int i = 1; i <= N; i++) {
            File novo = new File("./" + nome + i + "." + extensao);
            try {
                novo.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
