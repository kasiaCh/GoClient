package Presenter;

import Usecase.Game;
import View.GameFrame;
import View.ImageIcon.BlackFieldsImg;
import View.ImageIcon.FreeFieldsImg;
import View.ImageIcon.WhiteFieldsImg;
import View.Listener.GameMessageListener;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Enum Singleton class.
 * It Mediates between View(GUI) and Usecase(Game - concrete communication with server class).
 */
public enum MyPresenter {

    /**
     *  Instance of MyPresenter.
     */
    INSTANCE;

    GameMessageListener listener;

    /**
     *  Field of Game class, needed to communication with server (to use it's methods).
     */
    private Game game;

    private MyPresenter(){

        try {
            game = new Game();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sends player's login to server.
     * @param login - player's login to send
     */
    public void sendLogin(String login){
        game.sendResponse(login);
    }

    /**
     * Sends info to server, that BOT was chosen.
     */
    public void botChosen(){
        game.sendResponse("BOT");
    }

    /**
     * Sends info to server, that HUMAN was chosen.
     */
    public void humanChosen(){
        game.sendResponse("HUMAN");
    }

    /**
     * Sends info to server, that NEW ROOM was chosen.
     */
    public void newRoomChosen(){
        game.sendResponse("NEW");
    }

    /**
     * Sends info to server, that EXISTING ROOM was chosen.
     */
    public void existingRoomChosen(){
        game.sendResponse("EXISTING");
    }

    /**
     * Sends index of chosen room to server.
     * @param index - index of chosen room
     */
    public void indexOfRoomChosen(String index){
        game.sendResponse(index);
    }

    /**
     * Sends move made by player to server.
     * @param move - player's turn
     */
    public void moveMade(String move){
        game.sendResponse(move);
    }

    public void playerPassed(){
        game.sendResponse("PASS");
    }

    public void sendInfo(String info) {
        game.sendResponse(info);
    }

    public void sendFieldsMarkedAsDead(boolean[][] fieldsMarkedAsDead){
        ArrayList<String> marked = new ArrayList<>();

        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                marked.add(Boolean.toString(fieldsMarkedAsDead[i][j]));
            }
        }
        game.sendResponse("MARKED_AS_DEAD: " + marked);
    }

