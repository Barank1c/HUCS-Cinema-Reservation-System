import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {
    public static void main(String[] args)  {
        //starting
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        //Processor class doesn't include any GUI process,it provides the important processes
        Process processor = new Process();
        primaryStage.setTitle(processor.getTitle());
        primaryStage.getIcons().add(new Image(new File("assets\\icons\\logo.png").toURI().toURL().toString()));

        //The windows array which will be sent to display functions and that provides pass through scenes
        Window[] windows = new Window[]{new LogInSignUpWindow(),new WelcomeWindow(),new FilmWindow()
                ,new AddFilm(),new RemoveFilm(),new EditUsers(),new HallWindow(),new AddHall(),new RemoveHall()};
        //starting with logIn scene
        windows[0].display(primaryStage,processor,windows,0);
        primaryStage.show();

    }
}
