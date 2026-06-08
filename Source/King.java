class King extends Piece {

    private boolean hasMoved = false;

    public void setMoved() {
        hasMoved = true;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public King(boolean white) {

        super(white);
    }

    public String getSymbol() {

        return white ? "K" : "k";
    }

    public boolean canMove(
            Board board,
            int fromRow,
            int fromCol,
            int toRow,
            int toCol) {

        int rowDiff = Math.abs(toRow - fromRow);
        int colDiff = Math.abs(toCol - fromCol);

        if (rowDiff <= 1 && colDiff <= 1) {
            return true;
        }

        if (!hasMoved && rowDiff == 0 && colDiff == 2) {

            if (toCol == 6) {
                Piece rook = board.getPiece(fromRow, 7);
                if (rook instanceof Rook &&
                        !((Rook) rook).hasMoved() &&
                        board.isPathClearStraight(fromRow, fromCol, fromRow, 7)) {
                    return true;
                }
            }

            if (toCol == 2) {
                Piece rook = board.getPiece(fromRow, 0);
                if (rook instanceof Rook &&
                        !((Rook) rook).hasMoved() &&
                        board.isPathClearStraight(fromRow, fromCol, fromRow, 0)) {
                    return true;
                }
            }
        }

        return false;
    }
}