package models.local;


import com.google.cloud.firestore.annotation.Exclude;
import models.exceptions.NameTooShortException;
import models.exceptions.TooManyUndoneItemsException;
import utilities.Subject;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TodoList extends Subject implements Serializable, Cloneable {

    private String listName;
    private List<Item> items;
    private User user;
    private int maxTodos;
    private static int count = 0;

    public TodoList(String listName, List<Item> itemsToBeAdded, int maxTodos) throws NameTooShortException {
        setListName(listName);
        setMaxTodos(maxTodos);
        this.items = itemsToBeAdded;
        count++;
    }

    public TodoList(String listName, int maxTodos) throws NameTooShortException {
        this(listName, new ArrayList<>(), maxTodos);
    }

    public TodoList(String name) throws NameTooShortException {
        this(name, new ArrayList<>(), 10);
    }

    public TodoList() throws NameTooShortException {
        this("NewList "+count, new ArrayList<>(), 10);
    }

    public void toggleItem(Item item){
        items.get(items.indexOf(item)).toggleCompleted();
        try{
            maxTodosCrossed();
            notifyObservers(false);
        }catch (TooManyUndoneItemsException e){
            notifyObservers(true);
        }
    }

    public void setListName(String name) throws NameTooShortException{
        if(name.length()>=5) listName = name;
        else{
            throw new NameTooShortException();
        }
    }

    public List<Item> getItems(){
        return items;
    }

    public int calculateUndoneTodos(){
        int c = 0;
        for(Item item: items){
            if(!item.getCompleted()) c++;
        }
       return c;
    }

    public void maxTodosCrossed() throws TooManyUndoneItemsException {
        if (calculateUndoneTodos()>maxTodos){
            throw new TooManyUndoneItemsException();
        }
    }

    public boolean canItemBeAdded(){
        return calculateUndoneTodos() < maxTodos;
    }

    public List<Item> getAllItems() {
        return items;
    }

    public void addItem(Item todo) throws TooManyUndoneItemsException {
        if(canItemBeAdded()){
            items.add(todo);
        }else{
            throw new TooManyUndoneItemsException();
        }
    }

    public boolean removeItem(Item todo){
        boolean exists = items.contains(todo);
        if(exists){
            items.remove(todo);
            return true;
        }
        return false;
    }

    public boolean removeItem(int index){
        boolean exists = index<items.size();
        if(exists){
            items.remove(index);
            return true;
        }
        return false;
    }

    public boolean removeAllItems(){
        if(items.size() != 0){
            items.clear();
            return true;
        }
        return false;
    }

    public Item getItemByPos(int n){
        return items.get(n);
    }

    public int getMaxTodos() {
        return maxTodos;
    }

    public void setMaxTodos(int maxTodos) {
        this.maxTodos = maxTodos;
    }

    @Exclude
    public int getItemCount(){
        return items.size();
    }

    public String getListName() {
        return listName;
    }

    public void addUser(User user){
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TodoList)) return false;
        TodoList todoList = (TodoList) o;
        return listName.equals(todoList.listName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listName);
    }

    @Override
    public String toString() {
        return listName + ": maximum undone items allowed: "+maxTodos;
    }

    public static Date dateParser(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        formatter.setLenient(false);
        return formatter.parse(date);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
