package models.local;

import com.google.cloud.firestore.annotation.Exclude;
import models.exceptions.ItemPastDeadlineException;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public abstract class Item implements Serializable, Cloneable {

    private String id;
    private String taskName;
    private boolean isCompleted;
    private Date deadline;
    private boolean pastDeadline;
    private Date completedDate;

    public Item(){}

    public Item(String taskName, Date deadline, boolean isCompleted, boolean pastDeadline, String id, Date completedDate) throws ItemPastDeadlineException {
        setTaskName(taskName);
        setCompleted(isCompleted);
        setDeadline(deadline);
        setPastDeadline(pastDeadline);
        setCompletedDate(completedDate);
        this.id = id;
    }

    public Item(String taskName, Date deadline, boolean isCompleted, boolean pastDeadline) throws ItemPastDeadlineException {
        this(taskName, deadline, isCompleted , pastDeadline, UUID.randomUUID().toString(),null);
    }

    public Item(String taskName, Date deadline) throws ItemPastDeadlineException {
        this(taskName, deadline, false , false);
    }

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        if(completedDate!=null) isCompleted = true;
        this.completedDate = completedDate;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
        if(completed){
            Date current = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
            setCompletedDate(current);
        }else{
            setCompletedDate(null);
        }
    }

    public void toggleCompleted(){
        setCompleted(!isCompleted);
    }


    public Date getDeadline(){
        return deadline;
    }

    @Exclude
    public String getStringDate(Date d) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        return formatter.format(d);
    }

    public void setDeadline(Date deadline) throws ItemPastDeadlineException {
        if(getDifferenceDays(deadline)==0L){
            throw new ItemPastDeadlineException();
        }
        this.deadline = deadline;
    }

    public boolean isPastDeadline() {
        return pastDeadline;
    }

    public void setPastDeadline(boolean pastDeadline) {
        this.pastDeadline = pastDeadline;
    }

    public void completeItem(){
        setCompleted(true);
    }

    public long getDifferenceDays(Date d1) {
        LocalDate localDate = LocalDate.now();
        Date current = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        long diff = d1.getTime() - current.getTime();
        if (diff<0) return 0;
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    public String getId() {
        return id;
    }

    @Exclude
    public abstract boolean isUrgent();

    @Override
    public String toString() {
        Date date;
        if(isCompleted){
            date = completedDate;
        }else date = getDeadline();
        String completed = isCompleted?"completed":"due";
        return String.format("%s is/was %s on %s \t",taskName, completed, getStringDate(date));
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
