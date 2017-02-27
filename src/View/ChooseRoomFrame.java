package View;

import Presenter.MyPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Class of frame, which gives player opportunity to choose room for the game.
 */
public class ChooseRoomFrame extends JFrame {

    /**
     * ArrayList of rooms' indexes.
     */
    private ArrayList<String> indexes = new ArrayList<>();

    /**
     * ArrayList of initiators' logins.
     */
    private ArrayList<String> initiators = new ArrayList<>();

    /**
     * ArrayList of joiners' logins.
     */
    private ArrayList<String> joiners = new ArrayList<>();

    private String[] roomsInfo;

    private JList list;

    private JButton chooseButton;

    /**
     * JPanel which contains list of existing rooms.
     */
    private JPanel panelForExistingRooms;

    /**
     * JPanel which contains header (New or existing?) and two buttons to choose.
     */
    private JPanel header = new JPanel();

    /**
     * JPanel which contains button for updating.
     */
    private JPanel panelForUpdating = new JPanel();

    private Boolean ifExistingClicked = false;


    public ChooseRoomFrame() {

        makeFinalFrame();
        makeLists();
        makeViewOfRooms();
        makeHeader();
        makeFooter();
        setVisible(true);
    }

    /**
     * Makes 3 ArrayLists:
     * 1) indexes of rooms
     * 2) logins of players (player1)
     * 3) logins of opponents (player2)
     */
    private void makeLists() {

        MyPresenter myPresenter = MyPresenter.INSTANCE;
        indexes = myPresenter.receiveListOfRoomsInfo();
        initiators = myPresenter.receiveListOfRoomsInfo();
        joiners = myPresenter.receiveListOfRoomsInfo();
        roomsInfo = new String[indexes.size()];

    }

    /**
     * Creates labels with indexes and logins.
     * If room contains only one player, it creates button to give another player opportunity to choose this room.
     * After all it puts everything on two JPanels (panelForExistingRooms and panelForChooseRoomButtons).
     */
    private void makeViewOfRooms() {
        panelForExistingRooms = new JPanel();

        for (int i = 0; i < indexes.size(); i++) {
            roomsInfo[i] = indexes.get(i) + "                  " + initiators.get(i) + "                 " + joiners.get(i);
        }
        list = new JList(roomsInfo);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setSize(260,250);
        panelForExistingRooms.add(scrollPane);
        add(BorderLayout.CENTER, panelForExistingRooms);
    }

    /**
     * Makes header and two buttons:
     * 1) to create new room
     * 2) to choose existing room.
     * Puts also everything on JPanel (panelForNewExistingButtons).
     */
    private void makeHeader() {

        JLabel chooseRoomLabel = new JLabel();
        chooseRoomLabel.setText("Do you want to create a new room or choose an existing one?");

        JButton newRoomButton = new JButton();
        newRoomButton.setText("New");
        JButton existingRoomButton = new JButton();
        existingRoomButton.setText("Existing");

        newRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.newRoomChosen();
                GameFrame gameFrame = new GameFrame();
                gameFrame.waitingForOpponent();
                myPresenter.receiveGameMessage(gameFrame);
                dispose();

            }
        });


        existingRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.existingRoomChosen();
                newRoomButton.setEnabled(false);
                existingRoomButton.setEnabled(false);
                ifExistingClicked = true;
                chooseButton.setEnabled(true);
            }
        });

        header.setLayout(new BorderLayout());

        JPanel panelForNewExistingButtons = new JPanel();
        panelForNewExistingButtons.add(newRoomButton);
        panelForNewExistingButtons.add(existingRoomButton);

        header.add(BorderLayout.NORTH, chooseRoomLabel);
        header.add(BorderLayout.CENTER, panelForNewExistingButtons);
        add(BorderLayout.NORTH, header);

    }

    private void makeFooter() {

        JButton refreshButton = new JButton("REFRESH");
        chooseButton = new JButton("CHOOSE");
        panelForUpdating.add(refreshButton);
        panelForUpdating.add(chooseButton);

        chooseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.indexOfRoomChosen(Integer.toString(list.getSelectedIndex()));
                GameFrame gameFrame = new GameFrame();
                gameFrame.joinToRoom(initiators.get(list.getSelectedIndex()));
                myPresenter.receiveGameMessage(gameFrame);
                dispose();
            }
        });

        chooseButton.setVisible(true);
        chooseButton.setEnabled(false);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MyPresenter myPresenter = MyPresenter.INSTANCE;
                myPresenter.sendInfo("REFRESH");
                panelForExistingRooms.setVisible(false);
                makeLists();
                makeViewOfRooms();
                if (ifExistingClicked)
                    chooseButton.setEnabled(true);
                if (!ifExistingClicked)
                    chooseButton.setEnabled(false);
                refreshButton.setVisible(true);
            }
        });

        add(BorderLayout.SOUTH, panelForUpdating);
    }

    /**
     * Sets frame visible.
     */
    private void makeFinalFrame() {

        setLayout(new BorderLayout());
        setTitle("Go Game");
        setSize(360, 280);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    }
}