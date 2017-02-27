package ViewTests;

import View.GameFrame;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

import javax.swing.*;


@RunWith(MockitoJUnitRunner.class)

public class GameFrameTest {


    @Mock
    JLabel infoLabel;

    @Mock
    JLabel opponentLoginLabel;

    @Mock
    JButton acceptButton;

    @Mock
    JButton passButton;

    @Mock
    JButton suggestButton;

    @Mock
    JButton notAcceptButton;

    @Mock
    JButton resumeButton;

    @Mock
    JTextField myNumberOfCaptured;

    @Mock
    JTextField opponentNumberOfCaptured;


    @InjectMocks
    GameFrame gameFrame;





    @Test
    public void testWaitingForOpponent() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        gameFrame.waitingForOpponent();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Waiting for the opponent...", captor.getValue());
        Mockito.verify(opponentLoginLabel).setText(captor.capture());
        Assert.assertEquals("-", captor.getValue());


    }

    @Test
    public void testOpponentJoined() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        gameFrame.opponentJoined("Login");
        Mockito.verify(opponentLoginLabel).setText(captor.capture());
        Assert.assertEquals("Login", captor.getValue());
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Opponent's turn!", captor.getValue());
    }

    @Test
    public void testJoinToRoom() throws Exception {

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.joinToRoom("Login");
        Mockito.verify(opponentLoginLabel).setText(captor.capture());
        Assert.assertEquals("Login", captor.getValue());
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Your turn!", captor.getValue());
       Mockito.verify(passButton).setEnabled(captor1.capture());
        Assert.assertEquals(true, captor1.getValue());


    }

    @Test
    public void testPlayerReceivedPermissionToMove() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        gameFrame.playerReceivedPermissionToMove();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Your turn!", captor.getValue());

    }

    @Test
    public void testPlayerMadeLegalMove() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        gameFrame.playerMadeLegalMove();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Opponent's turn!", captor.getValue());
    }



    @Test
    public void testOpponentPassed() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.opponentPassed();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Opponent passed. Your turn!", captor.getValue());
        Mockito.verify(passButton).setEnabled(captor1.capture());
        Assert.assertEquals(true, captor1.getValue());
    }

    @Test
    public void testOpponentGaveUp() throws Exception {

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.opponentGaveUp();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("End of the game.", captor.getValue());
        Mockito.verify(acceptButton).setEnabled(captor1.capture());
        Assert.assertEquals(false, captor1.getValue());
        Mockito.verify(passButton).setEnabled(captor1.capture());
        Assert.assertEquals(false, captor1.getValue());
        Mockito.verify(suggestButton).setEnabled(captor1.capture());
       Assert.assertEquals(false, captor1.getValue());
        Mockito.verify(notAcceptButton).setEnabled(captor1.capture());
        Assert.assertEquals(false, captor1.getValue());

    }



    @Test
    public void testWaitForOpponentToMarkDeadStones() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.waitForOpponentToMarkDeadStones();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("<html>Wait for opponent <br/>to mark dead stones!</html>", captor.getValue());
        Mockito.verify(passButton).setEnabled(captor1.capture());
        Assert.assertEquals(false, captor1.getValue());
    }

    @Test
    public void testMarkDeadStones() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.markDeadStones();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Mark dead stones of your opponent.", captor.getValue());
        Mockito.verify(passButton).setEnabled(captor1.capture());
        Assert.assertEquals(false, captor1.getValue());
        Mockito.verify(suggestButton).setVisible(captor1.capture());
       Assert.assertEquals(true, captor1.getValue());
    }




    @Test
    public void testDeadStonesNotAccepted() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.deadStonesNotAccepted();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("<html>Opponent didn't accept<br/>your suggestion.<br/>Mark his dead stones again!</html>", captor.getValue());
        Mockito.verify(suggestButton).setEnabled(captor1.capture());
        Assert.assertEquals(true, captor1.getValue());
    }


    @Test
    public void testMarkArea() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.markArea();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("Mark your area.", captor.getValue());
        Mockito.verify(suggestButton).setEnabled(captor1.capture());
        Assert.assertEquals(true, captor1.getValue());
    }


    @Test
    public void areaNotAccepted() throws Exception {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> captor1 = ArgumentCaptor.forClass(Boolean.class);
        gameFrame.areaNotAccepted();
        Mockito.verify(infoLabel).setText(captor.capture());
        Assert.assertEquals("<html>Opponent didn't accept<br/>your suggestion.<br/>Mark your area again!</html>", captor.getValue());
        Mockito.verify(suggestButton).setEnabled(captor1.capture());
        Assert.assertEquals(true, captor1.getValue());
    }





}