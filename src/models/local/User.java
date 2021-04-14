package models.local;

import models.exceptions.TodoListAlreadyExistsException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String name;
    private String username;
    private ArrayList<TodoList> userTodos;

    public User(String name, String username) {
        this.name = name;
        this.username = username;
        userTodos = new ArrayList<>();
    }

    public User(String username) {
        this("Test User",username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void addTodoList(TodoList todoList) throws TodoListAlreadyExistsException {
        boolean contains = userTodos.contains(todoList);
        if(contains) throw new TodoListAlreadyExistsException();
        userTodos.add(todoList);
        todoList.addUser(this);
    }

    public void addAllTodoLists(List<TodoList> todoLists) throws TodoListAlreadyExistsException{
        for(TodoList todoList: todoLists)
            addTodoList(todoList);
    }

    public TodoList getListByName(String listname){
        for(TodoList tl : userTodos){
            if(tl.getListName().equals(listname)) return tl;
        }
        return null;
    }

    public int getTodoListCount(){
        return userTodos.size();
    }

    public ArrayList<TodoList> getUserTodoLists(){
        return userTodos;
    }

    public boolean todoListExists(TodoList todoList){
        return userTodos.contains(todoList);
    }

    public void deleteAllLists(){
        userTodos.clear();
    }

    public void deleteList(int index){
        boolean exists = index<userTodos.size();
        if(exists){
            userTodos.remove(index);
            System.out.println("List removed.");
        }else{
            System.out.println("Index does not exist.");
        }    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return (username.hashCode());
    }
}
