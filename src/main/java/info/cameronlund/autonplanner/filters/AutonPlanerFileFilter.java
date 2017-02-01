package info.cameronlund.autonplanner.filters;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class AutonPlanerFileFilter extends FileFilter {

    public String getDescription() {
        return "Auton Planner files (*.apf)";
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        } else {
            String filename = f.getName().toLowerCase();
            return filename.endsWith(".apf");
        }
    }

}
