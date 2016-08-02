package maderski.chargingindicator;

/**
 * Created by Jason on 8/2/16.
 */
public interface Actions {
    public void vibrate();
    public void makeSound();
    public void showToast(String message);
    public void showNotification();
    public void removeNotification();
}
