package model;

/**
 * A handlebar is a component of a bicycle
 *
 * Handlebar.java
 */
public class HandleBar extends Component {

    private HandleBarType handleType;

    public HandleBar(String brandName, String serialNumber, String componentName, double unitCost, int quantity, HandleBarType a) {
        super(brandName, serialNumber, componentName, unitCost, quantity);
        this.handleType = a;
    }

    public HandleBarType getHandleType() {
        return handleType;
    }

    @Override
    public String toReceiptString() {
        String str = super.toReceiptString();
        str += "Type: " + handleType;
        return str;
    }
}
