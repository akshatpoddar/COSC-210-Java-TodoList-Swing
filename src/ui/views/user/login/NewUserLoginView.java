package ui.views.user.login;

import models.exceptions.UserAlreadyExistsException;
import ui.views.selection.UserSelectionView;
import utilities.UserManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

import static ui.views.SoundPlayer.playErrorSound;

public class NewUserLoginView extends JFrame{

    JPanel mainPanel;
    JLabel loginLabel;
    JTextField usernameField, nameField;
    JButton registerBtn, loginBtn;
    UserManager userManager = UserManager.getInstance();

    //EFFECTS: Displays a window with 3 input boxes and a log in button and creates a new user
    public static void display(){
        new NewUserLoginView();
    }

    public NewUserLoginView(){
        super("Create your account");
        setUpDisplay();
    }

    private void setUpDisplay(){
        registerBtn = new JButton("Register");
        loginBtn = new JButton("Login as existing user");
        setUpButtonListener();
        loginLabel = new JLabel("Enter your name, username and maxUndoneItems to login");
        nameField = new JTextField();
        nameField.setMaximumSize(new Dimension(300,20));
        JLabel labelName = new JLabel("Name: ");
        labelName.setLabelFor(nameField);
        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(300,20));
        JLabel labelUsername = new JLabel("Username: ");
        labelUsername.setLabelFor(usernameField);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(loginLabel);
        mainPanel.add(labelName);
        mainPanel.add(nameField);
        mainPanel.add(labelUsername);
        mainPanel.add(usernameField);
        mainPanel.add(registerBtn);
        mainPanel.add(loginBtn);
        mainPanel.setBorder(new EmptyBorder(20, 50, 0, 0));
        add(mainPanel);
        setSize(500,200);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setUpButtonListener(){
        ActionListener buttonListener = (e) -> {
            JButton ae = (JButton) e.getSource();
            if(ae.equals(registerBtn)){
                String name = nameField.getText().trim();
                String username = usernameField.getText().trim();
                if(createNewUser(name, username)){
                    JOptionPane.showMessageDialog(NewUserLoginView.this,
                            "Welcome "+userManager.getCurrentUser().getName());
                    dispose();
                    UserSelectionView.display();
                }
            }else if(ae.equals(loginBtn)){
                dispose();
                ExistingUserLoginView.display();
            }
        };
        registerBtn.addActionListener(buttonListener);
        loginBtn.addActionListener(buttonListener);
    }

    private boolean createNewUser(String name, String username){
        try{
            userManager.addUser(name, username);
            return true;
        }catch (UserAlreadyExistsException e){
            playErrorSound();
            JOptionPane.showMessageDialog(NewUserLoginView.this, "Username already exists. Please create a new username",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

}
