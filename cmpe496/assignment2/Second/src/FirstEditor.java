import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import java.awt.Font;

public class FirstEditor implements ActionListener {

	private JFrame frame; // frame of the bigger dialog box

	// Launching the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					// initializing the first dialog box
					FirstEditor window = new FirstEditor();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// Creating the first dialog box
	public FirstEditor() {
		initialize();
	}

	// Initializing the contents of the first dialog box
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setBounds(50, 100, 660, 471);
		frame.setTitle("Cheap Shop Catalog Store");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		// Initializing "Purchaser" panel
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(null);
		firstPanel.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Purchaser", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		firstPanel.setBounds(10, 35, 618, 235);
		frame.getContentPane().add(firstPanel);

		// First panel for first row in the "Purchaser" panel which contains JTextField
		// and JLabel objects
		JPanel f_firstRow = new JPanel();
		f_firstRow.setBorder(new EmptyBorder(3, 3, 3, 3));
		f_firstRow.setBounds(10, 21, 597, 32);
		f_firstRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JTextField nameText = new JTextField();
		nameText.setColumns(20);
		JTextField phoneText = new JTextField();
		phoneText.setColumns(10);
		JLabel name = new JLabel("Name: ", SwingConstants.RIGHT);
		name.setPreferredSize(new Dimension(109, 20));
		f_firstRow.add(name);
		f_firstRow.add(nameText);
		JLabel phone = new JLabel("Phone: ", SwingConstants.RIGHT);
		phone.setPreferredSize(new Dimension(70, 20));
		f_firstRow.add(phone);
		f_firstRow.add(phoneText);
		firstPanel.add(f_firstRow);
		JTextField pcodeText = new JTextField();
		pcodeText.setColumns(10);
		JTextField provinceText = new JTextField();
		provinceText.setColumns(10);
		JTextField cityText = new JTextField();
		cityText.setColumns(10);

		// Second panel for second row in the "Purchaser" panel which contains
		// JTextField and JLabel objects
		JPanel f_secondRow = new JPanel();
		f_secondRow.setBorder(new EmptyBorder(3, 3, 3, 3));
		f_secondRow.setBounds(10, 61, 597, 32);
		f_secondRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel pCode = new JLabel("Postal Code: ", SwingConstants.RIGHT);
		pCode.setPreferredSize(new Dimension(110, 20));
		f_secondRow.add(pCode);
		JLabel province = new JLabel("Province: ", SwingConstants.RIGHT);
		province.setPreferredSize(new Dimension(70, 20));
		f_secondRow.add(province);
		JLabel city = new JLabel("City: ", SwingConstants.RIGHT);
		city.setPreferredSize(new Dimension(70, 20));
		f_secondRow.add(pcodeText);
		f_secondRow.add(province);
		f_secondRow.add(provinceText);
		f_secondRow.add(city);
		f_secondRow.add(cityText);
		firstPanel.add(f_secondRow);

		// Third panel for third row in the "Purchaser" panel which contains JTextField
		// and JLabel objects
		JPanel f_thirdRow = new JPanel();
		f_thirdRow.setBounds(10, 101, 597, 32);
		f_thirdRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel delivery = new JLabel("Delivery Address: ", SwingConstants.RIGHT);
		delivery.setPreferredSize(new Dimension(112, 20));
		JTextField deliveryText = new JTextField();
		deliveryText.setColumns(50);
		f_thirdRow.add(delivery);
		f_thirdRow.add(deliveryText);
		firstPanel.add(f_thirdRow);

		// Fourth panel for fourth row in the "Purchaser" panel which contains
		// JTextField and JLabel objects
		JPanel f_fourthRow = new JPanel();
		f_fourthRow.setBounds(10, 141, 597, 32);
		f_fourthRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel date = new JLabel("Today's Date: ", SwingConstants.RIGHT);
		date.setPreferredSize(new Dimension(112, 20));
		JTextField dateText = new JTextField();
		dateText.setColumns(10);
		f_fourthRow.add(date);
		f_fourthRow.add(dateText);
		firstPanel.add(f_fourthRow);

		// Fifth panel for fifth row in the "Purchaser" panel which contains JTextField
		// and JLabel objects
		JPanel f_fifthRow = new JPanel();
		f_fifthRow.setBounds(10, 181, 597, 32);
		f_fifthRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel card = new JLabel("Credit Card No.: ", SwingConstants.RIGHT);
		card.setPreferredSize(new Dimension(112, 20));
		JTextField cardText = new JTextField();
		cardText.setColumns(10);
		JLabel valid = new JLabel("for depth use: validation id: ", SwingConstants.RIGHT);
		valid.setPreferredSize(new Dimension(250, 20));
		JTextField validText = new JTextField();
		validText.setColumns(10);
		f_fifthRow.add(card);
		f_fifthRow.add(cardText);
		f_fifthRow.add(valid);
		f_fifthRow.add(validText);
		firstPanel.add(f_fifthRow);

