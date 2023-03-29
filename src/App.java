import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws Exception {
        // fazer uma conexão HTTP e buscar os top 250 filmes
        Properties props = new Properties();
        try {
            FileInputStream input = new FileInputStream("config.properties");
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        /* String API_KEY = props.getProperty("API_KEY_IMDB");
        String url = "https://imdb-api.com/pt-br/API/MostPopularMovies/".concat(API_KEY);
        ExtratorDeConteudo extrator = new ExtratorDeConteudoDoIMDB(); */

        String url = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&start_date=2022-06-12&end_date=2022-06-14";
        ExtratorDeConteudo extrator = new ExtratorDeConteudoDaNasa();

        ClienteHttp http = new ClienteHttp();
        
        String json = http.buscaDados(url);

        List<Conteudo> conteudos =  extrator.extraiConteudos(json);

        // exibir e manipular os dados
        GeradoraDeFigurinhas geradora = new GeradoraDeFigurinhas();
        File diretorio = new File("figurinhas/");
        diretorio.mkdir();

        for (int i = 0; i < 3; i++) {
            try {
                Conteudo conteudo = conteudos.get(i);
                String nomeDoArquivo = "figurinhas/" + conteudo.getTitulo() + ".png";

                InputStream inputStream = new URL(conteudo.getUrlImagem()).openStream();
                
                geradora.cria(inputStream, nomeDoArquivo);    
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
