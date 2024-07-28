import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Properties;
import java.util.Scanner;

public class Process {
    public static String hashPassword(String password){
        byte[] bytesOfPassword = password.getBytes(StandardCharsets.UTF_8) ;
        byte[] md5Digest;
        try {
            md5Digest = MessageDigest.getInstance("MD5").digest(bytesOfPassword);
        } catch(NoSuchAlgorithmException e){
            return null ;
        }return Base64.getEncoder().encodeToString(md5Digest);
    }

    private ArrayList<Film> films = new ArrayList<>();
    private ArrayList<User> users= new ArrayList<>();
    private int maxError,discountPercentage,blockTime;
    private String title;
    private User currentUser;
    private Film currentFilm;
    private Hall currentHall;
    private MediaPlayer error = new MediaPlayer(new Media(new File("assets\\effects\\error.mp3").toURI().toString()));

    public Process() throws IOException {
        //reading backup file if exists
        File backupFile = new File("assets\\data\\backup.dat");
        if(backupFile.exists()){
            Scanner backupReader = new Scanner(backupFile);
            while (backupReader.hasNextLine()){
                try{
                    String[] line = backupReader.nextLine().split("\t");
                    if(line[0].equals("user")){
                        this.users.add(new User(line[1],line[2],line[3],line[4]));
                    }else if(line[0].equals("film")){
                        films.add(new Film(line[1],line[2],Integer.parseInt(line[3])));
                    }else if(line[0].equals("hall")||line[0].equals("seat")){//add hall or seat
                        Film temp=null;
                        for(Film i:films){
                            if(i.getName().equals(line[1])) temp = i;
                        }
                        assert temp!=null;
                        if(line[0].equals("hall")){
                            temp.getHalls().add(new Hall(line[2],Integer.parseInt(line[3]),
                                    Integer.parseInt(line[4]),Integer.parseInt(line[5])));
                        }else{
                            for(Hall i: temp.getHalls()){
                                if(i.getHallName().equals(line[2])) i.getSeats().add(new Seat(Integer.parseInt(line[3])
                                        ,Integer.parseInt(line[4]),Integer.parseInt(line[6]),line[5]));
                            }
                        }
                    }
                    else throw new Exception() ;
                }catch (Exception ignored){
                }
            }backupReader.close();
        }else {
            this.users.add(new User("admin",hashPassword("password"),"true","true"));
            updateBackUp();
        }

        //i assumed there will be always a properties file so i didn't handle it
        FileReader reader = new FileReader("assets\\data\\properties.dat");
        Properties propertiesReader = new Properties();
        propertiesReader.load(reader);
        this.maxError = Integer.parseInt(propertiesReader.getProperty("maximum-error-without-getting-blocked"));
        this.title = propertiesReader.getProperty("title");
        this.discountPercentage= Integer.parseInt(propertiesReader.getProperty("discount-percentage"));
        this.blockTime = Integer.parseInt(propertiesReader.getProperty("block-time"));
        reader.close();
    }

    public void updateBackUp() throws IOException {
        //this function updates backup file
        FileWriter backUp = new FileWriter("assets\\data\\backup.dat");
        backUp.write("");
        for(User i: users){
            backUp.append("user\t"+i.getUsername()+"\t"+i.getHashedPassword()+"\t"+i.getClubMember()+"\t"+i.getAdmin()+"\n");
        }
        for(Film i : films){
            backUp.append("film\t"+i.getName()+"\t"+i.getTrailerPath()+"\t"+i.getDuration()+"\n");
            for(Hall j : i.getHalls()){
                backUp.append("hall\t"+i.getName()+"\t"+j.getHallName() +"\t"+j.getPricePerSeat()+"\t"+
                        j.getRow()+"\t"+j.getColumn()+"\n");
                for(Seat k:j.getSeats()){
                    backUp.append("seat\t"+i.getName()+"\t"+j.getHallName()+"\t"+k.getRow()+"\t"+k.getColumn()
                            +"\t"+k.getOwner()+"\t"+k.getPriceBought()+"\n");
                }
            }
        }
        backUp.close();
    }

    public void setCurrentFilm(Film currentFilm) {
        this.currentFilm = currentFilm;
    }

    public void setCurrentHall(Hall currentHall) {
        this.currentHall = currentHall;
    }

    public void logIn(User currentUser) {
        this.currentUser = currentUser;
    }

    //signup process
    public int signUp(String username, String password1, String password2) throws IOException {
        User temp = null;
        //check username unique
        for(User i:users){
            if(i.getUsername().equals(username)) temp = i;
        }
        if(username.isEmpty()) return 1;
        else if (password1.isEmpty()) return 2;
        else if(!password1.equals(password2)) return 3;
        else if(temp!=null) return 4;
        else {
            users.add(new User(username,hashPassword(password1),"false","false"));
            updateBackUp();
            return 5;
        }
    }

    public int addHall(int row,int column,String hallName,String price) throws IOException {
        //addHall process
        int price1;
        try{
            price1=Integer.parseInt(price);
        }catch (Exception e){
            price1=0;
        }
        Hall temp=null;
        //check hallname unique
        for(Film i:films){
            for(Hall j:i.getHalls()){
                if(j.getHallName().equals(hallName)) temp= j;
            }
        }

        if(hallName.isEmpty()) return 1;
        else if(temp!=null) return 2;
        else if(price.isEmpty()) return 3;
        else if(price1<=0) return 4;
        else {
            Hall temp1 = new Hall(hallName,price1,row,column);
            //creating seats for hall
            for(int i=0;i<row;i++){
                for(int j=0;j<column;j++){
                    temp1.getSeats().add(new Seat(i,j,0,"null"));
                }
            }
            currentFilm.getHalls().add(temp1);
            updateBackUp();
            return 5;
        }
    }

    public int addFilm(String name, String trailerPath, String duration) throws IOException {
        //addFilm process
        //check for duration can be converted to inteher
        int duration1;
        try{
            duration1 = Integer.parseInt(duration);
        }catch (Exception ignored){
            duration1=0;
        }
        String currentTrailerPath = "assets\\trailers\\"+trailerPath;
        File trailer = new File(currentTrailerPath);
        //look for film exists
        Film temp = null;
        for(Film i : films){
            if(i.getName().equals(name)) temp= i;
        }
        if(name.isEmpty())return 1;
        else if(temp!=null) return 2;
        else if(trailerPath.isEmpty()) return 3;
        else if(duration1<=0) return 4;
        else if(!trailer.exists()) return 5;
        else {
            films.add(new Film(name,trailerPath,duration1));
            updateBackUp();
            return 6;
        }
    }

    public void proDemoAdmin(User user) throws IOException {
        //promote and demote admin
        if(user.getAdmin().equals("true")){
            user.setAdmin("false");
        }else {
            user.setAdmin("true");
        }
        updateBackUp();
    }

    public void proDemoClubMember(User user) throws IOException {
        //promote and demote club member
        if(user.getClubMember().equals("true")){
            user.setClubMember("false");
        }else {
            user.setClubMember("true");
        }
        updateBackUp();
    }


    //getters
    public ArrayList<Film> getFilms() {
        return films;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public int getMaxError() {
        return maxError;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public int getBlockTime() {
        return blockTime;
    }

    public String getTitle() {
        return title;
    }

    public void playError() {
        error.seek(error.getStartTime());
        error.play();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public Film getCurrentFilm() {
        return currentFilm;
    }

    public Hall getCurrentHall() {
        return currentHall;
    }
}

