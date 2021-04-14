package ui.views.item;

import models.exceptions.ItemPastDeadlineException;
import models.exceptions.TooManyUndoneItemsException;
import models.local.Item;
import models.local.RegularItem;

import models.local.UrgentItem;
import ui.TodoRun;
import ui.views.selection.ListSelectionView;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;

import static models.local.TodoList.dateParser;
import static ui.views.SoundPlayer.playErrorSound;

public class CreateNewTodoItem extends JFrame{

    private JPanel mainPanel;
    private JButton createBtn, menuBtn;
    private JRadioButton urgentBtn, regularBtn;
    JTextField nameField, dateField;

    public static void display(){
        new CreateNewTodoItem();
    }

    public CreateNewTodoItem(){
        super("Make a new todo item");
        setUpDisplay();
    }

    private void init(){
        mainPanel = new JPanel();
        createBtn = new JButton("Create");
        menuBtn = new JButton("Go back to menu");
        regularBtn = new JRadioButton("Regular Item");
        regularBtn.setSelected(true);
        urgentBtn = new JRadioButton("Urgent Item");
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(regularBtn);
        btnGroup.add(urgentBtn);
        nameField = new JTextField();
        dateField = new JTextField();
        nameField.setMaximumSize(new Dimension(500,20));
        dateField.setMaximumSize(new Dimension(200,20));
    }

    private void setUpDisplay(){

        init();
        setUpButtonListener();

        JLabel labelName = new JLabel("Name: ");
        labelName.setLabelFor(nameField);
        JLabel labelDate = new JLabel("Deadline date in (dd-MM-yyyy): ");
        labelDate.setLabelFor(dateField);
        JLabel title = new JLabel("Enter task name and deadline: ");
        JLabel choose = new JLabel("Select one of urgent or regular task: ");
        mainPanel.add(title);

        choose.setBorder(new EmptyBorder(5, 0, 5, 5));

        mainPanel.add(labelName);
        mainPanel.add(nameField);

        mainPanel.add(labelDate);
        mainPanel.add(dateField);

        mainPanel.add(choose);
        mainPanel.add(regularBtn);
        mainPanel.add(urgentBtn);

        mainPanel.add(createBtn);
        mainPanel.add(menuBtn);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        add(mainPanel);
        setSize(400,500);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setUpButtonListener(){
        ActionListener buttonListener = (e) -> {
            JButton ae = (JButton) e.getSource();
            if(ae.equals(createBtn)){
                String name = nameField.getText().trim();
                String date = dateField.getText();
                boolean isUrgent = false;
                if(urgentBtn.isSelected()){
                    isUrgent = true;
                }
                if(createNewTodoItem(name, date, isUrgent)){
                    dispose();
                    ItemListView.display();
                }
            }else if(ae.equals(menuBtn)){
                dispose();
                ListSelectionView.display();
            }
        };
        createBtn.addActionListener(buttonListener);
        menuBtn.addActionListener(buttonListener);
    }

    private boolean createNewTodoItem(String taskName, String date, boolean urgent){
        try{
            Date deadline = dateParser(date);
            Item item;
            if(urgent){
                item = new UrgentItem(taskName, deadline);
            }else item = new RegularItem(taskName, deadline);
            TodoRun.getCurrentTodoList().addItem(item);
            return true;
        }catch (ParseException e) {
            playErrorSound();
            JOptionPane.showMessageDialog(CreateNewTodoItem.this,
                    "Incorrect date format entered! Try again.",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }catch (ItemPastDeadlineException e){
            playErrorSound();
            JOptionPane.showMessageDialog(CreateNewTodoItem.this,
                    "The deadline you entered has already passed. Try again.",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }catch (TooManyUndoneItemsException e){
            playErrorSound();
            JOptionPane.showMessageDialog(CreateNewTodoItem.this,
                    "Item cannot be added. Too many undone items in your list.",
                    "Error", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }
}
