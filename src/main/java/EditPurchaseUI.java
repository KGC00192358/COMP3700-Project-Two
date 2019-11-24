import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditPurchaseUI {

    public JFrame view;

    public JButton btnAdd = new JButton("Confirm Changes");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtProductID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtPrice = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);
    ProductModel outProd;


    public EditPurchaseUI(PurchaseModel in_purch)   {


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