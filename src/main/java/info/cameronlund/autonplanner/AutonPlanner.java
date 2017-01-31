package info.cameronlund.autonplanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import info.cameronlund.autonplanner.actions.ActionManager;
import info.cameronlund.autonplanner.actions.ActionType;
import info.cameronlund.autonplanner.panels.ActionListPanel;
import info.cameronlund.autonplanner.panels.FieldPanel;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AutonPlanner {
    private static boolean isSkill = false;
    private static boolean preloadLoaded = false;
    private static int startingRotation = 0;
    private ArrayList<ActionListener> modeSwitchListeners = new ArrayList<>();
    private int actionId = 0;
    private String autonName = "Unnamed Auton";
    private ActionManager manager;
    private FieldPanel fieldPanel;
    private JTextField offsetField;
    private JTextField angleField;

    public AutonPlanner() {
        // Main frame for the project
        JFrame frame = new JFrame("[2616E] Auton Planner");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1393, 715));
        frame.setResizable(false);
        frame.setMinimumSize(frame.getPreferredSize());
        BorderLayout layout = (BorderLayout) frame.getLayout();
        layout.setVgap(0);
        layout.setHgap(0);

        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(1393, 715));
        contentPanel.setMinimumSize(contentPanel.getPreferredSize());
        contentPanel.setLayout(new BorderLayout());
        layout = (BorderLayout) contentPanel.getLayout();
        layout.setVgap(0);
        layout.setHgap(0);

        manager = new ActionManager(frame);
        // Load the list of events the auton does
        ActionListPanel actionList = manager.getActionListPanel();
        // Load the field image, scaled 3 pixels -> 1 tick
        fieldPanel = new FieldPanel("scale_field(2-1).png",
                "cube.png", "star.png");
        fieldPanel.setManager(manager);

        // Main panel containing field and list of moves
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        layout = (BorderLayout) mainPanel.getLayout();
        layout.setVgap(0);
        layout.setHgap(0);

        // Panel to the right containing options and settings
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        layout = (BorderLayout) sidePanel.getLayout();
        layout.setVgap(0);
        layout.setHgap(0);
        sidePanel.setBackground(Color.lightGray);
        sidePanel.setMaximumSize(new Dimension(400, 715));
        sidePanel.setPreferredSize(new Dimension(400, 715));
        sidePanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.GRAY));

        // Section containing global options
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 10);
        optionsPanel.setBackground(Color.lightGray);
        optionsPanel.setMaximumSize(new Dimension(400, 75));
        optionsPanel.setPreferredSize(new Dimension(400, 75));
        optionsPanel.setBorder(new MatteBorder(1, 0, 0, 0, Color.GRAY));

        JCheckBox preloadCheckbox = new JCheckBox("Preloaded");
        preloadCheckbox.setSelected(AutonPlanner.isPreloadLoaded());
        preloadCheckbox.setOpaque(false);
        preloadCheckbox.setFocusPainted(false);
        preloadCheckbox.addActionListener(e -> {
            setPreloadLoaded(preloadCheckbox.isSelected());
            frame.repaint();
        });

        JCheckBox skillCheckBox = new JCheckBox("Skills");
        skillCheckBox.setSelected(AutonPlanner.isSkill());
        skillCheckBox.setOpaque(false);
        skillCheckBox.setFocusPainted(false);
        skillCheckBox.addActionListener(e -> {
            setIsSkill(skillCheckBox.isSelected());
            frame.repaint();
        });

        // Make a field for starting angle
        JPanel startingAngleWrapper = new JPanel();
        startingAngleWrapper.setOpaque(false);
        startingAngleWrapper.setLayout(new GridBagLayout());
        JLabel angleLabel = new JLabel("Start angle:");
        angleLabel.setOpaque(false);
        angleField = new JTextField();
        angleField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        angleField.setPreferredSize(new Dimension(75, 30));
        angleField.setMaximumSize(new Dimension(75, 30));
        angleField.addActionListener(e -> {
            try {
                int rotation = Integer.parseInt(angleField.getText());
                setStartingRotation(rotation);
                frame.repaint();
            } catch (NumberFormatException ignored) {
                angleField.setText(((int) fieldPanel.getRobot().getRotation()) + "");
            }
        });
        angleField.setText(((int) fieldPanel.getRobot().getRotation()) + "");
        gbc.gridx = 0;
        gbc.gridy = 0;
        startingAngleWrapper.add(angleLabel, gbc);
        gbc.gridx = 1;
        startingAngleWrapper.add(angleField, gbc);

        // Make a field for offset position
        JPanel offsetPosWrapper = new JPanel();
        offsetPosWrapper.setOpaque(false);
        offsetPosWrapper.setLayout(new GridBagLayout());
        JLabel offsetLabel = new JLabel("Start offset:");
        offsetLabel.setOpaque(false);
        offsetField = new JTextField();
        offsetField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        offsetField.setPreferredSize(new Dimension(75, 30));
        offsetField.setMaximumSize(new Dimension(75, 30));
        offsetField.addActionListener(e -> {
            try {
                String text = offsetField.getText();
                String[] parts = text.split(",");
                fieldPanel.getRobot().setResting(515 + Integer.parseInt(parts[0]),
                        (630 - (Integer.parseInt(parts[1]))));
                frame.repaint();
            } catch (Exception ignored) {
                offsetField.setText((fieldPanel.getRobot().getRestingX() - 515) + "," +
                        -1 * (fieldPanel.getRobot().getRestingY() - 630));
            }
        });
        offsetField.setText((fieldPanel.getRobot().getRestingX() - 515) + "," +
                (fieldPanel.getRobot().getRestingY() - 630));
        gbc.gridx = 0;
        gbc.gridy = 0;
        offsetPosWrapper.add(offsetLabel, gbc);
        gbc.gridx = 1;
        offsetPosWrapper.add(offsetField, gbc);

        JTextField autonNameField = new JTextField();
        autonNameField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        autonNameField.setPreferredSize(new Dimension(150, 30));
        autonNameField.setMaximumSize(new Dimension(150, 30));
        autonNameField.addActionListener(e -> {
            autonName = autonNameField.getText();
        });
        autonNameField.setText(autonName);

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        optionsPanel.add(autonNameField, gbc);
        gbc.gridx = 2;
        optionsPanel.add(startingAngleWrapper, gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        optionsPanel.add(skillCheckBox, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        optionsPanel.add(preloadCheckbox, gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        optionsPanel.add(offsetPosWrapper, gbc);
        sidePanel.add(optionsPanel, BorderLayout.SOUTH);
        gbc.gridwidth = 1;

        // Section containing action options
        JPanel moveOptionPanel = new JPanel();
        moveOptionPanel.setLayout(new GridBagLayout());
        gbc.insets = new Insets(0, 0, 0, 10);
        moveOptionPanel.setBackground(Color.lightGray);
        moveOptionPanel.setMaximumSize(new Dimension(400, 50));
        moveOptionPanel.setPreferredSize(new Dimension(400, 50));
        moveOptionPanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.GRAY));

        JComboBox<String> modeList = new JComboBox<>(ActionType.getTypesList());
        modeList.setOpaque(false);
        modeList.setPreferredSize(new Dimension(100, 30));
        modeList.setMaximumSize(new Dimension(100, 30));
        modeList.setSelectedIndex(0);
        manager.setActionTypeBox(modeList);

        JTextField actionNameField = new JTextField();
        actionNameField.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        actionNameField.setPreferredSize(new Dimension(200, 30));
        actionNameField.setMaximumSize(new Dimension(200, 30));
        manager.setActionNameField(actionNameField);
        gbc.gridx = 0;
        gbc.gridy = 0;
        moveOptionPanel.add(actionNameField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        moveOptionPanel.add(modeList, gbc);

        sidePanel.add(moveOptionPanel, BorderLayout.NORTH);

        JPanel actionSettingsPanel = new JPanel();
        actionSettingsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        sidePanel.add(actionSettingsPanel, BorderLayout.CENTER);
        manager.setOptionsPanel(actionSettingsPanel);

        // Add our content to the frame
        mainPanel.add(fieldPanel, BorderLayout.CENTER);
        mainPanel.add(actionList, BorderLayout.EAST);
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(sidePanel, BorderLayout.EAST);
        frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

        // Display frame
        frame.pack();
        frame.setVisible(true);

        loadJson(new JsonParser().parse("{\"autonName\":\"Unnamed Auton\",\"startRot\":180,\"startX\":515,\"startY" +
                "\":630,\"actions\":[{\"type\":\"DRIVE\",\"name\":\"Drive forward\",\"distance\":-1560},{\"type\":" +
                "\"WAIT\",\"name\":\"Wait for drive\",\"millis\":0,\"action\":\"waitForPID();\"},{\"type\":\"CLAW\"," +
                "\"name\":\"Open claw\",\"angleTarget\":300,\"speed\":127,\"millis\":0,\"action\":\"action1\"},{\"type" +
                "\":\"WAIT\",\"name\":\"Wait for claw\",\"millis\":0,\"action\":\"waitForClaw();\"},{\"type\":\"DRIVE\"," +
                "\"name\":\"Drive to stars\",\"distance\":450},{\"type\":\"WAIT\",\"name\":\"Delay claw close\",\"millis" +
                "\":200,\"action\":\"delay\"},{\"type\":\"CLAW\",\"name\":\"Close claw\",\"angleTarget\":0,\"speed\":127," +
                "\"millis\":500,\"action\":\"action2\"},{\"type\":\"WAIT\",\"name\":\"Wait for drive\",\"millis\":0," +
                "\"action\":\"waitForPID();\"},{\"type\":\"DRIVE\",\"name\":\"Drive to fence\",\"distance\":-1550},{" +
                "\"type\":\"WAIT\",\"name\":\"Wait for drive\",\"millis\":0,\"action\":\"waitForPID();\"},{\"type\":" +
                "\"LIFT\",\"name\":\"Lift stars\",\"speed\":127,\"angleTarget\":1900.0},{\"type\":\"WAIT\",\"name\":" +
                "\"Wait for lift\",\"millis\":0,\"action\":\"waitForLift();\"},{\"type\":\"CLAW\",\"name\":\"Open claw" +
                "\",\"angleTarget\":300,\"speed\":127,\"millis\":0,\"action\":\"action1\"},{\"type\":\"WAIT\",\"name\":" +
                "\"Wait for claw\",\"millis\":0,\"action\":\"waitForClaw();\"},{\"type\":\"LIFT\",\"name\":\"Lower Lift" +
                "\",\"speed\":127,\"angleTarget\":600.0},{\"type\":\"WAIT\",\"name\":\"Wait for lift\",\"millis\":0," +
                "\"action\":\"waitForLift();\"},{\"type\":\"TURN\",\"name\":\"Turn to wall\",\"angleDelta\":90.0,\"action" +
                "\":\"action1\",\"speed\":100},{\"type\":\"CLAW\",\"name\":\"Narrow claw\",\"angleTarget\":200,\"speed\":0," +
                "\"millis\":0,\"action\":\"action1\"},{\"type\":\"DRIVE\",\"name\":\"Back towards star group\",\"distance\"" +
                ":4800},{\"type\":\"WAIT\",\"name\":\"Wait for drive\",\"millis\":0,\"action\":\"waitForPID();\"},{\"type" +
                "\":\"CLAW\",\"name\":\"Close claw\",\"angleTarget\":0,\"speed\":0,\"millis\":500,\"action\":\"action2" +
                "\"},{\"type\":\"LIFT\",\"name\":\"Lift stars\",\"speed\":127,\"angleTarget\":1500.0},{\"type\":\"TURN" +
                "\",\"name\":\"Turn to fence\",\"angleDelta\":-90.0,\"action\":\"action1\",\"speed\":127},{\"type\":" +
                "\"LIFT\",\"name\":\"Finish lift motion\",\"speed\":127,\"angleTarget\":1900.0},{\"type\":\"WAIT\"," +
                "\"name\":\"Wait for lift\",\"millis\":0,\"action\":\"waitForLift();\"},{\"type\":\"CLAW\",\"name\":" +
                "\"Open claw\",\"angleTarget\":300,\"speed\":127,\"millis\":0,\"action\":\"action1\"},{\"type\":\"WAIT" +
                "\",\"name\":\"Wait for claw\",\"millis\":0,\"action\":\"waitForClaw();\"},{\"type\":\"LIFT\",\"name\":" +
                "\"Lower lift\",\"speed\":127,\"angleTarget\":600.0},{\"type\":\"TURN\",\"name\":\"Turn to cube\"," +
                "\"angleDelta\":-10.0,\"action\":\"action1\",\"speed\":100}]}\n").getAsJsonObject());

        new Thread(() -> {
            while (true) {
                System.out.println(toJson().toString());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }).start();
    }

    public static boolean isPreloadLoaded() {
        return preloadLoaded;
    }

    private static void setPreloadLoaded(boolean preloadLoaded) {
        AutonPlanner.preloadLoaded = preloadLoaded;
    }

    public static int getStartingRotation() {
        return startingRotation;
    }

    public static void setStartingRotation(int startingRotation) {
        AutonPlanner.startingRotation = startingRotation;
    }

    public static boolean isSkill() {
        return isSkill;
    }

    public void addModeListener(ActionListener listener) {
        modeSwitchListeners.add(listener);
    }

    public void removeModeListener(Action listener) {
        modeSwitchListeners.remove(listener);
    }

    public void setIsSkill(boolean skill) {
        actionId++;
        isSkill = skill;
        for (ActionListener l : modeSwitchListeners)
            l.actionPerformed(new ActionEvent(this, actionId, "mode_switched"));
    }

    public void loadJson(JsonObject object) {
        autonName = object.get("autonName").getAsString();
        manager.loadJson(object.get("actions").getAsJsonArray());
        startingRotation = object.get("startRot").getAsInt();
        angleField.setText(startingRotation + "");
        offsetField.setText(object.get("startX").getAsInt() + "," + object.get("startY").getAsInt());
        fieldPanel.getRobot().setRestingReturn(object.get("startX").getAsInt(), object.get("startY").getAsInt());
    }

    public JsonObject toJson() {
        JsonObject file = new JsonObject();
        file.addProperty("autonName", autonName);
        file.addProperty("startRot", startingRotation);
        file.addProperty("startX", fieldPanel.getRobot().getRestingX());
        file.addProperty("startY", fieldPanel.getRobot().getRestingY());
        file.add("actions", manager.toJson());
        System.out.println(file.toString());
        return file;
    }
}
