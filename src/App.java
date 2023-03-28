import java.io.FileInputStream;
import java.net.URI;
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
        URI endereco = URI.create("https://imdb-api.com/pt-br/API/MostPopularMovies/".concat(API_KEY));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(endereco).GET().build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        String body = response.body();
        
        // pegar só os dados que interessam (titulo, poster, classificação)
        JsonParser parser = new JsonParser();
        List<Map<String, String>> listaDeFilmes = parser.parse(body);

        // exibir e manipular os dados
        for (Map<String,String> filme : listaDeFilmes) {
            System.out.println("\u001b[1m Dados do Filme: ");
            System.out.println("Título:" + "\u001b[37;1m \u001b[44;1m" + filme.get("title") + "\u001b[m");
            System.out.println("Poster: " + filme.get("image"));
            System.out.print("Rating: ");
            
            if (!filme.get("imDbRating").isEmpty()) {
                double ratingComPontoFlutuante = Double.parseDouble(filme.get("imDbRating"));
                int rating = (int) ratingComPontoFlutuante;
                
                for (int i = 0; i < rating; i++) {
                    System.out.print("\u2B50");
                }
            }
            
            System.out.println("\n");
        }
    }
}
