package framework.common.functions;

import java.awt.*;

public final class MouseFunctions {
    /**
     * Moves the mouse pointer to the center of screen
     */
    public static void centerMouse() {
        try {
            Robot robot = new Robot();
            int x = java.awt.Toolkit.getDefaultToolkit().getScreenSize().width;
            int y = java.awt.Toolkit.getDefaultToolkit().getScreenSize().height;
            robot.mouseMove(x / 2, y / 2);
        } catch (AWTException e) {
            DateFunctions.logger.debug("MouseFunctions.centerMouse", e);
        }
    }

    /**
     * Away mouse to 0.0 coordinates
     */
    public static void awayMouse() {
        try {
            Robot robot = new Robot();
            robot.mouseMove(1, 1);
        } catch (AWTException e) {
            DateFunctions.logger.debug("MouseFunctions.awayMouse", e);
        }
    }
}
