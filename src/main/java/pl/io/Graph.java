    package pl.io;
 
 
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.io.plots.BitmapWriter;
import de.erichseifert.gral.io.plots.DrawableWriterFactory;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.areas.AreaRenderer;
import de.erichseifert.gral.plots.areas.DefaultAreaRenderer2D;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
 
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
@SuppressWarnings("restriction")
public class Graph extends JFrame {
   
    private static final long serialVersionUID = 1L;
    private static XYPlot plot = null;
       
    public Graph(double[] tab)
    {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 150);
        @SuppressWarnings("unchecked")
        DataTable data = new DataTable(Double.class, Double.class);
        AreaRenderer area = new DefaultAreaRenderer2D();
        area.setColor(Color.white);
        for(int i = 0; i<tab.length;i++)
        {
            data.add((double)i,tab[i]);
        }
        plot = new XYPlot(data);
        getContentPane().add(new InteractivePanel(plot));
        LineRenderer lines = new DefaultLineRenderer2D();
        plot.setLineRenderers(data, lines);
      //  plot.setBackground(color1);
        plot.setBorderColor(Color.blue);
        plot.setAreaRenderers(data, area);
       
        lines.setColor(Color.blue);
        Color color = new Color(0,0,0,0);
        for(int i=0; i<plot.getPointRenderers(data).size();i++)
        {
            plot.getPointRenderers(data).get(i).setColor(color);
        }
        double insetsTop = 10.0,
                   insetsLeft = 60.0,
                   insetsBottom = 20.0,
                   insetsRight = 45.0;
        plot.setInsets(new Insets2D.Double(
                insetsTop, insetsLeft, insetsBottom, insetsRight));
     //   plot.getTitle().setText("Acoustic Spectrum ");
        Label lb = new Label("X");
        Label lb2 = new Label("Y");
        plot.getAxisRenderer(XYPlot.AXIS_X).setLabel(lb);
        plot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(lb2);
       // plot.getAxis(XYPlot.AXIS_X).setRange(300, arg1);
    }
    public  static byte[] save(double[] tab) throws IOException
    {
        new Graph(tab);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapWriter writer = (BitmapWriter) DrawableWriterFactory.getInstance().get("image/png");
        writer.write(plot, baos, 700, 150);
        baos.flush();
        byte[] bytes = baos.toByteArray();
        baos.close();
        return bytes;
        }
   
    public static void GenerateAndSetImage(final ImageView iv, final double[] tab)
    {
        new Thread(new Runnable() {
            public void run() {
                try {
                    System.out.println("Przygotowanie wykresu....");
                    byte[] bytes = Graph.save(tab);
                    BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
                    Image image = SwingFXUtils.toFXImage(img, null);
                    iv.setImage(image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).run();
    }
}
	