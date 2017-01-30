package info.cameronlund.autonplanner.helpers;

import java.awt.*;

public final class HtmlColor {
    public static Color getColor(String HTML) {
        int intValue = Integer.parseInt(HTML, 16);
        return new Color(intValue);
    }
}
