import java.util.ArrayList;

public class Film {
    private ArrayList<Hall> halls = new ArrayList<>();
    private String name,trailerPath;
    private int duration;

    public Film(String name, String trailerPath, int duration) {
        this.name = name;
        this.trailerPath = trailerPath;
        this.duration = duration;
    }

    public void addHall(String hallName, int row, int column, int pricePerSeat){
        halls.add(new Hall(hallName, row, column,  pricePerSeat));
    }

    public ArrayList<Hall> getHalls() {
        return halls;
    }

    public String getName() {
        return name;
    }

    public String getTrailerPath() {
        return trailerPath;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return name;
    }
}