    public void sendFieldsMarkedAsArea(boolean[][] fieldsMarkedAsArea){
        ArrayList<String> marked = new ArrayList<>();

        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                marked.add(Boolean.toString(fieldsMarkedAsArea[i][j]));
            }
        }
        game.sendResponse("FINAL_MARKED_AS_AREA: " + marked);
    }

    public void sendUpdatedBoard(GameFrame.FieldLabel[][] updatedBoard){
        ArrayList<String> updatedBoardToString = new ArrayList<>();
        for(int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (updatedBoard[i][j].getIcon() instanceof BlackFieldsImg)
                    updatedBoardToString.add("BLACK");
                else if (updatedBoard[i][j].getIcon() instanceof WhiteFieldsImg)
                    updatedBoardToString.add("WHITE");
                else if (updatedBoard[i][j].getIcon() instanceof FreeFieldsImg)
                    updatedBoardToString.add("FREE");
            }
        }
        game.sendResponse("MARK_AREA " + updatedBoardToString);
    }

    /**
     * Receives info about indexes of existing rooms from server.
     * @return - info about indexes of existing rooms
     */
    public ArrayList<String> receiveListOfRoomsInfo() {

        ArrayList<String> roomsInfo = new ArrayList<>();
        String response = game.receiveResponse();

        String list = response.replace("[","");
        String readyList = list.replace("]","");

        if (!readyList.equals("")) {

            for (String info : readyList.split(", ")) {
                roomsInfo.add(info);
            }
        }
        return roomsInfo;

    }

    public String[][] receiveUpdatedBoard(String response){
        String[][] updatedBoard = new String[19][19];
        String[] responseBoard;
        String response2;

        if(response.startsWith("UPDATED_BOARD")) {
            String response1 = response.replace("UPDATED_BOARD [", "");
            response2 = response1.replace("]", "");
        }
        else if (response.startsWith("MARKED_AS_DEAD: ")) {
            String response1 = response.replace("MARKED_AS_DEAD: [", "");
            response2 = response1.replace("]", "");
        } else if (response.startsWith("MARKED_AREA: ")){
            String response1 = response.replace("MARKED_AREA: [","");
            response2 = response1.replace("]","");
        } else if (response.startsWith("FINAL_MARKED_AS_AREA: ")){
            String response1 = response.replace("FINAL_MARKED_AS_AREA: [","");
            response2 = response1.replace("]","");
        } else {
            String response1 = response.replace("SINGLE_MARKED_AREA: [","");
            response2 = response1.replace("]","");
        }

        responseBoard = response2.split(", ");

        if(!response2.equals("")){
            for(int i = 0; i < 19; i++){
                for(int j = 0; j < 19; j++){
                    updatedBoard[i][j] = responseBoard[(i*18)+i+j];
                }
            }
        }

        return updatedBoard;
    }


    /**
     * Receives response from server during game.
     * @return response from server.
     */
    public void receiveGameMessage (GameMessageListener listener){

        this.listener  = listener;

        new Thread(() -> {

            while (true) {
                String response = game.receiveResponse();
                if(response.startsWith("LOGIN: ")){
                    String login = response.replace("LOGIN: ", "");
                    listener.opponentJoined(login);
                } else if(response.equals("MAKE_TURN")) {
                    listener.playerReceivedPermissionToMove();
                } else if (response.equals("LEGAL_MOVE")) {
                    listener.playerMadeLegalMove();
                } else if (response.equals("KO")) {
                    listener.playerMadeIllegalMoveKO();
                } else if (response.equals("SUICIDE")) {
                    listener.playerMadeIllegalMoveSuicide();
                } else if (response.equals("OCCUPIED_FIELD")) {
                    listener.playerMadeIllegalMoveOccupiedField();
                } else if (response.equals("OPPONENT_PASSED")) {
                    listener.opponentPassed();
                } else if (response.equals("OPPONENT_GAVE_UP")) {
                    listener.opponentGaveUp();
                } else if (response.startsWith("UPDATED_BOARD ")) {
                    String[][] updatedBoard = receiveUpdatedBoard(response);
                    listener.updateBoard(updatedBoard);
                } else if(response.startsWith("CAPTURED")){
                    String string = response.replace("CAPTURED ", "");
                    String[] captured = string.split(",");
                    listener.updateCaptured(captured[0], captured[1]);
                } else if (response.equals("WAIT_FOR_MARKING_DEAD")){
                    listener.waitForOpponentToMarkDeadStones();
                } else if (response.equals("MARK_DEAD")){
                    listener.markDeadStones();
                } else if (response.startsWith("MARKED_AS_DEAD: ")) {
                    String[][] markedAsDead = receiveUpdatedBoard(response);
                    listener.showMarkedAsDead(markedAsDead);
                } else if (response.equals("BOT_ACCEPTED_DEAD_STONES")){
                    listener.botAcceptedDeadStones();
                } else if (response.equals("DEAD_STONES_ACCEPTED")){
                    listener.deadStonesAccepted();
                } else if (response.equals("DEAD_STONES_NOT_ACCEPTED")){
                    listener.deadStonesNotAccepted();
                } else if (response.startsWith("MARK_AREA ")){
                    listener.markArea();
                } else if (response.startsWith("MARKED_AREA: ")){
                    String[][] markedArea = receiveUpdatedBoard(response);
                    listener.showMarkedArea(markedArea);
                } else if (response.startsWith("FINAL_MARKED_AS_AREA: ")){
                    String[][] markedArea = receiveUpdatedBoard(response);
                    listener.showFinalMarkedArea(markedArea);
                } else if (response.startsWith("SINGLE_MARKED_AREA: ")) {
                    String[][] markedArea = receiveUpdatedBoard(response);
                    listener.showSingleSerwerMarkedArea(markedArea);
                } else if (response.equals("SERWER_MARKED_AREA")){
                    listener.serwerMarkedArea();
                } else if (response.equals("AREA_ACCEPTED")){
                    listener.areaAccepted();
                } else if (response.equals("AREA_NOT_ACCEPTED")){
                    listener.areaNotAccepted();
                } else if(response.equals("RESUME")){
                    listener.opponentResumed();
                }
                else return;
            }

        }).start();
    }
}