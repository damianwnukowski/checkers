package wnukowski.damian.server;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GameRoom {
    private LocalDateTime lastTimeUpdate = LocalDateTime.now();
    private UUID roomUUID;
    private UUID whiteUUID;
    private UUID blackUUID;
    private long whiteMilliseconds = 1000 * 10;
    private long blackMilliseconds = 1000 * 10;
    private Color currentTurn = Color.WHITE;
    private boolean isDraw = false;

    //b - indicates black piece, w indicates white pieces, W and B indicates queens of their colour
    private char[][] board = {
            {'0','b','0','b','0','b','0','b'}, // 8
            {'b','0','b','0','b','0','b','0'}, // 7
            {'0','b','0','b','0','b','0','b'}, // 6
            {'0','0','0','0','0','0','0','0'}, // 5
            {'0','0','0','0','0','0','0','0'}, // 4
            {'w','0','w','0','w','0','w','0'}, // 3
            {'0','w','0','w','0','w','0','w'}, // 2
            {'w','0','w','0','w','0','w','0'}  // 1
          //  a   b   c   d   e   f   g   h
    };

    public synchronized long getWhiteMilliseconds() {
        updateTime();
        return whiteMilliseconds;
    }

    public synchronized long getBlackMilliseconds() {
        updateTime();
        return blackMilliseconds;
    }

    public synchronized Color getCurrentTurn() {
        return currentTurn;
    }

    public synchronized State getGameState() {
        if (isDraw) {
            return State.DRAW;
        }

        if (didEnemyLose('b', 'B', blackMilliseconds)) {
            return State.WHITE_WON;
        }

        if (didEnemyLose('w', 'W', whiteMilliseconds)) {
            return State.BLACK_WON;
        }

        return State.PLAYING;
    }

    /**
     * @param commands list of commands in format [a-h][1-8] where first is chosen piece.
     * @return true if validation passed, false otherwise
     */
    public synchronized boolean move (List<String> commands, Color movingPlayer) {
        int[] pieceCoords =
                convertIntoArrayCoordinates(convertCommandToNumberCoordinates(commands.get(0)));

        return true;
    }

    // Returns null on failed validation
    private int[] convertCommandToNumberCoordinates(String command) {
        if (command.length() != 2) {
            return null;
        }

        int secondCoordinate;
        try {
            secondCoordinate = Integer.parseInt(command.substring(1, 2));
        } catch (Exception e) {
            return null;
        }

        int[] result = {command.charAt(0) - 'a' + 1, secondCoordinate};
        if (result[0] > 8 || result[0] < 1 || result [1] > 8 || result[1] < 1) {
            return  null;
        }

        return result;
    }

    // Expects valid numbers
    private int[] convertIntoArrayCoordinates(int[] numberCoordinates) {
        if (numberCoordinates == null) {
            return null;
        }
        // b4 -> 2,4 -> 4, 1
        return new int[]{8 - numberCoordinates[1], numberCoordinates[0] - 1};
    }

    private boolean didEnemyLose(char oppositePieceSymbol1, char oppositePieceSymbol2, long oppositePieceMillis) {
        boolean enemyLost = true;
        for (char[] row : board) {
            for (char cell : row) {
                if (cell ==  oppositePieceSymbol1 || cell == oppositePieceSymbol2) {
                    enemyLost = false;
                    break;
                }
            }
        }

        if (oppositePieceMillis <= 0) {
            enemyLost = true;
        }

        return enemyLost;
    }

    // SHOULD BE ALWAYS CALLED BEFORE TURN CHANE!!!
    private void updateTime() {
        long millisPassed = Duration.between(lastTimeUpdate, LocalDateTime.now()).toMillis();
        if (currentTurn.equals(Color.WHITE)) {
            whiteMilliseconds -= millisPassed;
        } else {
            blackMilliseconds -= millisPassed;
        }

        // Don't store negative time
        blackMilliseconds = Math.max(blackMilliseconds, 0);
        whiteMilliseconds = Math.max(whiteMilliseconds, 0);
    }

    public enum Color {
        BLACK, WHITE
    }

    public enum State {
        PLAYING, DRAW, WHITE_WON, BLACK_WON
    }
}
