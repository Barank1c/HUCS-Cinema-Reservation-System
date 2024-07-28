import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class WelcomeWindow implements Window{
    @Override
    public void display(Stage stage, Process processor, Window[] windows, int operationNum) {
        //buttons and combobox
        Button logOut = new Button("LOG OUT");
        Button ok = new Button("OK");
        Button addFilm = new Button("Add Film");
        Button removeFilm = new Button("Remove Film");
        Button editUsers = new Button("Edit Users");
        ComboBox<Film> choiceFilm = new ComboBox<>(FXCollections.observableArrayList(processor.getFilms()));
        choiceFilm.getSelectionModel().selectFirst();

        Label welcome1;
        Label welcome2;
        //defining labels
        if(processor.getCurrentUser().getAdmin().equals("true")){
            welcome2=new Label("You can either select film below or do edits.");
            if(processor.getCurrentUser().getClubMember().equals("true")){
                welcome1 = new Label("Welcome "+processor.getCurrentUser().getUsername()+" (Admin - Club Member)!");
            }else {
                welcome1 = new Label("Welcome "+processor.getCurrentUser().getUsername()+" (Admin)!");
            }
        }else{
            welcome2 = new Label("Select a film and then click OK to continue.");
            if(processor.getCurrentUser().getClubMember().equals("true")){
                welcome1 = new Label("Welcome "+processor.getCurrentUser().getUsername()+" (Club Member)!");
            }else {
                welcome1 = new Label("Welcome "+processor.getCurrentUser().getUsername()+"!");
            }
        }
        //logout button
        Label space = new Label("                                                                            ");
        space.setVisible(false);
        HBox logOut1 = new HBox(space,logOut);
        logOut1.setPrefSize(100,50);
        logOut1.setAlignment(Pos.CENTER);

        VBox welcome = new VBox(welcome1,welcome2);
        welcome.setAlignment(Pos.CENTER);

        HBox selectFilm = new HBox(choiceFilm,ok);
        selectFilm.setAlignment(Pos.CENTER);
        selectFilm.setSpacing(10);

        HBox adminFeatures = new HBox(addFilm,removeFilm,editUsers);
        adminFeatures.setSpacing(10);
        adminFeatures.setAlignment(Pos.CENTER);

        //total
        VBox total = new VBox(welcome,selectFilm);
        if(processor.getCurrentUser().getAdmin().equals("true")) total.getChildren().add(adminFeatures);
        total.getChildren().add(logOut1);
        total.setSpacing(10);
        total.setAlignment(Pos.CENTER);


        //actions
        logOut.setOnAction(e->{
            processor.logIn(null);
            windows[0].display(stage,processor,windows,0);
        });

        ok.setOnAction(e->{
            if(choiceFilm.getItems().size()!=0){
                processor.setCurrentFilm(choiceFilm.getValue());
                windows[2].display(stage,processor,windows,0);
            }
        });
        addFilm.setOnAction(e->windows[3].display(stage,processor,windows,0));
        removeFilm.setOnAction(e->windows[4].display(stage,processor,windows,0));
        editUsers.setOnAction(e->windows[5].display(stage,processor,windows,0));
        stage.setScene(new Scene(total,450,230));
    }
}
