class Rook extends Piece {

    private boolean hasMoved = false;

    public void setMoved() {
        hasMoved = true;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public Rook(boolean white) {

        super(white);
    }

    public String getSymbol() {

        return white ? "R" : "r";
    }

    public boolean canMove(
            Board board,
            int fromRow,
            int fromCol,
            int toRow,
            int toCol) {

        if (fromRow == toRow ||
                fromCol == toCol) {

            return board.isPathClearStraight(
                    fromRow,
                    fromCol,
                    toRow,
                    toCol
            );
        }

        return false;
    }
}