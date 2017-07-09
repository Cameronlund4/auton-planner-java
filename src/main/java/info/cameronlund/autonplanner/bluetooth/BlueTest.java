package info.cameronlund.autonplanner.bluetooth;

import jssc.SerialPort;
import jssc.SerialPortException;

public class BlueTest {
    public static void main(String[] args) {
        SerialPort serialPort = new SerialPort("COM3");
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
            serialPort.writeBytes("ryan\r\n".getBytes());//Write data to port
            System.out.println("Wrote!");
        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Waited!");

        try {
            String reply = serialPort.readString();
            System.out.println("Reply:" + reply);
        } catch (SerialPortException ex) {
            ex.printStackTrace();
        }
    }
}
