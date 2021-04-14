package ui.views.selection;

import models.exceptions.UserDoesNotExistException;

import ui.views.user.UserListView;
import ui.views.todolist.CreateNewListView;
import ui.views.todolist.TodoListView;
import ui.TodoRun;
import ui.views.user.login.LoginView;
import utilities.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserSelectionView extends JFrame {

    private final List<JButton> buttons;
    JPanel mainPanel;
    UserManager userManager = UserManager.getInstance();

    public static void display(){
        new UserSelectionView(getUserOptions());
    }

    public UserSelectionView(List<String> options){
        super("Select one of the options");
        buttons = new ArrayList<>();
        for(String option: options)
            buttons.add(new JButton(option));
        setUpButtonListener();
        setUpDisplay();
    }

    public static List<String> getUserOptions(){
        String[] userOptionsArray = {"Create a new list",
                "Show all your lists",
                "Save all changes",
                "Delete Account",
                "Show all users",
                "Logout"};
        return Arrays.asList(userOptionsArray);
    }

    private void setUpDisplay(){
        mainPanel = new JPanel(new GridLayout(buttons.size(), 1, 0 , 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        for (JButton button : buttons) {
            mainPanel.add(button);
        }
        add(mainPanel);
        setSize(600,800);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleClose();
            }
        });
    }

    private void handleClose(){
        dispose();
        saveOption();
        JOptionPane.showMessageDialog(UserSelectionView.this,"Thank you for using this application! See you again!");
        System.exit(0);
    }

    private void setUpButtonListener(){
        ActionListener buttonListener = (e) -> {
            JButton ae = (JButton) e.getSource();
            if(ae.equals(buttons.get(0))){
                dispose();
                CreateNewListView.display();
            }else if(ae.equals(buttons.get(1))){
                dispose();
                TodoListView.display();
            }else if(ae.equals(buttons.get(2))){
                TodoRun.save();
                JOptionPane.showMessageDialog(UserSelectionView.this, "All data has been saved!");
            }else if(ae.equals(buttons.get(3))){
                dispose();
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete your account?", "Warning", dialogButton);
                if(dialogResult == dialogButton) {
                    deleteUser();
                }
            }else if(ae.equals(buttons.get(4))){
                dispose();
                UserListView.display();
            }else if(ae.equals(buttons.get(5))){
                saveOption();
                logout();
                dispose();
                LoginView.display();
            }
        };
        for(JButton button: buttons)
            button.addActionListener(buttonListener);
    }

    private void logout(){
        JOptionPane.showMessageDialog(UserSelectionView.this,
                "You have been logged out of your account "+userManager.getCurrentUser().getUsername());
        try {
            userManager.setCurrentUser(null);
        }catch (UserDoesNotExistException e) {
            System.out.println(e.getClass()+": "+e.getMessage());
        }
    }

    private void deleteUser() {
        String username = userManager.getCurrentUser().getUsername();
        logout();
        JOptionPane.showMessageDialog(UserSelectionView.this,
                "Your account has been deleted " + username);
        userManager.deleteUserByUsername(username);
        dispose();
        LoginView.display();
    }

    private void saveOption(){
        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(UserSelectionView.this, "Would you like to save your data?", "Warning", dialogButton);
        if(dialogResult == dialogButton) {
            TodoRun.save();
        }
    }
}
