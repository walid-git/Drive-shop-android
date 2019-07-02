package Observer;

public interface Observable {
    void addObserver(Observer observer);

    void notifyObservers();

    void removeObserver(Observer observer);
}
