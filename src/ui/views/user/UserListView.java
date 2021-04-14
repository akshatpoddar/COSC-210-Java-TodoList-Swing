package ui.views.user;

import ui.TodoRun;
import ui.views.selection.UserSelectionView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UserListView extends JFrame {
    private List<String> userNames;
    private List<JLabel> labels;
    private JPanel mainPanel;
    private JButton menuBtn;

    public static void display() {
        List<String> users = TodoRun.getAllNamesOfUsers();
        if(users.isEmpty()){
            new UserListView();
        }else{
            new UserListView(users);
        }
    }

    public UserListView(){
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

    public UserListView(List<String> users){
        super("List of names of users registered");
        this.userNames = users;
        init();
        for(int i=0; i<userNames.size(); i++) {
            JLabel label = new JLabel((i+1)+". "+userNames.get(i));
            label.setFont(new Font("Serif", Font.PLAIN, 20));
            label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            labels.add(label);
        }
        setUpPanel();
    }

    private void setUpPanel(){
        menuBtn = new JButton("Go back to menu");
        setUpButtonListener();
        mainPanel = new JPanel();

        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        for(JLabel label: labels)
            mainPanel.add(label);
        mainPanel.add(menuBtn);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        add(mainPanel);
        setResizable(false);
        setLocationRelativeTo(null);
        setSize(400,500);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void setUpButtonListener(){
        ActionListener buttonListener = (e) -> {
            JButton ae = (JButton) e.getSource();
            if (ae.equals(menuBtn)) {
                dispose();
                UserSelectionView.display();
            }
        };
        menuBtn.addActionListener(buttonListener);
    }

    private void init(){
        labels = new ArrayList<>();
    }
}
