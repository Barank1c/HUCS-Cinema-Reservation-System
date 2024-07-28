import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class EditUsers implements Window{
    @Override
    public void display(Stage stage, Process processor, Window[] windows, int operationNum) {
        //buttons textlabels etc.
        Button back = new Button("Back");
        Button proDemoClubMember = new Button("Promote/Demote Club Member");
        Button proDemoAdmin = new Button("Promote/Demote Admin");
        HBox buttons = new HBox(back,proDemoClubMember,proDemoAdmin);
        buttons.setSpacing(10);
        buttons.setAlignment(Pos.CENTER);

        TableView<User> table = new TableView<>();
        TableColumn<User,String> username = new TableColumn<>("Username");
        TableColumn<User,String> clubMember = new TableColumn<>("Club Member");
        TableColumn<User,String> admin = new TableColumn<>("Admin");
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        clubMember.setCellValueFactory(new PropertyValueFactory<>("clubMember"));
        admin.setCellValueFactory(new PropertyValueFactory<>("admin"));


        table.getColumns().add(username);
        table.getColumns().add(clubMember);
        table.getColumns().add(admin);


        //adding users to table
        for(User i : processor.getUsers()){
            if(i!=processor.getCurrentUser()) table.getItems().add(i);
        }

        back.setOnAction(e-> windows[1].display(stage,processor,windows,0));
        proDemoAdmin.setOnAction(e->{
            try {
                processor.proDemoAdmin(table.getSelectionModel().getSelectedItem());
                table.refresh();
            } catch (IOException ex) {
                ex.printStackTrace();
            }catch (Exception ignored){//this is for when there is not any user
            }
        });
        proDemoClubMember.setOnAction(e-> {
            try {
                processor.proDemoClubMember(table.getSelectionModel().getSelectedItem());
                table.refresh();
            } catch (IOException ex) {
                ex.printStackTrace();
            }catch (Exception ignored){//this is for when there is not any user
            }
        });


        VBox total = new VBox(table,buttons);
        total.setPadding(new Insets(20,20,20,20));
        total.setSpacing(10);
        total.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(total,600,600));
    }
}
