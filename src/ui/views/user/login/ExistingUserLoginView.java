package ui.views.user.login;

import models.exceptions.UserDoesNotExistException;
import ui.views.selection.UserSelectionView;
import utilities.UserManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

import static ui.views.SoundPlayer.playErrorSound;

public class ExistingUserLoginView extends JFrame {

    JPanel panel;
    JLabel loginLabel;
    JTextField usernameField;
    JButton loginBtn, registerBtn;
    UserManager userManager = UserManager.getInstance();

    //EFFECTS: Displays a window with 1 input box and a log button and logs in an existing user
    public static void display(){
        new ExistingUserLoginView();
    }

    public ExistingUserLoginView(){
        super("Login to your account");
        setUpDisplay();
    }

    private void setUpDisplay(){
        loginBtn = new JButton("Login");
        registerBtn = new JButton("Register as new user");
        setUpButtonListener();
        loginLabel = new JLabel("Enter your username to login: ");
        usernameField = new JTextField();
        usernameField.setMaximumSize(new Dimension(300,20));
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(loginLabel);
        panel.add(usernameField);
        panel.add(loginBtn);
        panel.add(registerBtn);
        panel.setBorder(new EmptyBorder(20, 50, 0, 0));
        add(panel);
        setSize(400,200);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setUpButtonListener(){
        ActionListener buttonListener = (e) -> {
            JButton ae = (JButton) e.getSource();
            if(ae.equals(loginBtn)){
                String username = usernameField.getText().trim();
                if(loginExistingUser(username)){
                    JOptionPane.showMessageDialog(ExistingUserLoginView.this,
                            "Welcome back "+userManager.getCurrentUser().getName());
                    dispose();
                    UserSelectionView.display();
                }
            }else if(ae.equals(registerBtn)){
                dispose();
                NewUserLoginView.display();
            }
        };
        loginBtn.addActionListener(buttonListener);
        registerBtn.addActionListener(buttonListener);
    }


    private boolean loginExistingUser(String username){
        try{
            userManager.setCurrentUser(userManager.getUserByUsername(username));
            return true;
        } catch (UserDoesNotExistException e) {
            playErrorSound();
            JOptionPane.showMessageDialog(ExistingUserLoginView.this, "No user with this username exists.",
                    "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

}
