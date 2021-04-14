package ui;

import ui.views.user.login.LoginView;
import utilities.UserManager;
import models.exceptions.*;
import models.local.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static models.local.TodoList.dateParser;

public class TodoRun {

    private static UserManager userManager;
    private static Scanner sc;
    private static TodoList currentList;

    public static void launch(){
        init();
        LoginView.display();
    }

    public static void init(){
        sc = new Scanner(System.in);
        userManager = UserManager.getInstance();
    }

    public static void deleteList(int index){
        userManager.deleteTodoList(index);
//        System.out.print("Enter 'R' to remove all lists. Enter anything else to remove only one list: ");
//        String str = sc.next().toUpperCase();
//        if(str.equals("R")) userManager.deleteAllTodoLists();
//        else{
//            System.out.print("Enter the list number to remove from list: ");
//            int index;
//            index = sc.nextInt();
//            userManager.deleteTodoList(index-1);
//        }
    }

    public static void deleteTodoItem(int index) {
        currentList.removeItem(index);
    }

    public static List<String> getAllNamesOfUsers(){
        return userManager.getAllNamesOfUsers();
    }

    public static TodoList getCurrentTodoList(){
        return currentList;
    }

    public static List<Item> getAllItems(){
        return currentList.getAllItems();
    }

    public static List<String> getAllListNames(){
        return userManager.getAllNamesOfTodoLists();
    }

    public static void setCurrentTodoList(String listName){
        currentList = userManager.getAndSetCurrentList(listName);
    }

    public static void save() {
        try {
            userManager.save();
            System.out.println("The data has been saved.");
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            System.out.println(e.getClass() + " " + e.getMessage());
        }
    }

    //CLI functions or not used in GUI

    public static void greeting () {
        //System.out.println("Welcome to the Todo List Application!");
        login();
    }

    public static void login() {
        LoginView.display();
//        System.out.println("Are you an existing user or a new user?");
//        System.out.print("Enter 'E' for existing user and anything else for new user: ");
//        String str = sc.next();
//        sc.nextLine();
//        if(str.equalsIgnoreCase("E")){
//            loginExistingUser();
//        }else{
//            createNewUser();
//        }
    }

    public static void loginExistingUser(){
//        System.out.print("Enter your username: ");
//        String username = sc.next();
//        boolean done = false;
//        while(!done){
//            try{
//                if(username.equals("0")){
//                    sc.nextLine();
//                    createNewUser();
//                    return;
//                }
//                userManager.setCurrentUser(userManager.getUserByUsername(username));
//                System.out.println("Welcome back "+username);
//                done = true;
//                sc.nextLine();
//                chooseUserOptions();
//            } catch (UserDoesNotExistException e){
//                System.out.printf("Please enter your username. No users with the username %s exist.\n",username);
//                System.out.print("Enter your correct username or enter 0 for creating a new user: ");
//                username = sc.next();
//            }
//        }
//    }
//
//    public static void createNewUser(){
//        System.out.print("Enter your name: ");
//        String name = sc.nextLine();
//        System.out.print("Create your username: ");
//        String username = sc.next();
//        System.out.print("Enter maximum number of undone items allowed in your list: ");
//        int n = sc.nextInt();
//        boolean done = false;
//        while(!done){
//            try{
//                userManager.addUser(name, username, n);
//                System.out.println("Welcome "+username);
//                done = true;
//                sc.nextLine();
//                chooseUserOptions();
//            }catch (UserAlreadyExistsException e){
//                System.out.println("Please enter a different username. A user with the same username already exists.");
//                System.out.print("Create a different username: ");
//                username = sc.next();
//            }
//        }
    }

    public static void logout(){
        try {
            userManager.setCurrentUser(null);
        } catch (UserDoesNotExistException e) {
            System.out.println(e.getClass()+": "+e.getMessage());
        }
        System.out.println("Logged out.");
    }

    public static void deleteUser(){
        userManager.deleteCurrentUser();
        System.out.println("Your account has been deleted!");
        logout();
        login();
    }

