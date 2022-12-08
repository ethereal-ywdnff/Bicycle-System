package view.StaffSystem.Stocks;

import model.FrameSet;
import model.ShockAbsorberType;
import view.StaffSystem.Stocks.Popup.CreateFramesetPopup;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.util.List;

/**
 * This class creates the page for frameset properties managed by staff.
 *
 * FramesetStock.java
 */

public class FramesetStock extends StockDashboard {

    private JFormattedTextField framesizeSection;
    private JComboBox<ShockAbsorberType> shocksSection;
    private JFormattedTextField gearCountSection;

    public FramesetStock(FramesetTableModel model) {
        super(model);
    }

    @Override
    public JPanel changePropertiesRightPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        this.framesizeSection = this.createNumberField(2);
        panel.add(createLayoutForLabelAndField("Frame Size", this.framesizeSection));

        this.gearCountSection = this.createNumberField(0);
        panel.add(createLayoutForLabelAndField("Gear Count", this.gearCountSection));

        this.shocksSection = new JComboBox<>();

        for (ShockAbsorberType type : ShockAbsorberType.values()) {
            this.shocksSection.addItem(type);
        }
        panel.add(createLayoutForLabelAndField("Shock Type", this.shocksSection));

        return panel;
    }

    @Override
    protected List<FrameSet> getAll() {
        return this.componentService.getAllFrameSets();
    }

    @Override
    protected void createComponentPopup() {
        new CreateFramesetPopup();
    }

    @Override
    protected void onApplyChanges() {
        FramesetTableModel model = (FramesetTableModel) getModel();
        FrameSet frameSet = model.getDataAt(getSelectedRow());

        String componentName = this.componentNameSection.getText().replace(",", "");
        double unitCost = Double.parseDouble(this.unitCostSection.getText().replace(",", ""));
        int quantity = Integer.parseInt(this.quantitySection.getText().replace(",", ""));
        double frameSize = Double.parseDouble(this.framesizeSection.getText().replace(",", ""));
        ShockAbsorberType shocksType = (ShockAbsorberType) this.shocksSection.getSelectedItem();
        int gearCount = Integer.parseInt(this.gearCountSection.getText().replace(",", ""));
        System.out.println("Updating object to: " + unitCost + " " + quantity + " " + frameSize + " " + shocksType);
        this.componentService.updateFrameset(frameSet.getBrandName(), componentName, frameSet.getSerialNumber(), unitCost, quantity, frameSize, shocksType, gearCount);
    }

    @Override
    protected void onRowSelected() {
        boolean isEnabled = getSelectedRow() != -1;
        this.unitCostSection.setEnabled(isEnabled);
        this.quantitySection.setEnabled(isEnabled);
        this.framesizeSection.setEnabled(isEnabled);
        this.shocksSection.setEnabled(isEnabled);
        this.gearCountSection.setEnabled(isEnabled);

        if (isEnabled) {
            StockTableModel model = getModel();
            FrameSet data = (FrameSet) model.getDataAt(getSelectedRow());

            this.componentNameSection.setText(data.getComponentName());
            this.unitCostSection.setText(String.valueOf(data.getUnitCost()));
            this.quantitySection.setText(String.valueOf(data.getQuantity()));
            this.framesizeSection.setText(String.valueOf(data.getFrameSize()));
            this.shocksSection.setSelectedItem(data.getShocks());
            this.gearCountSection.setValue(data.getGears());
            System.out.println("We're populating the fields with " + data.getBrandName() + " to " + data.getFrameSize() + " " + data.getShocks());
        }
    }
}

