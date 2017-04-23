package pl.io;

import java.awt.BasicStroke;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
public class Graph extends JFrame {

	public Graph(List<Double> list)
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        DataTable data = new DataTable(Double.class, Double.class);
		for(int i=0; i<list.size();i++)
		{
			double a = list.get(i);
			data.add((double)i,a);
		}
		XYPlot plot = new XYPlot(data);
        getContentPane().add(new InteractivePanel(plot));
        LineRenderer lines = new DefaultLineRenderer2D();
        plot.setLineRenderers(data, lines);
        double insetsTop = 10.0,
        	       insetsLeft = 60.0,
        	       insetsBottom = 60.0,
        	       insetsRight = 10.0;
        plot.setInsets(new Insets2D.Double(
        	    insetsTop, insetsLeft, insetsBottom, insetsRight));
        plot.getTitle().setText("Acoustic Spectrum ");
        Label lb = new Label("X");
        Label lb2 = new Label("Y");
        plot.getAxisRenderer(XYPlot.AXIS_X).setLabel(lb);
        plot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(lb2);
	}
	public static void main(String[] args) {
        List<Double> list = new ArrayList<Double>();
        for(int i=0; i<10;i++)
		{
			list.add(i, (double)i*i);
		}
        Graph graph = new Graph(list);
        graph.setVisible(true);
        
        }
}