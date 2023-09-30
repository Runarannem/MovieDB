package moviedb.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractObservableDatabase {

    protected Collection<DatabaseObserver> observers = new ArrayList<>();

    public void addObserver(DatabaseObserver observer) {
        observers.add(observer);
    }

//    public void clearObserver() {
//        this.observer = null;
//    }

    public abstract List<AbstractMedia> getAbstractMedia();

    public abstract void notifyObservers();
}
