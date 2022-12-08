package view.StaffSystem;

import model.BicycleOrder;
import model.OrderStatus;
import controllers.OrderService;
import controllers.ServiceProvider;
import model.users.Staff;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.text.DecimalFormat;

/**
 * Allows a staff member to manage the orders, such as checking
 * customer details, bikes ordered and the status of orders.
 * OrderManagement.java
 */

public class OrderManagement extends JPanel {

    private static final DecimalFormat df = new DecimalFormat("0.00");
    private JTable allOrdersTable;
    private JTextArea rowInformation;
    private OrderService orderService;
    private JButton removeOrderButton;
    private OrderStatusButtonGroup changeStatus;
    private JButton applyButton;
    private JButton refreshButton;
    private int selectedRow = -1;
    private Staff staff;
    private JTextField searchOrderField;
    private JButton searchOrderSubmitButton;

    /**
     * Constructor - Creates the panel for which staff members can
     * see orders in a table and make actions.
     */
    public OrderManagement(Staff staff) {
        this.orderService = ServiceProvider.getSingleton(OrderService.class);
        this.staff = staff;
        this.setBackground(Color.BLUE);
        this.setLayout(new BorderLayout());
        this.add(createOrderTable(), BorderLayout.CENTER);
        this.add(viewRowInformation(), BorderLayout.SOUTH);
        this.addActionListeners();
        this.resetState();
    }

    /**
     * Gets the model of allOrderTable.
     * @return OrderTableModel which has casted the allOrdersTable JTable.
     */
    private OrderTableModel getModel() {
        return (OrderTableModel)this.allOrdersTable.getModel();
    }

    /**
     * Gets the order at the row selected by the staff member.
     * @return Order at the chosen row.
     */
    private BicycleOrder getSelectedOrder() {
        if(this.selectedRow == -1 || this.getModel() == null) {
            return null;
        }
        return this.getModel().getOrderAt(this.selectedRow);
    }

    /**
     * Access controls on the bottom of the staff dashboard where staff
     * can see order information, change status of order and use buttons
     * to apply different operations on the table such as refresh and remove.
     * @return JPanel allowing staff to see order information and apply changes to orders.
     */
    private JPanel viewRowInformation() {
        JPanel informationPanel = new JPanel();
        informationPanel.setLayout(new GridLayout(1, 3));

        // Set text area properties
        rowInformation = new JTextArea();
        rowInformation.setEditable(false);
        rowInformation.setLineWrap(true);
        rowInformation.setWrapStyleWord(true);

        // Make sure all order information can be seen using a scroll bar
        JScrollPane rowInformationScrollArea = new JScrollPane(rowInformation);
        rowInformationScrollArea.setPreferredSize(new Dimension(this.getWidth(), 150));
        rowInformationScrollArea.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        // Creates the section where the staff member can set the status of an order
        JPanel buttonGroup = new JPanel();
        buttonGroup.setLayout(new BoxLayout(buttonGroup, BoxLayout.Y_AXIS));
        JLabel orderStatusLabel = new JLabel("Change Status");
        buttonGroup.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonGroup.add(orderStatusLabel);

        // Buttons and status change button group
        changeStatus = new OrderStatusButtonGroup(buttonGroup);
        applyButton = new JButton("Apply Changes");
        refreshButton = new JButton("Refresh Table");
        removeOrderButton = new JButton("Remove Order");

        JPanel refreshRemoveApplyAndSearchPanel = new JPanel();
        refreshRemoveApplyAndSearchPanel.setLayout(new GridLayout(4, 1));
        refreshRemoveApplyAndSearchPanel.add(refreshButton);
        refreshRemoveApplyAndSearchPanel.add(removeOrderButton);
        refreshRemoveApplyAndSearchPanel.add(applyButton);
        refreshRemoveApplyAndSearchPanel.add(searchOrderSection());

        informationPanel.add(rowInformationScrollArea);
        informationPanel.add(buttonGroup);
        informationPanel.add(refreshRemoveApplyAndSearchPanel);
        return informationPanel;
    }

    private JPanel searchOrderSection() {
        JPanel searchSection = new JPanel();
        searchSection.setLayout(new BoxLayout(searchSection, BoxLayout.X_AXIS));
        searchOrderField = new JTextField();
        searchOrderSubmitButton = new JButton("Search Order");
        searchSection.add(searchOrderField);
        searchSection.add(searchOrderSubmitButton);
        return searchSection;
    }

    /**
     * A section that shows the orders of the customers in a table like.
     * @return JPanel with the table of orders.
     */
    private JPanel createOrderTable() {
        JPanel orderTable = new JPanel();
        orderTable.setLayout(new BoxLayout(orderTable, BoxLayout.X_AXIS));

        OrderTableModel setupModel = new OrderTableModel();

        allOrdersTable = new JTable(setupModel);
        // Make the cells uneditable
        allOrdersTable.setDefaultEditor(Object.class, null);
        refreshTable();
        allOrdersTable.getTableHeader().setBackground(Color.GRAY);
        allOrdersTable.getTableHeader().setForeground(Color.WHITE);
        allOrdersTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(allOrdersTable);
        orderTable.add(scrollPane);
        return orderTable;
    }

