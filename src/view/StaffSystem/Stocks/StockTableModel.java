package view.StaffSystem.Stocks;

import model.Component;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

/**
 * A model that backs a component that allows us to get the component selected
 * and make modifications to the table.
 * @param <C> component type such as frameset, wheel and handlebar.
 */

public abstract class StockTableModel<C extends Component> extends DefaultTableModel {

    protected List<C> data;

    public StockTableModel() {
        this.data = new ArrayList<>();
    }

    /**
     * Resets the data in the table
     */
    public void reset() {
        this.setRowCount(0);
        this.data = new ArrayList<>();
    }

    public C getDataAt(int rowIndex) {
        return this.data.get(rowIndex);
    }
    public abstract void addDataRow(C data);
}