		// Initialing "Catalog Item" panel
		JPanel secondPanel = new JPanel();
		secondPanel.setLayout(null);
		secondPanel.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Catalog Item", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		secondPanel.setBounds(10, 280, 618, 65);
		frame.getContentPane().add(secondPanel);

		// First panel for the first row in the "Catalog Item" panel which contains
		// JTextField, JSpinner and JLabel objects
		JPanel s_row = new JPanel();
		s_row.setBounds(10, 22, 598, 26);
		s_row.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel number = new JLabel("Number: ", SwingConstants.RIGHT);
		number.setPreferredSize(new Dimension(70, 20));
		JTextField numberText = new JTextField();
		numberText.setColumns(10);
		s_row.add(number);
		s_row.add(numberText);
		JLabel quantity = new JLabel("Quantity: ", SwingConstants.RIGHT);
		quantity.setPreferredSize(new Dimension(70, 20));
		SpinnerModel value = new SpinnerNumberModel(0, 0, 10, 1);
		JSpinner spinner = new JSpinner(value);
		s_row.add(quantity);
		s_row.add(spinner);
		JLabel total = new JLabel("Total: ", SwingConstants.RIGHT);
		total.setPreferredSize(new Dimension(70, 20));
		JTextField totalText = new JTextField();
		totalText.setColumns(5);
		JLabel costI = new JLabel("Cost/item: ", SwingConstants.RIGHT);
		costI.setPreferredSize(new Dimension(70, 20));
		s_row.add(costI);
		JTextField costIText = new JTextField();
		s_row.add(costIText);
		costIText.setColumns(5);
		s_row.add(total);
		s_row.add(totalText);
		secondPanel.add(s_row);

		// "Next Catalog Item" button
		JButton ctlg_btn = new JButton("Next Catalog Item (PF5)");
		ctlg_btn.setBounds(453, 356, 175, 23);
		ctlg_btn.setBackground(Color.LIGHT_GRAY);
		ctlg_btn.setOpaque(true);
		frame.getContentPane().add(ctlg_btn);

		// "Trigger Invoice" button
		JButton trigger_btn = new JButton("Trigger Invoice (PF8)");
		trigger_btn.setBounds(453, 390, 175, 23);
		trigger_btn.setBackground(Color.LIGHT_GRAY);
		trigger_btn.setOpaque(true);
		frame.getContentPane().add(trigger_btn);

		// Third panel for Balance Owing field, which contains JLabel and JTextField objects
		JPanel thirdPanel = new JPanel();
		thirdPanel.setBounds(10, 374, 357, 39);
		thirdPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		frame.getContentPane().add(thirdPanel);
		JLabel balance = new JLabel("Balance Owing: ", SwingConstants.RIGHT);
		balance.setPreferredSize(new Dimension(110, 20));
		JTextField balanceText = new JTextField();
		balanceText.setColumns(10);
		thirdPanel.add(balance);
		thirdPanel.add(balanceText);

		// Action listener for "Next Catalog Item" button, which adds product to the
		// product list
		ctlg_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Product product = null;
				if (numberText.getText() != "") {
					product = new Product(Integer.parseInt(numberText.getText()), Integer.parseInt(costIText.getText()),
							(Integer) spinner.getValue(), Integer.parseInt(totalText.getText()));
				}
				numberText.setText("");
				costIText.setText("");
				totalText.setText("");
				spinner.setValue(0);
				SecondEditor window2 = new SecondEditor(nameText.getText(), phoneText.getText(), pcodeText.getText(),
						cityText.getText(), provinceText.getText(), deliveryText.getText(), dateText.getText(),
						cardText.getText(), validText.getText(), product,balanceText.getText());
				window2.getFrame().setVisible(true);

			}

		});

		// Action listener for "Triggering Invoice" button, which opens additional
		// dialog box for the bill
		trigger_btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Product> products = new ArrayList<Product>();
				Product product = new Product(Integer.parseInt(numberText.getText()),
						Integer.parseInt(costIText.getText()), (Integer) spinner.getValue(),
						Integer.parseInt(totalText.getText()));
				products.add(product);
				Bill bill = new Bill(nameText.getText(), phoneText.getText(), pcodeText.getText(), cityText.getText(),
						provinceText.getText(), deliveryText.getText(), dateText.getText(), cardText.getText(),
						validText.getText(), products, balanceText.getText());
				bill.getFrame().setVisible(true);

			}

		});

		JLabel name_1 = new JLabel("Donderly software, Screen A1.1", SwingConstants.RIGHT);
		name_1.setFont(new Font("Tahoma", Font.ITALIC, 11));
		name_1.setPreferredSize(new Dimension(109, 20));
		name_1.setBounds(453, 4, 167, 20);
		frame.getContentPane().add(name_1);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
