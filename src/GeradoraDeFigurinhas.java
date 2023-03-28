import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class GeradoraDeFigurinhas {
    
    public void cria(InputStream inputStream, String nomeArquivo) throws Exception {
        // leitura da imagem
        // InputStream inputStrem = new FileInputStream(new File("entrada/filme.jpg"));
        // InputStream inputStream = new URL("https://m.media-amazon.com/images/M/MV5BNDE3ODcxYzMtY2YzZC00NmNlLWJiNDMtZDViZWM2MzIxZDYwXkEyXkFqcGdeQXVyNjAwNDUxODI@.jpg").openStream();
        BufferedImage imagemOriginal = ImageIO.read(inputStream);

        // cria nova imagem em memória com transparência e com tamanho novo~
        int largura = imagemOriginal.getWidth();
        int altura = imagemOriginal.getHeight();
        int novaAltura = altura + 200;

        BufferedImage novaImagem = new BufferedImage(largura, novaAltura, BufferedImage.TRANSLUCENT);  
        
        // copiar a imagem original pra nova imagem (em memória) 
        Graphics2D graphics = novaImagem.createGraphics();
        graphics.drawImage(imagemOriginal, 0, 0, null);

        // configurar a fonte
        Font fonte = new Font(Font.SANS_SERIF, Font.BOLD, 128);
        graphics.setFont(fonte);
        graphics.setColor(Color.YELLOW);
        FontMetrics medidasDaFonte = graphics.getFontMetrics();

        // escrever uma frase em uma nova imagem
        String texto = "TOPZERA";
        var x = (largura - medidasDaFonte.stringWidth(texto)) / 2;
        var y = (((200 - medidasDaFonte.getHeight()) / 2) + altura) + medidasDaFonte.getAscent();
        graphics.drawString(texto, x, y);

        FontRenderContext fontRenderContext = graphics.getFontRenderContext();
        var textLayout = new TextLayout(texto, fonte, fontRenderContext);
        Shape outline = textLayout.getOutline(null);
        AffineTransform transform = graphics.getTransform();
        transform.translate(x, y);
        graphics.setTransform(transform);
        
        var outlineStroke = new BasicStroke(largura * 0.004f);
        graphics.setStroke(outlineStroke);

        graphics.setColor(Color.BLACK);
        graphics.draw(outline);
        graphics.setClip(outline);


        // escrever a nova imagem em um arquivo
        ImageIO.write(novaImagem, "png", new File(nomeArquivo));
    }
}
