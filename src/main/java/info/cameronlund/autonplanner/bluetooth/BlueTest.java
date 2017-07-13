package info.cameronlund.autonplanner.bluetooth;

import info.cameronlund.autonplanner.panels.FieldPanel;
import jssc.SerialPort;
import jssc.SerialPortException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

class BlueTest extends FieldPanel {
    private BufferedImage fieldImage;
    private BluetoothConnection conn;

    public BlueTest() {
        super(915);
        try {
            conn = new BluetoothConnection("COM3", SerialPort.BAUDRATE_9600, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
        } catch (SerialPortException e) {
            e.printStackTrace();
            return;
        }
        /*try {
            while (true) {
                conn.sendMessage("pos");
                conn.sendMessage("gyro");
                Thread.sleep(250);
            }
        } catch (SerialPortException | InterruptedException e) {
            e.printStackTrace();
        }*/
        conn.addMessageListener((conn, message) -> {
            if (message == null)
                return;
            try {
            if (message.startsWith("Robot pos:")) {
                message = message.replaceAll("Robot pos: ", "").trim();
                message = message.replace("(", "");
                message = message.replace(")", "");
                String[] parts = message.split(",");
                double x = Integer.parseInt(parts[0]);
                x /= 3.99781420765;
                double y = Integer.parseInt(parts[1]);
                y /= 3.99781420765;
                getRobot().setPosition(Math.floor(x + 0.5), Math.floor(y + 0.5));
                System.out.println("Robot's pos: (" + x + "," + y + ")");
            } else if (message.startsWith("Robot gyro:")) {
                message = message.replaceAll("Robot gyro: ", "");
                int gyro = Integer.parseInt(message.trim());
                getRobot().setRotation(gyro);
                System.out.println("Robot's gyro value: " + gyro);
            }
            repaint();} catch (Exception e) {
                e.printStackTrace();
            }
        });
        // Set up the images we need
        try {
            fieldImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("test_field.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintField(Graphics g) {
        g.drawImage(fieldImage, 0, 0, this);
        getRobot().paint(g);
    }

    @Override
    public void resetField() {

    }
}
