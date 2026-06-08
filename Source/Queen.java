class Queen extends Piece {

    public Queen(boolean white) {

        super(white);
    }

    public String getSymbol() {

        return white ? "Q" : "q";
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

        if (fromRow == toRow ||
                fromCol == toCol) {

            return board.isPathClearStraight(
                    fromRow,
                    fromCol,
                    toRow,
                    toCol
            );
        }

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