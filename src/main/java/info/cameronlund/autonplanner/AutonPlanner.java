package info.cameronlund.autonplanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import info.cameronlund.autonplanner.actions.ActionManager;
import info.cameronlund.autonplanner.filters.AutonPlanerFileFilter;
import info.cameronlund.autonplanner.helpers.ActionCallHelper;
import info.cameronlund.autonplanner.panels.ActionListPanel;
import info.cameronlund.autonplanner.panels.FieldPanel;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
    private File currentFile;
    //"Drive", "Turn", "Lift", "Claw", "Wait"

    public AutonPlanner(ActionManager manager, FieldPanel fieldPanel) {
        // Main frame for the project
        JFrame frame = new JFrame("[2616E] Auton Planner");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1393, 738));
        frame.setResizable(false);
        frame.setMinimumSize(frame.getPreferredSize());
        BorderLayout layout = (BorderLayout) frame.getLayout();
        layout.setVgap(0);
        layout.setHgap(0);

        this.manager = manager;
        manager.setFrame(frame);

        this.fieldPanel = fieldPanel;
        fieldPanel.setManager(manager);

        JPanel contentPanel = new JPanel();
        contentPanel.setPreferredSize(new Dimension(1393, 715));
        contentPanel.setMinimumSize(contentPanel.getPreferredSize());
        contentPanel.setLayout(new BorderLayout());
        layout = (BorderLayout) contentPanel.getLayout();
        layout.setVgap(0);
        layout.setHgap(0);
        // Load the list of events the auton does
        ActionListPanel actionList = manager.getActionListPanel();

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
        moveOptionPanel.add(manager.getModeCombo(), gbc);

        sidePanel.add(moveOptionPanel, BorderLayout.NORTH);

        JPanel actionSettingsPanel = new JPanel();
        actionSettingsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        sidePanel.add(actionSettingsPanel, BorderLayout.CENTER);
        manager.setOptionsPanel(actionSettingsPanel);

        // Menu creation ------------
        JMenu fileMenu = new JMenu("File");

        JMenuItem clearMenuItem = new JMenuItem("Clear");
        clearMenuItem.setActionCommand("Clear");
        clearMenuItem.addActionListener(l -> {
            System.out.println("Pressed clear");
            manager.clear();
        });
        fileMenu.add(clearMenuItem);

        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.setActionCommand("Open");
        openMenuItem.addActionListener(l -> {
            System.out.println("Pressed open");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(currentFile == null ? new File(System.getProperty("user.home")) :
                    currentFile.getParentFile());
            fileChooser.setFileFilter(new AutonPlanerFileFilter());
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) { // If they selected a file
                currentFile = fileChooser.getSelectedFile();
                manager.clear();
                try {
                    String line = new Scanner(currentFile).nextLine();
                    if (line == null)
                        return;
                    loadJson(new JsonParser().parse(line).getAsJsonObject());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        fileMenu.add(openMenuItem);

        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem saveAsMenuItem = new JMenuItem("Save as");

        saveAsMenuItem.setActionCommand("Save as");
        saveAsMenuItem.addActionListener(l -> {
            System.out.println("Pressed save as");
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(currentFile == null ? new File(System.getProperty("user.home")) :
                    currentFile.getParentFile());
            fileChooser.setSelectedFile(currentFile == null ? new File("newAuton.apf") : currentFile);
            fileChooser.setFileFilter(new AutonPlanerFileFilter());
            int result = fileChooser.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) { // If they selected a file
                currentFile = fileChooser.getSelectedFile();
                new ActionCallHelper().callActions(saveMenuItem);
            }
        });

        saveMenuItem.setActionCommand("Save");
        saveMenuItem.addActionListener(l -> {
            System.out.println("Pressed save");
            if (currentFile == null) {
                new ActionCallHelper().callActions(saveAsMenuItem);
                return;
            }
            try {
                PrintWriter writer = new PrintWriter(currentFile);
                writer.print(toJson().toString());
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        // End menu creation ---------

        // Add our content to the frame
        mainPanel.add(fieldPanel, BorderLayout.CENTER);
        mainPanel.add(actionList, BorderLayout.EAST);
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        contentPanel.add(sidePanel, BorderLayout.EAST);
        frame.setJMenuBar(menuBar);
        frame.getContentPane().add(contentPanel, BorderLayout.CENTER);

        // Display frame
        frame.pack();
        frame.setVisible(true);
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
        offsetField.setText(object.get("startX").getAsInt() + "," + -1 * object.get("startY").getAsInt());
        fieldPanel.getRobot().setResting(515 + object.get("startX").getAsInt(),
                630 + object.get("startY").getAsInt());
        // TODO Load skills and preloaded state
    }

    public JsonObject toJson() {
        JsonObject file = new JsonObject();
        file.addProperty("autonName", autonName);
        file.addProperty("startRot", startingRotation);
        file.addProperty("startX", fieldPanel.getRobot().getRestingX() - 515);
        file.addProperty("startY", fieldPanel.getRobot().getRestingY() - 630);
        file.add("actions", manager.toJson());
        // TODO Save skills and preloaded state
        System.out.println(file.toString());
        return file;
    }

    public ArrayList<String> getActionTypes() {
        return manager.getActionTypes();
    }
}
