package edu.galileo.android.facebookrecipes.lib;

/**
 * Created by ykro.
 */
public class GreenRobotEventBus implements EventBus {
    org.greenrobot.eventbus.EventBus eventBus;

    public GreenRobotEventBus(){
        eventBus = org.greenrobot.eventbus.EventBus.getDefault();
    }

    public void post(Object event){
        eventBus.post(event);
    }

    public void register(Object subscriber){
        eventBus.register(subscriber);
    }

    public void unregister(Object subscriber){
        eventBus.unregister(subscriber);
    }

    @Override
    public boolean isRegistered(Object subscriber) {
        return eventBus.isRegistered(subscriber);
    }

    @Override
    public boolean hasSubscriberForEvent(Class<?> subscriber) {
        return eventBus.hasSubscriberForEvent(subscriber);
    }
}
