import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class AddHall implements Window{
    @Override
    public void display(Stage stage, Process processor, Window[] windows, int operationNum) {

        //buttons,textfields etc.
        TextField name = new TextField();
        TextField price = new TextField();

        Button back =new Button("Back");
        Button ok = new Button("OK");

        ComboBox<Integer> rowComboBox = new ComboBox<>(FXCollections.observableArrayList(3,4,5,6,7,8,9,10));
        rowComboBox.getSelectionModel().selectFirst();

        ComboBox<Integer> columnComboBox = new ComboBox<>(FXCollections.observableArrayList(3,4,5,6,7,8,9,10));
        columnComboBox.getSelectionModel().selectFirst();

        VBox text = new VBox(new Label(processor.getCurrentFilm().getName()+" ("+processor.getCurrentFilm().getDuration()
                +" minutes)"));
        text.setAlignment(Pos.CENTER);
        VBox column1 = new VBox(new Label("Row:"),new Label("Column:"),new Label("Name:")
                , new Label("Price:"),back);
        column1.setSpacing(19);column1.setAlignment(Pos.CENTER);
        VBox column2 = new VBox(rowComboBox,columnComboBox,name,price,new HBox(new Label("    " +
                "                                      "),ok));
        column2.setSpacing(10);column2.setAlignment(Pos.CENTER);
        HBox columns = new HBox(column1,column2);
        columns.setSpacing(10);columns.setAlignment(Pos.CENTER);

        VBox total = new VBox(text,columns);

        back.setOnAction(e->windows[2].display(stage,processor,windows,0));
        ok.setOnAction(e->{
            //delete last label
            if(total.getChildren().size()!=2) total.getChildren().remove(2);
            VBox temp = new VBox();
            int number=0;
            try {
                number = processor.addHall(rowComboBox.getValue(),columnComboBox.getValue(),name.getText(),price.getText());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(number==5){
                temp.getChildren().add(new Label("SUCCESS: Hall succesfully created!"));
                name.clear();
                price.clear();
                rowComboBox.getSelectionModel().selectFirst();
                columnComboBox.getSelectionModel().selectFirst();
            }else {
                processor.playError();
                if(number==1){
                    temp.getChildren().add(new Label("Error: Hall name could not be empty!"));
                }else if(number==2){
                    temp.getChildren().add(new Label("Error: Hall name has to be unique!"));
                }else if(number==3){
                    temp.getChildren().add(new Label("Error: Price could not be empty!"));
                }else if(number==4){
                    temp.getChildren().add(new Label("Error: Price has to be a positive integer!"));
                }
            }
            temp.setAlignment(Pos.CENTER);
            total.getChildren().add(temp);
        });

        total.setSpacing(10);
        stage.setScene(new Scene(total,400,300));


    }
}
