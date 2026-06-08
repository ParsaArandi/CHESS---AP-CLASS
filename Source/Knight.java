class Knight extends Piece {

    public Knight(boolean white) {

        super(white);
    }

    public String getSymbol() {

        return white ? "N" : "n";
    }

    public boolean canMove(
            Board board,
            int fromRow,
            int fromCol,
            int toRow,
            int toCol) {

        int rowDiff =
                Math.abs(toRow - fromRow);

        int colDiff =
                Math.abs(toCol - fromCol);

        return (rowDiff == 2 && colDiff == 1) ||
                (rowDiff == 1 && colDiff == 2);
    }
}