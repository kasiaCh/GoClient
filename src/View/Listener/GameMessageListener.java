package View.Listener;

/**
 * Created by Izabela on 2016-12-07.
 */
public interface GameMessageListener {

    void opponentJoined(String opponentLogin);

    void playerReceivedPermissionToMove();

    void playerMadeLegalMove();

    void playerMadeIllegalMoveKO();

    void playerMadeIllegalMoveSuicide();

    void playerMadeIllegalMoveOccupiedField();

    void opponentPassed();

    void opponentGaveUp();

    void updateBoard(String[][] updatedBoard);

    void updateCaptured(String capturedForWhite, String capturedForBlack);

    void waitForOpponentToMarkDeadStones();

    void markDeadStones();

    void showMarkedAsDead(String[][] markedAsDead);

    void botAcceptedDeadStones();

    void deadStonesAccepted();

    void deadStonesNotAccepted();

    void markArea();

    void showMarkedArea(String[][] markedArea);

    void showFinalMarkedArea(String[][] markedArea);

    void showSingleSerwerMarkedArea(String[][] markedArea);

    void serwerMarkedArea();

    void areaAccepted();

    void areaNotAccepted();

    void opponentResumed();
}
