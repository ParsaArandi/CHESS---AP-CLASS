class Bishop extends Piece {

    public Bishop(boolean white) {

        super(white);
    }
    public String getSymbol() {

        return white ? "B" : "b";
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

        if (rowDiff == colDiff) {

            return board.isPathClearDiagonal(
                    fromRow,
                    fromCol,
                    toRow,
                    toCol
            );
        }

        return false;
    }
}