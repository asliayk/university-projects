import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class SecondEditor {

	private JFrame frame; // frame of the application
	String name;  // name information of purchaser
	String phone; // phone information of purchaser
	String pCode; // postal code information of purchaser
	String city; // city information of purchaser
	String province; // province information of purchaser
	String address; // address information of purchaser
	String date; // date information of purchaser
	String cardNo; // card number information of purchaser
	String vId; // validation id information of purchaser
	String balanceA; // balance of the user
	ArrayList<Product> products = new ArrayList<Product>(); // product list for user
	

	// Creating the second dialog box
	public SecondEditor(String name, String phone, String pCode, String city, String province, String address,
			String date, String cardNo, String vId, Product product, String balance) {
		this.name = name;
		this.phone = phone;
		this.pCode = pCode;
		this.city = city;
		this.province = province;
		this.address = address;
		this.date = date;
		this.cardNo = cardNo;
		this.vId = vId;
		balanceA= balance;
		initialize();
		if(product!=null)
		 products.add(product);
	}

	// Initializing the contents of the second dialog box
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setBounds(600, 100, 659, 210);
		frame.setTitle("Cheap Shop Catalog Store");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// Initializing "Catalog Item" panel
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(null);
		firstPanel.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Catalog Item", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		firstPanel.setBounds(10, 34, 618, 64);
		frame.getContentPane().add(firstPanel);
		
		// First panel for first row in the "Catalog Item" panel which contains JTextField, JSpinner and JLabel objects
		JPanel s_row = new JPanel();
		s_row.setBounds(10, 22, 598, 26);
		firstPanel.add(s_row);
		s_row.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel number = new JLabel("Number: ", SwingConstants.RIGHT);
		number.setPreferredSize(new Dimension(70, 20));
		s_row.add(number);
		JTextField numberText = new JTextField();
		numberText.setColumns(10);
		s_row.add(numberText);
		JLabel quantity = new JLabel("Quantity: ", SwingConstants.RIGHT);
		quantity.setPreferredSize(new Dimension(70, 20));
		s_row.add(quantity);
		SpinnerModel value = new SpinnerNumberModel(0, 0, 10, 1);
		JSpinner spinner = new JSpinner(value);
		s_row.add(spinner);
		JLabel costI = new JLabel("Cost/item: ", SwingConstants.RIGHT);
		costI.setPreferredSize(new Dimension(70, 20));
		s_row.add(costI);
		JTextField costText = new JTextField();
		costText.setColumns(5);
		s_row.add(costText);
		JLabel total = new JLabel("Total: ", SwingConstants.RIGHT);
		total.setPreferredSize(new Dimension(70, 20));
		s_row.add(total);
		JTextField totalText = new JTextField();
		totalText.setColumns(5);
		s_row.add(totalText);

		// Second panel for the Balance Owing field, which contains JTextField and JLabel objects
		JPanel secondPanel = new JPanel();
		secondPanel.setBounds(10, 109, 357, 39);
		frame.getContentPane().add(secondPanel);
		secondPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel balance = new JLabel("Balance Owing: ", SwingConstants.RIGHT);
		balance.setPreferredSize(new Dimension(110, 20));
		secondPanel.add(balance);
		JTextField balanceText = new JTextField();
		balanceText.setColumns(10);
		secondPanel.add(balanceText);

		// "Next Catalog Item" button
		JButton ctlg_btn = new JButton("Next Catalog Item (PF5)");
		ctlg_btn.setOpaque(true);
		ctlg_btn.setBackground(Color.LIGHT_GRAY);
		ctlg_btn.setBounds(453, 109, 175, 23);
		frame.getContentPane().add(ctlg_btn);
		
		// "Trigger Invoice" button
		JButton trg_btn = new JButton("Trigger Invoice (PF8)");
		trg_btn.setOpaque(true);
		trg_btn.setBackground(Color.LIGHT_GRAY);
		trg_btn.setBounds(453, 139, 175, 23);
		frame.getContentPane().add(trg_btn);
		
		JLabel name_1 = new JLabel("Donderly software, Screen A1.2", SwingConstants.RIGHT);
		name_1.setFont(new Font("Tahoma", Font.ITALIC, 11));
		name_1.setPreferredSize(new Dimension(109, 20));
		name_1.setBounds(453, 3, 167, 20);
		frame.getContentPane().add(name_1);
		
		// Action listener for "Next Catalog Item" button, which adds product to the product list
		ctlg_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Product product = new Product(Integer.parseInt(numberText.getText()),
						Integer.parseInt(costText.getText()), (Integer) spinner.getValue(),
						Integer.parseInt(totalText.getText()));
				products.add(product);
				numberText.setText("");
				costText.setText("");
				totalText.setText("");
				spinner.setValue(0);

			}
		});
		
		// Action listener for "Triggering Invoice" button, which opens additional dialog box for the bill
		trg_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Product product = new Product(Integer.parseInt(numberText.getText()),
						Integer.parseInt(costText.getText()), (Integer) spinner.getValue(),
						Integer.parseInt(totalText.getText()));
				products.add(product);
				Bill bill = new Bill(name,phone, pCode, city,province,address, date,cardNo,vId,products,balanceA);
				products = new ArrayList<Product>();
				bill.getFrame().setVisible(true);

			}

		});

		
	}

	// getter method for the frame of the second dialog box
	public JFrame getFrame() {
		return frame;
	}
}
