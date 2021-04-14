package models.local;

import models.exceptions.ItemPastDeadlineException;

import java.util.Date;

public class UrgentItem extends Item{


    public UrgentItem(String taskName, Date deadline) throws ItemPastDeadlineException {
        super(taskName, deadline);
    }

    public UrgentItem(String taskName, Date deadline, boolean isCompleted, boolean pastDeadline) throws ItemPastDeadlineException {
        super(taskName, deadline, isCompleted, pastDeadline);
    }

    public UrgentItem(String taskName, Date deadline, boolean isCompleted, boolean pastDeadline, String id, Date completedDate) throws ItemPastDeadlineException {
        super(taskName, deadline, isCompleted, pastDeadline, id, completedDate);
    }


    @Override
    public boolean isUrgent() {
        return true;
    }

    @Override
    public String toString() {
        if(getCompleted()){
            return super.toString();
        }else {
            return super.toString() + String.format("Urgent Task!! You only have %d days left!!", getDifferenceDays(getDeadline()));
        }
    }

    @Override
    public UrgentItem clone() throws CloneNotSupportedException {
        return (UrgentItem) super.clone();
    }
}
