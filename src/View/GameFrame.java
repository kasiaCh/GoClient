package View;

import Presenter.MyPresenter;
import View.ImageIcon.*;
import View.Listener.GameMessageListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class of frame with game board.
 */


public class GameFrame extends JFrame implements GameMessageListener{

    /**
     * Table with images of free fields.
     */
    private FreeFieldsImg[][] freeFieldsImg = new FreeFieldsImg[19][19];

    /**
     * Table with images of fields occupied by black stones.
     */
    private BlackFieldsImg[][] blackFieldsImg = new BlackFieldsImg[19][19];

    /**
     * Table with images of fields occupied by white stones.
     */
    private WhiteFieldsImg[][] whiteFieldsImg = new WhiteFieldsImg[19][19];

    /**
     * Table with images of white fields marked as dead.
     */
    private DeadWhiteFieldsImg[][] deadWhiteFieldsImg = new DeadWhiteFieldsImg[19][19];

    /**
     * Table with images of black fields marked as dead.
     */
    private DeadBlackFieldsImg[][] deadBlackFieldsImg = new DeadBlackFieldsImg[19][19];

    /**
     * Table with images of marked fields as white player's area.
     */
    private WhiteMarkedFieldsImg[][] whiteMarkedFieldsImg = new WhiteMarkedFieldsImg[19][19];

    /**
     * Table with images of marked fields as black player's area.
     */
    private BlackMarkedFieldsImg[][] blackMarkedFieldsImg = new BlackMarkedFieldsImg[19][19];

    /**
     * Table with images of entered fields.
     */
    private EnteredFieldsImg[][] enteredFieldsImg = new EnteredFieldsImg[19][19];

    /**
     * Table with images of entered white fields during marking dead fields.
     */
    private EnteredDeadWhiteFieldsImg[][] enteredDeadWhiteFieldsImg = new EnteredDeadWhiteFieldsImg[19][19];

    /**
     * Table with images of entered black fields during marking dead fields.
     */
    private EnteredDeadBlackFieldsImg[][] enteredDeadBlackFieldsImg = new EnteredDeadBlackFieldsImg[19][19];

    /**
     * Table with images of entered white fields during marking area.
     */
    private EnteredMarkedWhiteFieldsImg[][] enteredMarkedWhiteFieldsImg = new EnteredMarkedWhiteFieldsImg[19][19];

    /**
     * Table with images of entered black fields during marking area.
     */
    private EnteredMarkedBlackFieldsImg[][] enteredMarkedBlackFieldsImg = new EnteredMarkedBlackFieldsImg[19][19];

    /**
     * Table with labels treated like fields.
     */
    private FieldLabel[][] fields = new FieldLabel[19][19];

    /**
     * Table with fields marked as dead which is going to be send to the server.
     */
    private boolean[][] markedAsDead = new boolean[19][19];

    /**
     * Table with fields marked as player's area.
     */
    private boolean[][] markedArea = new boolean[19][19];

    /**
     * State of fields which is going to be restored after clicking "Resume" button.
     */
    private ImageIcon[][] fieldsAfterResume = new ImageIcon[19][19];

    /**
     * Panel with game board.
     */
    private JPanel panelForBoard;

    /**
     * Left panel with info about player.
     */
    private JPanel leftPanel;

    /**
     * Right panel with info about player.
     */
    private JPanel rightPanel;

    /**
     * Panel with buttons.
     */
    private JPanel bottomPanel;

    /**
     * Label with info for me.
     */


    private JLabel infoLabel;

    /**
     * Label with opponent's login.
     */
    private JLabel opponentLoginLabel;

    /**
     * TextField with number of my captured.
     */
    JTextField myNumberOfCaptured;

    /**
     * TextField with number of opponent's captured.
     */
    JTextField opponentNumberOfCaptured;
    /**
     * Pass button.
     */

    private JButton passButton;

    /**
     * Button which accepts suggested dead stones.
     */

    private JButton acceptButton;

    /**
     * Button which doesn't accept suggested dead stones.
     */
    private JButton notAcceptButton;

    /**
     * Suggest button.
     */
    private JButton suggestButton;

    /**
     * Resume game button.
     */
    private JButton resumeButton;

    /**
     * Player color.
     */
    private String playerColor;

    /**
     * Statement whether this is the player's turn now.
     */
    private Boolean myTurn = false;

    /**
     * If marking stones is enabled for the player.
     */
    private Boolean ifMarkDeadStones = false;

    /**
     * If this player accepted his opponent suggestion.
     */
    private Boolean ifAccepted = false;

    /**
     * If opponent accepted this player's suggestion.
     */
    private Boolean ifOpponentAccepted = false;

    /**
     * If marking area is enabled for the player.
     */
    private Boolean ifMarkArea = false;

    private Boolean ifOpponentPassed = false;

    private Boolean ifTimeToAcceptDeadFields = false;

    private Boolean ifTimeToAcceptArea = false;

    /**
     * My number of captured which is going to be after resume.
     */
    private String myCapturedAfterResume;

    /**
     * Opponent's number of captured which is going to be after resume.
     */
    private String opponentCapturedAfterResume;

    private int myDeadStones;

    private int opponentDeadStones;


    public GameFrame()  {

        makeListsOfImages();
        makeBoard();
        makeSidePanels();
        makeBottomPanel();
        makeFinalFrame();


    }

