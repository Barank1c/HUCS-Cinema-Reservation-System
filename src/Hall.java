import java.util.ArrayList;

public class Hall {
    private String hallName;
    private int row,column,pricePerSeat;
    private ArrayList<Seat> seats = new ArrayList<>();

    public Hall(String hallName,int pricePerSeat ,int row, int column) {
        this.hallName = hallName;
        this.row = row;
        this.column = column;
        this.pricePerSeat = pricePerSeat;
    }

    public String getHallName() {
        return hallName;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPricePerSeat() {
        return pricePerSeat;
    }

    public ArrayList<Seat> getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return hallName;
    }
}
