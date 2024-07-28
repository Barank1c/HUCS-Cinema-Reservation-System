import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

public class FilmWindow implements Window{

    @Override
    public void display(Stage stage, Process processor, Window[] windows, int operationNum) {
        //buttons labels etc.
        Label text = new Label(processor.getCurrentFilm().getName()+" ("+processor.getCurrentFilm().getDuration()
                +" minutes)");
        MediaView trailerVideo = new MediaView(new MediaPlayer(new Media(new File("assets\\trailers\\"
                +processor.getCurrentFilm().getTrailerPath()).toURI().toString())));
        trailerVideo.setFitWidth(800);
        MediaPlayer controller = trailerVideo.getMediaPlayer();

        Button back = new Button("BACK");
        Button addHall = new Button("Add Hall");
        Button removeHall = new Button("Remove Hall");
        Button ok = new Button("OK");
        Button startStop=new Button(">");startStop.setPrefSize(90,10);//LOOK SYMBOL
        Button skipForward = new Button(">>");skipForward.setPrefSize(90,10);
        Button skipBack = new Button("<<");skipBack.setPrefSize(90,10);
        Button rewindButton = new Button("|<<");rewindButton.setPrefSize(90,10);

        Slider volumeSlider = new Slider();
        volumeSlider.setOrientation(Orientation.VERTICAL);
        volumeSlider.setValue(controller.getVolume()*100);
        volumeSlider.valueProperty().addListener(observable -> controller.setVolume(volumeSlider.getValue()/100));


        ComboBox<Hall> hallComboBox = new ComboBox<>(FXCollections.observableArrayList(processor.getCurrentFilm().getHalls()));
        hallComboBox.getSelectionModel().selectFirst();

        //button operations
        controller.setOnEndOfMedia(()->{
            controller.seek(controller.getStartTime());
            controller.pause();
            startStop.setText(">");
        });
        back.setOnAction(e->{
            controller.stop();
            windows[1].display(stage,processor,windows,0);
            processor.setCurrentFilm(null);
        });
        startStop.setOnAction(e-> {
            if(controller.getStatus().equals(MediaPlayer.Status.PLAYING)) {
                controller.pause();
                startStop.setText(">");
            }
            else {
                controller.play();
                startStop.setText("||");
            }

        });
        skipForward.setOnAction(e->{
            if(5+controller.getCurrentTime().toSeconds()>=controller.getStopTime().toSeconds()){
                controller.seek(controller.getStopTime());
            }else {
                controller.seek(controller.getCurrentTime().add(new Duration(5000)));
            }
        });
        skipBack.setOnAction(e->{
            if(controller.getCurrentTime().toSeconds()-5<=0){
                controller.seek(controller.getStartTime());
            }else {
                controller.seek(controller.getCurrentTime().subtract(new Duration(5000)));
            }
        });
        rewindButton.setOnAction(e->controller.seek(controller.getStartTime()));
        ok.setOnAction(e->{
            if(processor.getCurrentFilm().getHalls().size()!=0){
                controller.stop();
                processor.setCurrentHall(hallComboBox.getValue());
                windows[6].display(stage,processor,windows,0);
            }
        });
        addHall.setOnAction(e->{
            controller.stop();
            windows[7].display(stage,processor,windows,0);
        });
        removeHall.setOnAction(e->{
            controller.stop();
            windows[8].display(stage,processor,windows,0);
        });

        VBox buttons = new VBox(startStop,skipBack,skipForward,rewindButton,volumeSlider);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);
        HBox filmBox = new HBox(trailerVideo,buttons);
        filmBox.setSpacing(10);
        filmBox.setAlignment(Pos.CENTER);
        HBox bottom = new HBox(back);
        bottom.setSpacing(10);
        bottom.setAlignment(Pos.CENTER);
        if(processor.getCurrentUser().getAdmin().equals("true")) bottom.getChildren().addAll(addHall,removeHall);
        bottom.getChildren().addAll(hallComboBox,ok);
        VBox total = new VBox(text,filmBox,bottom);
        total.setSpacing(10);
        total.setAlignment(Pos.CENTER);
        Scene scene = new Scene(total,1000,600);

        stage.setScene(scene);


    }
}
