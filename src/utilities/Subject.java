package utilities;


import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    List<Observer> observers = new ArrayList<>();

    public void notifyObservers(boolean warning){
        if(warning)
            for(Observer observer: observers)
                observer.updateIncomplete();
        else
            for(Observer observer: observers)
                observer.updateComplete();
    }

    public void addObserver(Observer o){
        if(!observers.contains(o))
            observers.add(o);
    }

    public void removeObserver(Observer o){
        observers.remove(o);
    }
}
