import com.mycompany.dlaminichat.Message;
import com.mycompany.dlaminichat.MessageManager;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

/**
 *
 * @author Student
 */
public class MessageManagerTest {
    
    // Test data messages 1 to 5 (message 4's text is typed as the brief's expected output)
    private static final String[] IDS = {"1000000001", "1000000002", "1000000003", "1000000004", "1000000005"};
    private static final String[] RECIPIENTS = {"+27834557896", "+27838884567", "+27834484567",
        "0838884567", "+27838884567"};
    private static final String[] TEXTS = {"Did you get the cake?",
        "Where are you? You are late! I have asked you to be on time.",
        "Yohoooo, I am at your gate.",
        "It is dinner time!",
        "Ok, I am leaving without you."};
    private static final int[] FLAGS = {Message.SEND, Message.STORE, Message.DISREGARD,
        Message.SEND, Message.STORE};

    private MessageManager manager;
    private Message[] messages = new Message[5];
    
    public MessageManagerTest() {
    }
    
    @TempDir
    Path tempFolder;
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @BeforeEach
    public void setUp() {
        Message.clearMessages();
        Message.setJsonFileName(tempFolder.resolve("messages.json").toString());
        manager = new MessageManager();

        // populate the arrays from the test data (no hard-coded arrays in the application)
        for (int x = 0; x < messages.length; ++x) {
            messages[x] = new Message(IDS[x], x, RECIPIENTS[x], TEXTS[x]);
            manager.addMessage(messages[x], FLAGS[x]);
        }
    }
    
    @AfterEach
    public void tearDown() {
        Message.clearMessages();
    }
    
    //------- Arrays correctly populated -------

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        String[] sent = manager.getSentMessages();

