package com.mktech28.wsdownloader.utils;

public interface Subject {
    void register(Observer observer);

    void unregister(Observer observer);

    void notifyObservers();
}
