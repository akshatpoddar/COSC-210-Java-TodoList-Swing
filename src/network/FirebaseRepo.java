package network;


import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import models.exceptions.NameTooShortException;
import models.exceptions.TodoListAlreadyExistsException;
import models.local.Item;
import models.local.User;
import models.network.ItemNetwork;
import models.network.UserNetwork;
import models.local.TodoList;
import utilities.Mappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FirebaseRepo {

    private static FirebaseRepo firebaseRepo;
    private static Firestore db;
    private User currentUser;
    private TodoList currentTodoList;
    private String currentUserName;
    private String currentTodoListName;

    public static FirebaseRepo getInstance(){
        if(firebaseRepo == null){
            firebaseRepo = new FirebaseRepo();
        }
        return firebaseRepo;
    }

    private FirebaseRepo(){
        FirebaseApi.initialise();
        db = FirestoreClient.getFirestore();
    }

    public void addAllUsersToDB(Collection<User> users){
        for(User user: users)
            addUserToDB(user);
    }

    public void addAllTodoListsToDB(List<TodoList> todoLists){
        deleteAllTodoList(getTodoListsToDelete());
        for(TodoList todoList: getTodoListsToAdd())
            addTodoListToDB(todoList);
    }


    public void deleteUserByUsername(String username){
        db.collection("users").document(username).delete();
    }

    public void addUserToDB(User user){
        UserNetwork userNetwork = Mappers.UserToUserNetwork(user);
        CollectionReference usersCollection = db.collection("users");
        usersCollection.document(userNetwork.getUsername()).set(userNetwork);
    }

    public void addTodoListToDB(TodoList todoList){
        setCurrentTodoList(todoList);
        String listName = todoList.getListName();
        int maxUndoneItemsAllowed = todoList.getMaxTodos();
        CollectionReference todoListCollection = db.collection("users").document(currentUserName).collection("todolists");
        HashMap<String, String> fields = new HashMap<>();
        fields.put("name",listName);
        fields.put("maxUndoneItemsAllowed",String.valueOf(maxUndoneItemsAllowed));
        todoListCollection.document(listName).set(fields);
    }

    public List<Item> getItemsToAdd(TodoList todoList){
        List<Item> networkItems = getAllItemsFromListDB(todoList.getListName());
        List<Item> allItems = todoList.getItems();
        List<Item> clone = cloneItems(allItems);
        clone.removeAll(networkItems);
        return clone;
    }

    public List<Item> getItemsToDelete(TodoList todoList){
        List<Item> networkItems = getAllItemsFromListDB(todoList.getListName());
        List<Item> allItems = todoList.getItems();
        List<Item> clone = cloneItems(networkItems);
        clone.removeAll(allItems);
        return clone;
    }


    public List<Item> cloneItems(List<Item> items){
        List<Item> clone = new ArrayList<>();
        for(Item item: items){
            try {
                clone.add((Item)item.clone());
            }catch (CloneNotSupportedException e){
                System.out.println(e.getMessage());
            }
        }
        return clone;
    }

    public void addItem(Item item){
        CollectionReference todoListCollection = db.collection("users").document(currentUserName).collection("todolists");
        todoListCollection.document(currentTodoListName).collection("items").document(item.getId()).set(item);
    }

    public void addAllItems(List<Item> items){
        for(Item item: items)
            addItem(item);
    }

    public boolean checkListExists(TodoList todoList){
        String listName = todoList.getListName();
        DocumentReference documentReference = db.collection("users").document(currentUserName).collection("todolists")
                .document(listName);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        try {
            DocumentSnapshot document = future.get();
            return document.exists();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: "+e.getMessage());
        }
        return false;
    }

    public boolean checkUserExists(User user){
        UserNetwork userNetwork = Mappers.UserToUserNetwork(user);
        String username = userNetwork.getUsername();
        ApiFuture<DocumentSnapshot> documentSnapshotApiFuture = db.collection("users").document(username).get();
        try {
            DocumentSnapshot documentSnapshot = documentSnapshotApiFuture.get();
            return documentSnapshot.exists();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: "+e.getMessage());
        }
        return false;
    }

    public void setCurrentUser(User user){
        currentUser = user;
        currentUserName = currentUser.getUsername();
    }

    public void setCurrentTodoList(TodoList todoList){
        currentTodoList = todoList;
        currentTodoListName = currentTodoList.getListName();
    }

    public List<String> getTodoListNames(){
        List<String> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("users").document(currentUserName).collection("todolists").get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for(QueryDocumentSnapshot document: documents)
                list.add((String) document.get("name"));
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: "+e.getMessage());
        }
        return list;
    }

    public List<TodoList> getTodoLists(){
        List<TodoList> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("users").document(currentUserName).collection("todolists").get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for(QueryDocumentSnapshot document: documents){
                String listName = (String) document.get("name");
                int maxTodos = Integer.parseInt((String) document.get("maxUndoneItemsAllowed"));
                TodoList todoList = new TodoList(listName, getAllItemsFromListDB(listName), maxTodos);
                list.add(todoList);
            }

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: "+e.getMessage());
        } catch (NameTooShortException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Item> getAllItemsFromListDB(String listName){
        ArrayList<Item> list = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("users").document(currentUserName).collection("todolists")
                .document(listName).collection("items").get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for(QueryDocumentSnapshot document: documents){
                ItemNetwork itemNetwork = document.toObject(ItemNetwork.class);
                Item item = Mappers.ItemNetworkToItem(itemNetwork);
                list.add(item);
            }

        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: "+e.getMessage());
        }
        return list;
    }

    public List<Item> getAllItemsFromListDB(TodoList todoList){
        return getAllItemsFromListDB(todoList.getListName());
    }

    public List<String> getAllUsernamesFromDB(){
        List<String> usernames = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("users").get();
        try{
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for(QueryDocumentSnapshot document: documents){
                usernames.add(document.getId());
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: "+e.getMessage());
        }
        return usernames;
    }

    public List<User> getAllUsersFromDB(){
        List<User> users = new ArrayList<>();
        ApiFuture<QuerySnapshot> future = db.collection("users").get();
        try{
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for(QueryDocumentSnapshot document: documents){
                String username = document.getId();
                String name = (String) document.get("name");
                User user = new User(name, username);
                user.addAllTodoLists(getTodoListsByUserFromDB(user));
                users.add(user);
            }
        } catch (InterruptedException | ExecutionException | TodoListAlreadyExistsException e) {
            System.out.println("Error: "+e.getMessage());
        }
        return users;
    }

    public List<TodoList> getTodoListsByUserFromDB(User user){
        List<TodoList> list = new ArrayList<>();
        String username = user.getUsername();
        setCurrentUser(user);
        ApiFuture<QuerySnapshot> future = db.collection("users").document(username).collection("todolists").get();
        try{
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for(QueryDocumentSnapshot document: documents){
                String listName = document.getId();
                String max = (String) document.get("maxUndoneItemsAllowed");
                int maxTodos = Integer.parseInt(max);
                TodoList todoList = new TodoList(listName, getAllItemsFromListDB(listName), maxTodos);
                todoList.addUser(user);
                list.add(todoList);
            }
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Error: "+e.getMessage());
        } catch (NameTooShortException e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<TodoList> getTodoListsToAdd(){
        List<TodoList> networkLists = getTodoLists();
        List<TodoList> currentLists = currentUser.getUserTodoLists();
        List<TodoList> clone = cloneTodoLists(currentLists);
        clone.removeAll(networkLists);
        return clone;
    }

    public List<TodoList> getTodoListsToDelete(){
        List<TodoList> networkLists = getTodoLists();
        List<TodoList> currentLists = currentUser.getUserTodoLists();
        List<TodoList> clone = cloneTodoLists(networkLists);
        clone.removeAll(currentLists);
        return clone;
    }

    public List<TodoList> cloneTodoLists(List<TodoList> todoLists){
        List<TodoList> clone = new ArrayList<>();
        for(TodoList todoList: todoLists){
            try {
                clone.add((TodoList) todoList.clone());
            }catch (CloneNotSupportedException e){
                System.out.println(e.getMessage());
            }
        }
        return clone;
    }

    public void deleteAllTodoList(List<TodoList> list){
        for(TodoList todoList: list)
            deleteTodoList(todoList);
    }

    public void deleteTodoList(TodoList todoList){
        String listName = todoList.getListName();
        db.collection("users").document(currentUserName).collection("todolists").document(listName).delete();
    }

    public void deleteItem(Item item){
        db.collection("users").document(currentUserName).collection("todolists").document(currentTodoListName)
                .collection("items").document(item.getId()).delete();
    }

    public void deleteAllItems(List<Item> items){
        for(Item item: items)
            deleteItem(item);
    }


}
