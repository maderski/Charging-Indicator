package maderski.chargingindicator.Actions;

/**
 * Created by Jason on 8/2/16.
 */
public interface Actions {
    void connectVibrate();
    void disconnectVibrate();
    void connectSound();
    void disconnectSound();
    void batteryChargedSound();
    void showToast(String message);
    void showNotification();
    void removeNotification();
}
