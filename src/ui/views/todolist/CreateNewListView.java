package ui.views.todolist;

import models.exceptions.NameTooShortException;
import models.exceptions.TodoListAlreadyExistsException;
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

public class CreateNewListView extends JFrame {

    private JPanel mainPanel;
    private JButton createBtn, menuBtn;
    JTextField nameField, maxUndoneTodosField;

    public static void display(){
        new CreateNewListView();
    }

    public CreateNewListView(){
        super("Make a new todo List");
        setUpDisplay();
    }

    private void setUpDisplay(){
        mainPanel = new JPanel();
        createBtn = new JButton("Create");
        menuBtn = new JButton("Go back to menu");
        nameField = new JTextField();
        maxUndoneTodosField = new JTextField();

        setUpButtonListener();

        JLabel labelName = new JLabel("Name: ");
        labelName.setLabelFor(nameField);
        JLabel labelMax = new JLabel("Maximum undone items allowed: ");
        labelMax.setLabelFor(maxUndoneTodosField);
        JLabel title = new JLabel("Enter list name and max undone items allowed in list: ");

        mainPanel.add(title);
        mainPanel.add(labelName);
        mainPanel.add(nameField);
        nameField.setMaximumSize(new Dimension(300,20));
        mainPanel.add(labelMax);
        mainPanel.add(maxUndoneTodosField);
        mainPanel.add(createBtn);
        mainPanel.add(menuBtn);

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20,20, 20));
        add(mainPanel);
        setSize(400,200);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setUpButtonListener(){
        ActionListener buttonListener = (e) -> {
            JButton ae = (JButton) e.getSource();
            if(ae.equals(createBtn)){
                String name = nameField.getText().trim();
                try{
                    int max = Integer.parseInt(maxUndoneTodosField.getText().trim());
                    if(createNewTodoList(name, max)){
                        dispose();
                        ListSelectionView.display();
                    }
                }catch (NumberFormatException exception){
                    playErrorSound();
                    JOptionPane.showMessageDialog(CreateNewListView.this,
                            "Please enter a number for the maximum undone items allowed.",
                            "Error", JOptionPane.WARNING_MESSAGE);
                }
            }else if(ae.equals(menuBtn)){
                dispose();
                UserSelectionView.display();
            }
        };
        createBtn.addActionListener(buttonListener);
        menuBtn.addActionListener(buttonListener);
    }

    private boolean createNewTodoList(String listName, int max){
        UserManager userManager  = UserManager.getInstance();
        try{
            TodoList todoList = new TodoList(listName, max);
            userManager.addTodoList(todoList);
            TodoRun.setCurrentTodoList(listName);
            return true;
        }catch (NameTooShortException e){
            playErrorSound();
            JOptionPane.showMessageDialog(CreateNewListView.this,
                    "The name of the list is too short. Try again.",
                    "Error", JOptionPane.WARNING_MESSAGE);
        } catch (TodoListAlreadyExistsException e) {
            playErrorSound();
            JOptionPane.showMessageDialog(CreateNewListView.this,
                    "List with the same name already exists. Please enter a different name.",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }
}