        assertEquals(2, sent.length);
        assertEquals("Did you get the cake?", sent[0]);
        assertEquals("It is dinner time!", sent[1]);
    }
    
    @Test
    public void testDisregardedAndStoredArraysCorrectlyPopulated() {
        assertEquals(1, manager.getDisregardedMessages().length);
        assertEquals("Yohoooo, I am at your gate.", manager.getDisregardedMessages()[0]);
        assertEquals(2, manager.getStoredMessages().length);
        assertEquals(TEXTS[1], manager.getStoredMessages()[0]);
        assertEquals(TEXTS[4], manager.getStoredMessages()[1]);
    }
    
    @Test
    public void testMessageHashAndIDArraysCorrectlyPopulated() {
        assertEquals(5, manager.getMessageHashes().length);
        assertEquals(5, manager.getMessageIDs().length);
        for (int x = 0; x < messages.length; ++x) {
            assertEquals(messages[x].getMessageHash(), manager.getMessageHashes()[x]);
            assertEquals(IDS[x], manager.getMessageIDs()[x]);
        }
    }
    
    //------- Display the longest message -------

    @Test
    public void testDisplayLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                manager.longestStoredMessage());
    }

    @Test
    public void testDisplayLongestMessage_NothingStored() {
        assertEquals("No stored messages.", new MessageManager().longestStoredMessage());
    }
    
    //------- Search for a message ID -------

    @Test
    public void testSearchForMessageID() {
        assertEquals("It is dinner time!", manager.searchByMessageID("1000000004"));
        assertEquals("Recipient: 0838884567\nMessage: It is dinner time!",
                manager.displayByMessageID("1000000004"));
    }

    @Test
    public void testSearchForMessageID_NotFound() {
        assertEquals("Message ID not found.", manager.searchByMessageID("9999999999"));
    }
    
    //------- Search all the messages for a particular recipient -------

    @Test
    public void testSearchMessagesForRecipient() {
        String[] found = manager.searchByRecipient("+27838884567");

        assertEquals(2, found.length);
        assertEquals("Where are you? You are late! I have asked you to be on time.", found[0]);
        assertEquals("Ok, I am leaving without you.", found[1]);
    }

    @Test
    public void testSearchMessagesForRecipient_DisregardedNotIncluded() {
        assertEquals(0, manager.searchByRecipient("+27834484567").length);
    }
    
    //------- Delete a message using a message hash -------

    @Test
    public void testDeleteMessageUsingHash() {
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time\""
                + " successfully deleted.", manager.deleteByHash(messages[1].getMessageHash()));
    }

    @Test
    public void testDeleteMessageUsingHash_RemovedFromTheArrays() {
        manager.deleteByHash(messages[1].getMessageHash());

        assertEquals(1, manager.getStoredMessages().length);
        assertEquals("Ok, I am leaving without you.", manager.getStoredMessages()[0]);
        assertEquals(4, manager.getMessageHashes().length);
        assertEquals("Message ID not found.", manager.searchByMessageID("1000000002"));
        assertEquals("Message hash not found.", manager.deleteByHash(messages[1].getMessageHash()));
    }
    
    //------- Display report -------

    @Test
    public void testDisplayReport_SentMessages() {
        String report = manager.buildReport(Message.SEND);

        assertTrue(report.contains("Message Hash: " + messages[0].getMessageHash()));
        assertTrue(report.contains("Recipient: +27834557896"));
        assertTrue(report.contains("Message: Did you get the cake?"));
        assertTrue(report.contains("Message: It is dinner time!"));
        assertFalse(report.contains("Yohoooo"));
    }

    @Test
    public void testDisplayReport_StoredMessages() {
        String report = manager.buildReport(Message.STORE);

        assertTrue(report.contains("Message ID: 1000000002"));
        assertTrue(report.contains("Message Hash: " + messages[1].getMessageHash()));
        assertTrue(report.contains("Recipient: +27838884567"));
        assertTrue(report.contains("Message: Ok, I am leaving without you."));
        assertFalse(report.contains("cake"));
    }

    @Test
    public void testDisplaySenderAndRecipientOfStoredMessages() {
        String expected = "Sender: Avela Baduza\nRecipient: +27838884567\n\n"
                + "Sender: Avela Baduza\nRecipient: +27838884567\n\n";

        assertEquals(expected, manager.displaySendersAndRecipients("Avela Baduza"));
    }
    
    //------- Read the JSON file into the stored messages array -------

    @Test
    public void testReadJsonFileIntoStoredArray() {
        messages[1].storeMessage();
        messages[4].storeMessage();

        MessageManager fromFile = new MessageManager();

        assertEquals(2, fromFile.loadStoredMessages(Message.getJsonFileName()));
        assertEquals(2, fromFile.getStoredMessages().length);
        assertEquals(TEXTS[1], fromFile.getStoredMessages()[0]);
        assertEquals(TEXTS[4], fromFile.getStoredMessages()[1]);
        assertEquals(messages[1].getMessageHash(), fromFile.getMessageHashes()[0]);
        assertEquals("Recipient: +27838884567\nMessage: " + TEXTS[4],
                fromFile.displayByMessageID("1000000005"));
    }

    @Test
    public void testReadJsonFile_QuotesInTheMessage() {
        new Message("1000000006", 5, "+27838884567", "He said \"hi\" there").storeMessage();

        MessageManager fromFile = new MessageManager();
        fromFile.loadStoredMessages(Message.getJsonFileName());

        assertEquals("He said \"hi\" there", fromFile.getStoredMessages()[0]);
    }

    @Test
    public void testReadJsonFile_MessagesAlreadyInTheArraysAreNotAddedTwice() {
        messages[1].storeMessage();

        assertEquals(0, manager.loadStoredMessages(Message.getJsonFileName()));
        assertEquals(2, manager.getStoredMessages().length);
    }

    @Test
    public void testReadJsonFile_FileMissing() {
        assertEquals(-1, manager.loadStoredMessages(tempFolder.resolve("missing.json").toString()));
    }
}
