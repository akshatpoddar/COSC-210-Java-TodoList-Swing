package utilities;

import models.exceptions.ItemPastDeadlineException;
import models.local.Item;
import models.local.RegularItem;
import models.local.UrgentItem;
import models.local.User;
import models.network.ItemNetwork;
import models.network.UserNetwork;

import java.util.Date;

public class Mappers {

    public static UserNetwork UserToUserNetwork(User user){
        return new UserNetwork(user.getUsername(), user.getName());
    }

    public static Item ItemNetworkToItem(ItemNetwork itemNetwork){
        Item item = null;
        String name = itemNetwork.getTaskName();
        Date deadline = itemNetwork.getDeadline();
        boolean completed = itemNetwork.isCompleted();
        boolean pastDeadline = itemNetwork.isPastDeadline();
        String id = itemNetwork.getId();
        Date completedDate = itemNetwork.getCompletedDate();

        try{
            if(itemNetwork.isUrgent()) item = new UrgentItem(name,deadline, completed, pastDeadline, id, completedDate);
            else item = new RegularItem(name,deadline, completed, pastDeadline, id, completedDate);
        }catch (ItemPastDeadlineException e){
            System.out.println(e.getMessage());
        }
        return item;
    }

}
