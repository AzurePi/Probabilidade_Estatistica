import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextBreaker {
    public static void main(String[] args) {
        File parent;
        int d; //controla o número do arquivo
        ArrayList<String> palavras;
        StringBuilder trecho;
        File newDir;
        int p, tamanhoTexto, projecao; //controla que linha do arquivo de origem está sendo lida e seu tamanho total
        int a; //controla o nome do arquivo txt de cada trecho, e a quantidade total de arquivos
        Scanner sc = new Scanner(System.in);

        System.out.println("Recebe o endereço de um diretório, dentro do qual há apenas arquivos .txt, e os quebra em trechos com  4000 caracteres (sem quebrar palavras ao meio), e salva esse textos em arquivos txt em um novo sub-diretório");

        System.out.print("Diretório pai: ");
        parent = new File(validar(sc.nextLine()));

        System.out.print("Número inicial: ");
        d = Integer.parseInt(sc.nextLine());

        File[] artigos = parent.listFiles();

        if (artigos != null) {
            Arrays.sort(artigos); //garante que estará em ordem alfabética
            for (File artigo : artigos) {
                if (getExtension(artigo).equalsIgnoreCase("txt")) {
                    try {
                        palavras = lerTexto(artigo);
                        tamanhoTexto = palavras.size(); //calcula o número total de palavras no texto

                        newDir = new File(parent, "result" + d);
                        newDir.mkdir();
                    } catch (IOException e) {
                        System.out.println("ERRO: falha ao ler o arquivo de origem");
                        throw new RuntimeException(e);
                    }

                    a = p = 0; //zera o contador de arquivos e o contador de palavras
                    trecho = new StringBuilder();
                    while (p < tamanhoTexto) {
                        projecao = trecho.length() + palavras.get(p).length() + 1; //soma 1 pleo espaço adicionado no final

                        //se ainda não chegamos ao tamanho máximo
                        if (projecao <= 4000) {
                            trecho.append(palavras.get(p)).append(" ");
                            p++; //passamos para a próxima palavra
                        } else { //já chegamos ao tamanho máximo do trecho
                            salvarTrecho(newDir.toString(), trecho.toString(), a);
                            a++; //passamos para o próximo arquivo
                            trecho = new StringBuilder();
                        }
                    }
                    //se ainda há informações a salvar no trecho
                    if (!trecho.toString().isEmpty())
                        salvarTrecho(newDir.toString(), trecho.toString(), a);

                    d++;
                }
            }
        }
        sc.close();
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

    public static void salvarTrecho(String destino, String trecho, int a) {
        try {
            String fileName = a + ".txt";
            Path fileDestino = Paths.get(destino, fileName);
            salvar(trecho, fileDestino);
        } catch (IOException e) {
            System.out.println("ERRO: falha no salvamento do arquivo");
            throw new RuntimeException(e);
        }
    }

    public static void salvar(String trecho, Path destino) throws IOException {
        Files.createFile(destino);
        BufferedWriter writer = Files.newBufferedWriter(destino);
        writer.write(trecho);
        writer.close();
    }
}
