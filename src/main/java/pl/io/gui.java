package pl.io;
 
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
 
import javafx.stage.WindowEvent;
 
//@SuppressWarnings("restriction")
public class gui extends Application {
 
	ImageView iv1 = new ImageView();
    ImageView iv2 = new ImageView();
    ImageView iv3 = new ImageView();
    
    public void start(Stage primaryStage) throws Exception{
        //set program title
        primaryStage.setTitle("Acoustic Insulation");
 
        //load program icon
        primaryStage.getIcons().add(new Image("file:icon.png"));
 
        //close program execution after clicking exit button
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });
        
        //set frequency label and create frequency input field
        Label frequencyLabel = new Label("Frequency [Hz]:");
        final TextField frequencyText = new TextField();
 
        //set time label and create time input field
        Label timeLabel = new Label("Set time [s]:");
        final TextField timeText = new TextField();
 
        ToggleGroup signalGroup = new ToggleGroup();
 
        //set sweep radio button
        final RadioButton radioSweep = new RadioButton("Sweep");
        radioSweep.setToggleGroup(signalGroup);
 
        //disable frequencyText after checked sweep radio button
        radioSweep.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                frequencyText.setDisable(true);
 
            }
        });
 
        //set sin radio button
        final RadioButton radioSin = new RadioButton("Sin");
        radioSin.setSelected(true);
        radioSin.setToggleGroup(signalGroup);
 
        //disable frequencyText after checked sin radio button
        radioSin.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                frequencyText.setDisable(false);
            }
        });
 
        final Button startBtn = new Button();
        startBtn.setText("START");
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    int time =Integer.parseInt(timeText.getText());

 
                    startBtn.setDisable(true);
 
                    if (radioSweep.isSelected()) {
                        outputStream.sweep(20, 20000, 1000*time, 350);
         
                    } else {
                        double frequency = Double.parseDouble(frequencyText.getText());
                        outputStream.sin((int)frequency, 1000*time, 350);
                    }
                   double[][] result= InputStream.reading((float)1000*time);
                   double[] channel1 = result[0];
                   double[] channel2 = result[1];
                   channel1 = Statistics.outliners(channel1);
                   channel2 = Statistics.outliners(channel2);
                   
                   channel1 = Statistics.normalization(channel1);
                   channel2 = Statistics.normalization(channel2);
               
                   double[] diff=Calculation.calculateIsolation(channel1, channel2, 48000);
                   channel1=Calculation.calcDBs(channel1, 48000);
                   channel2=Calculation.calcDBs(channel2, 48000);
                   
                   IO.saveCSV(channel1, channel2, diff);

                   Graph g1= new Graph(channel1);
                   Graph g2= new Graph(channel2);
                   Graph g3= new Graph(diff);

                   Graph.GenerateAndSetImage(iv1, channel1); //set Image from channel1
                   Graph.GenerateAndSetImage(iv2, channel1); //set Image from channel2
                   Graph.GenerateAndSetImage(iv3, diff); //set Image showing differences between channel1 nad channel2
                   
                    startBtn.setDisable(false);
                }
                catch(Exception ex){
                    Alert exAlert = new Alert(Alert.AlertType.INFORMATION);
                    exAlert.setTitle("Acoustic-Insulation");
                    exAlert.setHeaderText("Błąd");
                    exAlert.setContentText("Nieprawidłowe dane wejściowe. " + ex.getMessage());
                    exAlert.showAndWait();
                    startBtn.setDisable(false);
 
                }
            }
        });
        //Setting layout of the graph images
        iv1.fitWidthProperty().bind(primaryStage.widthProperty().divide(1.076));
        iv1.fitHeightProperty().bind(iv2.fitHeightProperty());
        iv2.fitWidthProperty().bind(primaryStage.widthProperty().divide(1.076));
        iv2.fitHeightProperty().bind(iv3.fitHeightProperty());
        iv3.fitWidthProperty().bind(primaryStage.widthProperty().divide(1.076));
        iv3.fitHeightProperty().bind(primaryStage.minHeightProperty());
       
       
        GridPane root = new GridPane();
        root.setPadding(new Insets(20, 0, 20, 0));
 
        HBox box = new HBox();
        box.getChildren().addAll(radioSweep, radioSin);
        box.setMinWidth(400);
        box.setSpacing(200);
        box.setPadding(new Insets(0,0,20,0));
        box.setAlignment(Pos.CENTER);
 
        HBox box1 = new HBox();
        box1.getChildren().addAll(frequencyLabel, timeLabel);
        box1.setMinWidth(400);
        box1.setSpacing(200);
        box1.setAlignment(Pos.CENTER);
 
        HBox box2 = new HBox();
        box2.getChildren().addAll(frequencyText, timeText);
        box2.setMinWidth(400);
        box2.setSpacing(100);
        box2.setPadding(new Insets(0,0,10,0));
        box2.setAlignment(Pos.CENTER);
 
        HBox box3 = new HBox();
        box3.getChildren().add(startBtn);
        box3.setAlignment(Pos.CENTER);
        box3.setStyle("-fx-font-weight:bold;" + " -fx-font-size:20px;" + " -fx-padding:10px;" );
 
        HBox box4 = new HBox();
        box4.getChildren().addAll(iv1);
        box4.setPadding(new Insets(10,0,0,35));
 
        HBox box5 = new HBox();
        box5.getChildren().addAll(iv2);
        box5.setPadding(new Insets(5,0,0,35));
 
        HBox box6 = new HBox();
        box6.getChildren().addAll(iv3);
        box6.setPadding(new Insets(5,0,0,35));
 
        root.add(box, 0, 0);
        root.add(box1, 0, 1);
        root.add(box2, 0, 2);
        root.add(box3, 0, 3);
        root.add(box4, 0, 4);
        root.add(box5, 0, 5);
        root.add(box6, 0, 6);
 
        Scene scene = new Scene(root, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    public static void start(String[] args) {
     
      launch(args);
   }
}