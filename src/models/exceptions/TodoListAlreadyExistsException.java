package models.exceptions;

public class TodoListAlreadyExistsException extends Exception{

    public TodoListAlreadyExistsException(){
        super("Todo list with the same name already exists!");
    }
}
