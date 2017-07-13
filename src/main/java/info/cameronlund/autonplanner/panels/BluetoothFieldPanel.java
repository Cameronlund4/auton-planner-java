package info.cameronlund.autonplanner.panels;


import info.cameronlund.autonplanner.bluetooth.BluetoothConnection;
import info.cameronlund.autonplanner.bluetooth.BluetoothMessageListener;
import info.cameronlund.autonplanner.robot.Robot;
import jssc.SerialPort;
import jssc.SerialPortException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BluetoothFieldPanel extends FieldPanel {
    private BufferedImage fieldImage;
    private BluetoothConnection conn;
    private BluetoothMessageListener posHandler = (conn, message) -> {
        if (message == null)
            return;
        try {
            if (message.startsWith("Robot pos:")) {
                if (!message.endsWith(">")) // Make sure we got the whole message
                    return;
                message = message.replaceAll("Robot pos: ", "").trim();
                message = message.replaceAll(">", "").trim();
                message = message.replace("(", "");
                message = message.replace(")", "");
                String[] parts = message.split(",");
                double x = robotToFieldPos(Integer.parseInt(parts[0]));
                double y = 687-robotToFieldPos(Integer.parseInt(parts[1]));
                getRobot().setPosition(Math.floor(x + 0.5), Math.floor(y + 0.5));
            } else if (message.startsWith("Robot gyro:")) {
                if (!message.endsWith(">")) // Make sure we got the whole message
                    return;
                message = message.replaceAll("Robot gyro: ", "");
                message = message.replaceAll(">", "").trim();
                int gyro = Integer.parseInt(message.trim());
                getRobot().setRotation(gyro%360*-1);
            }
            getManager().repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    };

    public BluetoothFieldPanel(Robot robot) {
        this.robot = robot;

        // Set up the images we need
        try {
            fieldImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("itz/itz_field.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean connect() {
        try {
            // TODO Make customizable
            if (conn == null)
                conn = new BluetoothConnection("COM3", SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
                        SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            conn.open();
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }

        // Listen to any messages
        conn.addMessageListener(posHandler);
        return true;
    }

    public void disconnect() {
        try {
            if (conn == null)
                return;
            conn.removeMessageListener(posHandler);
            conn.close();
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintField(Graphics g) {
        g.drawImage(fieldImage, 0, 0, this);
        g.setColor(Color.WHITE);
        int x = 687 - 67;
        int y = 18;
        g.drawString("Bluetooth", x - 1, y - 1);
        g.drawString("Bluetooth", x - 1, y + 1);
        g.drawString("Bluetooth", x + 1, y - 1);
        g.drawString("Bluetooth", x + 1, y + 1);
        // Now draw the actual number
        g.setColor(Color.BLUE);
        g.drawString("Bluetooth", x, y);
    }

    @Override
    public void resetField() {

    }

    public int robotToFieldPos(double x) {
        x /= 3.99781420765; // Pixel to mm conversion ((12 * 12 * 25.4)/687)
        return (int) Math.floor(x + (x > 0 ? 0.5 : -0.5));
    }

    public double fieldToRobotPos(int x) {
        x *= 3.99781420765;
        return x;
    }
}
