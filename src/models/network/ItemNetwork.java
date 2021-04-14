package models.network;

import java.util.Date;

public class ItemNetwork {

    private String id;
    private boolean urgent;
    private String taskName;
    private boolean isCompleted;
    private Date deadline;
    private boolean pastDeadline;
    private Date completedDate;

    public ItemNetwork(){}

    public ItemNetwork(boolean urgent, String taskName, boolean isCompleted, Date deadline, boolean pastDeadline, String id) {
        this.urgent = urgent;
        this.taskName = taskName;
        this.isCompleted = isCompleted;
        this.deadline = deadline;
        this.pastDeadline = pastDeadline;
        this.id = id;
    }

    public boolean isUrgent() {
        return urgent;
    }

    public void setUrgent(boolean urgent) {
        this.urgent = urgent;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public boolean isPastDeadline() {
        return pastDeadline;
    }

    public void setPastDeadline(boolean pastDeadline) {
        this.pastDeadline = pastDeadline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id){ this.id = id;}

    public Date getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Date completedDate) {
        this.completedDate = completedDate;
    }
}
