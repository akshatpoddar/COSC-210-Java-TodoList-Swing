package models;

import models.local.Item;
import models.local.RegularItem;
import models.exceptions.ItemPastDeadlineException;
import org.junit.jupiter.api.Test;
import models.local.TodoList;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    public void testItemInit() throws ParseException, ItemPastDeadlineException {
        Item item = new RegularItem("Buy milk", TodoList.dateParser("12-12-3000"));
        assertFalse(item.getCompleted());
        assertFalse(item.isPastDeadline());
    }

    @Test
    public void testItemMarkCompleted() throws ParseException, ItemPastDeadlineException {
        Item item = new RegularItem("Buy milk",TodoList.dateParser("12-12-3000"));
        assertFalse(item.getCompleted());

        item.completeItem();
        assertTrue(item.getCompleted());
    }

    @Test
    public void testItemPastDeadlineException(){
        try{
            Item item = new RegularItem("Buy milk",TodoList.dateParser("12-12-2002"));
            fail("ItemPastDeadlineException not thronw");
        }catch (ParseException e){
            fail(e.getClass()+" thrown");
        }catch (ItemPastDeadlineException e){}
    }

    @Test
    public void testItemDateParseException(){
        try{
            Item item = new RegularItem("Buy milk",TodoList.dateParser("127-12-2023"));
            fail("ParseException not thronw");
        }catch (ItemPastDeadlineException e){
            fail(e.getClass()+" thrown");
        }catch (ParseException e){}
    }

}
