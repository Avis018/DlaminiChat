/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class MessageTest {
    
    private static final String RECIPIENT_1 = "+27833025474";
    private static final String TEXT_1 = "Hey baby, did you get home safely?";
    private static final String RECIPIENT_2 = "0697555172";
    private static final String TEXT_2 = "Ma, is there anything cooked for dinner?";
    
    private static final String ID_1 = "0012345678";
    private static final String ID_2 = "0087654321";
    
    private Message message1;
    private Message message2;
    
    public MessageTest() {
    }
    
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
}
