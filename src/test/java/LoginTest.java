/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
import com.mycompany.dlaminichat.Login;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;




/**
 *
 * @author Student
 */
public class LoginTest {
    
    private static Login login;
    
    public LoginTest() {
        
    }
    
    @BeforeAll
    public static void setUpClass() {
        //Baseline for all/most tests
        login = new Login("Avela", "Baduza", "Anan_", "Baduza1$", "+27833025474");
    }
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    //----------assertEquals: register user()-----------
    
    @Test
    public void testRegisterUser_UsernameCorrectlyFormatted() {
        assertEquals("Welcome Avela, Baduza it is great to see you.", login.registerUser());
    }
    
    @Test
    public void testRegisterUser_UsernameIncorrectlyFormatted() {
        Login badUsername = new Login("Avela", "Baduza", "Anhgiyiv", "Baduza1$", "+27833025474");
        assertEquals("Username is not correctly formatted, please make sure username has no more than 5 "
                + "characters and has an underscore", badUsername.registerUser());
    }
    
    @Test
    public void testRegisterUser_PasswordmeetsComplexity() {
        assertEquals("Welcome Avela, Baduza it is great to see you.", login.registerUser());
    }
    
    @Test
    public void testRegisterUser_PasswordDoesNotMeetComplexity() {
        Login badPassword = new Login("Avela", "Baduza", "Anan_", "sbfibsisdeg", "+27833025474");
        assertEquals("Password is not correctly formatted, please enure your "
                    + "password contains at least 8 characters, 1 capital letter "
                    + "1 special character, and a number",
                    badPassword.registerUser());
    }
    
    //CellPhone Message
    
    @Test
    public void testCellPhoneMessage_CorrectlyFormatted() {
        assertEquals("Cell number is successfully captured.", login.cellPhoneMessage());
    }
    
    @Test
    public void testCellPhoneMessage_IncorrectlyFormatted() {
        Login badCell = new Login("Avela", "Baduza", "Anan_", "Baduza1$", "0833025474");
        assertEquals("Cell phone number is incorrectly formatted and/or does not contain"
                    + " an international code, please fix your error and try again.",
                badCell.cellPhoneMessage());
    }
    
    @Test
    public void testLoginUser_Successful() {
        login.registerUser();
        assertTrue(login.loginUser("Anan_", "Baduza1$"));
    }
    
    @Test
    public void testLoginUser_NotSuccessful() {
        login.registerUser();
        assertFalse(login.loginUser("Ana_", "sgsrgduzashsr"));
    }
    
    
    @Test
    public void testCheckUserName_CorrectlyFormatted() {
        assertTrue(login.checkUserName());
    }
    
    @Test 
    public void testCheckUserName_IncorrectlyFormatted() {
        Login badUsername = new Login("Avela", "Baduza", "Anan^%^%^$", "Baduza1$", "+27833025474");
        assertFalse(badUsername.checkUserName());
    }
    
    @Test
    public void testCheckPasswordComplexity_MeetsRequirements() {
        assertTrue(login.checkPasswordComplexity());
    }
    
    @Test
    public void testCheckPasswordComplexity_DoesNotMeetRequirements() {
        Login badPassword = new Login("Avela", "Baduza", "Anan_", "jhbjaduzai$", "+27833025474");
        assertFalse(badPassword.checkPasswordComplexity());
    }
    
    @Test
    public void testCheckCellPhoneNumber_CorrectlyFormatted() {
        assertTrue(login.checkCellPhoneNumber());
    }
    
     @Test
    public void testCheckCellPhoneNumber_IncorrectlyFormatted() {
        Login badCell = new Login("Avela", "Baduza", "Anan_", "Baduza1$", "0833025474");
        assertFalse(badCell.checkCellPhoneNumber());
    }
}
