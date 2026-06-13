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

                System.out.println("White's Turn");

            } else {

                System.out.println("Black's Turn");
            }
            System.out.println("Enter move (e.g. e2 e4): ");

            String input = scanner.nextLine().trim();

            String[] parts = input.split(" ");

            if (parts.length != 2) {

                System.out.println("Invalid Input!");
                continue;
            }

            String from = parts[0];
            String to   = parts[1];

            // Ezafe shodan check kardan line bozorg (mesl e99)
            if (from.length() != 2 || to.length() != 2) {
                System.out.println("Invalid Input! Use format like e2 e4.");
                continue;
            }

            int fromRow = 8 - Character.getNumericValue(from.charAt(1));
            int fromCol = from.charAt(0) - 'a';
            int toRow   = 8 - Character.getNumericValue(to.charAt(1));
            int toCol   = to.charAt(0) - 'a';

            if (!board.isInside(fromRow, fromCol) ||
                    !board.isInside(toRow, toCol)) {

                System.out.println("Out Of Board!");
                continue;
            }

            Piece piece = board.getPiece(fromRow, fromCol);

            if (piece == null) {

                System.out.println("No Piece Selected!");
                continue;
            }

            if (piece.isWhite() != whiteTurn) {

                System.out.println("Not Your Piece!");
                continue;
            }

            if (!piece.canMove(board, fromRow, fromCol, toRow, toCol)) {

                System.out.println("Illegal Move!");
                continue;
            }

            Piece target = board.getPiece(toRow, toCol);

            if (target != null && target.isWhite() == piece.isWhite()) {

                System.out.println("Cannot Capture Own Piece!");
                continue;
            }

           for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    Piece p = board.getPiece(r, c);
                    if (p instanceof Pawn && p.isWhite() != whiteTurn) {
                        ((Pawn) p).setJustDoubleMoved(false);
                    }
                }
            }

            boolean isCastling = piece instanceof King && Math.abs(toCol - fromCol) == 2;

            boolean isEnPassant = piece instanceof Pawn &&
                    Math.abs(toCol - fromCol) == 1 &&
                    board.getPiece(toRow, toCol) == null;

            if (isCastling) {
                boolean byEnemy = !whiteTurn; // enemy is opposite color

                if (toCol == 6) {
                    if (isSquareAttacked(fromRow, fromCol,     byEnemy) ||
                            isSquareAttacked(fromRow, fromCol + 1, byEnemy) ||
                            isSquareAttacked(fromRow, fromCol + 2, byEnemy)) {
                        System.out.println("Cannot castle through or out of check!");
                        continue;
                    }
                } else if (toCol == 2) {
                    if (isSquareAttacked(fromRow, fromCol,     byEnemy) ||
                            isSquareAttacked(fromRow, fromCol - 1, byEnemy) ||
                            isSquareAttacked(fromRow, fromCol - 2, byEnemy)) {
                        System.out.println("Cannot castle through or out of check!");
                        continue;
                    }
                }
            }

            Piece savedTarget = board.getPiece(toRow, toCol);

            int epCaptureRow = -1, epCaptureCol = -1;
            Piece epCapturedPawn = null;

            if (isEnPassant) {
                epCaptureRow = fromRow;
                epCaptureCol = toCol;
                epCapturedPawn = board.getPiece(epCaptureRow, epCaptureCol);
                board.setPiece(epCaptureRow, epCaptureCol, null);
            }

            if (isCastling) {
                if (toCol == 6) {
                    board.movePiece(fromRow, 7, fromRow, 5);
                } else if (toCol == 2) {
                    board.movePiece(fromRow, 0, fromRow, 3);
                }
            }

            boolean wasDoubleMove = piece instanceof Pawn && Math.abs(toRow - fromRow) == 2;

            board.movePiece(fromRow, fromCol, toRow, toCol);

            if (wasDoubleMove) {
                ((Pawn) piece).setJustDoubleMoved(true);
            }

            if (isInCheck(whiteTurn)) {
                board.movePiece(toRow, toCol, fromRow, fromCol);
                board.setPiece(toRow, toCol, savedTarget);

                if (wasDoubleMove) {
                    ((Pawn) piece).setJustDoubleMoved(false);
                }

                if (isEnPassant && epCapturedPawn != null) {
                    board.setPiece(epCaptureRow, epCaptureCol, epCapturedPawn);
                }

                if (isCastling) {
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

            if (isCastling) {
                Rook castledRook = null;
                if (toCol == 6) {
                    castledRook = (Rook) board.getPiece(fromRow, 5);
                } else if (toCol == 2) {
                    castledRook = (Rook) board.getPiece(fromRow, 3);
                }
                if (castledRook != null) castledRook.setMoved();
            }

            Piece moved = board.getPiece(toRow, toCol);
            if (moved instanceof Pawn) {
                if ((whiteTurn && toRow == 0) || (!whiteTurn && toRow == 7)) {
                    board.setPiece(toRow, toCol, new Queen(whiteTurn));
                    System.out.println("Pawn promoted to Queen!");
                }
            }

            boolean opponentInCheck   = isInCheck(!whiteTurn);
            boolean opponentHasMoves  = hasAnyLegalMove(!whiteTurn);

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
        if (kingPos == null) return false; // safety guard
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

    private boolean isSquareAttacked(int row, int col, boolean byWhite) {

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getPiece(r, c);
                if (p != null && p.isWhite() == byWhite) {
                    if (p.canMove(board, r, c, row, col)) {
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

                        boolean wasEnPassant = false;
                        Piece enPassantPawn = null;
                        int enPassantRow = -1, enPassantCol = -1;

                        if (p instanceof Pawn &&
                                Math.abs(tc - fc) == 1 &&
                                captured == null) {
                            enPassantRow = fr;
                            enPassantCol = tc;
                            enPassantPawn = board.getPiece(enPassantRow, enPassantCol);
                            if (enPassantPawn instanceof Pawn &&
                                    enPassantPawn.isWhite() != white &&
                                    ((Pawn) enPassantPawn).justDoubleMoved()) {
                                wasEnPassant = true;
                                board.setPiece(enPassantRow, enPassantCol, null);
                            }
                        }

                        board.movePiece(fr, fc, tr, tc);
                        boolean stillInCheck = isInCheck(white);

                        board.movePiece(tr, tc, fr, fc);
                        board.setPiece(tr, tc, captured);
                        if (wasEnPassant) {
                            board.setPiece(enPassantRow, enPassantCol, enPassantPawn);
                        }

                        if (!stillInCheck) return true;
                    }
                }
            }
        }
        return false;
    }
}
