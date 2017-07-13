package info.cameronlund.autonplanner.bluetooth;

import jssc.SerialPort;
import jssc.SerialPortException;

import java.util.ArrayList;

public class BluetoothConnection {
    private SerialPort port;
    private ArrayList<BluetoothMessageListener> listeners = new ArrayList<>();
    private Thread asyncListener;
    private int baudRate, dataBits, stopBits, parity;

    public BluetoothConnection(String comPort, int baudRate, int dataBits, int stopBits, int parity) throws SerialPortException {
        port = new SerialPort(comPort);
        this.baudRate = baudRate;
        this.dataBits = dataBits;
        this.stopBits = stopBits;
        this.parity = parity;

    }

    public void open() throws SerialPortException {
        if (!port.isOpened())
            port.openPort();
        port.setParams(baudRate, dataBits, stopBits, parity);
    }

    public void close() throws SerialPortException {
        if (port.isOpened())
            port.closePort();
    }

    private void createListeningThread() {
        if (asyncListener != null)
            return;
        asyncListener = new Thread(() -> {
            while (!asyncListener.isInterrupted()) {
                try {
                    Thread.sleep(250);
                    String raw = port.readString();
                    if (raw == null) continue;
                    String[] messages = raw.split("\n");
                    for (String message : messages) {
                        if (message == null) continue;
                        for (BluetoothMessageListener listener : listeners)
                            listener.onMessageReceived(this, message);
                    }
                } catch (SerialPortException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        asyncListener.start();
    }

    public void addMessageListener(BluetoothMessageListener listener) {
        listeners.add(listener);
        createListeningThread();
    }

    public void removeMessageListener(BluetoothMessageListener listener) {
        listeners.remove(listener);
        if (listeners.size() < 1) {
            asyncListener.interrupt();
            asyncListener = null;
        }
    }

    public void sendMessage(String message) throws SerialPortException {
        port.writeBytes((message + "\r\n").getBytes());
    }

    public SerialPort getPort() {
        return port;
    }

    // TODO JSON 'packet' communication protocol
}
