package models;

import models.exceptions.ItemPastDeadlineException;
import models.exceptions.NameTooShortException;
import models.exceptions.TooManyUndoneItemsException;
import models.local.Item;
import models.local.RegularItem;
import models.local.TodoList;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static models.local.TodoList.dateParser;
import static org.junit.jupiter.api.Assertions.*;

public class TodoListTest {

    @Test
    public void testAddingOneItem() throws ParseException, ItemPastDeadlineException, TooManyUndoneItemsException, NameTooShortException {
        TodoList todoList = new TodoList("New list another", 12);
        Item item = new RegularItem("Buy milk", TodoList.dateParser("12-12-2021"));
        todoList.addItem(item);
        assertEquals(1, todoList.getItemCount());
    }

    @Test
    public void testAddingMultipleItems() throws ParseException, ItemPastDeadlineException, TooManyUndoneItemsException, NameTooShortException {
        TodoList todoList;
        todoList = addNItems(3);
        assertEquals(3, todoList.getItemCount());
    }

    @Test
    public void testDeletingAnItem() throws ParseException, ItemPastDeadlineException, TooManyUndoneItemsException, NameTooShortException {
        TodoList todoList;
        todoList = addNItems(3);
        todoList.removeItem(todoList.getItemByPos(2));
        assertEquals(2, todoList.getItemCount());
    }


    @Test
    public void testDeletingMultipleItems() throws NameTooShortException, ItemPastDeadlineException, ParseException, TooManyUndoneItemsException {
        TodoList todoList;
        todoList = addNItems(5);
        todoList.removeItem(0);
        todoList.removeItem(0);
        assertEquals(3,todoList.getItemCount());
    }

    @Test
    public void testNameTooShortException() {
        try{
            TodoList todoList = new TodoList("New");
            fail("NameTooShortException not thrown");
        }catch (NameTooShortException e){ }
    }

    @Test
    public void testTooManyUndoneItemsException() {
        try{
            TodoList todoList = new TodoList("New list", 2);
            todoList.addItem(new RegularItem("new item 1",dateParser("12-12-3000")));
            todoList.addItem(new RegularItem("new item 2",dateParser("12-12-3000")));
            todoList.addItem(new RegularItem("new item 3",dateParser("12-12-3000")));
            fail("TooManyUndoneItemsException not thrown");
        }catch (NameTooShortException | ItemPastDeadlineException | ParseException e) {
            fail(e.getClass()+" thrown");
        }catch (TooManyUndoneItemsException ignored){}
    }


    private static TodoList addNItems(int n) throws ParseException, ItemPastDeadlineException, TooManyUndoneItemsException, NameTooShortException {
        TodoList l1 = new TodoList();
        for(int i=0; i<n; i++){
            l1.addItem(new RegularItem("buy apples", TodoList.dateParser("12-12-3000")));
        }
        return l1;
    }


}
