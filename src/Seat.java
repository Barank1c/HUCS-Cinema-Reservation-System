public class Seat {
    private int row,column,priceBought;
    private String owner;

    public Seat(int row, int column,int priceBought,String owner) {
        this.row = row;
        this.column = column;
        this.priceBought = priceBought;
        this.owner = owner;
    }

    public void setOwnerPrice(String owner,int priceBought){
        this.owner=owner;
        this.priceBought=priceBought;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public int getPriceBought() {
        return priceBought;
    }

    public String getOwner() {
        return owner;
    }

}
