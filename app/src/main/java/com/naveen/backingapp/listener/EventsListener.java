package com.naveen.backingapp.listener;



/**
 * Created by Naveen on 6/17/2018.
 */

public interface EventsListener<T> {
    void onSuccess(T events);
}
