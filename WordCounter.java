import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounter {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        File parent;
        File result;
        FileWriter wr;

        System.out.println("Recebe o endereço de um diretório, e cria um arquivo txt com a quantidade de palavras de cada txt dentro do diretório");

        System.out.print("Diretório pai: ");
        parent = new File(validar(sc.nextLine()));

        File[] artigos = parent.listFiles();

        if (artigos != null) {
            Arrays.sort(artigos); //garante que estará em ordem alfabética

            result = new File(parent, "result.txt");
            try {
                wr = new FileWriter(result);

                for (File artigo : artigos) {
                    if (getExtension(artigo).equalsIgnoreCase("txt")) {
                        try {
                            ArrayList<String> palavras = lerTexto(artigo);
                            int tamanhoTexto = palavras.size(); //calcula o número total de palavras no texto
                            wr.write(tamanhoTexto + "\n");
                        } catch (IOException e) {
                            System.out.println("ERRO: falha ao ler o arquivo de origem");
                            throw new RuntimeException(e);
                        }
                    }
                }
                wr.close();
            } catch (IOException e) {
                System.out.println("ERRO: falha ao criar arquivo de destino");
                throw new RuntimeException(e);
            }
        }
    }

    public static ArrayList<String> lerTexto(File artigo) throws IOException {
        ArrayList<String> palavras = new ArrayList<>();
        String line;
        Pattern pattern = Pattern.compile("\\b\\w+\\b"); //Usa uma expressão regular para dividir o texto em palavras
        Matcher matcher;

        try (BufferedReader reader = new BufferedReader(new FileReader(artigo));) {
            //lê linha por linha, para limitar o uso de memória
            while ((line = reader.readLine()) != null) {
                //ignora linhas em branco
                if (!line.trim().isEmpty()) {
                    matcher = pattern.matcher(line);
                    while (matcher.find())
                        palavras.add(matcher.group());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return palavras;
    }

    public static String validar(String original) {
        if (original.charAt(0) == '"') //se há aspas, retira as aspas
            return original.substring(original.indexOf('"'), original.lastIndexOf('"'));
        return original;
    }

    public static String getExtension(File file) {
        int i = file.getName().lastIndexOf(".");
        return file.getName().substring(i + 1);
    }
}