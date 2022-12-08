package view.MainWindow;

import model.*;
import view.GuiFrame;
import controllers.ComponentService;
import controllers.ServiceProvider;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

/**
 * Main window seen when software is first launched.
 * Allows the shopper to select bike components, name the bike
 * and checkout. Also has customer and staff login buttons.
 *
 * MainWindowPanel.java
 */

public class MainWindowPanel extends JPanel {

    private GuiFrame frame;
    private JComboBox<Wheel> wheelOptionChosen;
    private JComboBox<FrameSet> framesetOptionChosen;
    private JComboBox<HandleBar> handlebarOptionChosen;
    private JButton staffUser;
    private JButton customerUser;
    private BasketComponent basketComponent;
    private ComponentService componentService;
    private JButton checkoutButton;
    private JTextField bicycleNameField;

    /**
     * Creates the main window panel that is seen by the user when
     * the application is first launched.
     */
    public MainWindowPanel(GuiFrame frame) {
        // Required services
        this.componentService = ServiceProvider.getSingleton(ComponentService.class);
        this.frame = frame;

        // Panel properties
        this.setLayout(new GridLayout(1, 2));
        this.add(leftPanel());
        this.add(rightPanel());
        this.updateBasket();
        this.addActionListeners();
    }

    /**
     * Collects the JPanels together for the left side of the window.
     * @return JPanel for the whole left side of the window.
     */
    private JPanel leftPanel() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.DARK_GRAY);
        leftPanel.setBorder(new EmptyBorder(20, 10, 10, 10));
        leftPanel.setLayout(new BorderLayout());

        JPanel bicycleOptionsAndNamePanelCombined= new JPanel();
        bicycleOptionsAndNamePanelCombined.setLayout(new BorderLayout());
        bicycleOptionsAndNamePanelCombined.add(buildBikeInformationLabelsPanel(), BorderLayout.NORTH);
        bicycleOptionsAndNamePanelCombined.add(bicycleNameSection(), BorderLayout.CENTER);

        leftPanel.add(bicycleOptionsAndNamePanelCombined, BorderLayout.NORTH);
        leftPanel.add(optionsForBicyclePanel(), BorderLayout.CENTER);
        return leftPanel;
    }

    private JPanel bicycleNameSection() {
        JPanel bicycleNameSection = new JPanel();
        bicycleNameSection.setBackground(Color.DARK_GRAY);
        JLabel bicycleNameLabel = new JLabel("Bicycle Name");
        bicycleNameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        bicycleNameLabel.setForeground(Color.WHITE);
        bicycleNameField = new JTextField();
        bicycleNameField.setColumns(20);
        bicycleNameField.setText("Example Bicycle Name");
        bicycleNameSection.add(bicycleNameLabel);
        bicycleNameSection.add(bicycleNameField);

        return bicycleNameSection;
    }

    /**
     * Collects the JPanels together for the right side of the window.
     * @return JPanel for the whole right side of the window.
     */
    private JPanel rightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(userOptionPanel(), BorderLayout.NORTH);
        rightPanel.add(itemsTextAreaPanel(), BorderLayout.CENTER);

        JPanel updateBasketAndCheckoutPanel = new JPanel();
        updateBasketAndCheckoutPanel.setBackground(Color.DARK_GRAY);
        updateBasketAndCheckoutPanel.setLayout(new FlowLayout());

        // Checkout button when bicycle is ready
        checkoutButton = new JButton("Continue");
        checkoutButton.setAlignmentX(CENTER_ALIGNMENT);
        updateBasketAndCheckoutPanel.add(checkoutButton);

        rightPanel.add(updateBasketAndCheckoutPanel, BorderLayout.SOUTH);
        return rightPanel;
    }

    /**
     * Creates the panel with drop down options for the different bicycle parts.
     * @return panel containing options for bicycle parts.
     */
    private JPanel optionsForBicyclePanel() {
        JPanel optionsPanel = new JPanel();
        optionsPanel.setBackground(Color.DARK_GRAY);

        JPanel comboboxPanel = new JPanel();
        // Set panel properties
        comboboxPanel.setLayout(new BoxLayout(comboboxPanel, BoxLayout.Y_AXIS));
        comboboxPanel.setBackground(Color.DARK_GRAY);

        // Wheel options for the bicycle
        Wheel[] wheels = componentService.getAllWheels()
                .stream().filter(x -> x.getQuantity() > 0)
                .toArray(Wheel[]::new);
        wheelOptionChosen = new JComboBox<>(wheels);
        createComboBoxOptionWithLabel("Wheel Type", wheelOptionChosen, comboboxPanel);

        // Frameset options for the bicycle
        FrameSet[] frameSets = componentService.getAllFrameSets()
                .stream().filter(x -> x.getQuantity() > 0)
                .toArray(FrameSet[]::new);
        framesetOptionChosen = new JComboBox(frameSets);
        createComboBoxOptionWithLabel("Frameset", framesetOptionChosen, comboboxPanel);

        // Handlebar options for the bicycle
        HandleBar[] handleBars = componentService.getAllHandlebars()
                .stream().filter(x -> x.getQuantity() > 0)
                .toArray(HandleBar[]::new);
        handlebarOptionChosen = new JComboBox<>(handleBars);
        createComboBoxOptionWithLabel("Handlebar Type", handlebarOptionChosen, comboboxPanel);

        optionsPanel.add(comboboxPanel);
        return optionsPanel;
    }

    /**
     * Creates a panel with a combobox and a corresponding label.
     * The label is positioned above the combobox.
     *
     * @param labelName name for that combobox (purpose it serves).
     * @param combobox that needs positioning.
     * @param panel main panel which the combobox-label section get added to.
     * @return JPanel containing a neat layout of a combobox and the corresponding label.
     */
    private JPanel createComboBoxOptionWithLabel(String labelName, JComboBox combobox, JPanel panel) {
        JPanel section = new JPanel();
        // The label must be positioned above the combobox
        section.setLayout(new GridLayout(2, 1));
        section.setBackground(Color.DARK_GRAY);

        // Spacing / Padding
        section.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Creates the label
        JLabel label = new JLabel(labelName);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        section.add(label);

        if(combobox.getItemCount() > 0) {
            combobox.setSelectedIndex(0);
        }
        section.add(combobox);
        panel.add(section);
        return section;
    }

    /**
     * Contains buttons in which a staff and customer are redirected onto staff
     * login or customer can access their order.
     * @return panel for the customer or staff access
     */
    private JPanel userOptionPanel() {
        JPanel userPanel = new JPanel();
        userPanel.setBackground(Color.DARK_GRAY);
        int BUTTON_SIZE_WIDTH = 100;
        int BUTTON_SIZE_HEIGHT = 20;

        // Login Label for whether the user wants to login as a customer or staff member
        JLabel loginLabel = new JLabel("Login:");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 15));
        loginLabel.setAlignmentX(CENTER_ALIGNMENT);
        loginLabel.setForeground(Color.WHITE);
        userPanel.add(loginLabel);

        // Takes you to the staff login page
        staffUser = new JButton("Staff");
        staffUser.setPreferredSize(new Dimension(BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT));
        userPanel.add(staffUser);

        // Takes you to the customer login page
        customerUser = new JButton("Customer");
        customerUser.setPreferredSize(new Dimension(BUTTON_SIZE_WIDTH, BUTTON_SIZE_HEIGHT));
        userPanel.add(customerUser);

        return userPanel;
    }

    /**
     * Creates the area where items can be seen in the basket.
     * @return JPanel of the sections where items can be seen in the basket.
     */
    private JPanel itemsTextAreaPanel() {
        JPanel informationAreaPanel = new JPanel();
        informationAreaPanel.setLayout(new BoxLayout(informationAreaPanel, BoxLayout.Y_AXIS));
        informationAreaPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        informationAreaPanel.setBackground(Color.DARK_GRAY);

        // Basket Label
        JLabel informationLabel = new JLabel("Items in Basket");
        informationLabel.setFont(new Font("Arial", Font.BOLD, 15));
        informationLabel.setForeground(Color.WHITE);
        informationLabel.setAlignmentX(CENTER_ALIGNMENT);
        informationAreaPanel.add(informationLabel);

        // Creates a basket so the items the customer has added can be seen
        basketComponent = new BasketComponent();
        informationAreaPanel.add(basketComponent);

        return informationAreaPanel;
    }

    /**
     * Updates the information text panel to show information about the options the user has selected.
     */
    private void updateBasket() {
        BicycleOrder bike = this.getBicycleFromFields();
        basketComponent.setBike(bike);

        boolean enabled = bike.isComplete() && !bicycleNameField.getText().equals("");
        checkoutButton.setEnabled(enabled);
    }

    /**
     * Adds the actions listeners that have been used, for the main window panel.
     */
    public void addActionListeners() {
        // Update Basket action listener
        wheelOptionChosen.addActionListener(e -> updateBasket());
        handlebarOptionChosen.addActionListener(e -> updateBasket());
        framesetOptionChosen.addActionListener(e -> updateBasket());

        // Take user to staff user page if clicked
        staffUser.addActionListener(e -> frame.switchToStaffLoginPage());

        // Take user to customer login page
        customerUser.addActionListener(e -> frame.switchToCustomerLoginPage());
        checkoutButton.addActionListener(e -> frame.switchToCheckoutPage());

        // Listen for changes in the text
        bicycleNameField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                updateBasket();
            }
            public void removeUpdate(DocumentEvent e) {
                updateBasket();
            }
            public void insertUpdate(DocumentEvent e) {
                updateBasket();
            }
        });
    }

    /**
     * Creates the text area panel that welcomes the user to the bicycle software.
     * @return JPanel containing the welcome message text.
     */
    private JPanel buildBikeInformationLabelsPanel() {
        JPanel buildingBikeTextPanel = new JPanel();
        buildingBikeTextPanel.setLayout(new BoxLayout(buildingBikeTextPanel, BoxLayout.Y_AXIS));
        buildingBikeTextPanel.setBackground(Color.DARK_GRAY);

        JLabel welcomeLabel = new JLabel("WELCOME TO BUILD-A-BIKE LTD.");
        JLabel buildCustomerBikeLabel = new JLabel("Customise Your Bicycle!");
        JLabel buildingCheckoutInformation = new JLabel("Click 'CHECKOUT' once your bicycle is complete");

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        welcomeLabel.setAlignmentX(CENTER_ALIGNMENT);
        welcomeLabel.setForeground(Color.lightGray);

        buildCustomerBikeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        buildCustomerBikeLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buildCustomerBikeLabel.setAlignmentX(CENTER_ALIGNMENT);
        buildCustomerBikeLabel.setForeground(Color.lightGray);

        buildingCheckoutInformation.setFont(new Font("Arial", Font.BOLD, 15));
        buildingCheckoutInformation.setBorder(new EmptyBorder(10, 10, 10, 10));
        buildingCheckoutInformation.setAlignmentX(CENTER_ALIGNMENT);
        buildingCheckoutInformation.setForeground(Color.lightGray);

        buildingBikeTextPanel.add(welcomeLabel);
        buildingBikeTextPanel.add(buildCustomerBikeLabel);
        buildingBikeTextPanel.add(buildingCheckoutInformation);
        return buildingBikeTextPanel;
    }

    /**
     * Gets the bicycle options that the customer has chosen.
     * @return a bicycle that has been created by the customer.
     */
    public BicycleOrder getBicycleFromFields() {
        // Get the string names of the options chosen by the user.
        Wheel wheelSelected = (Wheel)wheelOptionChosen.getSelectedItem();
        FrameSet frameSetSelected = (FrameSet)framesetOptionChosen.getSelectedItem();
        HandleBar handleBarSelected = (HandleBar)handlebarOptionChosen.getSelectedItem();

        // Construct the bike and pass it to the basket component
        return new BicycleOrder(bicycleNameField.getText(), frameSetSelected,wheelSelected,handleBarSelected);
    }
}
