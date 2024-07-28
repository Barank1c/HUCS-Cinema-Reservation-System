import javafx.stage.Stage;

public interface Window {
     void display(Stage stage, Process processor,Window[] windows,int operationNum);
    //operation num is for signUp(1)/logIn(0) I implemented them in the same class
}
