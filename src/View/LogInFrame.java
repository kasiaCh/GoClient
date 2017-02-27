package View;

import Presenter.MyPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class of frame, which gives player opportunity to login.
 */
public class LogInFrame extends JFrame {

    public LogInFrame(){
        makeFinalFrame();
    }

    /**
     * Sets frame visible and enables player to login.
     */
    private void makeFinalFrame(){
        MyPresenter myPresenter = MyPresenter.INSTANCE;

        JLabel welcomeLabel = new JLabel();
        welcomeLabel.setText("Welcome! If you want to play, insert your login");

        TextField loginField = new TextField();

        JButton insertLoginButton = new JButton("OK");

        setLayout(new BorderLayout());
        add(BorderLayout.NORTH, welcomeLabel);
        add(BorderLayout.CENTER, loginField);
        add(BorderLayout.SOUTH, insertLoginButton);

        insertLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();
                myPresenter.sendLogin(login);
                ChooseOpponentFrame chooseOpponentFrame = new ChooseOpponentFrame();
                dispose();

            }
        });


        setTitle("Go Game");
        setSize(300,100);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
