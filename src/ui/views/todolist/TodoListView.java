package ui.views.todolist;

import models.exceptions.NameTooShortException;
import models.local.TodoList;
import ui.TodoRun;
import ui.views.selection.ListSelectionView;
import ui.views.selection.UserSelectionView;
import utilities.UserManager;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import static ui.views.SoundPlayer.playErrorSound;

public class TodoListView extends JFrame {

    private List<String> listNames;
    private List<JLabel> labels;
    private List<JButton> delButtons;
    private List<JButton> accessButtons;
    private JPanel mainPanel;
    private JButton menuBtn;

    public static void display() {
        List<String> names = TodoRun.getAllListNames();
        if(names.isEmpty()){
            new TodoListView();
        }else{
            new TodoListView(names);
        }
    }

    public TodoListView(){
        super("List of todo lists");
        init();
        JLabel jLabel= new JLabel("No lists to display!");
        mainPanel = new JPanel();
        mainPanel.add(jLabel);
        menuBtn = new JButton("Go back to menu");
        mainPanel.add(menuBtn);
        setUpButtonListener();
        add(mainPanel);
        setSize(new Dimension(200,300));
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame. DISPOSE_ON_CLOSE);
    }

    public TodoListView(List<String> listnames){
        super("List of todo lists");
        this.listNames = listnames;
        init();
        for(String name: listNames) {
            JButton jButton = new JButton("Delete");
            jButton.setMaximumSize(new Dimension(100, 60));
            delButtons.add(jButton);
            JButton button = new JButton("Access");
            jButton.setMaximumSize(new Dimension(100, 60));
            accessButtons.add(button);
            JLabel label = new JLabel(name);
            labels.add(label);
        }
        setUpPanel();
    }

    private void setUpPanel(){
        menuBtn = new JButton("Go back to menu");
        setUpButtonListener();
        mainPanel = new JPanel(new GridLayout(listNames.size(),3,20,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        for(int i=0; i< listNames.size(); i++){
            mainPanel.add(labels.get(i), i, 0);
            mainPanel.add(delButtons.get(i), i, 1);
            mainPanel.add(accessButtons.get(i), i, 2);
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(mainPanel);
        panel.add(menuBtn);
        add(panel);
        setMaximumSize(new Dimension(400,80* delButtons.size()));
        setLocationRelativeTo(null);
        pack();
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void init(){
        accessButtons = new ArrayList<>();
        delButtons = new ArrayList<>();
        labels = new ArrayList<>();
    }


    private void setUpButtonListener(){
        ActionListener buttonListener = (e) -> {
            JButton ae = (JButton) e.getSource();
            if (!ae.equals(menuBtn)) {
                if(delButtons.contains(ae)){
                    int index = delButtons.indexOf(ae);
                    TodoRun.deleteList(index);
                    dispose();
                    UserSelectionView.display();
                }else if(accessButtons.contains(ae)){
                    int index = accessButtons.indexOf(ae);
                    accessTodoList(listNames.get(index));
                    dispose();
                    ListSelectionView.display();
                }
            }else{
                dispose();
                UserSelectionView.display();
            }
        };
        if(!delButtons.isEmpty()){
            for(JButton button: delButtons)
                button.addActionListener(buttonListener);
        }
        if(!accessButtons.isEmpty()){
            for(JButton button: accessButtons)
                button.addActionListener(buttonListener);
        }
        menuBtn.addActionListener(buttonListener);

    }

    private void accessTodoList(String listName){
        UserManager userManager  = UserManager.getInstance();
        try{
            TodoList todoList = new TodoList(listName);
            if(userManager.todoListExists(todoList)){
                TodoRun.setCurrentTodoList(listName);
            }else{
                playErrorSound();
                JOptionPane.showMessageDialog(TodoListView.this,
                        "The list with the name you entered does not exist. Try again.",
                        "Error", JOptionPane.WARNING_MESSAGE);
            }
        }catch (NameTooShortException e){
            playErrorSound();
            JOptionPane.showMessageDialog(TodoListView.this,
                    "The name of the list is too short. Try again.",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
}
