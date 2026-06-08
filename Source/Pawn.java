class Pawn extends Piece {

    private boolean justDoubleMoved = false;

    public void setJustDoubleMoved(boolean value) {
        justDoubleMoved = value;
    }

    public boolean justDoubleMoved() {
        return justDoubleMoved;
    }

    public Pawn(boolean white) {

        super(white);
    }

    public String getSymbol() {

        return white ? "P" : "p";
    }

    public boolean canMove(
            Board board,
            int fromRow,
            int fromCol,
            int toRow,
            int toCol) {

        int direction;

        if (white) {

            direction = -1;

        } else {

            direction = 1;
        }

        if (fromCol == toCol &&
                toRow == fromRow + direction &&
                board.getPiece(toRow, toCol) == null) {

            return true;
        }

        if (fromCol == toCol) {

            if (white &&
                    fromRow == 6 &&
                    toRow == 4 &&
                    board.getPiece(5, toCol) == null &&
                    board.getPiece(4, toCol) == null) {

                return true;
            }

            if (!white &&
                    fromRow == 1 &&
                    toRow == 3 &&
                    board.getPiece(2, toCol) == null &&
                    board.getPiece(3, toCol) == null) {

                return true;
            }
        }

        if (Math.abs(toCol - fromCol) == 1 &&
                toRow == fromRow + direction) {

            Piece target =
                    board.getPiece(toRow, toCol);

            if (target != null &&
                    target.isWhite() != white) {

                return true;
            }
        }

        if (Math.abs(toCol - fromCol) == 1 &&
                toRow == fromRow + direction &&
                board.getPiece(toRow, toCol) == null) {

            Piece adjacent = board.getPiece(fromRow, toCol);
            return adjacent instanceof Pawn &&
                    adjacent.isWhite() != white &&
                    ((Pawn) adjacent).justDoubleMoved();
        }

        return false;
    }
}