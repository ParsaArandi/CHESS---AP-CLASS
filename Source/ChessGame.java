import java.util.Scanner;

class ChessGame {

    private Board board;
    private boolean whiteTurn;
    private Scanner scanner;

    public ChessGame() {

        board = new Board();

        whiteTurn = true;

        scanner = new Scanner(System.in);
    }

    public void start() {

        while (true) {

            board.printBoard();
            if (whiteTurn) {

                System.out.println("White Turn");

            } else {

                System.out.println("Black Turn");
            }
            System.out.println("Enter move example: e2 e4");

            String input = scanner.nextLine();

            String[] parts = input.split(" ");

            if (parts.length != 2) {

    
                System.out.println("Invalid Input!");

                continue;
            }

            String from = parts[0];

            String to = parts[1];

            int fromRow =
                    8 - Character.getNumericValue(from.charAt(1));

            int fromCol =
                    from.charAt(0) - 'a';

            int toRow =
                    8 - Character.getNumericValue(to.charAt(1));

            int toCol =
                    to.charAt(0) - 'a';

            if (!board.isInside(fromRow, fromCol) ||
                    !board.isInside(toRow, toCol)) {

                System.out.println("Out Of Board!");

                continue;
            }

            Piece piece =
                    board.getPiece(fromRow, fromCol);

            if (piece == null) {

                System.out.println("No Piece Selected!");

                continue;
            }

            if (piece.isWhite() != whiteTurn) {

                System.out.println("Not Your Piece!");

                continue;
            }

            if (!piece.canMove(
                    board,
                    fromRow,
                    fromCol,
                    toRow,
                    toCol)) {

                System.out.println("Illegal Move!");

                continue;
            }

            Piece target =
                    board.getPiece(toRow, toCol);

            if (target != null &&
                    target.isWhite() == piece.isWhite()) {

                System.out.println("Cannot Capture Own Piece!");

                continue;
            }

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece p = board.getPiece(r, c);
                    if (p instanceof Pawn && p.isWhite() == whiteTurn) {
                        ((Pawn) p).setJustDoubleMoved(false);
                    }
                }
            }

            if (piece instanceof Pawn &&
                    Math.abs(toCol - fromCol) == 1 &&
                    board.getPiece(toRow, toCol) == null) {
                board.setPiece(fromRow, toCol, null);
            }

            if (piece instanceof Pawn && Math.abs(toRow - fromRow) == 2) {
                ((Pawn) piece).setJustDoubleMoved(true);
            }

            if (piece instanceof King && Math.abs(toCol - fromCol) == 2) {
                if (toCol == 6) {
                    board.movePiece(fromRow, 7, fromRow, 5);
                    ((Rook) board.getPiece(fromRow, 5)).setMoved();
                } else if (toCol == 2) {
                    board.movePiece(fromRow, 0, fromRow, 3);
                    ((Rook) board.getPiece(fromRow, 3)).setMoved();
                }
                ((King) piece).setMoved();
            }


            Piece savedTarget = board.getPiece(toRow, toCol);

            board.movePiece(fromRow, fromCol, toRow, toCol);

            if (isInCheck(whiteTurn)) {
                board.movePiece(toRow, toCol, fromRow, fromCol);
                board.setPiece(toRow, toCol, savedTarget);  // restore captured piece

                if (piece instanceof King && Math.abs(toCol - fromCol) == 2) {
                    if (toCol == 6) {
                        board.movePiece(fromRow, 5, fromRow, 7);
                    } else if (toCol == 2) {
                        board.movePiece(fromRow, 3, fromRow, 0);
                    }
                }

                System.out.println("Move leaves your King in check!");
                continue;
            }

            if (piece instanceof Rook) {
                ((Rook) piece).setMoved();
            }

            if (piece instanceof King) {
                ((King) piece).setMoved();
            }

            Piece moved = board.getPiece(toRow, toCol);
            if (moved instanceof Pawn) {
                if ((whiteTurn && toRow == 0) || (!whiteTurn && toRow == 7)) {
                    board.setPiece(toRow, toCol, new Queen(whiteTurn));
                    System.out.println("Pawn promoted to Queen!");
                }
            }

            boolean opponentInCheck = isInCheck(!whiteTurn);
            boolean opponentHasMoves = hasAnyLegalMove(!whiteTurn);

            if (opponentInCheck && !opponentHasMoves) {
                board.printBoard();
                System.out.println(whiteTurn ? "White wins! Checkmate!" : "Black wins! Checkmate!");
                break;
            } else if (opponentInCheck) {
                System.out.println("Check!");
            } else if (!opponentHasMoves) {
                board.printBoard();
                System.out.println("Stalemate! It's a draw.");
                break;
            }

            whiteTurn = !whiteTurn;
        }
    }

    private boolean isInCheck(boolean white) {

        int[] kingPos = board.findKing(white);
        int kingRow = kingPos[0];
        int kingCol = kingPos[1];

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(r, c);
                if (p != null && p.isWhite() != white) {
                    if (p.canMove(board, r, c, kingRow, kingCol)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



    private boolean hasAnyLegalMove(boolean white) {

        for (int fr = 0; fr < 8; fr++) {
            for (int fc = 0; fc < 8; fc++) {
                Piece p = board.getPiece(fr, fc);
                if (p == null || p.isWhite() != white) continue;

                for (int tr = 0; tr < 8; tr++) {
                    for (int tc = 0; tc < 8; tc++) {
                        if (!p.canMove(board, fr, fc, tr, tc)) continue;
                        Piece captured = board.getPiece(tr, tc);
                        if (captured != null && captured.isWhite() == white) continue;


                        board.movePiece(fr, fc, tr, tc);
                        boolean stillInCheck = isInCheck(white);

                        board.movePiece(tr, tc, fr, fc);
                        board.setPiece(tr, tc, captured);

                        if (!stillInCheck) return true;
                    }
                }
            }
        }
        return false;
    }
}