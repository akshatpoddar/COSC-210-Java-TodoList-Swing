package models.local;

import models.exceptions.ItemPastDeadlineException;

import java.util.Date;

public class RegularItem extends Item {

    public RegularItem(){}

    public RegularItem(String taskName, Date deadline) throws ItemPastDeadlineException {
        super(taskName, deadline);
    }

    public RegularItem(String taskName, Date deadline, boolean isCompleted, boolean pastDeadline) throws ItemPastDeadlineException {
        super(taskName, deadline, isCompleted, pastDeadline);
    }

    public RegularItem(String taskName, Date deadline, boolean isCompleted, boolean pastDeadline, String id, Date completedDate) throws ItemPastDeadlineException {
        super(taskName, deadline, isCompleted, pastDeadline, id, completedDate);
    }

    @Override
    public boolean isUrgent() {
        return false;
    }

    @Override
    public String toString() {
        if(getCompleted()){
            return super.toString();
        }else{
            return super.toString()+ String.format(" You still have %d days left.",getDifferenceDays(getDeadline()));
        }
    }

    @Override
    public RegularItem clone() throws CloneNotSupportedException {
        return (RegularItem) super.clone();
    }
}
