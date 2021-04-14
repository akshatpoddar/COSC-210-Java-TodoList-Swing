package ui.views.todolist;

import models.exceptions.NameTooShortException;
import models.local.TodoList;
import ui.TodoRun;
import ui.views.selection.ListSelectionView;
import ui.views.selection.UserSelectionView;
import utilities.UserManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;

import static ui.views.SoundPlayer.playErrorSound;

public class AccessListView extends JFrame {

    private JPanel mainPanel;
    private JButton accessBtn;
    JTextField nameField;
    private JButton menuBtn;

    public static void display(){
        new AccessListView();
    }

    public AccessListView(){
        super("Access a todo list");
        setUpDisplay();
    }

    private void setUpDisplay(){
        mainPanel = new JPanel();
        accessBtn = new JButton("Access");
        nameField = new JTextField();
        menuBtn = new JButton("Go back to menu");

        setUpButtonListener();
        JLabel labelName = new JLabel("Name: ");
        labelName.setLabelFor(nameField);
        JLabel title = new JLabel("Enter list name to access list: ");
        nameField.setMaximumSize(new Dimension(300,20));

        mainPanel.add(title);
        mainPanel.add(labelName);
        mainPanel.add(nameField);
        mainPanel.add(accessBtn);
        mainPanel.add(menuBtn);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(mainPanel);
        setSize(400,200);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setUpButtonListener(){
        ActionListener buttonListener = (e) -> {
            JButton ae = (JButton) e.getSource();
            if(ae.equals(accessBtn)){
                String name = nameField.getText().trim();
                if(accessTodoList(name)){
                    dispose();
                    ListSelectionView.display();
                }
            }else if(ae.equals(menuBtn)){
                dispose();
                UserSelectionView.display();
            }
        };
        accessBtn.addActionListener(buttonListener);
        menuBtn.addActionListener(buttonListener);
    }

    private boolean accessTodoList(String listName){
        UserManager userManager  = UserManager.getInstance();
        try{
            TodoList todoList = new TodoList(listName);
            if(userManager.todoListExists(todoList)){
                TodoRun.setCurrentTodoList(listName);
                return true;
            }else{
                playErrorSound();
                JOptionPane.showMessageDialog(AccessListView.this,
                        "The list with the name you entered does not exist. Try again.",
                        "Error", JOptionPane.WARNING_MESSAGE);
            }
        }catch (NameTooShortException e){
            playErrorSound();
            JOptionPane.showMessageDialog(AccessListView.this,
                    "The name of the list is too short. Try again.",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }
}
