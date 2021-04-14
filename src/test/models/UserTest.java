package models;

import models.exceptions.NameTooShortException;
import models.exceptions.TodoListAlreadyExistsException;
import models.local.TodoList;
import models.local.User;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class UserTest {

    @Test
    public void testAddingTodoLists() throws NameTooShortException, TodoListAlreadyExistsException {
        User user = new User("dummyUsername");
        user.addTodoList(new TodoList());
        assertEquals(1,user.getTodoListCount());
        user.addTodoList(new TodoList());
        assertEquals(2,user.getTodoListCount());
    }

    @Test
    public void testTodoListAlreadyExistsException() {
        User user = new User("dummyUsername");
        try{
            user.addTodoList(new TodoList("same name"));
            user.addTodoList(new TodoList("same name"));
            fail("TodoListAlreadyExistsException not thrown");
        }catch (NameTooShortException e){
            fail(e.getClass()+" thrown");
        }catch (TodoListAlreadyExistsException ignored){}
    }

    @Test
    public void testRemovingTodoLists() throws NameTooShortException, TodoListAlreadyExistsException {
        User user = new User("dummyUsername");
        user.addTodoList(new TodoList());
        user.addTodoList(new TodoList());
        user.addTodoList(new TodoList());
        user.deleteList(0);
        assertEquals(2,user.getTodoListCount());
        user.deleteAllLists();
        assertEquals(0,user.getTodoListCount());
    }

}
