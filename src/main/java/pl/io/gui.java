package pl.io;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.*;

public class gui extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Acoustic Insulation");

        final CheckBox cb1 = new CheckBox("Sweep");
        final CheckBox cb2 = new CheckBox("Sin");

        Label frequencyLabel = new Label("Frequency:");
        final TextField frequencyText = new TextField();

        Label timeLabel = new Label("Set time [s]:");
        final TextField timeText = new TextField();

        Button startBtn = new Button();
        startBtn.setText("START");

        // force the field to be numeric only
        /*frequencyText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    frequencyText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        timeText.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    timeText.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });*/

        /*
        After click start button we'll expect execute generateGraph() method

        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                generateGraph();
            }
        });*/

        /*graphs section- for now its only example of displaying graphs without using generateGraph() method */

        /* graph 1*/

        final NumberAxis x1Axis = new NumberAxis();
        final NumberAxis y1Axis = new NumberAxis();
        x1Axis.setLabel("Time");
        y1Axis.setLabel("Frequency");

        final LineChart<Number,Number> lineChart1 =
                new LineChart<Number,Number>(x1Axis,y1Axis);

        lineChart1.setTitle("Graph 1");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("graph placeholder");

        series1.getData().add(new XYChart.Data(0, 23));
        series1.getData().add(new XYChart.Data(1, 23));
        series1.getData().add(new XYChart.Data(2, 14));
        series1.getData().add(new XYChart.Data(3, 15));
        series1.getData().add(new XYChart.Data(4, 24));
        series1.getData().add(new XYChart.Data(5, 34));
        series1.getData().add(new XYChart.Data(6, 36));
        series1.getData().add(new XYChart.Data(7, 22));
        series1.getData().add(new XYChart.Data(8, 45));
        series1.getData().add(new XYChart.Data(9, 43));

        /*graph 2*/
        final NumberAxis x2Axis = new NumberAxis();
        final NumberAxis y2Axis = new NumberAxis();
        x2Axis.setLabel("Time");
        y2Axis.setLabel("Frequency");

        final LineChart<Number,Number> lineChart2 =
                new LineChart<Number,Number>(x2Axis,y2Axis);

        lineChart2.setTitle("Graph 2");

        XYChart.Series series2 = new XYChart.Series();
        series2.setName("graph placeholder");

        series2.getData().add(new XYChart.Data(0, 23));
        series2.getData().add(new XYChart.Data(1, 23));
        series2.getData().add(new XYChart.Data(2, 14));
        series2.getData().add(new XYChart.Data(3, 15));
        series2.getData().add(new XYChart.Data(4, 24));
        series2.getData().add(new XYChart.Data(5, 34));
        series2.getData().add(new XYChart.Data(6, 36));
        series2.getData().add(new XYChart.Data(7, 22));
        series2.getData().add(new XYChart.Data(8, 45));
        series2.getData().add(new XYChart.Data(9, 43));

        /*graph3*/
        final NumberAxis x3Axis = new NumberAxis();
        final NumberAxis y3Axis = new NumberAxis();
        x3Axis.setLabel("Time");
        y3Axis.setLabel("Frequency");

        final LineChart<Number,Number> lineChart3 =
                new LineChart<Number,Number>(x3Axis,y3Axis);

        lineChart3.setTitle("Graph 3");

        XYChart.Series series3 = new XYChart.Series();
        series3.setName("graph placeholder");

        series3.getData().add(new XYChart.Data(0, 23));
        series3.getData().add(new XYChart.Data(1, 23));
        series3.getData().add(new XYChart.Data(2, 14));
        series3.getData().add(new XYChart.Data(3, 15));
        series3.getData().add(new XYChart.Data(4, 24));
        series3.getData().add(new XYChart.Data(5, 34));
        series3.getData().add(new XYChart.Data(6, 36));
        series3.getData().add(new XYChart.Data(7, 22));
        series3.getData().add(new XYChart.Data(8, 45));
        series3.getData().add(new XYChart.Data(9, 43));

        /* end of graph section*/
        Image graph1 = new Image("file:bitmap.png"); 
        Image graph2 = new Image("file:bitmap.png"); 
        Image graph3 = new Image("file:bitmap.png"); 
        ImageView iv1 = new ImageView();
        ImageView iv2 = new ImageView();
        ImageView iv3 = new ImageView();
        
        iv1.fitWidthProperty().bind(primaryStage.widthProperty());
        iv1.fitHeightProperty().bind(iv2.fitHeightProperty());
        iv2.fitWidthProperty().bind(primaryStage.widthProperty());
        iv2.fitHeightProperty().bind(iv3.fitHeightProperty());
        iv3.fitWidthProperty().bind(primaryStage.widthProperty());
        iv3.fitHeightProperty().bind(primaryStage.minHeightProperty());
 
        iv1.setImage(graph1);
        iv2.setImage(graph2);
        iv3.setImage(graph3);
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20,20,20,20));
        layout.getChildren().addAll(cb1,cb2,frequencyLabel,frequencyText,timeLabel,timeText,startBtn,iv1,iv2,iv3);

        Scene scene = new Scene(layout,800,700);

        lineChart1.getData().add(series1);
        lineChart2.getData().add(series2);
        lineChart3.getData().add(series3);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
    	Graph.Call();
        launch(args);
    }
}
