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


/**
 * Class Graph generates a acoustic spectrum from double array.
 * Final Plot is exported to gui class.
 * 
 * @author Dawid Włoszek
 */
@SuppressWarnings("restriction")
public class Graph extends JFrame {
   
    private static final long serialVersionUID = 1L;
    private static XYPlot plot = null;
       /**
        * 
        * @param tab
        */
    //Constructor initializes area for graph, then adds data 
    //from parameter tab to DataTable class, which is used in plot initialization.
    //Thereafter constructor initializes plot,  sets margin and creates label.
    public Graph(double[] tab)
    {
        //Initialization of area for graph 
    	setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 150);
        @SuppressWarnings("unchecked")
        DataTable data = new DataTable(Double.class, Double.class);
        AreaRenderer area = new DefaultAreaRenderer2D();
        area.setColor(Color.white);
        
        //Add data from the array to DataTable class
        for(int i = 0; i<tab.length;i++)
        {
            data.add((double)i,tab[i]);
        }
        
        //Plot Initialization
        plot = new XYPlot(data);
        getContentPane().add(new InteractivePanel(plot));
        LineRenderer lines = new DefaultLineRenderer2D();
        plot.setLineRenderers(data, lines);
        plot.setBorderColor(Color.blue);
        plot.setAreaRenderers(data, area);
        lines.setColor(Color.blue);
        Color color = new Color(0,0,0,0);
        for(int i=0; i<plot.getPointRenderers(data).size();i++)
        {
            plot.getPointRenderers(data).get(i).setColor(color);
        }
        
        //Setting a margin
        double insetsTop = 10.0,
                   insetsLeft = 60.0,
                   insetsBottom = 20.0,
                   insetsRight = 45.0;
        plot.setInsets(new Insets2D.Double(
                insetsTop, insetsLeft, insetsBottom, insetsRight));
        
        //Setting label
        Label lb = new Label("X");
        Label lb2 = new Label("Y");
        plot.getAxisRenderer(XYPlot.AXIS_X).setLabel(lb);
        plot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(lb2);
       
    }
    //This method converts a Graph class to byte array and returns necessary byte[].
    //Parameter double[] tab is required for using a constructor. 
    /**
     * 
     * @param tab
     * @return
     * @throws IOException
     */
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
   
    // This method creates an Image object by using save() method and parameter double[] tab.
    // 
    /**
     * 
     * @param iv
     * @param tab
     */
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
	