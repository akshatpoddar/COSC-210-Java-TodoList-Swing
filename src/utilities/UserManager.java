package utilities;

import models.exceptions.TodoListAlreadyExistsException;
import models.local.TodoList;
import models.local.User;
import models.exceptions.UserAlreadyExistsException;
import models.exceptions.UserDoesNotExistException;
import network.FirebaseRepo;

import java.io.*;
import java.util.*;

public class UserManager implements Loadable, Savable {

    private HashMap<String, User> users;
    private User currentUser;
    private TodoList currentTodoList;
    FirebaseRepo firebaseRepo = FirebaseRepo.getInstance();
    private static UserManager userManager = null;

    private UserManager(){
        load();
    }

    public static UserManager getInstance(){
        if(userManager == null)
            userManager = new UserManager();
        return userManager;
    }

    public void addUser(String name, String username) throws UserAlreadyExistsException{
        if (userExists(username)){
            throw new UserAlreadyExistsException();
        }else{
            User user = new User(name, username);
            users.put(username, user);
            currentUser = user;
        }
    }

    public void addUser(User user) throws UserAlreadyExistsException{
        if (userExists(user.getUsername())){
            throw new UserAlreadyExistsException();
        }else{
            users.put(user.getUsername(), user);
            currentUser = user;
        }
    }

    public boolean userExists(String username) {
        return users.containsKey(username);
    }


    public void setCurrentUser(User user) throws UserDoesNotExistException {
        if(user == null) {
            currentUser = null;
            return;
        }
        if(userExists(user.getUsername())){
            currentUser = users.get(user.getUsername());
            firebaseRepo.setCurrentUser(currentUser);
        }else{
            throw new UserDoesNotExistException();
        }
    }



    public void setCurrentTodoList(TodoList todoList) {
        currentTodoList = todoList;
    }

    public void addTodoList(TodoList todoList) throws TodoListAlreadyExistsException {
        currentUser.addTodoList(todoList);
    }


    public boolean todoListExists(TodoList todoList){
        return currentUser.todoListExists(todoList);
    }


    public List<TodoList> getAllTodoLists(){
        return currentUser.getUserTodoLists();
    }

    public List<String> getAllNamesOfTodoLists(){
        List<String> todoListsNamesList = new ArrayList<>();
        List<TodoList> allLists = getAllTodoLists();
        for(TodoList todoList: allLists)
            todoListsNamesList.add(todoList.getListName());
        return todoListsNamesList;
    }

    public void deleteAllTodoLists(){
        currentUser.deleteAllLists();
    }

    public void deleteTodoList(int index){
        currentUser.deleteList(index);
    }

    public void deleteCurrentUser(){
        String username = currentUser.getUsername();
        users.remove(username);
        firebaseRepo.deleteUserByUsername(username);
    }

    public void deleteUserByUsername(String username){
        users.remove(username);
        firebaseRepo.deleteUserByUsername(username);
    }

    public TodoList getAndSetCurrentList(String listName){
        setCurrentTodoList(currentUser.getListByName(listName));
        return currentUser.getListByName(listName);
    }

    public List<String> getAllNamesOfUsers(){
        List<String> usersNameList = new ArrayList<>();
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(users.values());
        for(User user: allUsers)
            usersNameList.add(user.getName());
        return usersNameList;
    }

    public List<User> getAllUsers(){
        List<User> userList = new ArrayList<>();
        userList.addAll(users.values());
        return userList;
    }

    public User getUserByUsername(String username) throws UserDoesNotExistException{
        if(userExists(username)){
            User user = users.get(username);
            return user;
        }else{
            throw new UserDoesNotExistException();
        }
    }

    public User getCurrentUser(){
        return currentUser;
    }


    public int getCountTodoLists(){
        return currentUser.getTodoListCount();
    }

    @Override
    public void load() {
        //loadFromFile();
        loadFromFirebaseDB();
    }

//    public void loadFromFile() throws FileNotFoundException, IOException, ClassNotFoundException {
//        try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream("./data/data.dat")))){
//            users = (HashMap<String, User>) in.readObject();
//            System.out.println("The data has been loaded from a pre-existing file.");
//            return;
//        } catch (FileNotFoundException e) {
//            throw new FileNotFoundException();
//            System.out.println("No pre-existing file found. Starting with a clean slate!");
//        } catch (IOException e) {
//            throw new IOException();
//            System.out.println(e.getClass()+": "+e.getMessage());
//        }catch(ClassNotFoundException e){
//            throw new ClassNotFoundException();
//            System.out.println(e.getClass()+": "+e.getMessage());
//
//        }
//        users = new HashMap<>();
//    }

    public void loadFromFirebaseDB(){
        List<String> usernames = firebaseRepo.getAllUsernamesFromDB();
        List<User> allUser = firebaseRepo.getAllUsersFromDB();
        HashMap<String, User> userMap = new HashMap<>();
        for(int i=0; i<usernames.size(); i++){
            userMap.put(usernames.get(i), allUser.get(i));
        }
        users = userMap;
    }

    @Override
    public void save() throws FileNotFoundException, IOException {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("./data/data.dat")))){
            //saveToFile();
            saveToFirebaseDB();
        }catch (FileNotFoundException e){
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        }
    }

    public void saveToFile() throws IOException {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("./data/data.dat")))){
            out.writeObject(users);
        }catch (FileNotFoundException e){
            throw new FileNotFoundException();
        } catch (IOException e) {
            throw new IOException();
        }
    }

    public void saveToFirebaseDB(){
        List<User> userList = getAllUsers();
        firebaseRepo.addAllUsersToDB(userList);
        for(User user: userList){
            firebaseRepo.setCurrentUser(user);
            for(TodoList todoList: user.getUserTodoLists()) {
                firebaseRepo.setCurrentTodoList(todoList);
                firebaseRepo.deleteAllItems(firebaseRepo.getItemsToDelete(todoList));
                firebaseRepo.addAllItems(firebaseRepo.getItemsToAdd(todoList));
            }
            firebaseRepo.addAllTodoListsToDB(user.getUserTodoLists());
        }
    }
}
