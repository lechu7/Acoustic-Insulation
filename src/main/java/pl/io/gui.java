package pl.io;
 
import java.util.ArrayList;
import java.util.Optional;

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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
import javafx.stage.WindowEvent;
 
@SuppressWarnings("restriction")
public class gui extends Application {
 
	ImageView iv1 = new ImageView();
    ImageView iv2 = new ImageView();
    ImageView iv3 = new ImageView();
    
    /**
     * 
     */
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
        Label frequencyLabel = new Label("Częstotliwość [Hz]:");
        final TextField frequencyText = new TextField();
 
        //set time label and create time input field
        Label timeLabel = new Label("Czas [s]:");
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
        
        Button channelPickerBtn = new Button();
        channelPickerBtn.setText("Kanał przed przegrodą");
        channelPickerBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				ArrayList<String> choices = new ArrayList<String>();
				choices.add("Kanał 0");
				choices.add("Kanał 1");
				
				
				ChoiceDialog<String> dialog = new ChoiceDialog<String>("Kanał 0", choices);
				dialog.setTitle("Wybór kanału przed przegrodą");
				dialog.setHeaderText("Który kanał znajduje się przed przegrodą?");
				Optional<String> result = dialog.showAndWait();
				
				if (result.isPresent()){
					if (result.get() == "Kanał 0") {
						Calculation.setFirstChannelIsBeforeBarrier(true);
					} else if (result.get() == "Kanał 1") {
						Calculation.setFirstChannelIsBeforeBarrier(false);
					}
				}
			}
		});
        
        Button calibrateBtn = new Button();
        calibrateBtn.setText("Kalibracja");
        calibrateBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Kalibracja");
				alert.setHeaderText("Czy chciałbyś przeprowadzić kalibrację?");
