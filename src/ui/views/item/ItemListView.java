package ui.views.item;

import models.local.Item;
import models.local.TodoList;
import ui.TodoRun;
import ui.views.selection.ListSelectionView;
import utilities.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class ItemListView extends JFrame implements Observer {

    private TodoList todoList;
    private List<JLabel> labels;
    private List<JButton> buttons;
    private List<JCheckBox> checkboxes;
    private JPanel mainPanel, panel;
    private JButton menuBtn;
    JLabel warningLabel;

    public static void display() {
        TodoList todoList = TodoRun.getCurrentTodoList();
        if(todoList.getAllItems().isEmpty()){
            new ItemListView();
        }else{
            new ItemListView(todoList);
        }
    }

    public ItemListView(){
        super("List of items in todo list");
        init();
        JLabel jLabel= new JLabel("List is empty! No items to display!");
        mainPanel = new JPanel();
        mainPanel.add(jLabel);
        menuBtn = new JButton("Go back to menu");
        mainPanel.add(menuBtn);
        setUpButtonListener();
        add(mainPanel);
        setSize(new Dimension(500,300));
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(JFrame. DISPOSE_ON_CLOSE);
    }

    public ItemListView(TodoList todoList){
        super("List of items in todo list");
        this.todoList = todoList;
        todoList.addObserver(this);
        init();
        for(Item item: todoList.getAllItems()) {
            JButton jButton = new JButton("Delete");
            jButton.setMaximumSize(new Dimension(100, 60));
            buttons.add(jButton);
            JLabel label = new JLabel(item.toString());
            labels.add(label);
            JCheckBox checkBox = new JCheckBox();
            if(item.getCompleted()) checkBox.setSelected(true);
            checkboxes.add(checkBox);
        }
        setUpPanel();
    }

    private void setUpPanel(){
        menuBtn = new JButton("Go back to menu");
        setUpButtonListener();
        mainPanel = new JPanel(new GridLayout(todoList.getItemCount(), 3,20,10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
        for(int i = 0; i< todoList.getItemCount(); i++){
            mainPanel.add(labels.get(i), i, 0);
            mainPanel.add(checkboxes.get(i), i, 1);
            mainPanel.add(buttons.get(i), i, 2);
        }
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(mainPanel);
        panel.add(menuBtn);
        add(panel);
        setMaximumSize(new Dimension(1000,(80* buttons.size())+500));
        setResizable(false);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setUpButtonListener(){
        ActionListener buttonListener = (e) -> {
            JButton ae = (JButton) e.getSource();
            if (!ae.equals(menuBtn)) {
                int index = buttons.indexOf(ae);
                TodoRun.deleteTodoItem(index);
            }
            dispose();
            todoList.removeObserver(this);
            ListSelectionView.display();
        };
        if(!buttons.isEmpty()) {
            for(JButton button: buttons)
                button.addActionListener(buttonListener);
        }
        menuBtn.addActionListener(buttonListener);

        for(int i=0; i<checkboxes.size(); i++){
            int finalI = i;
            checkboxes.get(i).addItemListener(e -> todoList.toggleItem(todoList.getItemByPos(finalI)));
        }
    }

    private void init(){
        buttons = new ArrayList<>();
        labels = new ArrayList<>();
        checkboxes = new ArrayList<>();
    }

    @Override
    public void updateIncomplete() {
        JOptionPane.showMessageDialog(ItemListView.this,
                "Please complete or delete items. Maximum undone items allowed have been crossed!",
                "Error",
                JOptionPane.WARNING_MESSAGE);
        warningLabel = new JLabel("Maximum undone items allowed in this list have been crossed!");
        warningLabel.setFont(new Font("Calibri",Font.PLAIN, 20));
        warningLabel.setForeground(Color.RED);
        warningLabel.setBorder(new EmptyBorder(10,0,10,500));
        panel.add(warningLabel);
        revalidate();
        repaint();
        pack();
    }

    @Override
    public void updateComplete() {
        panel.remove(warningLabel);
        revalidate();
        repaint();
        pack();
    }
}
