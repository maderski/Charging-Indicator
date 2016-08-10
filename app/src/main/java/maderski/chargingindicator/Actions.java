package maderski.chargingindicator;

/**
 * Created by Jason on 8/2/16.
 */
public interface Actions {
    void vibrate();
    void makeSound();
    void showToast(String message);
    void showNotification(Battery battery);
    void removeNotification();
}
