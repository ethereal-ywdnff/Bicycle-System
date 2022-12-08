package view.StaffSystem;

import model.OrderStatus;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class OrderStatusButtonGroup extends ButtonGroup {

    private List<JRadioButton> buttons = new ArrayList<>();

    public OrderStatusButtonGroup(JPanel buttonGroup) {
        super();

        // Set up the order status JRadioButtons
        for(String status : OrderStatus.getOrderStatusNames()) {
            JRadioButton button = new JRadioButton(status);
            button.setActionCommand(status);
            buttonGroup.add(button);
            this.buttons.add(button);
            this.add(button);
        }
    }

    /**
     * A helper function to get the value from the selected radio button.
     * @return The OrderStatus selected in the UI
     */
    public OrderStatus getOrderStatusSelected() {
        for (JRadioButton button : buttons) {
            if (button.isSelected()) {
                return OrderStatus.valueOf(button.getActionCommand().toUpperCase());
            }
        }
        return null;
    }

    /**
     * Sets the JRadioGroup button pertaining to the status passed in.
     * @param status The status to set
     */
    public void setOrderStatusValue(OrderStatus status) {
        for (JRadioButton button : buttons) {
            boolean isEnabled = status.toString().equalsIgnoreCase(button.getActionCommand());
            button.setSelected(isEnabled);
        }
    }

    /**
     * A helper function to set the states of all the buttons in OrderStatus
     * @param enabled The state to set the components
     */
    public void setEnabled(boolean enabled) {
        for(JRadioButton button : buttons) {
            button.setEnabled(enabled);
        }
    }
}
