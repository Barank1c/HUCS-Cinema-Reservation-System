import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class LogInSignUpWindow implements Window{

    private int timercount,errorCount=0,botControlNumber;

    //timer function
    private void timer(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                timercount-=1;
                if (timercount == 0) {

                    timer.cancel();
                }
            }
        }, 0, 1000);
    }

    //most important function display(0=login/1=signup)
    @Override
    public void display(Stage stage, Process processor,Window[] windows,int operationNum)  {
        this.timercount = processor.getBlockTime();//getting blockTime

        do {
            botControlNumber = (int) (Math.random() * 1000000);//For bot control
        } while (botControlNumber < 99999);

        //buttons
        Button logInButton = new Button("LOG IN");
        Button signUpButton = new Button("SIGN UP");

        //textfields
        TextField botControl = new TextField();
        TextField usernameTextField = new TextField();
        PasswordField passwordTextField = new PasswordField();
        PasswordField passwordTextField1 = new PasswordField();

        //welcometext
        VBox welcomeText = new VBox(new Label("Welcome to the HUCS Cinema Reservation System!"));
        if(operationNum==0){
            welcomeText.getChildren().addAll(new Label("Please enter your credentials below and click LOGIN."),
                    new Label("You can create a new account by clicking SIGN UP button."));
        }else {
            welcomeText.getChildren().addAll(new Label("Fill the form below to create a new account.")
                    ,new Label("You can go to Log In page by clicking LOG IN Button."));
        }
        welcomeText.setPrefSize(500,110);
        welcomeText.setAlignment(Pos.CENTER);

        //username line
        HBox usernameHBox = new HBox(new Label("Username:"), usernameTextField);
        usernameHBox.setSpacing(20);
        usernameHBox.setAlignment(Pos.CENTER);

        //botcontrol
        HBox botControlHbox = new HBox(new Label("Bot Control\n"+ botControlNumber),botControl);
        botControlHbox.setSpacing(14);
        botControlHbox.setAlignment(Pos.CENTER);

        //password line
        HBox passwordHBox = new HBox(new Label("Password:"),passwordTextField);
        passwordHBox.setSpacing(24);
        passwordHBox.setAlignment(Pos.CENTER);

        //password line for signup
        HBox passwordHBox1 = new HBox(new Label("Password:"),passwordTextField1);
        passwordHBox1.setSpacing(24);
        passwordHBox1.setAlignment(Pos.CENTER);

        //taking buttons on same line
        HBox buttons;
        if(operationNum==0)   buttons = new HBox(signUpButton,logInButton);
        else buttons = new HBox(logInButton,signUpButton);
        buttons.setSpacing(132);
        buttons.setAlignment(Pos.CENTER);

        //putting buttons in vbox
        VBox bottom = new VBox(buttons);
        bottom.setSpacing(15);
        bottom.setAlignment(Pos.CENTER);

        //center
        VBox center = new VBox(usernameHBox,passwordHBox);
        if(operationNum==0) center.getChildren().add(botControlHbox);
        else center.getChildren().add(passwordHBox1);
        center.setSpacing(10);

        //all of them
        VBox total = new VBox(welcomeText,center,bottom);
        total.setSpacing(10);


        logInButton.setOnAction(e -> {
            //deletes last error line if it exists
            if(bottom.getChildren().size()!=1) bottom.getChildren().remove(1);
            //login process
            if(operationNum==0){
                //makes timercount to blocktime when it is 0
                if(this.timercount ==0 )this.timercount = processor.getBlockTime();
                //if timer is not working
                if(timercount==processor.getBlockTime()){
                    User temp=null;
                    for(User i: processor.getUsers()){
                        if(i.getUsername().equals(usernameTextField.getText())
                                && i.getHashedPassword().equals(Process.hashPassword(passwordTextField.getText())) ){
                            temp=i;
                            break;
                        }
                    }
                    if(temp!=null&&Integer.toString(botControlNumber).equals(botControl.getText())){
                        this.errorCount=0;
                        processor.logIn(temp);
                        //go welcome scene
                        windows[1].display(stage,processor,windows,0);
                    }
                    else {
                        processor.playError();
                        errorCount+=1;
                        botControl.clear();
                        do {
                            botControlNumber = (int) (Math.random() * 1000000);//For bot control
                        } while (botControlNumber < 99999);
                        botControlHbox.getChildren().clear();
                        botControlHbox.getChildren().addAll(new Label("Bot Control\n"
                                + botControlNumber),botControl);
                        if(errorCount==processor.getMaxError()){
                            errorCount=0;
                            bottom.getChildren().add(new Label("ERROR: Please wait for 5 seconds to make a new operation!"));
                            timer();
                        }
                        else if(temp!=null){
                            bottom.getChildren().add(new Label("ERROR: Number for bot control is wrong!"));
                        }
                        else{
                            bottom.getChildren().add(new Label("ERROR: There is no such a credential!"));
                            passwordTextField.clear();
                        }
                    }
                }else{
                    //if timercount!=blocktime
                    passwordTextField.clear();
                    botControl.clear();
                    bottom.getChildren().add(new Label("ERROR: Please wait until end of the 5 seconds to make a new operation!"));
                    processor.playError();
                }
            }else windows[0].display(stage,processor,windows,0);//operation==1
        });

        signUpButton.setOnAction(e ->{
            //operation==0
            if(operationNum==0) windows[0].display(stage,processor,windows,1);
            else{
                //deletes last error line if it exists
                if(bottom.getChildren().size()!=1) bottom.getChildren().remove(1);
                int number = 0;
                try {
                    number = processor.signUp(usernameTextField.getText(),passwordTextField.getText(),passwordTextField1.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                if(number==2){
                    processor.playError();
                    bottom.getChildren().add(new Label("ERROR: Password cannot be empty!"));
                    passwordTextField.clear();
                    passwordTextField1.clear();
                }
                else{
                    usernameTextField.clear();
                    passwordTextField.clear();
                    passwordTextField1.clear();
                    if(number==5)bottom.getChildren().add(new Label("Success: You have succesfully registered with your new credentials!"));
                    else {
                        processor.playError();
                        if(number==1)bottom.getChildren().add(new Label("ERROR: Username cannot be empty!"));
                        else if(number==3)bottom.getChildren().add(new Label("ERROR: Passwords do not match!"));
                        else if(number==4) bottom.getChildren().add(new Label("ERROR: This username already exists!"));
                    }
                }
            }
        });
        stage.setScene(new Scene(total,500,400));
    }
}

