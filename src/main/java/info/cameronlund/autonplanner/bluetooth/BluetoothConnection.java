package info.cameronlund.autonplanner.bluetooth;

import jssc.SerialPort;
import jssc.SerialPortException;

public class BluetoothConnection {
    private SerialPort port;

    public BluetoothConnection(String comPort, int baudRate, int dataBits, int stopBits, int parity) throws SerialPortException {
        port = new SerialPort(comPort);
        port.setParams(baudRate, dataBits, stopBits, parity);
    }

    public void open() throws SerialPortException {
        if (!port.isOpened())
            port.openPort();
    }

    public void close() throws SerialPortException {
        if (port.isOpened())
            port.closePort();
    }

    public void sendMessage(String message) throws SerialPortException {
        message+="\r\n";
        port.writeBytes(message.getBytes());
    }


}
