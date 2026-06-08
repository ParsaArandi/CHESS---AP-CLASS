class Board {
    private Piece[][] board;

    public Board() {

        board = new Piece[8][8];

        initializeBoard();
    }

    private void initializeBoard() {

        board[7][0] = new Rook(true);
        board[7][1] = new Knight(true);
        board[7][2] = new Bishop(true);
        board[7][3] = new Queen(true);
        board[7][4] = new King(true);
        board[7][5] = new Bishop(true);
        board[7][6] = new Knight(true);
        board[7][7] = new Rook(true);

        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(true);
        }
        board[0][0] = new Rook(false);
        board[0][1] = new Knight(false);
        board[0][2] = new Bishop(false);
        board[0][3] = new Queen(false);
        board[0][4] = new King(false);
        board[0][5] = new Bishop(false);
        board[0][6] = new Knight(false);
        board[0][7] = new Rook(false);
        
        for (int i = 0; i < 8; i++) {

            board[1][i] = new Pawn(false);
        }
    }
    public void printBoard() {

        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + " ");

            for (int col = 0; col < 8; col++) {

                if (board[row][col] == null) {

                    System.out.print(". ");
                }

                else {

                    System.out.print(
                            board[row][col].getSymbol() + " "
                    );
                }
            }

            System.out.println();
        }

        System.out.println("  a b c d e f g h");
    }

    public Piece getPiece(int row, int col) {

        return board[row][col];
    }

    public void movePiece(
            int fromRow,
            int fromCol,
            int toRow,
            int toCol) {

        board[toRow][toCol] =
                board[fromRow][fromCol];

        board[fromRow][fromCol] = null;
    }
    public void setPiece(int row, int col, Piece piece) {

        board[row][col] = piece;
    }
    public boolean isInside(int row, int col) {

        return row >= 0 &&
                row < 8 &&
                col >= 0 &&
                col < 8;
    }
    public boolean isPathClearStraight(
            int fromRow,
            int fromCol,
            int toRow,
            int toCol) {

        if (fromRow == toRow) {

            int step =
                    (toCol > fromCol) ? 1 : -1;

            for (int c = fromCol + step;
                 c != toCol;
                 c += step) {

                if (board[fromRow][c] != null) {

                    return false;
                }
            }
        }
        else if (fromCol == toCol) {
            int step =
                    (toRow > fromRow) ? 1 : -1;
            for (int r = fromRow + step;
                 r != toRow;
                 r += step) {
                if (board[r][fromCol] != null) {

                    return false;
                }
            }
        }

        return true;
    }
    public boolean isPathClearDiagonal(
            int fromRow,
            int fromCol,
            int toRow,
            int toCol) {

        int rowStep =
                (toRow > fromRow) ? 1 : -1;

        int colStep =
                (toCol > fromCol) ? 1 : -1;

        int r = fromRow + rowStep;
        int c = fromCol + colStep;

        while (r != toRow && c != toCol) {

            if (board[r][c] != null) {

                return false;
            }

            r += rowStep;
            c += colStep;
        }


        return true;
    }

    public int[] findKing(boolean white) {

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board[r][c];
                if (p instanceof King && p.isWhite() == white) {
                    return new int[]{r, c};
                }
            }
        }
        return null;
    }
}