import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HallWindow implements Window{
    @Override
    public void display(Stage stage, Process processor, Window[] windows, int operationNum) {
        //buttons labels etc.
        VBox total = new VBox();
        total.setSpacing(10);
        HBox text = new HBox(new Label(processor.getCurrentFilm().getName()+" ("
                +processor.getCurrentFilm().getDuration()+" Minutes) Hall: "+processor.getCurrentHall().getHallName()));
        text.setAlignment(Pos.CENTER);
        total.getChildren().add(text);

        Button back = new Button("BACK");
        back.setAlignment(Pos.CENTER_RIGHT);
        back.setOnAction(e->{
            processor.setCurrentHall(null);
            windows[2].display(stage,processor,windows,0);
        });
        ComboBox<User> users = new ComboBox<>(FXCollections.observableArrayList(processor.getUsers()));
        users.getSelectionModel().select(processor.getCurrentUser());

        //buttons array and hbox array for put buttons on scene
        Button[][] buttons = new Button[processor.getCurrentHall().getRow()][processor.getCurrentHall().getColumn()];
        HBox[] buttons1 = new HBox[processor.getCurrentHall().getRow()];

        HBox usersBox = new HBox(users);
        usersBox.setAlignment(Pos.CENTER);
        HBox bought = new HBox(new Label(""));
        bought.setAlignment(Pos.CENTER);
        HBox previousProcess = new HBox(new Label(""));
        previousProcess.setAlignment(Pos.CENTER);

        //the main iterator,most important thing in this class
        for(int i=0;i<processor.getCurrentHall().getRow();i++){
            buttons1[i] = new HBox();
            //find seat at i,j
            for(int j=0;j<processor.getCurrentHall().getColumn();j++){
                Seat temp=null;
                for(Seat k:processor.getCurrentHall().getSeats()){
                    if(k.getRow()==i && k.getColumn()==j) {
                        temp=k;
                        break;
                    }
                }assert temp!=null;
                buttons[i][j]= new Button();
                Seat finalTemp = temp;
                buttons[i][j].setOnMouseEntered(e->{
                    //delete last label
                    bought.getChildren().remove(0);
                    if(finalTemp.getOwner().equals("null")){
                        bought.getChildren().add(new Label("Not bought yet!"));
                    }else{
                        bought.getChildren().add(new Label("Bought by "+ finalTemp.getOwner()+" for "+ finalTemp.getPriceBought()+" TL!"));
                    }
                });
                buttons[i][j].setOnMouseExited(e->{
                    bought.getChildren().remove(0);
                    bought.getChildren().add(new Label(""));
                });
                buttons[i][j].setOnAction(e->{
                    //delete last label
                    previousProcess.getChildren().remove(0);
                    User currentUser;
                    int price=processor.getCurrentHall().getPricePerSeat();
                    if(processor.getCurrentUser().getAdmin().equals("true")) currentUser=users.getValue();
                    else currentUser=processor.getCurrentUser();
                    if(currentUser.getClubMember().equals("true")) price= price*(100-processor.getDiscountPercentage())/100;
                    if(finalTemp.getOwner().equals("null")){
                        ImageView reservedSeat = new ImageView(new Image(new File("assets\\icons\\reserved_seat.png").toURI().toString()));
                        reservedSeat.setFitWidth(50);reservedSeat.setFitHeight(50);
                        finalTemp.setOwnerPrice(currentUser.getUsername(),price);
                        previousProcess.getChildren().add(new Label("Seat at "
                                +(finalTemp.getRow()+1)+"-"+(finalTemp.getColumn()+1)+" is bought for "+ currentUser.getUsername()
                                +" for "+ finalTemp.getPriceBought()+" TL succesfully!" ));
                        buttons[finalTemp.getRow()][finalTemp.getColumn()].setGraphic(reservedSeat);
                    }else {
                        ImageView emptySeat = new ImageView(new Image(new File("assets\\icons\\empty_seat.png").toURI().toString()));
                        emptySeat.setFitWidth(50);emptySeat.setFitHeight(50);
                        finalTemp.setOwnerPrice("null",0);
                        previousProcess.getChildren().add(new Label("Seat at "
                                +(finalTemp.getRow()+1)+"-"+(finalTemp.getColumn()+1)+" refunded "+" succesfully!" ));
                        buttons[finalTemp.getRow()][finalTemp.getColumn()].setGraphic(emptySeat);
                    }
                    try {
                        processor.updateBackUp();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

                if(temp.getOwner().equals("null")){
                    ImageView emptySeat = new ImageView(new Image(new File("assets\\icons\\empty_seat.png").toURI().toString()));
                    emptySeat.setFitWidth(50);emptySeat.setFitHeight(50);
                    buttons[i][j].setGraphic(emptySeat);
                }else{
                    ImageView reservedSeat = new ImageView(new Image(new File("assets\\icons\\reserved_seat.png").toURI().toString()));
                    reservedSeat.setFitWidth(50);reservedSeat.setFitHeight(50);
                    buttons[i][j].setGraphic(reservedSeat);
                    if(processor.getCurrentUser().getAdmin().equals("false")
                            &&!temp.getOwner().equals(processor.getCurrentUser().getUsername())){
                        buttons[i][j].setDisable(true);
                    }
                }
                buttons1[i].getChildren().add(buttons[i][j]);
                buttons1[i].setSpacing(10);
                buttons1[i].setAlignment(Pos.CENTER);
            }
            total.getChildren().add(buttons1[i]);
        }
        if(processor.getCurrentUser().getAdmin().equals("true")) total.getChildren().addAll(usersBox,bought);
        total.getChildren().addAll(previousProcess,back);
        total.setPadding(new Insets(20,20,20,20));
        stage.setScene(new Scene(total));

    }
}
