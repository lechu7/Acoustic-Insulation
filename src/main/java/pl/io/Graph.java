package pl.io;

import pl.io.IO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.graphics.Insets2D;
import de.erichseifert.gral.graphics.Label;
import de.erichseifert.gral.io.data.DataWriter;
import de.erichseifert.gral.io.data.DataWriterFactory;
import de.erichseifert.gral.io.plots.BitmapWriter;
import de.erichseifert.gral.io.plots.DrawableWriter;
import de.erichseifert.gral.io.plots.DrawableWriterFactory;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.areas.AreaRenderer;
import de.erichseifert.gral.plots.areas.DefaultAreaRenderer2D;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
public class Graph extends JFrame {
	private final XYPlot plot;
		
	public Graph(List<Double> list)
	{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 150);
        DataTable data = new DataTable(Double.class, Double.class);
        AreaRenderer area = new DefaultAreaRenderer2D();
        area.setColor(Color.black);
		for(int i=0; i<list.size();i++)
		{
			double a = list.get(i);
			data.add((double)i,a);
		}
		plot = new XYPlot(data);
        getContentPane().add(new InteractivePanel(plot));
        LineRenderer lines = new DefaultLineRenderer2D();
        plot.setLineRenderers(data, lines);
        plot.setBackground(Color.gray);
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
        	       insetsRight = 60.0;
        plot.setInsets(new Insets2D.Double(
        	    insetsTop, insetsLeft, insetsBottom, insetsRight));
     //   plot.getTitle().setText("Acoustic Spectrum ");
        Label lb = new Label("X");
        Label lb2 = new Label("Y");
        plot.getAxisRenderer(XYPlot.AXIS_X).setLabel(lb);
        plot.getAxisRenderer(XYPlot.AXIS_Y).setLabel(lb2);
       // plot.getAxis(XYPlot.AXIS_X).setRange(300, arg1);
	}
	public void save() {
		
		JFileChooser chooser = new JFileChooser();
			
			File file = chooser.getSelectedFile();
			File outputFile = new File("bitmap.png");
			try {
				BitmapWriter writer = (BitmapWriter) DrawableWriterFactory.getInstance().get("image/png");
				writer.write(plot, new FileOutputStream(outputFile), 700, 150);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	public static void Call()
	{
		File file = new File("dane.perl");
        List<Double> list2 = new ArrayList<Double>();
			try
			{
				Scanner s = new Scanner(file);
				while(s.hasNextLine()){
					String line = s.nextLine();
					double d = Double.parseDouble(line);
				     list2.add(d);
				      }
				s.close();
			} catch (FileNotFoundException e)
			{
				
				e.printStackTrace();
			}
			
        Graph graph = new Graph(list2);
     //   graph.setVisible(true);
        graph.save();
	}
	
	public static void main(String[] args)  {
        
}}