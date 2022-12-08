import view.GuiFrame;
import controllers.*;

/**
 * Main file to run the system.
 */
public class BicycleSystem {
    public static void main(String[] args) {
        ServiceProvider.addSingleton(DatabaseService.class);
        ServiceProvider.addSingleton(CustomerService.class);
        ServiceProvider.addSingleton(OrderService.class);
        ServiceProvider.addSingleton(ComponentService.class);
        ServiceProvider.addSingleton(StaffService.class);
        new GuiFrame();
    }
}