import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditProductUI {

    public JFrame view;

    public JButton btnAdd = new JButton("Confirm Changes");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtProductID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtPrice = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);
    ProductModel outProd;


    public EditProductUI(ProductModel lProd)   {
        this.view = new JFrame();
        outProd = lProd;

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Edit Product");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("Id: " + String.valueOf(lProd.mProductID)));
        //line1.add(txtProductID);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Name: " + lProd.mName));
        line2.add(txtName);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Price: " +String.valueOf( lProd.mPrice)));
        line3.add(txtPrice);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Quantity: " + String.valueOf(lProd.mQuantity)));
        line4.add(txtQuantity);
        view.getContentPane().add(line4);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnAdd);
        panelButtons.add(btnCancel);
        view.getContentPane().add(panelButtons);

        btnAdd.addActionListener(new AddButtonListerner());

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                view.dispose();
            }
        });

    }

    public void run() {
        view.setVisible(true);
    }

    class AddButtonListerner implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            String name = txtName.getText();
            if (name.length() > 0) {
                outProd.mName = name;

            }


            String price = txtPrice.getText();
            if (price.length() > 0) {
                try {
                    outProd.mPrice = Double.parseDouble(price);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Price is invalid!");
                    return;
                }
            }

            String quant = txtQuantity.getText();
            if(quant.length() > 0) {
                try {
                   outProd.mQuantity = Double.parseDouble(quant);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Quantity is invalid!");
                    return;
                }
            }

            switch (StoreManager.getInstance().getDataAdapter().saveProduct(outProd)) {
                case SQLiteDataAdapter.PRODUCT_DUPLICATE_ERROR:
                    JOptionPane.showMessageDialog(null, "Product NOT added successfully! Duplicate product ID!");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Product added successfully!" + outProd);
            }
        }
    }

}