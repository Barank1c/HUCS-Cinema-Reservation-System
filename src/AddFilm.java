import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AddFilm implements Window {
    @Override
    public void display(Stage stage, Process processor, Window[] windows, int operationNum) {
        //labels and texts
        VBox text = new VBox(new Label("Please give name, relative path of the trailer and duration of the film."));
        text.setAlignment(Pos.CENTER);
        Label name = new Label("Name:");
        Label path = new Label("Trailer (Path):");
        Label duration = new Label("Duration (m):");

        //buttons
        Button back = new Button("BACK");
        Button ok = new Button("OK");
        Label spaceForOk = new Label("                                    ");
        spaceForOk.setVisible(false);
        HBox ok1 = new HBox(spaceForOk,ok);

        //textfields
        TextField name1 = new TextField();
        TextField duration1 = new TextField();
        TextField path1 = new TextField();

        VBox labelsBack = new VBox(name,path,duration,back);
        labelsBack.setSpacing(20);
        VBox textFieldsOk = new VBox(name1,path1,duration1,ok1);
        textFieldsOk.setSpacing(10);
        HBox labelTextButton = new HBox(labelsBack,textFieldsOk);
        labelTextButton.setSpacing(10);
        labelTextButton.setAlignment(Pos.CENTER);

        VBox total = new VBox(text,labelTextButton);

        back.setOnAction(e-> windows[1].display(stage,processor,windows,0));
        ok.setOnAction(e->{
            //deleta last label
            if(total.getChildren().size()!=2) total.getChildren().remove(2);
            VBox temp = new VBox();
            int number=0;
            try {
                number = processor.addFilm(name1.getText(),path1.getText(),duration1.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(number==6){
                temp.getChildren().add(new Label("Success: Film added succesfully!"));
                name1.clear();
                path1.clear();
                duration1.clear();
            }else {
                processor.playError();
                if(number==1){
                    temp.getChildren().add(new Label("Error: Film name could not be empty!"));
                }else if(number==2){
                    temp.getChildren().add(new Label("Error: Film name has to be unique!"));
                }else if(number==3){
                    temp.getChildren().add(new Label("Error: Trailer path could not be empty!"));
                }else if(number==4){
                    temp.getChildren().add(new Label("Error: Duration has to be a positive integer!"));
                }else if(number==5){
                    temp.getChildren().add(new Label("Error: There is no such a trailer!"));
                }
            }
            temp.setAlignment(Pos.CENTER);
            total.getChildren().add(temp);
        });
        total.setSpacing(10);
        stage.setScene(new Scene(total,500,250));

    }
}
