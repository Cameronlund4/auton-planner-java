package info.cameronlund.autonplanner.bluetooth;

public interface BluetoothMessageListener {
    void onMessageReceived(BluetoothConnection conn, String message);
}