    /**
     * Refreshes the table so new customer orders can be seen.
     */
    private void refreshTable() {
        OrderTableModel model = this.getModel();
        model.reset();

        // Refresh data from database
        for (BicycleOrder order : orderService.getAllOrders()) {
            model.addOrderRow(order);
        }
    }

    /**
     * Table only shows the order that was searched.
     * @param order to see in the table.
     */
    private void showSingleOrderInTable(BicycleOrder order) {
        OrderTableModel model = this.getModel();
        model.reset();
        model.addOrderRow(order);
    }

    /**
     * Gets the information of an order - its details and the items required to
     * build the bicycle.
     * @param row index of the row to get the information from.
     * @return row information and bicycle information as a string.
     */
    private String getRowInformation(int row) {
        if(row == -1) {
            return "";
        }
        BicycleOrder order = this.getModel().getOrderAt(selectedRow);

        String rowInformation = "";
        // Columns information
        int numberOfColumns = allOrdersTable.getColumnCount();
        OrderTableModel model = this.getModel();
        for (int i = 0; i < numberOfColumns; i++) {
            String columnName = model.getColumnName(i);
            String value = model.getValueAt(row, i).toString();
            rowInformation += columnName + ": " + value + "\n";
        }

        rowInformation += "Price: £" + df.format(order.getPrice()) + " + £10 Assembly Fee \n\n";

        // Bicycle information
        rowInformation += "Wheels: \n" + order.getWheel().toReceiptString() + "\n\n";
        rowInformation += "Handlebars: \n" + order.getHandleBar().toReceiptString() + "\n\n";
        rowInformation += "Frameset: \n" + order.getFrameSet().toReceiptString() + "\n\n";
        return rowInformation;
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
     * Enables and disables buttons depending on if an item is selected on not.
     * Prevents spamming of buttons.
     */
    private void updateToggles() {
        BicycleOrder order = getSelectedOrder();
        boolean isEnabled = order != null;

        // We only want to set the value of the status toggles if we're enabling the menu
        if (isEnabled) {
            changeStatus.setOrderStatusValue(getSelectedOrder().getOrderStatus());
        }
        removeOrderButton.setEnabled(isEnabled);
        changeStatus.setEnabled(isEnabled);
        applyButton.setEnabled(isEnabled);
        rowInformation.setText(getRowInformation(selectedRow));

        if(order != null) {
            changeStatus.setEnabled(order.getOrderStatus() != OrderStatus.FULFILLED);
        }
    }

    /**
     * Adds the action listeners
     */
    private void addActionListeners() {
        allOrdersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                selectedRow = allOrdersTable.rowAtPoint(evt.getPoint());
                updateToggles();
            }
        });

        searchOrderSubmitButton.addActionListener(e -> {
            try {
                int orderID = Integer.parseInt(searchOrderField.getText());
                System.out.println(orderID);
                BicycleOrder order = orderService.getOrderById(orderID);
                showSingleOrderInTable(order);
            } catch (Exception ex) {
                System.out.println("Order number does not exist.");
            }
        });

        removeOrderButton.addActionListener(e -> {
            OrderTableModel model = getModel();
            if (model.getRowCount() != 0) {
                if (selectedRow != -1) {
                    int orderID = model.getOrderAt(selectedRow).getOrderId();
                    orderService.deleteOrderById(orderID);
                    resetState();
                }
            }
        });

        refreshButton.addActionListener(e ->  {
            resetState();
            buttonDelay(refreshButton, 2000);
        });

        applyButton.addActionListener(e -> {
            OrderTableModel model = getModel();

            // If we haven't selected
            if (changeStatus.getSelection() != null && selectedRow != -1) {
                String optionSelected = changeStatus.getSelection().getActionCommand();
                if (optionSelected != null) {
                    BicycleOrder order = model.getOrderAt(selectedRow);
                    OrderStatus newOrderStatus = changeStatus.getOrderStatusSelected();

                    System.out.println("Updating order with id " + order.getOrderId());

                    orderService.setOrderStatus(order.getOrderId(), newOrderStatus);

                    // If we're fulfilling an order, then deplete the stock for each component
                    if(newOrderStatus == OrderStatus.FULFILLED) {
                        boolean result = orderService.fulfillOrder(order, staff);

                        if(result) {
                            System.out.println("Successfully fulfilled order status");
                        } else {
                            System.out.println("Failed to fulfil order as there is not enough quantity");
                        }
                    }
                }
            }
            resetState();
        });
    }

    /**
     * Resets the state so certain buttons are not clickable
     * again until a row is clicked again. Prevents spamming.
     */
    private void resetState() {
        // Deselect all table rows
        selectedRow = -1;
        changeStatus.clearSelection();
        updateToggles();
        refreshTable();
    }
}
