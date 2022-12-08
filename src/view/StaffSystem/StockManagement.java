package view.StaffSystem;

import view.StaffSystem.Stocks.*;
import javax.swing.*;

/**
 * Creates a JPanel that allows staff to switch between panels
 * whether they want to see wheels, framesets or handlebars properties.
 *
 * StockManagement.java
 */

public class StockManagement extends JPanel {

    private JTabbedPane panelChooser;

    /**
     * Constructor creates a tabbed pain to allow staff to switch
     * between the different panels.
     */
    public StockManagement() {
        this.panelChooser = new JTabbedPane();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Tabs
        panelChooser.addTab("Wheels Properties", new WheelStock(new WheelTableModel()));
        panelChooser.addTab("Framesets Properties", new FramesetStock(new FramesetTableModel()));
        panelChooser.addTab("Handlebars Properties", new HandlebarStock(new HandlebarTableModel()));
        this.add(panelChooser);
    }
}
