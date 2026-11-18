package productapp;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class ProductApp extends JFrame implements ActionListener {

    private static final String FILE_NAME = "data.txt";
    private static final String STARS = "*****************************************";

    private JTextField txtCustomerName;
    private JComboBox<String> cmbProduct;
    private JTextField txtPrice;
    private JButton btnProcess;
    private JMenuItem menuExit;
    private JMenuItem menuProcess;
    private JMenuItem menuClear;

    public ProductApp() {
        setTitle("Product App");
        setSize(480, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        createMenu();
        createForm();
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuExit = new JMenuItem("Exit");
        menuExit.addActionListener(this);
        fileMenu.add(menuExit);

        JMenu toolsMenu = new JMenu("Tools");
        menuProcess = new JMenuItem("Process");
        menuProcess.addActionListener(this);
        menuClear = new JMenuItem("Clear");
        menuClear.addActionListener(this);
        toolsMenu.add(menuProcess);
        toolsMenu.add(menuClear);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        setJMenuBar(menuBar);
    }

    private void createForm() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 20));
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        txtCustomerName = new JTextField();

        String[] products = {"Cozy Nights Monarch", "Restonic Barbados", "Sealy Phuket"};
        cmbProduct = new JComboBox<String>(products);

        txtPrice = new JTextField();

        btnProcess = new JButton("PROCESS");
        btnProcess.addActionListener(this);

        panel.add(new JLabel("Customer Name:"));
        panel.add(txtCustomerName);
        panel.add(new JLabel("Select Product:"));
        panel.add(cmbProduct);
        panel.add(new JLabel("Enter Price:"));
        panel.add(txtPrice);
        panel.add(new JLabel(""));
        panel.add(btnProcess);

        add(panel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == menuExit) {
            System.exit(0);
        } else if (source == btnProcess || source == menuProcess) {
            processSale();
        } else if (source == menuClear) {
            clearForm();
        }
    }

    private void processSale() {
        String customerName = txtCustomerName.getText().trim();
        String product = (String) cmbProduct.getSelectedItem();
        String priceText = txtPrice.getText().trim();

        if (customerName.isEmpty()) {
            showError("Please enter the customer name.");
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            showError("Please enter a valid price, for example 3500.");
            return;
        }

        if (price <= 0) {
            showError("The price must be greater than zero.");
            return;
        }

        ProductCalculations calculations = new ProductCalculations(price);
        double vat = calculations.GetVat();
        double commission = calculations.GetCommission();
        double total = calculations.GetTotal();

        saveToFile(customerName, product, vat, commission, total);

        String message = "Customer Name: " + customerName + "\n"
                + "Product: " + product + "\n"
                + "VAT: R " + vat + "\n"
                + "COMMISSION: R " + commission + "\n"
                + "TOTAL: R " + total;

        JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveToFile(String customerName, String product, double vat, double commission, double total) {
        File file = new File(FILE_NAME);
        boolean isNewFile = !file.exists();

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, true))) {
            if (isNewFile) {
                writer.println("DATA LOG");
                writer.println(STARS);
            }
            writer.println("Customer Name: " + customerName);
            writer.println("Product: " + product);
            writer.println("VAT: R " + vat);
            writer.println("COMMISSION: R " + commission);
            writer.println("TOTAL: R " + total);
            writer.println(STARS);
        } catch (IOException ex) {
            showError("The data could not be saved to " + FILE_NAME + ".");
        }
    }

    private void clearForm() {
        txtCustomerName.setText("");
        txtPrice.setText("");
        cmbProduct.setSelectedIndex(0);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ProductApp().setVisible(true);
            }
        });
    }
}
