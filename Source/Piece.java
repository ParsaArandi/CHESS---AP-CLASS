abstract class Piece {

    protected boolean white;

    public Piece(boolean white) {

        this.white = white;
    }

    public boolean isWhite() {

        return white;
    }

    public abstract String getSymbol();

    public abstract boolean canMove(
            Board board,
            int fromRow,
            int fromCol,
            int toRow,
            int toCol
    );
}