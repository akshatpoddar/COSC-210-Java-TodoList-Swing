package ui.views.user.login;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {

    JLabel welcomeLabel, loginLabel;
    JPanel panel;
    JButton button1, button2;

    //EFFECTS: Displays a window which allows the user to select from two login options and then calls a method depending on that
    public static void display(){
        new LoginView();
    }

    public LoginView(){
        super("Login to your account");
        setUpDisplay();
    }

    private void setUpDisplay(){
        button1 = new JButton("Existing User");
        button2 = new JButton("New User");
        setUpButtonListener();
        welcomeLabel = new JLabel("Welcome to the Todo List Application by Akshat Poddar");
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 22));
        welcomeLabel.setBorder(new EmptyBorder(10,10,10,10));
        loginLabel = new JLabel("Are you an existing user or a new user?");
        loginLabel.setBorder(new EmptyBorder(10,0,10,0));
        panel = new JPanel();
        panel.add(welcomeLabel);
        panel.add(loginLabel);
        panel.add(button1);
        panel.add(button2);
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new EmptyBorder(5,10,10,5));
        add(panel);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setUpButtonListener(){
        ActionListener buttonListener = (e) -> {
            JButton ae = (JButton) e.getSource();
            if(ae.equals(button1)){
                dispose();
                //TodoRun.loginExistingUser();
                ExistingUserLoginView.display();
            }else if(ae.equals(button2)){
                dispose();
                //TodoRun.createNewUser();
                NewUserLoginView.display();
            }
        };
        button1.addActionListener(buttonListener);
        button2.addActionListener(buttonListener);
    }
}
