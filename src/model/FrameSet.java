package model;

/**
 * A frameset is a component of a bicycle.
 *
 * Frameset.java
 */
public class FrameSet extends Component {

    private final double frameSize;
    private final ShockAbsorberType shocks;
    private final int gears;

    @Override
    public String toReceiptString() {
        String result = super.toReceiptString();
        result += "Frame Size: " + frameSize + "cm\n";
        result += "Shock Absorbers: " + shocks.toString() + "\n";

        return result;
    }

    public FrameSet(String brandName, String serialNumber, String componentName, double unitCost, int quantity, double frameSize, ShockAbsorberType b, int gears) {
        super(brandName, serialNumber, componentName, unitCost, quantity);
        this.frameSize = frameSize;
        this.shocks = b;
        this.gears = gears;
    }

    public double getFrameSize() {
        return frameSize;
    }

    public ShockAbsorberType getShocks() {
        return shocks;
    }

    public int getGears() {
        return gears;
    }
}