    public static void chooseUserOptions(){
        //displayUserOptions();
//        int in = 1;
//        while(in!=0){
//            displayUserOptions();
//            in = selectChoice();
//            sc.nextLine();
//            switch (in){
//                case 0:
//                    farewell();
//                    break;
//                case 1:
//                    createNewList();
//                    break;
//                case 2:
//                    deleteList();
//                    break;
//                case 3:
//                    displayAllLists();
//                    break;
//                case 4:
//                    setCurrentTodoList();
//                    break;
//                case 5:
//                    save();
//                    break;
//                case 6:
//                    deleteUser();
//                    break;
//                case 7:
//                    displayAllUsers();
//                    break;
//                default:
//                    System.out.println("Wrong input!");
//            }
//            System.out.println();
//        }
    }

    public static void displayUserOptions() {
//        System.out.println("What would you like to do?");

//        for(int i=0; i<userOptions.length-1; i++){
//            System.out.printf("[%d] %s\n", i+1, userOptions[i]);
//        }
//        System.out.println("[0] "+userOptions[userOptions.length-1]);
//        System.out.println("[1] create a new list," +
//                " [2] delete a list" +
//                " [3] show all your lists" +
//                " [4] access one of your lists" +
//                " [5] Save all changes" +
//                " [6] Delete account" +
//                " [7] Show all users" +
//                " [0] exit");
    }

    public static void displayListOptions(){
        System.out.println("What would you like to do?");
        String[] listOptions = {"Create a new item", "Delete an item", "Show all items in the list","Access one of your lists","Go back to main menu"};
        for(int i=0; i<listOptions.length-1; i++){
            System.out.printf("[%d] %s\n", i+1, listOptions[i]);
        }
        System.out.println("[0] "+listOptions[listOptions.length-1]);
    }

    public static void farewell(){
        System.out.println();
        System.out.println("You are leaving the ToDo List Application. Are you sure you don't want to save your list?");
        System.out.print("Enter 'Y' to quit without saving. Enter anything else to save and quit: ");
        String str = sc.next();
        if(!str.equalsIgnoreCase("Y")) save();
        System.out.println("Thank you for using the ToDo List Application!");
        sc.close();
    }

    public static int selectChoice(){
        int choice = 0;
        boolean done = false;
        System.out.print("Enter your option: ");
        while(!done){
            try{
                choice = sc.nextInt();
                done = true;
            }catch (InputMismatchException e){
                System.out.print("Incorrect input format! Please enter a number: ");
                sc.nextLine();
            }
        }
        return choice;
    }

    public static void accessList(String listname){
//        setCurrentTodoList(listname);
//        System.out.println("You are accessing list "+currentList.getListName());
//        int in = 1;
//        while(in!=0){
//            displayListOptions();
//            in = selectChoice();
//            sc.nextLine();
//            switch (in){
//                case 0:
//                    System.out.println("Returning to main menu.");
//                    break;
//                case 1:
//                    addTodoItem();
//                    break;
//                case 2:
//                    deleteTodoItem();
//                    break;
//                case 3:
//                    displayAllItems();
//                    break;
//                default:
//                    System.out.println("Wrong input!");
//            }
//            System.out.println();
//        }
    }

    public static void displayAllItems(){
        List<Item> items = currentList.getAllItems();
        System.out.println("List of items: ");
        for(int i=0; i<items.size(); i++){
            System.out.println((i+1)+". "+items.get(i));
        }
        if(items.size()==0) System.out.println("The list is empty!");
    }

    public static void displayAllLists(){
        List<TodoList> todoLists = userManager.getAllTodoLists();
        //List<String> listNames = new ArrayList<>();
//        if(todoLists.size()==0){
//            listNames.add("The list is empty!");
//        }
//        for(int i=0; i<todoLists.size(); i++){
//            String s= ((i+1)+". "+todoLists.get(i));
//            listNames.add(s);
//        }

//        ListView.display(listNames);
        System.out.println("List of your todo-lists: ");
        if(!todoLists.isEmpty()){
            for(int i=0; i<todoLists.size(); i++){
                TodoList todoList = todoLists.get(i);
                System.out.println((i+1)+". "+todoList.getListName());
                System.out.println(todoList.getAllItems());
                System.out.println("--------------");
            }
        }
        else System.out.println("No lists created yet!");
    }

