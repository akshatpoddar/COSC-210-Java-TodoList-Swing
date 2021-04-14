package ui.views.selection;

import ui.TodoRun;
import ui.views.item.CreateNewTodoItem;
import ui.views.item.ItemListView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListSelectionView extends JFrame{

    private final List<JButton> buttons;
    JPanel mainPanel;

    public static void display(){
        new ListSelectionView(getListOptions());
    }

    public ListSelectionView(List<String> options){
        super(TodoRun.getCurrentTodoList().getListName());
        buttons = new ArrayList<>();
        for(String option: options)
            buttons.add(new JButton(option));
        setUpButtonListener();
        setUpDisplay();
    }

    private static List<String> getListOptions(){
        String[] listOptions = {"Create a new item", "Show all items in the list","Go back to main menu"};
        return Arrays.asList(listOptions);
    }

    private void setUpDisplay(){
        mainPanel = new JPanel(new GridLayout(buttons.size(), 1,0,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        for (JButton button : buttons) {
            mainPanel.add(button);
        }
        add(mainPanel);
        setSize(600,400);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void setUpButtonListener(){
        ActionListener buttonListener = (e) -> {
            JButton ae = (JButton) e.getSource();
            if(ae.equals(buttons.get(0))){
                dispose();
                CreateNewTodoItem.display();
            }else if(ae.equals(buttons.get(1))){
                dispose();
                ItemListView.display();
            }else if(ae.equals(buttons.get(2))){
                dispose();
                UserSelectionView.display();
            }
        };
        for(JButton button: buttons)
            button.addActionListener(buttonListener);
    }
}
