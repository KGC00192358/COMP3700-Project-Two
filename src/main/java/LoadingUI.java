import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

    public class LoadingUI {

        public JFrame view;

        public JButton btnLoad = new JButton("Load");
        public JButton btnCancel = new JButton("Cancel");

        public JTextField txtID = new JTextField(20);


        public LoadingUI(String thing_to_load)   {
            final String option = thing_to_load;
            this.view = new JFrame();

            view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            view.setTitle("Add Product");
            view.setSize(600, 400);
            view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

            JPanel line1 = new JPanel(new FlowLayout());
            line1.add(new JLabel(thing_to_load + " ID "));
            line1.add(txtID);
            view.getContentPane().add(line1);

            JPanel panelButtons = new JPanel(new FlowLayout());
            panelButtons.add(btnLoad);
            panelButtons.add(btnCancel);
            view.getContentPane().add(panelButtons);

            btnLoad.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        ProductModel product = new ProductModel();

                        String id = txtID.getText();

                        if (id.length() == 0) {
                            JOptionPane.showMessageDialog(null, "ProductID cannot be null!");
                            return;
                        }
                        if(option.compareTo("Product") == 0) {
                            if (StoreManager.getInstance().getSQLDataAdapter().getProductOverHttp(id) != null) {

                                EditProductUI ep = new EditProductUI(StoreManager.getInstance().getSQLDataAdapter().getProductOverHttp(id));
                                ep.run();
                                view.dispose();

                            } else {
                                JOptionPane.showMessageDialog(null, "Product NOT Loaded!");
                            }
                        } else if (option.compareTo("Customer") == 0) {
                            if (StoreManager.getInstance().getSQLDataAdapter().getCustomerOverHttp(id) != null) {

                                EditCustomerUI ep = new EditCustomerUI(StoreManager.getInstance().getSQLDataAdapter().getCustomerOverHttp(id));
                                ep.run();
                                view.dispose();

                            } else {
                                JOptionPane.showMessageDialog(null, "Customer NOT Loaded!");
                            }
                        } else {if (StoreManager.getInstance().getSQLDataAdapter().getPurchaseOverHttp(id) != null) {

                           EditPurchaseUI ep = new EditPurchaseUI(StoreManager.getInstance().getSQLDataAdapter().getPurchaseOverHttp(id));
                           ep.run();
                            view.dispose();

                        } else {
                            JOptionPane.showMessageDialog(null, "Purchase NOT Loaded!");
                        }

                        }
                }
            });

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

       /* class AddButtonListerner implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ProductModel product = new ProductModel();

                String id = txtID.getText();

                if (id.length() == 0) {
                    JOptionPane.showMessageDialog(null, "ProductID cannot be null!");
                    return;
                }
                if(thing_to_load)
                if (StoreManager.getInstance().getSQLDataAdapter().getProductOverHttp(id) != null) {

                    EditProductUI ep = new EditProductUI(StoreManager.getInstance().getSQLDataAdapter().getProductOverHttp(id));
                    ep.run();
                    view.dispose();

                } else {
                    JOptionPane.showMessageDialog(null, "Product NOT Loaded!");
                }
            }
        } */

    }
