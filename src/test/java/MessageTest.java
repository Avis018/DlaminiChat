/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
import com.mycompany.dlaminichat.Message;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.io.TempDir;
/**
 *
 * @author Student
 */
public class MessageTest {
    
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";
    
    private static final String ID_1 = "0012345678";
    private static final String ID_2 = "0087654321";
    
    private Message message1;
    private Message message2;
    
    
    @TempDir
    Path tempFolder;
    
    @BeforeEach
    public void setUp() {
        Message.clearMessages();
        Message.setJsonFileName(tempFolder.resolve("messages.json").toString());
        message1 = new Message(ID_1, 0, RECIPIENT_1, TEXT_1);
        message2 = new Message(ID_2, 1, RECIPIENT_2, TEXT_2);
    }
    
    @AfterEach
    public void tearDown() {
        Message.clearMessages();
    }

    
    private String makeText(int length) {
        StringBuilder text = new StringBuilder();
        for (int x = 0; x < length; ++x) {
            text.append('a');
        }
        return text.toString();
    }
    
    //------- Message length: success & failure --------
    @Test
    public void testMessageLength_Success() {
        assertEquals("Message ready to send.", Message.checkMessageLength(TEXT_1));
    }
    
    @Test
    public void testMessageLength_ExactlyAtTheLimit() {
        assertEquals("Message ready to send.", Message.checkMessageLength(makeText(250)));
        assertTrue(Message.isMessageLengthValid(makeText(250)));
    }
    
    @Test
    public void testMessageLength_Failure() {
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength(makeText(260)));
        assertFalse(Message.isMessageLengthValid(makeText(251)));
    }
    
    //---------- Recipient number: success and failure -----------
    
    @Test
    public void testRecipientCell_Success() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }
    
    @Test
    public void testRecipientCell_Failure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }
    
    //------- Message Hash -------
    
    @Test
    public void testMessageHash_TestCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }
    
    @Test
    public void testMessageHash_RemainingMessagesInLoop() {
        String[] ids = {ID_2, "0099999999", "0011111111"};
        String[] texts = {TEXT_2, "Hello", "  Running late,   see you soon!  "};
        String[] expected = {"00:1:HIPAYMENT", "00:2:HELLOHELLO", "00:3:RUNNINGSOON"};

        for (int x = 0; x < ids.length; ++x) {
            Message message = new Message(ids[x], x + 1, RECIPIENT_1, texts[x]);
            assertEquals(expected[x], message.createMessageHash(), "Hash " + (x + 1));
        }
    }
    
    //--------- Message ID ---------
    
    @Test
    public void testMessageID_Created() {
        assertEquals("Message ID generated: 0012345678", message1.messageIDMessage());
    }
    
    @Test
    public void testMessageID_RandomIDHasTenDigits() {
        Message random = new Message(0, RECIPIENT_1, TEXT_1);
        String id = random.getMessageID();

        assertEquals(10, id.length());
        for (int x = 0; x < id.length(); ++x) {
            assertTrue(Character.isDigit(id.charAt(x)), "Character " + x + " is a digit");
        }
        assertTrue(random.checkMessageID());
    }
    
    @Test
    public void testMessageID_MoreThanTenCharacters() {
        Message tooLong = new Message("12345678901", 0, RECIPIENT_1, TEXT_1);
        assertFalse(tooLong.checkMessageID());
    }
    
    //----------- MessageSent: sent, disregard, store -----------
    
     @Test
    public void testSentMessage_Send() {
        assertEquals("Message successfully sent.", message1.sentMessage(Message.SEND));
    }
    
    @Test
    public void testSentMessage_Disregard() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(Message.DISREGARD));
    }
    
    @Test
    public void testSentMessage_Store() {
        assertEquals("Message successfully stored.", message1.sentMessage(Message.STORE));
    }
    
    @Test
    public void testSentMessage_InvalidChoice() {
        assertEquals("Invalid option, please choose 1, 2 or 3.", message1.sentMessage(9));
    }
    
    
    @Test
    public void testReturnTotalMessages() {
        message1.sentMessage(Message.SEND);
        message2.sentMessage(Message.DISREGARD);

        assertEquals(1, Message.returnTotalMessages());
    }
    
    @Test
    public void testPrintMessages_OnlySentMessageIsShown() {
        message1.sentMessage(Message.SEND);
        message2.sentMessage(Message.DISREGARD);

        String expected = "Message ID: 0012345678\n"
                + "Message Hash: 00:0:HITONIGHT\n"
                + "Recipient: +27718693002\n"
                + "Message: Hi Mike, can you join us for dinner tonight?\n\n";

        assertEquals(expected, Message.printMessages());
    }
    
    @Test
    public void testStoredMessageIsNotCountedAsSent() {
        message1.sentMessage(Message.STORE);
        assertEquals(0, Message.returnTotalMessages());
    }
    
    @Test
    public void testStoreMessage_WritesJsonFile() throws IOException {
        assertTrue(message1.storeMessage());
        assertTrue(message2.storeMessage());

        String json = Files.readString(tempFolder.resolve("messages.json"));

        assertTrue(json.startsWith("["));
        assertTrue(json.trim().endsWith("]"));
        assertTrue(json.contains("\"messageID\": \"0012345678\""));
        assertTrue(json.contains("\"messageHash\": \"00:0:HITONIGHT\""));
        assertTrue(json.contains("\"messageHash\": \"00:1:HIPAYMENT\""));
        assertTrue(json.contains("\"recipient\": \"+27718693002\""));
    }
    
    @Test
    public void testStoreMessage_QuotesInTheMessageAreEscaped() throws IOException {
        Message quoted = new Message(ID_1, 0, RECIPIENT_1, "He said \"hi\" there");
        assertTrue(quoted.storeMessage());

        String json = Files.readString(tempFolder.resolve("messages.json"));
        assertTrue(json.contains("\"message\": \"He said \\\"hi\\\" there\""));
    }
}
