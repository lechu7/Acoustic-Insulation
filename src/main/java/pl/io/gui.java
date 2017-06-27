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

/**
 * Class gui generates graphical user interface of existing program.
 * 
 * @author Zagórowicz Jakub
 * @author Radosz Szymon
 */
@SuppressWarnings("restriction")
public class gui extends Application {

	  ImageView iv1 = new ImageView();
    ImageView iv2 = new ImageView();
    ImageView iv3 = new ImageView();

    boolean frequencyTextDisabled=false;

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
                frequencyTextDisabled=true;
                frequencyText.setText("");

            }
        });

        final Label labelBeforeBarrier = new Label("");
        final Label labelAfterBarrier = new Label("");
        Label labelDifference = new Label("Różnica między kanałami.");
        UpdateLabels(labelBeforeBarrier,labelAfterBarrier);

        final Button channelPickerBtn = new Button();
        channelPickerBtn.setText("Kanał przed przegrodą");
        channelPickerBtn.setOnAction(new EventHandler<ActionEvent>()
		{
      //Initialize channels
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
				UpdateLabels(labelBeforeBarrier,labelAfterBarrier);
			}
		});

        final Button calibrateBtn = new Button();
        calibrateBtn.setText("Kalibracja");
        calibrateBtn.setOnAction(new EventHandler<ActionEvent>()
		{
      //Calibration functionality
			public void handle(ActionEvent event)
			{
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Kalibracja");
				alert.setHeaderText("Czy chciałbyś przeprowadzić kalibrację kanału 0?");
//				alert.setContentText("");

				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK){
					try{
						double[][] recording0 = InputStream.reading((float)1000 * 3);
						double[] channel0 = recording0[0];
						
						Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
						alert1.setTitle("Kalibracja");
						alert1.setHeaderText("Czy chciałbyś przeprowadzić kalibrację kanału 1?");
						
						Optional<ButtonType> result1 = alert1.showAndWait();
						if (result1.get() == ButtonType.OK){
							double[][] recording1 = InputStream.reading((float)1000 * 3);
							double[] channel1 = recording1[1];
							
							channel0 = Statistics.outliners(channel0);
							channel1 = Statistics.outliners(channel1);
							
							channel0 = Statistics.normalization(channel0);
							channel1 = Statistics.normalization(channel1);
							
							channel0 = Calculation.calcDBs(channel0, 48000);
							channel1 = Calculation.calcDBs(channel1, 48000);
							
							Calculation.setCalibrationChannel0(channel0);
							Calculation.setCalibrationChannel1(channel1);
							
							Alert calibratedAlert = new Alert(Alert.AlertType.INFORMATION);
							calibratedAlert.setTitle("Kalibracja Zakończona");
							calibratedAlert.setHeaderText("Kalibracja zakończona sukcesem!");
							calibratedAlert.show();
						}
					}catch(Exception e){
						System.out.println(e.getMessage());
					}
				}
			}
		});

        final Button minimalSignalBtn = new Button();
        minimalSignalBtn.setText("Minimalna moc sygnału");
        minimalSignalBtn.setOnAction(new EventHandler<ActionEvent>()
		{
      //Minimum signal strength functionality. User enter amount of percentages to make measurement.
			public void handle(ActionEvent event)
			{
				TextInputDialog dialog = new TextInputDialog();
				dialog.setTitle("Minimalna moc sygnału");
				dialog.setHeaderText("Proszę podać minimalną moc sygnału w procentach, dla którego przeprowadzony zostanie pomiar.");

				Optional<String> result = dialog.showAndWait();
				if (result.isPresent()){
					try{
						String value = result.get();
						if (value.endsWith("%")){
							value = value.substring(0, value.length() - 1);
						}
						double min = Double.parseDouble(value);
						if (min < 0 || min > 100) throw new Exception();
						Calculation.setMinimalSignalStrength(min);
					} catch (Exception e){
						Alert calibratedAlert = new Alert(Alert.AlertType.INFORMATION);
						calibratedAlert.setTitle("Nieprawidłowe dane");
						calibratedAlert.setHeaderText("Nieprawidłowe dane.");
						calibratedAlert.show();
					}
				}
			}
		});

        //set sin radio button
        final RadioButton radioSin = new RadioButton("Sin");
        radioSin.setSelected(true);
        radioSin.setToggleGroup(signalGroup);

        //enable frequencyText after checked sin radio button
        radioSin.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                frequencyText.setDisable(false);
                frequencyTextDisabled=false;
            }
        });


        /*
          Set start Button and disable rest of buttons and textfields while program is running
        */
        final Button startBtn = new Button();
        startBtn.setText("START");
        startBtn.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                	startBtn.setDisable(true);
                	channelPickerBtn.setDisable(true);
                	minimalSignalBtn.setDisable(true);
                	calibrateBtn.setDisable(true);

                	radioSin.setDisable(true);
                	radioSweep.setDisable(true);

                	timeText.setDisable(true);
                	frequencyText.setDisable(true);

                    Thread t = new Thread(new Runnable()
					{
						public void run()
						{
							try {
								if (timeText.getText().isEmpty()) throw new Exception("Nieprawidłowe dane wejściowe. Czas nie został podany.");
								int time = Integer.parseInt(timeText.getText());
								if (time < 1) throw new Exception("Nieprawidłowe dane wejściowe. Podany czas jest nieprawidłowy.");
								if (time > 120) throw new Exception("Nieprawidłowe dane wejściowe. Podany czas jest zbyt długi.");

								if (radioSweep.isSelected()) {
			                        outputStream.sweep(20, 20000, 1000*time, 350);
								} else {
									if (frequencyText.getText().isEmpty()) throw new Exception("Nieprawidłowe dane wejściowe. Częstotliwość nie została podana.");
			                        double frequency = Double.parseDouble(frequencyText.getText());
			                        if (frequency <= 20) throw new Exception("Nieprawidłowe dane wejściowe. Podana częstotliwość jest zbyt niska.");
			                        if (frequency > 20000) throw new Exception("Nieprawidłowe dane wejściowe. Podana częstotiwość jest za duża.");
			                        outputStream.sin((int)frequency, 1000*time, 350);
								}

								double[][] result = InputStream.reading((float)1000*time);
								double[] channel1 = result[0];
								double[] channel2 = result[1];

								channel1 = Statistics.outliners(channel1);
								channel2 = Statistics.outliners(channel2);

                //check strength of input signal and return error message if it is true
								if (!Calculation.isSignalStrongEnough(channel1)){
									Platform.runLater(new Runnable()
									{

										public void run()
										{
											Alert exAlert = new Alert(Alert.AlertType.INFORMATION);
						                    exAlert.setTitle("Acoustic-Insulation");
						                    exAlert.setHeaderText("Błąd");
						                    exAlert.setContentText("Sygnał wejściowy zbyt słaby");
						                    exAlert.show();
						                    ScreenEnabled(frequencyTextDisabled,startBtn,channelPickerBtn,
										    		minimalSignalBtn  ,calibrateBtn,radioSin,radioSweep,
										    		timeText, frequencyText);
										}
									});
				                    return;
								}

								channel1 = Statistics.normalization(channel1);
								channel2 = Statistics.normalization(channel2);

								double[] frontChannel, backChannel;
								if (Calculation.isFirstChannelBeforeBarrier()){
									frontChannel = channel1;
									backChannel = channel2;
								} else {
									frontChannel = channel2;
									backChannel = channel1;
								}

								double[] diff = Calculation.calculateIsolation(frontChannel, backChannel, 48000);
								frontChannel = Calculation.calcDBs(frontChannel, 48000);
								backChannel = Calculation.calcDBs(backChannel, 48000);

								IO.saveCSV(frontChannel, backChannel, diff);

								Graph.GenerateAndSetImage(iv1, frontChannel); //set Image from channel1
								Graph.GenerateAndSetImage(iv2, backChannel); //set Image from channel2
								Graph.GenerateAndSetImage(iv3, diff); //set Image showing differences between channel1 nad channel2

								ScreenEnabled(frequencyTextDisabled,startBtn,channelPickerBtn,
							    		minimalSignalBtn  ,calibrateBtn,radioSin,radioSweep,
							    		timeText, frequencyText);

							}catch (final Exception e){
								Platform.runLater(new Runnable()
								{
                  //display error message
									public void run()
									{
										Alert exAlert = new Alert(Alert.AlertType.ERROR);
					                    exAlert.setTitle("Acoustic-Insulation");
					                    exAlert.setHeaderText("Błąd");
					                    exAlert.setContentText(e.getMessage());
					                    exAlert.showAndWait();
					                    ScreenEnabled(frequencyTextDisabled,startBtn,channelPickerBtn,
									    		minimalSignalBtn  ,calibrateBtn,radioSin,radioSweep,
									    		timeText, frequencyText);
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

        //Initilize gui components with style settings
        GridPane root = new GridPane();
        root.setPadding(new Insets(20, 0, 20, 0));

        GridPane buttons = new GridPane();
        buttons.setPadding(new Insets(0, 0, 10, 0));
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
        box.setPadding(new Insets(0,0,10,0));
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

        HBox box7 = new HBox();
        box7.getChildren().add(labelBeforeBarrier);
        box7.setAlignment(Pos.CENTER);

        HBox box4 = new HBox();
        box4.getChildren().addAll(iv1);
        box4.setPadding(new Insets(0,0,0,35));

        HBox box8 = new HBox();
        box8.getChildren().add(labelAfterBarrier);
        box8.setAlignment(Pos.CENTER);

        HBox box5 = new HBox();
        box5.getChildren().addAll(iv2);
        box5.setPadding(new Insets(0,0,0,35));

        HBox box9 = new HBox();
        box9.getChildren().add(labelDifference);
        box9.setAlignment(Pos.CENTER);

        HBox box6 = new HBox();
        box6.getChildren().addAll(iv3);
        box6.setPadding(new Insets(0,0,0,35));

        root.add(buttons, 0, 0);
        root.add(box, 0, 1);
        root.add(box12, 0, 2);
        root.add(box3, 0, 3);
        root.add(box7, 0, 4);
        root.add(box4, 0, 5);
        root.add(box8, 0, 6);
        root.add(box5, 0, 7);
        root.add(box9, 0, 8);
        root.add(box6, 0, 9);

        //Initialize main GUI window with size parameters
        Scene scene = new Scene(root, 900, 700);
        primaryStage.resizableProperty().setValue(Boolean.FALSE);
        primaryStage.setScene(scene);
        primaryStage.show();

        Thread t = new Thread(new Runnable()
		{
			public void run()
			{
				Graph.GenerateAndSetImage(iv1, new double[]{0});
		        Graph.GenerateAndSetImage(iv2, new double[]{0});
		        Graph.GenerateAndSetImage(iv3, new double[]{0});
			}
		});
        t.start();
    }
 /**
  *
  * @param args
  */
    public static void start(String[] args) {

      launch(args);
   }
    /**
     *
     * @param frequencyTextDisabled
     * @param startBtn
     * @param channelPickerBtn
     * @param minimalSignalBtn
     * @param calibrateBtn
     * @param radioSin
     * @param radioSweep
     * @param timeText
     * @param frequencyText
     */
     //Make all elemnts enabled
    public static void ScreenEnabled(boolean frequencyTextDisabled, Button startBtn,Button channelPickerBtn,
    		Button minimalSignalBtn  ,Button calibrateBtn,RadioButton radioSin, RadioButton radioSweep,
    		TextField timeText, TextField frequencyText)
    {

							startBtn.setDisable(false);
							channelPickerBtn.setDisable(false);
		                	minimalSignalBtn.setDisable(false);
		                	calibrateBtn.setDisable(false);

		                	radioSin.setDisable(false);
		                	radioSweep.setDisable(false);

		                 	timeText.setDisable(false);
		                 	if	(!frequencyTextDisabled)
		                 		frequencyText.setDisable(false);

    }
    //Update chanmel labels with new values
    public void UpdateLabels(  Label labelBeforeBarrier, Label labelAfterBarrier)
    {
            int channel = Calculation.isFirstChannelBeforeBarrier()?0:1;
            String label1="Kanał przed przegrodą. (Kanał "+channel+")";
            String label2="Kanał za przegrodą. (Kanał "+((channel==1)?0:1)+")";
            labelBeforeBarrier.setText(label1);
            labelAfterBarrier.setText(label2);
    }
    }