//				alert.setContentText("");
				
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					try{
						double[][] recording = InputStream.reading((float)1000 * 3);
						double[] channel1 = recording[0];
						double[] channel2 = recording[1];
						
						channel1 = Statistics.outliners(channel1);
						channel2 = Statistics.outliners(channel2);
						
						channel1 = Statistics.normalization(channel1);
						channel2 = Statistics.normalization(channel2);
						
						channel1 = Calculation.calcDBs(channel1, 48000);
						channel2 = Calculation.calcDBs(channel2, 48000);
						double[] diff = Calculation.diff(channel1, channel2);
						
						Calculation.setCalibrationDifference(diff);
						
						Alert calibratedAlert = new Alert(Alert.AlertType.INFORMATION);
						calibratedAlert.setTitle("Kalibracja Zakończona");
						calibratedAlert.setHeaderText("Kalibracja zakończona sukcesem!");
						calibratedAlert.show();
					}catch(Exception e){
						System.out.println(e.getMessage());
					}
				}
			}
		});
        
        Button minimalSignalBtn = new Button();
        minimalSignalBtn.setText("Minimalna moc sygnału");
        minimalSignalBtn.setOnAction(new EventHandler<ActionEvent>()
		{
			
			public void handle(ActionEvent event)
			{
				TextInputDialog dialog = new TextInputDialog();
				dialog.setTitle("Minimalna moc sygnału");
				dialog.setHeaderText("Proszę podać minimalną moc sygnału, dla którego przeprowadzony zostanie pomiar [0-32768]");
				
				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){
					try{
						double min = Double.parseDouble(result.get());
						Calculation.setMinimalSignalStrength(min);
					} catch (Exception e){
						
					}
				}
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
                	startBtn.setDisable(true);
                	
                    Thread t = new Thread(new Runnable()
					{
						public void run()
						{
							try {
								int time = Integer.parseInt(timeText.getText());	
								if (radioSweep.isSelected()) {
			                        outputStream.sweep(20, 20000, 1000*time, 350);
								} else {
			                        double frequency = Double.parseDouble(frequencyText.getText());
			                        outputStream.sin((int)frequency, 1000*time, 350);
								}
								double[][] result = InputStream.reading((float)1000*time);
								double[] channel1 = result[0];
								double[] channel2 = result[1];
								
								channel1 = Statistics.outliners(channel1);
								channel2 = Statistics.outliners(channel2);
			                   
								if (!Calculation.isSignalStrongEnough(channel1)){
									Platform.runLater(new Runnable()
									{
										
										public void run()
										{
											Alert exAlert = new Alert(Alert.AlertType.INFORMATION);
						                    exAlert.setTitle("Acoustic-Insulation");
						                    exAlert.setHeaderText("Błąd");
						                    exAlert.setContentText("Synał wejściowy zbyt słaby");
						                    exAlert.show();
						                    startBtn.setDisable(false);
										}
									});
				                    return;
								}
								
								channel1 = Statistics.normalization(channel1);
								channel2 = Statistics.normalization(channel2);
			               
								double[] diff=Calculation.calculateIsolation(channel1, channel2, 48000);
								channel1=Calculation.calcDBs(channel1, 48000);
								channel2=Calculation.calcDBs(channel2, 48000);
			                   
								IO.saveCSV(channel1, channel2, diff);
	
								new Graph(channel1);
								new Graph(channel2);
								new Graph(diff);
	
								Graph.GenerateAndSetImage(iv1, channel1); //set Image from channel1
								Graph.GenerateAndSetImage(iv2, channel1); //set Image from channel2
								Graph.GenerateAndSetImage(iv3, diff); //set Image showing differences between channel1 nad channel2
			                   
								startBtn.setDisable(false);
							}catch (Exception e){
								Platform.runLater(new Runnable()
								{
									public void run()
									{
										Alert exAlert = new Alert(Alert.AlertType.INFORMATION);
					                    exAlert.setTitle("Acoustic-Insulation");
					                    exAlert.setHeaderText("Błąd");
					                    exAlert.setContentText("Nieprawidłowe dane wejściowe. ");
					                    exAlert.showAndWait();
					                    startBtn.setDisable(false);
									}
								});
							}
						}
					});
                    t.start();
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
        
        
        GridPane buttons = new GridPane();
        buttons.setPadding(new Insets(0, 0, 20, 0));
        buttons.setAlignment(Pos.TOP_LEFT);
        
        HBox box00 = new HBox();
        box00.getChildren().add(channelPickerBtn);
        box00.setPadding(new Insets(0, 0, 0, 10));
        
        HBox box01 = new HBox();
        box01.getChildren().add(calibrateBtn);
        box01.setPadding(new Insets(0, 0, 0, 10));
        
        HBox box02 = new HBox();
        box02.getChildren().add(minimalSignalBtn);
        box02.setPadding(new Insets(0, 0, 0, 10));
        
        buttons.add(box00, 0, 0);
        buttons.add(box01, 1, 0);
        buttons.add(box02, 2, 0);
        
 
        HBox box = new HBox();
        box.getChildren().addAll(radioSweep, radioSin);
        box.setMinWidth(400);
        box.setSpacing(200);
        box.setPadding(new Insets(0,0,20,0));
        box.setAlignment(Pos.CENTER);
        
        VBox box1 = new VBox();
        box1.getChildren().addAll(frequencyLabel, frequencyText);
        box1.setMaxWidth(100);
        box1.setAlignment(Pos.CENTER);
        
        VBox box2 = new VBox();
        box2.getChildren().addAll(timeLabel, timeText);
        box2.setMaxWidth(100);
        box2.setAlignment(Pos.CENTER);
        
        HBox box12 = new HBox();
        box12.getChildren().addAll(box1, box2);
        box12.setAlignment(Pos.CENTER);
        box12.setSpacing(150);
 
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
 
        root.add(buttons, 0, 0);
        root.add(box, 0, 1);
        root.add(box12, 0, 2);
        root.add(box3, 0, 3);
        root.add(box4, 0, 4);
        root.add(box5, 0, 5);
        root.add(box6, 0, 6);
 
        Scene scene = new Scene(root, 900, 700);
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
 
    public static void start(String[] args) {
     
      launch(args);
   }
}