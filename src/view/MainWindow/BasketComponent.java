package view.MainWindow;

import model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.text.DecimalFormat;

/**
 * Creates a section where it allows the customer to see what items are in their
 * basket. Each item is shown in a text area.
 *
 * BasketComponent.java
 */

public class BasketComponent extends JPanel {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final double assemblyFee = 10;
    private JTextArea basketTextArea;
    private BicycleOrder bicycle;

    /**
     * Constructor creates a JPanel basket where core.users can see what items
     * from their order have been added, with their price and other item information.
     */
    public BasketComponent() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // BasketTextArea displays the items in the basket and gives details about them.
        // Setting properties of basketTextArea
        basketTextArea = new JTextArea();
        basketTextArea.setLineWrap(true);
        basketTextArea.setWrapStyleWord(true);
        basketTextArea.setEditable(false);
        basketTextArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Ensure the text doesn't exceed the text area.
        JScrollPane informationAreaScroll = new JScrollPane(basketTextArea);
        informationAreaScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(informationAreaScroll);
    }

    /**
     * Updates the information in the basket.
     */
    public void update() {
        // Remove all current text
        basketTextArea.removeAll();

        if (this.bicycle == null)
            return;

        // Repopulate with receipt data
        Wheel wheel = this.bicycle.getWheel();
        HandleBar handlebar = this.bicycle.getHandleBar();
        FrameSet frameSet = this.bicycle.getFrameSet();

        // Formatting the receipt
        basketTextArea.setText("");

        // create a string made up of n copies of string s
        String heading = String.format("%0" + 85 + "d", 0).replace("0", "*") + "\n";

        basketTextArea.append("Custom Bicycle Name: " + this.bicycle.getBicycleName() + "\n");
        basketTextArea.append("Bicycle Brand Name: " + this.bicycle.getBrandName() + "\n\n");

        basketTextArea.append(heading);
        basketTextArea.append(centerString(130, "Receipt") + "\n");
        basketTextArea.append(heading);

        String sf = "%-35s%20s\t%15s\t%15s\n";
        basketTextArea.append(String.format(sf, "Product Details", "Quantity", "Unit Cost", "Amount") + "\n");

        if(wheel != null) {
            basketTextArea.append(String.format(sf, "Wheels", 2, "£" + wheel.getUnitCost(), "£" + wheel.getUnitCost() * 2));
            basketTextArea.append(wheel.toReceiptString() + "\n");
        }

        if(frameSet != null) {
            basketTextArea.append(String.format(sf, "Frameset", 1, "£" + frameSet.getUnitCost(), "£" + frameSet.getUnitCost()));
            basketTextArea.append(frameSet.toReceiptString() + "\n");
        }

        if(handlebar != null) {
            basketTextArea.append(String.format(sf, "Handlebars", 1, "£" + handlebar.getUnitCost(), "£" + handlebar.getUnitCost()));
            basketTextArea.append(handlebar.toReceiptString() + "\n");
        }
        basketTextArea.append("\n\n Total: £" + df.format(this.bicycle.getPrice()) + " + £" + df.format(assemblyFee) + " Assembly Fee" + "\n\n");
        basketTextArea.append(heading);
        basketTextArea.append(centerString(130, "End of Receipt") + "\n");
        basketTextArea.append(heading);
    }

    public static String centerString(int width, String s) {
        return String.format("%-" + width  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
    }

    /**
     * Sets the bike and updates so the parts for the bike can be seen in the basket.
     * @param bicycle that the customer has created.
     */
    public void setBike(BicycleOrder bicycle) {
        this.bicycle = bicycle;
        this.update();
    }
}