    /**
     * Creates game board.
     */
    private void makeBoard(){
        panelForBoard = new JPanel();
        panelForBoard.setLayout(new GridLayout(19,19));

        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                fields[j][i] = new FieldLabel();
                fields[j][i].setIcon(freeFieldsImg[j][i]);
                fields[j][i].setClickedX(j);
                fields[j][i].setClickedY(i);
                fields[j][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                            if (myTurn) {
                                MyPresenter myPresenter = MyPresenter.INSTANCE;
                                FieldLabel label = (FieldLabel) e.getSource();
                                myPresenter.moveMade("TURN " + label.getClickedX() + " " + label.getClickedY());
                            } else if(ifMarkDeadStones){
                                FieldLabel label = (FieldLabel) e.getSource();
                                if(label.getIcon() instanceof EnteredDeadBlackFieldsImg && playerColor.equals("WHITE")) {
                                    fields[label.getClickedX()][label.getClickedY()].setIcon(deadBlackFieldsImg[label.getClickedX()][label.getClickedY()]);
                                    fields[label.getClickedX()][label.getClickedY()].revalidate();
                                    fields[label.getClickedX()][label.getClickedY()].repaint();
                                    markedAsDead[label.getClickedX()][label.getClickedY()] = true;
                                }
                                else if(label.getIcon() instanceof EnteredDeadWhiteFieldsImg && playerColor.equals("BLACK")){
                                    fields[label.getClickedX()][label.getClickedY()].setIcon(deadWhiteFieldsImg[label.getClickedX()][label.getClickedY()]);
                                    fields[label.getClickedX()][label.getClickedY()].revalidate();
                                    fields[label.getClickedX()][label.getClickedY()].repaint();
                                    markedAsDead[label.getClickedX()][label.getClickedY()] = true;
                                }
                            } else if(ifMarkArea){
                                MyPresenter myPresenter = MyPresenter.INSTANCE;
                                FieldLabel label = (FieldLabel) e.getSource();
                                if(label.getIcon() instanceof EnteredMarkedBlackFieldsImg || label.getIcon() instanceof EnteredMarkedWhiteFieldsImg) {
                                    myPresenter.sendInfo("AREA: " + label.getClickedX() + " " + label.getClickedY());
                                }
                            }
                    }
                });

                fields[j][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        super.mouseEntered(e);
                        FieldLabel label = (FieldLabel) e.getSource();
                        int x = label.getClickedX();
                        int y = label.getClickedY();
                        if(myTurn){
                            if (fields[x][y].getIcon() instanceof FreeFieldsImg){
                                fields[x][y].setIcon(enteredFieldsImg[x][y]);
                                fields[x][y].revalidate();
                                fields[x][y].repaint();
                            }
                        } else if (ifMarkDeadStones) {
                            if (fields[x][y].getIcon() instanceof BlackFieldsImg && playerColor.equals("WHITE")){
                                fields[x][y].setIcon(enteredDeadBlackFieldsImg[x][y]);
                                fields[x][y].revalidate();
                                fields[x][y].repaint();
                            } else if (fields[x][y].getIcon() instanceof WhiteFieldsImg && playerColor.equals("BLACK")){
                                fields[x][y].setIcon(enteredDeadWhiteFieldsImg[x][y]);
                                fields[x][y].revalidate();
                                fields[x][y].repaint();
                            }
                        } else if (ifMarkArea) {
                            if (fields[x][y].getIcon() instanceof FreeFieldsImg && playerColor.equals("WHITE")){
                                fields[x][y].setIcon(enteredMarkedWhiteFieldsImg[x][y]);
                                fields[x][y].revalidate();
                                fields[x][y].repaint();
                            } else if (fields[x][y].getIcon() instanceof FreeFieldsImg && playerColor.equals("BLACK")){
                                fields[x][y].setIcon(enteredMarkedBlackFieldsImg[x][y]);
                                fields[x][y].revalidate();
                                fields[x][y].repaint();
                            }
                        }
                    }
                });
                fields[j][i].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseExited(MouseEvent e) {
                        super.mouseExited(e);
                        FieldLabel label = (FieldLabel) e.getSource();
                        int x = label.getClickedX();
                        int y = label.getClickedY();
                        if(myTurn){
                            if(fields[x][y].getIcon() instanceof EnteredFieldsImg){
                                fields[x][y].setIcon(freeFieldsImg[x][y]);
                                fields[x][y].revalidate();
                                fields[x][y].repaint();
                            }
                        } else if (ifMarkDeadStones){
                            if (fields[x][y].getIcon() instanceof EnteredDeadBlackFieldsImg){
                                fields[x][y].setIcon(blackFieldsImg[x][y]);
                                fields[x][y].revalidate();
                                fields[x][y].repaint();
                            } else if (fields[x][y].getIcon() instanceof EnteredDeadWhiteFieldsImg){
                                fields[x][y].setIcon(whiteFieldsImg[x][y]);
                                fields[x][y].revalidate();
                                fields[x][y].repaint();
                            }
                        } else if (ifMarkArea){
                            if (fields[x][y].getIcon() instanceof EnteredMarkedBlackFieldsImg || fields[x][y].getIcon() instanceof EnteredMarkedWhiteFieldsImg){
                                fields[x][y].setIcon(freeFieldsImg[x][y]);
                                fields[x][y].revalidate();
                                fields[x][y].repaint();
                            }
                        }
                    }
                });
                panelForBoard.add(fields[j][i]);
            }
        }

    }

    private void makeSidePanels(){
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(10,1));
        rightPanel.setLayout(new GridLayout(10,1));

        JLabel myLoginLabel = new JLabel("Me");
        opponentLoginLabel = new JLabel();
        JLabel myCapturedLabel = new JLabel("Captured:");
        JLabel opponentCapturedLabel = new JLabel("Captured:");
        myNumberOfCaptured = new JTextField("0");
        opponentNumberOfCaptured = new JTextField("0");
        myNumberOfCaptured.setEditable(false);
        opponentNumberOfCaptured.setEditable(false);
        infoLabel = new JLabel();

        JPanel acceptDeadStonesPanel = new JPanel();
        acceptButton = new JButton("YES");
        notAcceptButton = new JButton("NO");
        acceptDeadStonesPanel.add(acceptButton);
        acceptDeadStonesPanel.add(notAcceptButton);
        acceptButton.setVisible(false);
        notAcceptButton.setVisible(false);

        acceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ifTimeToAcceptDeadFields) {
                    ifAccepted = true;
                    MyPresenter myPresenter = MyPresenter.INSTANCE;
                    infoLabel.setText("Mark dead stones!");
                    deleteAcceptedDeadStones();
                    myPresenter.sendInfo("DEAD_STONES_ACCEPTED");
                    if (ifAccepted && ifOpponentAccepted){
                        ifAccepted = false;
                        ifOpponentAccepted = false;
                    }
                    ifTimeToAcceptDeadFields = false;
                } else if(ifTimeToAcceptArea){
                    ifAccepted = true;
                    ifMarkArea = true;
                    suggestButton.setEnabled(true);
                    MyPresenter myPresenter = MyPresenter.INSTANCE;
                    infoLabel.setText("Mark your area!");
                    myPresenter.sendInfo("AREA_ACCEPTED");
                    if(ifOpponentAccepted && ifAccepted){
                        infoLabel.setText("End of the game!");
                        checkScore();
                        ifAccepted = false;
                        ifOpponentAccepted = false;
                    }
                    ifTimeToAcceptArea = false;
                }
                resumeButton.setVisible(false);
                acceptButton.setVisible(false);
                notAcceptButton.setVisible(false);

            }
        });

        notAcceptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(ifTimeToAcceptDeadFields) {
                    MyPresenter myPresenter = MyPresenter.INSTANCE;
                    myPresenter.sendInfo("DEAD_STONES_NOT_ACCEPTED");
                    deleteNotAcceptedDeadStones();
                    ifTimeToAcceptDeadFields = false;
                } else if(ifTimeToAcceptArea){
                    MyPresenter myPresenter = MyPresenter.INSTANCE;
                    myPresenter.sendInfo("AREA_NOT_ACCEPTED");
                    deleteOpponentNotAcceptedArea();
                    ifTimeToAcceptArea = false;
                }
                resumeButton.setVisible(false);
                notAcceptButton.setEnabled(false);
                acceptButton.setEnabled(false);
            }
        });

        JLabel myEmptyLabel = new JLabel("                                                                         ");
        JLabel opponentEmptyLabel = new JLabel("                                                                         ");


        leftPanel.add(opponentLoginLabel);
        leftPanel.add(opponentCapturedLabel);
        leftPanel.add(opponentNumberOfCaptured);
        leftPanel.add(opponentEmptyLabel);

        rightPanel.add(myLoginLabel);
        rightPanel.add(myCapturedLabel);
        rightPanel.add(myNumberOfCaptured);
        rightPanel.add(infoLabel);
        rightPanel.add(acceptDeadStonesPanel);
        rightPanel.add(myEmptyLabel);

    }

    private void makeBottomPanel(){
        bottomPanel = new JPanel();

        passButton = new JButton("PASS");
        suggestButton = new JButton("SUGGEST");
        resumeButton = new JButton("RESUME");

        passButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.playerPassed();
                passButton.setEnabled(false);
                infoLabel.setText("Opponent's turn!");
                myTurn=false;
                restorePresentFields();
                myCapturedAfterResume = myNumberOfCaptured.getText();
                opponentCapturedAfterResume = opponentNumberOfCaptured.getText();

            }
        });

        suggestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ifMarkDeadStones) {
                    MyPresenter myPresenter = MyPresenter.INSTANCE;
                    myPresenter.sendFieldsMarkedAsDead(markedAsDead);
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            markedAsDead[i][j] = false;
                        }
                    }
                    ifMarkDeadStones = false;
                } else if (ifMarkArea){
                    MyPresenter myPresenter = MyPresenter.INSTANCE;
                    myPresenter.sendFieldsMarkedAsArea(markedArea);
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            markedArea[i][j] = false;
                        }
                    }
                    ifMarkArea = false;
                }
                infoLabel.setText("<html>Wait for your opponent<br/>to accept it.</html>.");
                suggestButton.setEnabled(false);
            }
        });

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.sendInfo("RESUME");

                myTurn = false;
                ifOpponentPassed = false;
                ifMarkDeadStones = false;
                ifMarkArea = false;
                resumeButton.setVisible(false);
                suggestButton.setVisible(false);
                acceptButton.setVisible(false);
                notAcceptButton.setVisible(false);
                infoLabel.setText("Opponent's turn!");
                showFieldsAfterResume();
                myNumberOfCaptured.setText(myCapturedAfterResume);
                opponentNumberOfCaptured.setText(opponentCapturedAfterResume);

            }
        });

        passButton.setEnabled(false);
        suggestButton.setVisible(false);
        resumeButton.setVisible(false);

        bottomPanel.add(passButton);
        bottomPanel.add(suggestButton);
        bottomPanel.add(resumeButton);
    }

    public void waitingForOpponent(){
        playerColor = "WHITE";
        opponentLoginLabel.setText("-");
        infoLabel.setText("Waiting for the opponent...");
    }

    public void opponentJoined(String login){
        opponentLoginLabel.setText(login);
        infoLabel.setText("Opponent's turn!");
        JOptionPane.showMessageDialog(null, "Opponent joined. Let's start the game!");
    }

    public void joinToRoom(String initiatorLogin){
        playerColor = "BLACK";
        opponentLoginLabel.setText(initiatorLogin);
        infoLabel.setText("Your turn!");
        passButton.setEnabled(true);

    }

    /**
     * Makes 3 Tables:
     * 1) images of free fields
     * 2) images of fields occupied by white stones
     * 3) images of fields occupied by black stones.
     */
    private void makeListsOfImages(){
        FreeFieldsImg leftSide = new FreeFieldsImg("img/boklewy.png");
        WhiteFieldsImg whiteLeftSide = new WhiteFieldsImg("img/boklewyBiały.png");
        BlackFieldsImg blackLeftSide = new BlackFieldsImg("img/boklewyCzarny.png");
        FreeFieldsImg rightSide = new FreeFieldsImg("img/bokprawy.png");
        WhiteFieldsImg whiteRightSide = new WhiteFieldsImg("img/bokprawyBiały.png");
        BlackFieldsImg blackRightSide = new BlackFieldsImg("img/bokprawyCzarny.png");
        FreeFieldsImg bottom = new FreeFieldsImg("img/dół.png");
        WhiteFieldsImg whiteBottom = new WhiteFieldsImg("img/dółBiały.png");
        BlackFieldsImg blackBottom = new BlackFieldsImg("img/dółCzarny.png");
        FreeFieldsImg top = new FreeFieldsImg("img/góra.png");
        WhiteFieldsImg whiteTop = new WhiteFieldsImg("img/góraBiała.png");
        BlackFieldsImg blackTop = new BlackFieldsImg("img/góraCzarna.png");
        FreeFieldsImg bottomLeftCorner = new FreeFieldsImg("img/lewydolny.png");
        WhiteFieldsImg whiteBottomLeftCorner = new WhiteFieldsImg("img/lewydolnyBiały.png");
        BlackFieldsImg blackBottomLeftCorner = new BlackFieldsImg("img/lewydolnyCzarny.png");
        FreeFieldsImg topLeftCorner = new FreeFieldsImg("img/lewygórny.png");
        WhiteFieldsImg whiteTopLeftCorner = new WhiteFieldsImg("img/lewygórnyBiały.png");
        BlackFieldsImg blackTopLeftCorner = new BlackFieldsImg("img/lewygórnyCzarny.png");
        FreeFieldsImg bottomRightCorner = new FreeFieldsImg("img/prawydolny.png");
        WhiteFieldsImg whiteBottomRightCorner = new WhiteFieldsImg("img/prawydolnyBiały.png");
        BlackFieldsImg blackBottomRightCorner = new BlackFieldsImg("img/prawydolnyCzarny.png");
        FreeFieldsImg topRightCorner = new FreeFieldsImg("img/prawygórny.png");
        WhiteFieldsImg whiteTopRightCorner = new WhiteFieldsImg("img/prawygórnyBiały.png");
        BlackFieldsImg blackTopRightCorner = new BlackFieldsImg("img/prawygórnyCzarny.png");
        FreeFieldsImg center = new FreeFieldsImg("img/środek.png");
        WhiteFieldsImg whiteCenter = new WhiteFieldsImg("img/środekBiały.png");
        BlackFieldsImg blackCenter= new BlackFieldsImg("img/środekCzarny.png");
        DeadWhiteFieldsImg deadWhiteTopLeftCorner = new DeadWhiteFieldsImg ("img/lewygórnyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackTopLeftCorner = new DeadBlackFieldsImg("img/lewygórnyCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteTopRightCorner = new DeadWhiteFieldsImg("img/prawygórnyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackTopRightCorner = new DeadBlackFieldsImg("img/prawygórnyCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteBottomLeftCorner = new DeadWhiteFieldsImg("img/lewydolnyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackBottomLeftCorner = new DeadBlackFieldsImg("img/lewydolnyCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteBottomRightCorner = new DeadWhiteFieldsImg("img/prawydolnyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackBottomRightCorner = new DeadBlackFieldsImg("img/prawydolnyCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteTop = new DeadWhiteFieldsImg("img/góraBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackTop = new DeadBlackFieldsImg("img/góraCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteBottom = new DeadWhiteFieldsImg("img/dółBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackBottom = new DeadBlackFieldsImg("img/dółCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteCenter = new DeadWhiteFieldsImg("img/środekBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackCenter = new DeadBlackFieldsImg("img/środekCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteLeftSide = new DeadWhiteFieldsImg("img/boklewyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackLeftSide = new DeadBlackFieldsImg("img/boklewyCzarnyMartwy.png");
        DeadWhiteFieldsImg deadWhiteRightSide = new DeadWhiteFieldsImg("img/bokprawyBiałyMartwy.png");
        DeadBlackFieldsImg deadBlackRightSide = new DeadBlackFieldsImg("img/bokprawyCzarnyMartwy.png");
        WhiteMarkedFieldsImg whiteMarkedLeftSide = new WhiteMarkedFieldsImg("img/boklewykolorbiały.png");
        WhiteMarkedFieldsImg whiteMarkedRightSide = new WhiteMarkedFieldsImg("img/bokprawykolorbiały.png");
        WhiteMarkedFieldsImg whiteMarkedBottom = new WhiteMarkedFieldsImg("img/dółkolorbiały.png");
        WhiteMarkedFieldsImg whiteMarkedTop = new WhiteMarkedFieldsImg("img/górakolorbiały.png");
        WhiteMarkedFieldsImg whiteMarkedBottomLeftCorner = new WhiteMarkedFieldsImg("img/lewydolnykolorbiały.png");
        WhiteMarkedFieldsImg whiteMarkedTopLeftCorner = new WhiteMarkedFieldsImg("img/lewygórnykolorbiały.png");
        WhiteMarkedFieldsImg whiteMarkedBottomRightCorner = new WhiteMarkedFieldsImg("img/prawydolnykolorbiały.png");
        WhiteMarkedFieldsImg whiteMarkedTopRightCorner = new WhiteMarkedFieldsImg("img/prawygórnykolorbiały.png");
        WhiteMarkedFieldsImg whiteMarkedCenter = new WhiteMarkedFieldsImg("img/środekkolorbiały.png");
        BlackMarkedFieldsImg blackMarkedLeftSide = new BlackMarkedFieldsImg("img/boklewykolorczarny.png");
        BlackMarkedFieldsImg blackMarkedRightSide = new BlackMarkedFieldsImg("img/bokprawykolorczarny.png");
        BlackMarkedFieldsImg blackMarkedBottom = new BlackMarkedFieldsImg("img/dółkolorczarny.png");
        BlackMarkedFieldsImg blackMarkedTop = new BlackMarkedFieldsImg("img/górakolorczarny.png");
        BlackMarkedFieldsImg blackMarkedBottomLeftCorner = new BlackMarkedFieldsImg("img/lewydolnykolorczarny.png");
        BlackMarkedFieldsImg blackMarkedTopLeftCorner = new BlackMarkedFieldsImg("img/lewygórnykolorczarny.png");
        BlackMarkedFieldsImg blackMarkedBottomRightCorner = new BlackMarkedFieldsImg("img/prawydolnykolorczarny.png");
        BlackMarkedFieldsImg blackMarkedTopRightCorner = new BlackMarkedFieldsImg("img/prawygórnykolorczarny.png");
        BlackMarkedFieldsImg blackMarkedCenter = new BlackMarkedFieldsImg("img/środekkolorczarny.png");
        EnteredFieldsImg enteredLeftSide = new EnteredFieldsImg("img/boklewySzary.png");
        EnteredFieldsImg enteredRightSide = new EnteredFieldsImg("img/bokprawySzary.png");
        EnteredFieldsImg enteredBottom = new EnteredFieldsImg("img/dółSzary.png");
        EnteredFieldsImg enteredTop = new EnteredFieldsImg("img/góraSzary.png");
        EnteredFieldsImg enteredBottomLeftCorner = new EnteredFieldsImg("img/lewydolnySzary.png");
        EnteredFieldsImg enteredTopLeftCorner = new EnteredFieldsImg("img/lewygórnySzary.png");
        EnteredFieldsImg enteredBottomRightCorner = new EnteredFieldsImg("img/prawydolnySzary.png");
        EnteredFieldsImg enteredTopRightCorner = new EnteredFieldsImg("img/prawygórnySzary.png");
        EnteredFieldsImg enteredCenter = new EnteredFieldsImg("img/środekSzary.png");
        EnteredDeadWhiteFieldsImg enteredDeadWhiteLeftSide = new EnteredDeadWhiteFieldsImg("img/boklewybiałymartwy.png");
        EnteredDeadWhiteFieldsImg enteredDeadWhiteRightSide = new EnteredDeadWhiteFieldsImg("img/bokprawybiałymartwy.png");
        EnteredDeadWhiteFieldsImg enteredDeadWhiteBottom = new EnteredDeadWhiteFieldsImg("img/dółbiałymartwy.png");
        EnteredDeadWhiteFieldsImg enteredDeadWhiteTop = new EnteredDeadWhiteFieldsImg("img/górabiałymartwy.png");
        EnteredDeadWhiteFieldsImg enteredDeadWhiteBottomLeftCorner = new EnteredDeadWhiteFieldsImg("img/lewydolnybiałymartwy.png");
        EnteredDeadWhiteFieldsImg enteredDeadWhiteBottomRightCorner = new EnteredDeadWhiteFieldsImg("img/prawydolnybiałymartwy.png");
        EnteredDeadWhiteFieldsImg enteredDeadWhiteTopLeftCorner = new EnteredDeadWhiteFieldsImg("img/lewygórnybiałymartwy.png");
        EnteredDeadWhiteFieldsImg enteredDeadWhiteTopRightCorner = new EnteredDeadWhiteFieldsImg("img/prawygórnybiałymartwy.png");
        EnteredDeadWhiteFieldsImg enteredDeadWhiteCenter = new EnteredDeadWhiteFieldsImg("img/środekbiałymartwy.png");
        EnteredDeadBlackFieldsImg enteredDeadBlackLeftSide = new EnteredDeadBlackFieldsImg("img/boklewyczarnymartwy.png");
        EnteredDeadBlackFieldsImg enteredDeadBlackRightSide = new EnteredDeadBlackFieldsImg("img/bokprawyczarnymartwy.png");
        EnteredDeadBlackFieldsImg enteredDeadBlackBottom = new EnteredDeadBlackFieldsImg("img/dółczarnymartwy.png");
        EnteredDeadBlackFieldsImg enteredDeadBlackTop = new EnteredDeadBlackFieldsImg("img/góraczarnymartwy.png");
        EnteredDeadBlackFieldsImg enteredDeadBlackBottomLeftCorner = new EnteredDeadBlackFieldsImg("img/lewydolnyczarnymartwy.png");
        EnteredDeadBlackFieldsImg enteredDeadBlackBottomRightCorner = new EnteredDeadBlackFieldsImg("img/prawydolnyczarnymartwy.png");
        EnteredDeadBlackFieldsImg enteredDeadBlackTopLeftCorner = new EnteredDeadBlackFieldsImg("img/lewygórnyczarnymartwy.png");
        EnteredDeadBlackFieldsImg enteredDeadBlackTopRightCorner = new EnteredDeadBlackFieldsImg("img/prawygórnyczarnymartwy.png");
        EnteredDeadBlackFieldsImg enteredDeadBlackCenter = new EnteredDeadBlackFieldsImg("img/środekczarnymartwy.png");
        EnteredMarkedWhiteFieldsImg enteredMarkedWhiteLeftSide = new EnteredMarkedWhiteFieldsImg("img/boklewykolorbiały.png");
        EnteredMarkedWhiteFieldsImg enteredMarkedWhiteRightSide = new EnteredMarkedWhiteFieldsImg("img/bokprawykolorbiały.png");
        EnteredMarkedWhiteFieldsImg enteredMarkedWhiteBottom = new EnteredMarkedWhiteFieldsImg("img/dółkolorbiały.png");
        EnteredMarkedWhiteFieldsImg enteredMarkedWhiteTop = new EnteredMarkedWhiteFieldsImg("img/górakolorbiały.png");
        EnteredMarkedWhiteFieldsImg enteredMarkedWhiteBottomLeftCorner = new EnteredMarkedWhiteFieldsImg("img/lewydolnykolorbiały.png");
        EnteredMarkedWhiteFieldsImg enteredMarkedWhiteBottomRightCorner = new EnteredMarkedWhiteFieldsImg("img/prawydolnykolorbiały.png");
        EnteredMarkedWhiteFieldsImg enteredMarkedWhiteTopLeftCorner = new EnteredMarkedWhiteFieldsImg("img/lewygórnykolorbiały.png");
        EnteredMarkedWhiteFieldsImg enteredMarkedWhiteTopRightCorner = new EnteredMarkedWhiteFieldsImg("img/prawygórnykolorbiały.png");
        EnteredMarkedWhiteFieldsImg enteredMarkedWhiteCenter = new EnteredMarkedWhiteFieldsImg("img/środekkolorbiały.png");
        EnteredMarkedBlackFieldsImg enteredMarkedBlackLeftSide = new EnteredMarkedBlackFieldsImg("img/boklewykolorczarny.png");
        EnteredMarkedBlackFieldsImg enteredMarkedBlackRightSide = new EnteredMarkedBlackFieldsImg("img/bokprawykolorczarny.png");
        EnteredMarkedBlackFieldsImg enteredMarkedBlackBottom = new EnteredMarkedBlackFieldsImg("img/dółkolorczarny.png");
        EnteredMarkedBlackFieldsImg enteredMarkedBlackTop = new EnteredMarkedBlackFieldsImg("img/górakolorczarny.png");
        EnteredMarkedBlackFieldsImg enteredMarkedBlackBottomLeftCorner = new EnteredMarkedBlackFieldsImg("img/lewydolnykolorczarny.png");
        EnteredMarkedBlackFieldsImg enteredMarkedBlackBottomRightCorner = new EnteredMarkedBlackFieldsImg("img/prawydolnykolorczarny.png");
        EnteredMarkedBlackFieldsImg enteredMarkedBlackTopLeftCorner = new EnteredMarkedBlackFieldsImg("img/lewygórnykolorczarny.png");
        EnteredMarkedBlackFieldsImg enteredMarkedBlackTopRightCorner = new EnteredMarkedBlackFieldsImg("img/prawygórnykolorczarny.png");
        EnteredMarkedBlackFieldsImg enteredMarkedBlackCenter = new EnteredMarkedBlackFieldsImg("img/środekkolorczarny.png");


        freeFieldsImg[0][0] = topLeftCorner;
        whiteFieldsImg[0][0] = whiteTopLeftCorner;
        blackFieldsImg[0][0] = blackTopLeftCorner;
        deadBlackFieldsImg[0][0] = deadBlackTopLeftCorner;
        deadWhiteFieldsImg[0][0] = deadWhiteTopLeftCorner;
        whiteMarkedFieldsImg[0][0] = whiteMarkedTopLeftCorner;
        blackMarkedFieldsImg[0][0] = blackMarkedTopLeftCorner;
        enteredFieldsImg[0][0] = enteredTopLeftCorner;
        enteredDeadBlackFieldsImg[0][0] = enteredDeadBlackTopLeftCorner;
        enteredDeadWhiteFieldsImg[0][0] = enteredDeadWhiteTopLeftCorner;
        enteredMarkedWhiteFieldsImg[0][0] = enteredMarkedWhiteTopLeftCorner;
        enteredMarkedBlackFieldsImg[0][0] = enteredMarkedBlackTopLeftCorner;


        freeFieldsImg[18][0] = topRightCorner;
        whiteFieldsImg[18][0] = whiteTopRightCorner;
        blackFieldsImg[18][0] = blackTopRightCorner;
        deadBlackFieldsImg[18][0] = deadBlackTopRightCorner;
        deadWhiteFieldsImg[18][0] = deadWhiteTopRightCorner;
        whiteMarkedFieldsImg[18][0] = whiteMarkedTopRightCorner;
        blackMarkedFieldsImg[18][0] = blackMarkedTopRightCorner;
        enteredFieldsImg[18][0] = enteredTopRightCorner;
        enteredDeadBlackFieldsImg[18][0] = enteredDeadBlackTopRightCorner;
        enteredDeadWhiteFieldsImg[18][0] = enteredDeadWhiteTopRightCorner;
        enteredMarkedWhiteFieldsImg[18][0] = enteredMarkedWhiteTopRightCorner;
        enteredMarkedBlackFieldsImg[18][0] = enteredMarkedBlackTopRightCorner;

        freeFieldsImg[0][18] = bottomLeftCorner;
        whiteFieldsImg[0][18] = whiteBottomLeftCorner;
        blackFieldsImg[0][18] = blackBottomLeftCorner;
        deadBlackFieldsImg[0][18] = deadBlackBottomLeftCorner;
        deadWhiteFieldsImg[0][18] = deadWhiteBottomLeftCorner;
        whiteMarkedFieldsImg[0][18] = whiteMarkedBottomLeftCorner;
        blackMarkedFieldsImg[0][18] = blackMarkedBottomLeftCorner;
        enteredFieldsImg[0][18] = enteredBottomLeftCorner;
        enteredDeadBlackFieldsImg[0][18] = enteredDeadBlackBottomLeftCorner;
        enteredDeadWhiteFieldsImg[0][18] = enteredDeadWhiteBottomLeftCorner;
        enteredMarkedWhiteFieldsImg[0][18] = enteredMarkedWhiteBottomLeftCorner;
        enteredMarkedBlackFieldsImg[0][18] = enteredMarkedBlackBottomLeftCorner;

        freeFieldsImg[18][18] = bottomRightCorner;
        whiteFieldsImg[18][18] = whiteBottomRightCorner;
        blackFieldsImg[18][18] = blackBottomRightCorner;
        deadBlackFieldsImg[18][18] = deadBlackBottomRightCorner;
        deadWhiteFieldsImg[18][18] = deadWhiteBottomRightCorner;
        whiteMarkedFieldsImg[18][18] = whiteMarkedBottomRightCorner;
        blackMarkedFieldsImg[18][18] = blackMarkedBottomRightCorner;
        enteredFieldsImg[18][18] = enteredBottomRightCorner;
        enteredDeadBlackFieldsImg[18][18] = enteredDeadBlackBottomRightCorner;
        enteredDeadWhiteFieldsImg[18][18] = enteredDeadWhiteBottomRightCorner;
        enteredMarkedWhiteFieldsImg[18][18] = enteredMarkedWhiteBottomRightCorner;
        enteredMarkedBlackFieldsImg[18][18] = enteredMarkedBlackBottomRightCorner;

        for(int i = 1; i < 18; i++){
            freeFieldsImg[i][0] = top;
            whiteFieldsImg[i][0] = whiteTop;
            blackFieldsImg[i][0] = blackTop;
            deadBlackFieldsImg[i][0] = deadBlackTop;
            deadWhiteFieldsImg[i][0] = deadWhiteTop;
            whiteMarkedFieldsImg[i][0] = whiteMarkedTop;
            blackMarkedFieldsImg[i][0] = blackMarkedTop;
            enteredFieldsImg[i][0] = enteredTop;
            enteredDeadBlackFieldsImg[i][0] = enteredDeadBlackTop;
            enteredDeadWhiteFieldsImg[i][0] = enteredDeadWhiteTop;
            enteredMarkedWhiteFieldsImg[i][0] = enteredMarkedWhiteTop;
            enteredMarkedBlackFieldsImg[i][0] = enteredMarkedBlackTop;

            freeFieldsImg[i][18] = bottom;
            whiteFieldsImg[i][18] = whiteBottom;
            blackFieldsImg[i][18] = blackBottom;
            deadBlackFieldsImg[i][18] = deadBlackBottom;
            deadWhiteFieldsImg[i][18] = deadWhiteBottom;
            whiteMarkedFieldsImg[i][18] = whiteMarkedBottom;
            blackMarkedFieldsImg[i][18] = blackMarkedBottom;
            enteredFieldsImg[i][18] = enteredBottom;
            enteredDeadBlackFieldsImg[i][18] = enteredDeadBlackBottom;
            enteredDeadWhiteFieldsImg[i][18] = enteredDeadWhiteBottom;
            enteredMarkedWhiteFieldsImg[i][18] = enteredMarkedWhiteBottom;
            enteredMarkedBlackFieldsImg[i][18] = enteredMarkedBlackBottom;

            freeFieldsImg[0][i] = leftSide;
            whiteFieldsImg[0][i] = whiteLeftSide;
            blackFieldsImg[0][i] = blackLeftSide;
            deadBlackFieldsImg[0][i] = deadBlackLeftSide;
            deadWhiteFieldsImg[0][i] = deadWhiteLeftSide;
            whiteMarkedFieldsImg[0][i] = whiteMarkedLeftSide;
            blackMarkedFieldsImg[0][i] = blackMarkedLeftSide;
            enteredFieldsImg[0][i] = enteredLeftSide;
            enteredDeadBlackFieldsImg[0][i] = enteredDeadBlackLeftSide;
            enteredDeadWhiteFieldsImg[0][i] = enteredDeadWhiteLeftSide;
            enteredMarkedWhiteFieldsImg[0][i] = enteredMarkedWhiteLeftSide;
            enteredMarkedBlackFieldsImg[0][i] = enteredMarkedBlackLeftSide;

            freeFieldsImg[18][i] = rightSide;
            whiteFieldsImg[18][i] = whiteRightSide;
            blackFieldsImg[18][i] = blackRightSide;
            deadBlackFieldsImg[18][i] = deadBlackRightSide;
            deadWhiteFieldsImg[18][i] = deadWhiteRightSide;
            whiteMarkedFieldsImg[18][i] = whiteMarkedRightSide;
            blackMarkedFieldsImg[18][i] = blackMarkedRightSide;
            enteredFieldsImg[18][i] = enteredRightSide;
            enteredDeadBlackFieldsImg[18][i] = enteredDeadBlackRightSide;
            enteredDeadWhiteFieldsImg[18][i] = enteredDeadWhiteRightSide;
            enteredMarkedWhiteFieldsImg[18][i] = enteredMarkedWhiteRightSide;
            enteredMarkedBlackFieldsImg[18][i] = enteredMarkedBlackRightSide;

        }

        for(int i = 1; i < 18; i++){
            for(int j = 1; j < 18; j++){
                freeFieldsImg[i][j] = center;
                whiteFieldsImg[i][j]= whiteCenter;
                blackFieldsImg[i][j] = blackCenter;
                deadBlackFieldsImg[i][j] = deadBlackCenter;
                deadWhiteFieldsImg[i][j] = deadWhiteCenter;
                whiteMarkedFieldsImg[i][j] = whiteMarkedCenter;
                blackMarkedFieldsImg[i][j] = blackMarkedCenter;
                enteredFieldsImg[i][j] = enteredCenter;
                enteredDeadBlackFieldsImg[i][j] = enteredDeadBlackCenter;
                enteredDeadWhiteFieldsImg[i][j] = enteredDeadWhiteCenter;
                enteredMarkedWhiteFieldsImg[i][j] = enteredMarkedWhiteCenter;
                enteredMarkedBlackFieldsImg[i][j] = enteredMarkedBlackCenter;

            }
        }

    }

    /**
     * Sets frame visible.
     */
    private void makeFinalFrame(){
        setLayout(new BorderLayout());
        panelForBoard.setPreferredSize(new Dimension(625,625));
        add(BorderLayout.CENTER, panelForBoard);
        add(BorderLayout.WEST, leftPanel);
        add(BorderLayout.EAST, rightPanel);
        add(BorderLayout.SOUTH, bottomPanel);
        setTitle("Go Game");
        setSize(1080,695);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

    }

    @Override
    public void playerReceivedPermissionToMove() {
        myTurn = true;
        passButton.setEnabled(true);
        if(!ifOpponentPassed)
            infoLabel.setText("Your turn!");
        ifOpponentPassed = false;
    }

    @Override
    public void playerMadeLegalMove() {
        myTurn = false;
        passButton.setEnabled(false);
        infoLabel.setText("Opponent's turn!");
    }

    @Override
    public void playerMadeIllegalMoveKO() {
        JOptionPane.showMessageDialog(null, "Illegal move: KO. Try again!");
    }

    @Override
    public void playerMadeIllegalMoveSuicide() {
        JOptionPane.showMessageDialog(null, "Illegal move: suicide. Try again!");
    }

    @Override
    public void playerMadeIllegalMoveOccupiedField() {
        JOptionPane.showMessageDialog(null, "Illegal move: occupied field. Try again!");
    }

    @Override
    public void opponentPassed() {
        ifOpponentPassed = true;
        myTurn = true;
        passButton.setEnabled(true);
        infoLabel.setText("Opponent passed. Your turn!");

    }

    @Override
    public void opponentGaveUp() {
        JOptionPane.showMessageDialog(null, "Congrats! Opponent gave up. You won!");
        infoLabel.setText("End of the game.");
        passButton.setEnabled(false);
        suggestButton.setEnabled(false);
        acceptButton.setEnabled(false);
        notAcceptButton.setEnabled(false);
        ifMarkDeadStones = false;
        ifMarkArea = false;
        myTurn = false;
    }

    @Override
    public void updateBoard(String[][] updatedBoard) {
        for(int i = 0; i < 19; i++){
            for (int j = 0; j < 19; j++){

                if (updatedBoard[i][j].equals("FREE")){
                    fields[i][j].setIcon(freeFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                }
                else if (updatedBoard[i][j].equals("BLACK")){
                    fields[i][j].setIcon(blackFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();

                }
                else if (updatedBoard[i][j].equals("WHITE")){
                    fields[i][j].setIcon(whiteFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                }
            }
        }
    }

    @Override
    public void updateCaptured(String capturedForWhite, String capturedForBlack){
        if(playerColor.equals("WHITE")){
            myNumberOfCaptured.setText(capturedForWhite);
            opponentNumberOfCaptured.setText(capturedForBlack);
        }
        else if(playerColor.equals("BLACK")){
            myNumberOfCaptured.setText(capturedForBlack);
            opponentNumberOfCaptured.setText(capturedForWhite);
        }
    }

    @Override
    public void waitForOpponentToMarkDeadStones(){
        infoLabel.setText("<html>Wait for opponent <br/>to mark dead stones!</html>");
        passButton.setEnabled(false);
        ifMarkDeadStones =false;

    }

    @Override
    public void markDeadStones(){
        passButton.setEnabled(false);
        ifMarkDeadStones = true;
        suggestButton.setVisible(true);
        suggestButton.setEnabled(true);
        infoLabel.setText("Mark dead stones of your opponent.");

    }

    @Override
    public void showMarkedAsDead(String[][] markedAsDead){
        ifTimeToAcceptDeadFields = true;
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                if(markedAsDead[i][j].equals("true")){
                    if(playerColor.equals("WHITE")) {
                        fields[i][j].setIcon(deadWhiteFieldsImg[i][j]);
                        fields[i][j].revalidate();
                        fields[i][j].repaint();
                    }
                    else if(playerColor.equals("BLACK")) {
                        fields[i][j].setIcon(deadBlackFieldsImg[i][j]);
                        fields[i][j].revalidate();
                        fields[i][j].repaint();
                    }
                }
            }
        }
        infoLabel.setText("Do you accept?");
        acceptButton.setVisible(true);
        notAcceptButton.setVisible(true);
        acceptButton.setEnabled(true);
        notAcceptButton.setEnabled(true);
        resumeButton.setVisible(true);
    }

    @Override
    public void botAcceptedDeadStones(){
        ifOpponentAccepted = true;
        MyPresenter myPresenter = MyPresenter.INSTANCE;
        deleteAcceptedDeadStones();
        myPresenter.sendUpdatedBoard(fields);
        infoLabel.setText("<html>Wait for the server<br/>to mark areas.</html>");
        ifOpponentAccepted = false;
        ifAccepted = false;
    }

    @Override
    public void deadStonesAccepted(){
        ifOpponentAccepted = true;
        MyPresenter myPresenter = MyPresenter.INSTANCE;
        if (ifAccepted && ifOpponentAccepted){
            deleteAcceptedDeadStones();
            myPresenter.sendUpdatedBoard(fields);
            infoLabel.setText("<html>Wait for the opponent<br/>to mark his area.</html>");
            ifOpponentAccepted = false;
            ifAccepted = false;
        }
        else {
            myPresenter.sendInfo("MARK_DEAD");
            deleteAcceptedDeadStones();
            infoLabel.setText("<html>Opponent accepted your suggestion!<br/>Wait for him to mark dead stones.</html>");
        }

    }

    @Override
    public void deadStonesNotAccepted(){
        deleteNotAcceptedDeadStones();
        ifMarkDeadStones = true;
        infoLabel.setText("<html>Opponent didn't accept<br/>your suggestion.<br/>Mark his dead stones again!</html>");
        suggestButton.setEnabled(true);

    }

    private void deleteNotAcceptedDeadStones(){
        for (int i = 0; i < 19; i++){
            for (int j = 0; j < 19; j++){
                if (fields[i][j].getIcon() instanceof DeadBlackFieldsImg) {
                    fields[i][j].setIcon(blackFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                }
                else if (fields[i][j].getIcon() instanceof DeadWhiteFieldsImg) {
                    fields[i][j].setIcon(whiteFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                }
            }
        }
    }

    private void deleteAcceptedDeadStones(){
        int myCaptured = Integer.parseInt(myNumberOfCaptured.getText());
        int opponentCaptured = Integer.parseInt(opponentNumberOfCaptured.getText());
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                if(fields[i][j].getIcon() instanceof DeadWhiteFieldsImg) {
                    fields[i][j].setIcon(freeFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                    if (playerColor.equals("BLACK")) {
                        myCaptured++;
                    }
                    else if (playerColor.equals("WHITE"))
                        opponentCaptured++;
                } else if (fields[i][j].getIcon() instanceof DeadBlackFieldsImg){
                    fields[i][j].setIcon(freeFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                    if (playerColor.equals("BLACK"))
                        opponentCaptured++;
                    else if (playerColor.equals("WHITE"))
                        myCaptured++;
                }
            }
        }
        myNumberOfCaptured.setText(Integer.toString(myCaptured));
        opponentNumberOfCaptured.setText(Integer.toString(opponentCaptured));
    }

    @Override
    public void markArea() {
        ifMarkArea = true;
        infoLabel.setText("Mark your area.");
        suggestButton.setEnabled(true);
        //resumeButton.setEnabled(true);
    }

    @Override
    public void showMarkedArea(String[][] markedArea){
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                if((!markedArea[i][j].equals("0")) && playerColor.equals("BLACK")) {
                    fields[i][j].setIcon(blackMarkedFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                    this.markedArea[i][j] = true;
                } else if ((!markedArea[i][j].equals("0")) && playerColor.equals("WHITE")){
                    fields[i][j].setIcon(whiteMarkedFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                    this.markedArea[i][j] = true;
                }
            }
        }
    }

    @Override
    public void showFinalMarkedArea(String[][] markedArea){
        ifTimeToAcceptArea = true;
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                if(markedArea[i][j].equals("true") && playerColor.equals("BLACK")) {
                    fields[i][j].setIcon(whiteMarkedFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                }
                else if(markedArea[i][j].equals("true") && playerColor.equals("WHITE")) {
                    fields[i][j].setIcon(blackMarkedFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                }
            }
        }

        infoLabel.setText("Do you accept?");
        acceptButton.setVisible(true);
        notAcceptButton.setVisible(true);
        acceptButton.setEnabled(true);
        notAcceptButton.setEnabled(true);
    }

    @Override
    public void showSingleSerwerMarkedArea(String[][] markedArea){
        for(int i = 0; i < 19; i++){
            for(int j = 0; j < 19; j++){
                if(markedArea[i][j].equals("WHITE")) {
                    fields[i][j].setIcon(whiteMarkedFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                }
                else if(markedArea[i][j].equals("BLACK")) {
                    fields[i][j].setIcon(blackMarkedFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                }
            }
        }
    }

    @Override
    public void serwerMarkedArea(){
        checkScore();
    }

    @Override
    public void areaAccepted(){
        ifOpponentAccepted = true;
        if(ifOpponentAccepted && ifAccepted){
            infoLabel.setText("<html>Opponent accepted your suggestion!<br/>End of the game!</html>");
            checkScore();
            ifOpponentAccepted = false;
            ifAccepted = false;
        } else {
            infoLabel.setText("<html>Opponent accepted your suggestion!<br/>Wait for him to mark his area.</html>");
        }
        ifMarkArea = false;
    }

    @Override
    public void areaNotAccepted(){
        infoLabel.setText("<html>Opponent didn't accept<br/>your suggestion.<br/>Mark your area again!</html>");
        suggestButton.setEnabled(true);
        deleteMyNotAcceptedArea();
        ifMarkArea = true;
    }

    @Override
    public void opponentResumed() {
        JOptionPane.showMessageDialog(null, "Opponent resumed game. You'r turn!");
        showFieldsAfterResume();
        myNumberOfCaptured.setText(myCapturedAfterResume);
        opponentNumberOfCaptured.setText(opponentCapturedAfterResume);

    }

    private void deleteMyNotAcceptedArea(){
        for (int i = 0; i < 19; i++){
            for (int j = 0; j < 19; j++){
                if ((fields[i][j].getIcon() instanceof WhiteMarkedFieldsImg && playerColor.equals("WHITE"))
                        || (fields[i][j].getIcon() instanceof BlackMarkedFieldsImg && playerColor.equals("BLACK"))) {
                    fields[i][j].setIcon(freeFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                }
            }
        }
    }

    public void deleteOpponentNotAcceptedArea(){
        for (int i = 0; i < 19; i++){
            for (int j = 0; j < 19; j++){
                if ((fields[i][j].getIcon() instanceof WhiteMarkedFieldsImg && playerColor.equals("BLACK"))
                        || (fields[i][j].getIcon() instanceof BlackMarkedFieldsImg && playerColor.equals("WHITE"))) {
                    fields[i][j].setIcon(freeFieldsImg[i][j]);
                    fields[i][j].revalidate();
                    fields[i][j].repaint();
                }
            }
        }
    }

    private void restorePresentFields(){
        for (int i = 0; i < 19; i++){
            for (int j = 0; j < 19; j++){
                fieldsAfterResume[i][j] = (ImageIcon)fields[i][j].getIcon();
            }
        }
    }

    private void showFieldsAfterResume(){
        for (int i = 0; i < 19; i++){
            for (int j = 0; j < 19; j++){
                fields[i][j].setIcon(fieldsAfterResume[i][j]);
            }
        }
    }

    private void checkScore(){
        double myArea = 0;
        double opponentArea = 0;

        double myCaptured = 0;
        double opponentCaptured = 0;

        double myScore = 0;
        double opponentScore = 0;

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if(playerColor.equals("WHITE")) {
                    if (fields[i][j].getIcon() instanceof BlackMarkedFieldsImg)
                        opponentArea++;
                    if (fields[i][j].getIcon() instanceof WhiteMarkedFieldsImg)
                        myArea++;
                } else if(playerColor.equals("BLACK")) {
                    if (fields[i][j].getIcon() instanceof BlackMarkedFieldsImg)
                        myArea++;
                    if (fields[i][j].getIcon() instanceof WhiteMarkedFieldsImg)
                        opponentArea++;
                }
            }
        }

        myCaptured = Integer.parseInt(myNumberOfCaptured.getText());
        opponentCaptured = Integer.parseInt(opponentNumberOfCaptured.getText());

        if(playerColor.equals("WHITE")) {
            myScore = myArea - opponentCaptured + 6.5;
            opponentScore = opponentArea - myCaptured;
        }
        else if(playerColor.equals("BLACK")) {
            myScore = myArea - opponentCaptured;
            opponentScore = opponentArea - myCaptured + 6.5;
        }

        if(myScore > opponentScore){
            JOptionPane.showMessageDialog(null,"Congrats, you won! :) Your score: " + myScore + ", opponent's score: " + opponentScore);
        } else if (myScore < opponentScore){
            JOptionPane.showMessageDialog(null,"What a pity, you lost :( Your score: " + myScore + ", opponent's score: " + opponentScore);
        } else
            JOptionPane.showMessageDialog(null,"Wow, we have a draw! Revenge?" + myScore + ", opponent's score: " + opponentScore);

        ifMarkArea = false;
        infoLabel.setText("End of the game!");
    }

    /**
     * Class of label treated like field.
     */
    public class FieldLabel extends JLabel{
        ImageIcon icon;
        int x;
        int y;

        public void setIcon(ImageIcon icon){
            super.setIcon(icon);
            this.icon = icon;
        }


        public void setClickedX(int x){
            this.x = x;
        }

        public void setClickedY(int y){
            this.y = y;
        }

        public int getClickedX(){
            return x;
        }

        public int getClickedY(){
            return y;
        }
    }
}
