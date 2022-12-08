package view.StaffSystem.Stocks;

import model.HandleBar;
import model.HandleBarType;
import view.StaffSystem.Stocks.Popup.CreateHandlebarPopup;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;

/**
 * This class creates the page for handlebar properties managed by staff.
 *
 * HandlebarStock.java
 */

public class HandlebarStock extends StockDashboard {

    private JComboBox<HandleBarType> handlebarSection;

    public HandlebarStock(HandlebarTableModel model) {
        super(model);
    }

    @Override
    public JPanel changePropertiesRightPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.handlebarSection = new JComboBox<>();

        for (HandleBarType type : HandleBarType.values()) {
            this.handlebarSection.addItem(type);
        }
        panel.add(createLayoutForLabelAndField("Handlebar Type", this.handlebarSection));

        return panel;
    }

    @Override
    protected List<HandleBar> getAll() {
        return this.componentService.getAllHandlebars();
    }

    @Override
    protected void createComponentPopup() {
        new CreateHandlebarPopup();
    }

    @Override
    protected void onApplyChanges() {
        HandlebarTableModel model = (HandlebarTableModel) getModel();
        HandleBar handleBar = model.getDataAt(getSelectedRow());

        String componentName = this.componentNameSection.getText();
        double unitCost = Double.parseDouble(this.unitCostSection.getText().replace(",", ""));
        int quantity = Integer.parseInt(this.quantitySection.getText().replace(",", ""));
        HandleBarType handleBarType = (HandleBarType) this.handlebarSection.getSelectedItem();

        System.out.println("We're updating the handlebar with: " + unitCost + " " + quantity + " " + handleBarType);
        this.componentService.updateHandlebar(handleBar.getBrandName(), componentName, handleBar.getSerialNumber(), unitCost, quantity, handleBarType);
    }

    @Override
    protected void onRowSelected() {
        boolean isEnabled = getSelectedRow() != -1;

        this.unitCostSection.setEnabled(isEnabled);
        this.quantitySection.setEnabled(isEnabled);
        this.handlebarSection.setEnabled(isEnabled);

        if (isEnabled) {
            StockTableModel model = getModel();
            HandleBar data = (HandleBar) model.getDataAt(getSelectedRow());

            this.componentNameSection.setText(data.getComponentName());
            this.unitCostSection.setText(String.valueOf(data.getUnitCost()));
            this.quantitySection.setText(String.valueOf(data.getQuantity()));
            this.handlebarSection.setSelectedItem(data.getHandleType());

            System.out.println("We're populating the fields with " + data.getBrandName() + " to " + data.getHandleType());
        }
    }

}
