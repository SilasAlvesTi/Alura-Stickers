import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws Exception {
        // fazer uma conexão HTTP e buscar os top 250 filmes
        Properties props = new Properties();
        FileInputStream input = new FileInputStream("config.properties");
        props.load(input);
        
        String API_KEY = props.getProperty("API_KEY");
        URI endereco = URI.create("https://imdb-api.com/en/API/Top250Movies/".concat(API_KEY));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(endereco).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();
        
        // pegar só os dados que interessam (titulo, poster, classificação)
        JsonParser parser = new JsonParser();
        List<Map<String, String>> listaDeFilmes = parser.parse(body);

        // exibir e manipular os dados
        GeradoraDeFigurinhas geradora = new GeradoraDeFigurinhas();
        File diretorio = new File("figurinhas/");
        diretorio.mkdir();
        for (Map<String,String> filme : listaDeFilmes) {
            String urlImagemMiniatura = filme.get("image");
            try {
                String[] parts = urlImagemMiniatura.split("@(.+?)\\.jpg");
                String urlImagem = parts[0] + "@.jpg";
                System.out.println(urlImagem);
                String titulo = filme.get("title");
                String nomeDoArquivo = "figurinhas/" + titulo + ".png";

                InputStream inputStream = new URL(urlImagem).openStream();
                
                geradora.cria(inputStream, nomeDoArquivo);    
            } catch (Exception e) {
                continue;
            }
            
                  
        }
    }
}