    public static void displayAllUsers(){
        List<String> usersNames = userManager.getAllNamesOfUsers();
        System.out.println("List of users: ");
        for(int i=0; i<usersNames.size(); i++){
            System.out.println((i+1)+". "+usersNames.get(i));
        }
        if(usersNames.size()==0) System.out.println("The list is empty!");
    }

    public static void createNewList(){
        boolean done = false;
        while(!done){
            try{
                System.out.println("Please enter the name of this new list and the maximum number of undone todos allowed: ");
                System.out.print("Please enter the name of this new list: ");
                String listName = sc.nextLine();
                System.out.print("Please enter the maximum number of undone todos allowed in this list: ");
                int max = sc.nextInt();
                TodoList todoList = new TodoList(listName, max);
                if(userManager.todoListExists(todoList)){
                    System.out.print("You already have a list with the same name! Please try again: ");
                }else{
                    System.out.println("New list called "+listName+" has been created!");
                    userManager.addTodoList(todoList);
                    setCurrentTodoList(listName);
                    done = true;
                }
            }catch (NameTooShortException e){
                System.out.println("The name of the list is too short. Try again. ");
            }catch (InputMismatchException e){
                System.out.println("Please enter a number. Try again. ");
            } catch (TodoListAlreadyExistsException e) {
                System.out.println("List with the same name already exist. Try again: ");
            }
        }
    }

    public static void setCurrentTodoList(){
        System.out.print("Enter name of your list: ");
        String listName;
        boolean done = false;
        while(!done){
            try{
                listName = sc.nextLine();
                TodoList todoList = new TodoList(listName);
                if(userManager.todoListExists(todoList)){
                    setCurrentTodoList(listName);
                    done = true;
                }else{
                    System.out.print("The list with the name you entered does not exist. Please try again: ");
                }
            }catch (NameTooShortException e){
                System.out.println("The name of the list is too short. Try again: ");
            }
        }
    }

    public static void addTodoItem() {
        System.out.println("Enter the item details to add to list: ");
        System.out.print("Enter task name: ");
        String taskName = sc.nextLine();
        System.out.print("Enter 1 for urgent task. Enter anything else for regular task: ");
        String urgent = sc.next();
        System.out.print("Enter task deadline (dd-MM-yyyy): ");
        Date d;
        boolean done = false;
        while(!done){
            try{
                String deadline = sc.next();
                d = dateParser(deadline);
                Item item;
                if (urgent.equals("1")) {
                    item = new UrgentItem(taskName, d);
                } else {
                    item = new RegularItem(taskName, d);
                }
                currentList.addItem(item);
                System.out.println("Item has been added to your list!");
                done = true;
            }catch (ParseException e) {
                System.out.print("Incorrect date format entered! Try adding again: ");
            }catch (ItemPastDeadlineException e){
                System.out.print("The deadline you entered has already passed. Try adding again: ");
            }catch (TooManyUndoneItemsException e){
                System.out.println("Item cannot be added. Too many undone items in your list.");
                break;
            }
        }
    }

    public static void deleteTodoItem(){
        System.out.print("Enter 'R' to remove all items. Enter anything else to remove only one item: ");
        String str = sc.next().toUpperCase();
        if(str.equals("R")){
            if(currentList.removeAllItems()) System.out.println("All items have been removed from the list.");
            else System.out.println("List is already empty!");
        }
        else{
            System.out.print("Enter the item number to remove from list: ");
            int index;
            index = sc.nextInt();
            if(currentList.removeItem(index-1)){
                System.out.println("Item removed from list.");
            }else System.out.println("Item does not exist.");
        }
    }

}
