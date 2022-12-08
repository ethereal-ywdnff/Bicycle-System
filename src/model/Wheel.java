package model;

/**
 * The wheel referenced in Bicycle.java.
 *
 * Wheel.java
 */
public class Wheel extends Component  {
    
    private double diameter;
    private WheelStyle wheelStyle;
    private BrakeType brakeType;

    public Wheel(String brandName, String serialNumber, String componentName, double unitCost, int quantity, double diameter, WheelStyle wheelStyle, BrakeType brakeType) {
        super(brandName, serialNumber, componentName, unitCost, quantity);
        this.diameter = diameter;
        this.wheelStyle = wheelStyle;
        this.brakeType = brakeType;
    }

    @Override
    public String toReceiptString() {
        String result = super.toReceiptString();
        result += "Wheel Style: " + wheelStyle + "\n";
        result += "Brake Type: " + brakeType + "\n";

        return result;
    }

    public double getDiameter() {
        return diameter;
    }

    public WheelStyle getWheelStyle() {
        return wheelStyle;
    }

    public BrakeType getBrakeType() {
        return brakeType;
    }

}
