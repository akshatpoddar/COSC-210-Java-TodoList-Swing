package models;

import models.exceptions.UserAlreadyExistsException;
import models.exceptions.UserDoesNotExistException;
import models.local.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.UserManager;

import static org.junit.jupiter.api.Assertions.fail;

public class UserManagerTest {

    private UserManager userManager;

    @BeforeEach
    public void init(){
        userManager= UserManager.getInstance();
    }

    @Test
    public void testUserAlreadyExistsException(){
        try{
            userManager.addUser(new User("Dummy Name", "dummyUsername"));
            userManager.addUser(new User("Dummy Name2", "dummyUsername"));
            fail("UserAlreadyExistsException not thrown");
        } catch (UserAlreadyExistsException e) {}
    }

    @Test
    public void testUserDoesNotExistException(){
        try{
            userManager.getUserByUsername("User does not exist");
            fail("UserAlreadyExistsException not thrown");
        } catch (UserDoesNotExistException e) {}
    }
}
