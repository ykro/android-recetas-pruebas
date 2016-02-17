package edu.galileo.android.facebookrecipes.lib;

/**
 * Created by ykro.
 */
public interface EventBus {
    void post(Object event);
    void register(Object subscriber);
    void unregister(Object subscriber);
    boolean isRegistered(Object subscriber);
    boolean hasSubscriberForEvent(Class<?> subscriber);
}
