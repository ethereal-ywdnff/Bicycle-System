package view.StaffSystem.Stocks;

import model.Component;
import controllers.ComponentService;
import controllers.ServiceProvider;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.InternationalFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * This manages the base display format for a stock dashboard. Handlebars, wheels and framesets all need a stock page,
 * and all look similar, except for a few things.
 * @param <C> The type of the stock model we are trying to display
 * @param <T> The table type we are using to get and store data from.
 */
public abstract class StockDashboard<C extends model.Component, T extends StockTableModel<C>> extends JPanel  {

    private JTable stockTable;
    private T tableModel;
    private JButton applyChangesButton;
    private JButton refreshButton;
    private JButton addCompononentButton;
    private int selectedRow;
    protected JFormattedTextField unitCostSection;
    protected JFormattedTextField quantitySection;
    protected JTextField componentNameSection;
    protected ComponentService componentService;

    public StockDashboard(T model) {
        this.componentService = ServiceProvider.getSingleton(ComponentService.class);
        this.setLayout(new BorderLayout());
        this.tableModel = model;
        this.add(createTable(model), BorderLayout.CENTER);
        this.add(controlPanel(), BorderLayout.SOUTH);
        this.refreshTable();
        this.addActionListeners();
    }

    /**
     * Creates a JTable inside a JPanel.
     * @param model The model to apply to the JTable.
     * @param <T> The type reference of table model.
     * @return
     */
    private <T extends DefaultTableModel> JPanel createTable(T model) {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.X_AXIS));

        this.stockTable = new JTable(model);
        // Make the cells uneditable
        this.stockTable.setDefaultEditor(Object.class, null);
        // Prevent sorting and moving columns in table
        this.stockTable.getTableHeader().setEnabled(false);
        this.stockTable.getTableHeader().setBackground(Color.GRAY);
        this.stockTable.getTableHeader().setForeground(Color.WHITE);
        this.stockTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(this.stockTable);
        tablePanel.add(scrollPane);

        return tablePanel;
    }

    /**
     * Creates the bottom control panel that every stock dashboard has. Responsible for altering the data of a table entry.
     * @return The JPanel containing the components in the control panel.
     */
    private JPanel controlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 3));

        controlPanel.add(changePropertiesLeftPanel());
        controlPanel.add(changePropertiesRightPanel());
        controlPanel.add(buttonsSection());

        return controlPanel;
    }

    /**
     * Creates a delay on a given button. Used for when refresh table button is pressed
     * to prevent spamming database call to retrieve orders.
     * @param button to delay
     * @param ms time to delay for
     */
    private void buttonDelay(JButton button, long ms) {
        button.setEnabled(false);
        new SwingWorker() {
            @Override protected Object doInBackground() throws Exception {
                Thread.sleep(ms);
                return null;
            }
            @Override protected void done() { button.setEnabled(true); }
        }.execute();
    }

    /**
     * Creates the buttons relevant to altering a stock (Apply Changes, Refresh and
     * adding a new component).
     * @return The JPanel containing the buttons in the control panel.
     */
    private JPanel buttonsSection() {
        JPanel buttonSection = new JPanel();

        buttonSection.setLayout(new GridLayout(3, 1));
        applyChangesButton = new JButton("Apply Changes");
        refreshButton = new JButton("Refresh");
        addCompononentButton = new JButton("Add Component");

        buttonSection.add(applyChangesButton);
        buttonSection.add(refreshButton);
        buttonSection.add(addCompononentButton);

        return buttonSection;
    }

    /**
     * Gets the generic table model of this table.
     * @return The generic type of the tabe model.
     */
    protected T getModel() {
        return (T)this.stockTable.getModel();
    }

    /**
     * Refreshes the table with new data from the database. Please note: This method may be very slow if there are alot
     * of entries in the database that need to be added to the table.
     */
    private void refreshTable() {
        System.out.println("Refresh Stock Table");

        this.tableModel.reset();
        setSelectedRow(-1);

        // refresh data from database
        for (Component order : getAll()) {
            this.tableModel.addDataRow((C)order);
        }
    }

    /**
     * Resets the entire table state to when it was first created. This includes refreshing the data from the database,
     * and which item is selected.
     */
    protected void resetState() {
        System.out.println("Refreshing Table");

        // Deselect all table rows
        setSelectedRow(-1);
        refreshTable();
    }

    /**
     * A helper method to create a number field that is never an empty string. Very helpful as we do not need to do backend
     * input sanitization.
     * @param decimalPlaces The decimal accuracy a user is allowed to specify
     * @return The JFormattedTextField component created.
     */
    protected JFormattedTextField createNumberField(int decimalPlaces) {
        JFormattedTextField numberField = new JFormattedTextField(new JFormattedTextField.AbstractFormatterFactory() {
            @Override
            public JFormattedTextField.AbstractFormatter getFormatter(JFormattedTextField tf) {
                NumberFormat format = DecimalFormat.getInstance();
                format.setMinimumFractionDigits(decimalPlaces);
                format.setMaximumFractionDigits(decimalPlaces);
                format.setRoundingMode(RoundingMode.HALF_UP);
                InternationalFormatter formatter = new InternationalFormatter(format);
                formatter.setAllowsInvalid(false);
                formatter.setMinimum(0.0);
                formatter.setMaximum(10_000_000.00);
                return formatter;
            }
        });
        numberField.setColumns(20);
        return numberField;
    }

    /**
     * Creates the generic component properties panel on the bottom left of the dashboard. This is for all fields that are
     * specific to the 'Component.java' class ONLY, such as component name, unit cost and quantity.
     * @return The JPanel containing the labels
     */
    protected JPanel changePropertiesLeftPanel() {
        JPanel propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new GridLayout(3, 1));
        propertiesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));

        unitCostSection = createNumberField(2);
        quantitySection = createNumberField(0);
        componentNameSection = new JTextField();

        propertiesPanel.add(createLayoutForLabelAndField("Component Name", componentNameSection));
        propertiesPanel.add(createLayoutForLabelAndField("Unit Cost (£)", unitCostSection));
        propertiesPanel.add(createLayoutForLabelAndField("Quantity", quantitySection));

        return propertiesPanel;
    }

    /**
     * A helper function that adds a label to a field.
     * @param label The label to add
     * @param field The field to add the label to
     * @return A JPanel containing the label and field.
     */
    protected JPanel createLayoutForLabelAndField(String label, JComponent field) {
        JPanel labelAndField = new JPanel();
        labelAndField.setLayout(new GridLayout(2, 1));

        labelAndField.add(new JLabel(label));
        labelAndField.add(field);
        return labelAndField;
    }

    /**
     * Creates the middle panel for component-specific fields. For example a frame-set will have a dropdown for shock absorber type.
     * @return The JPanel to display
     */
    public abstract JPanel changePropertiesRightPanel();

    /**
     * Gets all the components of a specified generic type. For example FramesetStock will return all framesets in the database.
     * @return All components from the database matching the type specified.
     */
    protected abstract List<? extends Component> getAll();

    /**
     * Registers the actions for when certain UI components are interacted with.
     */
    private void addActionListeners() {
        stockTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setSelectedRow(stockTable.rowAtPoint(evt.getPoint()));
            }
        });

        refreshButton.addActionListener(e -> {
            resetState();
            buttonDelay(refreshButton, 2000);
        });

        applyChangesButton.addActionListener(e -> {
            this.onApplyChanges();
            this.resetState();
        });

        addCompononentButton.addActionListener(e -> {
            createComponentPopup();
        });
    }

    protected abstract void createComponentPopup();

    /**
     * This is called whenever the 'Apply Changes' button gets clicked in a dashboard.
     */
    protected abstract void onApplyChanges();

    /**
     * This is called whenever a row from the table is selected or deselected to null.
     */
    protected abstract void onRowSelected();

    /**
     * Gets the current selected row. If nothing is selected, it will return -1.
     * @return The index of the current row selected, starting at 0 for the first row of the table.
     */
    public int getSelectedRow() {
        return selectedRow;
    }

    /**
     * This method sets which row is selected in the table. Note: This method will fire the onRowSelected method.
     * @param row The row to select.
     */
    public void setSelectedRow(int row) {
        this.selectedRow = row;
        // Set the state of these buttons - we shouldn't be able to use these buttons if we haven't selected anything.
        this.applyChangesButton.setEnabled(selectedRow != -1);
        this.componentNameSection.setEnabled(selectedRow != -1);
        onRowSelected();
    }
}
