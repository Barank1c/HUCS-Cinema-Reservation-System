import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class RemoveFilm implements Window{
    @Override
    public void display(Stage stage, Process processor, Window[] windows, int operationNum) {

        Label text = new Label("Select the film that you desire to remove and then click OK.");

        Button back = new Button("BACK");
        Button ok = new Button("OK");
        HBox buttons = new HBox(back,ok);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);

        ComboBox<Film> choiceFilm = new ComboBox<>(FXCollections.observableArrayList(processor.getFilms()));
        choiceFilm.getSelectionModel().selectFirst();


        back.setOnAction(e->windows[1].display(stage,processor,windows,0));
        ok.setOnAction(e->{
            try {
                processor.getFilms().remove(choiceFilm.getValue());
                processor.updateBackUp();
                choiceFilm.getItems().remove(choiceFilm.getValue());
                choiceFilm.getSelectionModel().selectFirst();
            } catch (IOException ex) {
                ex.printStackTrace();
            }catch (Exception ignored){//this is for null exception
            }
        });


        VBox total = new VBox(text,choiceFilm,buttons);
        total.setSpacing(10);
        total.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(total,500,200));
    }
}
