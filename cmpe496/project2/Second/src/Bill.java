import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Dimension;
import javax.swing.border.EmptyBorder;

public class Bill {

	private JFrame frame; // frame of the additional dialog box
	String name;  // name information of purchaser
	String phone; // phone information of purchaser
	String pCode; // postal code information of purchaser
	String city; // city information of purchaser
	String province; // province information of purchaser
	String address; // address information of purchaser
	String date; // date information of purchaser
	String cardNo; // card number information of purchaser
	String vId; // validation id information of purchaser
	String balance; // balance of the user
	ArrayList<Product> products; // list of products of user	

	// constructor for Bill object
	public Bill(String name, String phone, String pCode, String city, String province, String address,
			String date, String cardNo, String vId, ArrayList<Product> proList, String balance) {
		this.name = name;
		this.phone = phone;
		this.pCode = pCode;
		this.city = city;
		this.province = province;
		this.address = address;
		this.date = date;
		this.cardNo = cardNo;
		this.vId = vId;
		this.balance = balance;
		products = proList;
		initialize();
	}

	// Initializing the additional dialog box
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		frame.setBounds(600, 100, 499, 428);
		frame.setTitle("Bill");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		// Text area for product list
		JTextArea billText = new JTextArea("",20,20);
		billText.setBounds(293, 21, 172, 325);
		billText.setEditable(false);
		billText.setOpaque(false);
		
		// "Bill" panel for showing purchaser information
		JPanel firstPanel = new JPanel();
		firstPanel.setLayout(null);
		firstPanel.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Bill", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		firstPanel.setBounds(10, 11, 475, 369);
		frame.getContentPane().add(firstPanel);
		
		// Creating the text for product list
		String text = "              Products " + "\n";
		int amount = 0;
		for(int i=0; i<products.size(); i++) {
			amount += products.get(i).total;
			text += "  - No: " + Integer.toString(products.get(i).number) + " x" + Integer.toString(products.get(i).quantity) +
					" Cost: " + Integer.toString(products.get(i).cost) + " Total: " + Integer.toString(products.get(i).total) + "\n";	
		}
		text += "\n\n" + "Balance Left: " + Integer.toString(Integer.parseInt(this.balance)-amount);
		billText.setText(text);
		
		// First panel for first row in the "Bill" panel which contains JLabel objects
		JPanel f_firstRow = new JPanel();
		f_firstRow.setBorder(new EmptyBorder(3, 3, 3, 3));
		f_firstRow.setBounds(10, 21, 242, 32);
		firstPanel.add(f_firstRow);
		f_firstRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel name = new JLabel("Name: ", SwingConstants.RIGHT);
		name.setPreferredSize(new Dimension(110, 20));
		f_firstRow.add(name);
		JLabel nameInput = new JLabel("", SwingConstants.LEFT);
		nameInput.setPreferredSize(new Dimension(110, 20));
		nameInput.setText(this.name);
		f_firstRow.add(nameInput);
		
		// Second panel for the second row in the "Bill" panel which contains JLabel objects
		JPanel f_secondRow = new JPanel();
		f_secondRow.setBorder(new EmptyBorder(3, 3, 3, 3));
		f_secondRow.setBounds(10, 58, 242, 32);
		firstPanel.add(f_secondRow);
		f_secondRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel pCode = new JLabel("Postal Code: ", SwingConstants.RIGHT);
		pCode.setPreferredSize(new Dimension(110, 20));
		f_secondRow.add(pCode);
		JLabel pCodeInput = new JLabel("", SwingConstants.LEFT);
		pCodeInput.setPreferredSize(new Dimension(110, 20));
		pCodeInput.setText(this.pCode);
		f_secondRow.add(pCodeInput);
		
		// Third panel for the third row in the "Bill" panel which contains JLabel objects
		JPanel f_thirdRow = new JPanel();
		f_thirdRow.setBounds(10, 101, 242, 32);
		firstPanel.add(f_thirdRow);
		f_thirdRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel delivery = new JLabel("Delivery Address: ", SwingConstants.RIGHT);
		delivery.setPreferredSize(new Dimension(112, 20));
		f_thirdRow.add(delivery);
		JLabel addressInput = new JLabel("", SwingConstants.LEFT);
		addressInput.setPreferredSize(new Dimension(110, 20));
		addressInput.setText(this.address);
		f_thirdRow.add(addressInput);
		
		// Fifth panel for fifth row in the "Bill" panel which contains JLabel objects
		JPanel f_fifthRow = new JPanel();
		f_fifthRow.setBounds(10, 182, 242, 32);
		firstPanel.add(f_fifthRow);
		f_fifthRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel date = new JLabel("Province: ", SwingConstants.RIGHT);
		date.setPreferredSize(new Dimension(112, 20));
		f_fifthRow.add(date);
		JLabel provinceInput = new JLabel("", SwingConstants.LEFT);
		provinceInput.setPreferredSize(new Dimension(110, 20));
		provinceInput.setText(this.province);
		f_fifthRow.add(provinceInput);
		
		// Seventh panel for seventh row in the "Bill" panel which contains JLabel objects
		JPanel f_seventhRow = new JPanel();
		f_seventhRow.setBounds(10, 255, 242, 32);
		firstPanel.add(f_seventhRow);
		f_seventhRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel card = new JLabel("Credit Card No.: ", SwingConstants.RIGHT);
		card.setPreferredSize(new Dimension(112, 20));
		f_seventhRow.add(card);
		JLabel cardNoInput = new JLabel("", SwingConstants.LEFT);
		cardNoInput.setPreferredSize(new Dimension(110, 20));
		cardNoInput.setText(this.cardNo);
		f_seventhRow.add(cardNoInput);
		
		// Sixth panel for sixth row in the "Bill" panel which contains JLabel objects
		JPanel f_sixthRow = new JPanel();
		f_sixthRow.setBounds(10, 221, 242, 32);
		firstPanel.add(f_sixthRow);
		f_sixthRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel delivery_1 = new JLabel("Phone: ", SwingConstants.RIGHT);
		delivery_1.setPreferredSize(new Dimension(112, 20));
		f_sixthRow.add(delivery_1);
		JLabel phoneInput = new JLabel("", SwingConstants.LEFT);
		phoneInput.setPreferredSize(new Dimension(110, 20));
		phoneInput.setText(this.phone);
		f_sixthRow.add(phoneInput);
		
		// Fourth panel for fourth row in the "Bill" panel which contains JLabel objects
		JPanel f_fourthRow = new JPanel();
		f_fourthRow.setBounds(10, 144, 242, 32);
		firstPanel.add(f_fourthRow);
		f_fourthRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblCity = new JLabel("City: ", SwingConstants.RIGHT);
		lblCity.setPreferredSize(new Dimension(112, 20));
		f_fourthRow.add(lblCity);
		JLabel cityInput = new JLabel("", SwingConstants.LEFT);
		cityInput.setPreferredSize(new Dimension(110, 20));
		cityInput.setText(this.city);
		f_fourthRow.add(cityInput);
		
		// Eighth panel for eighth row in the "Bill" panel which contains JLabel objects
		JPanel f_eighthRow = new JPanel();
		f_eighthRow.setBounds(10, 283, 242, 32);
		firstPanel.add(f_eighthRow);
		f_eighthRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel date_2 = new JLabel("Validation Id: ", SwingConstants.RIGHT);
		date_2.setPreferredSize(new Dimension(112, 20));
		f_eighthRow.add(date_2);
		JLabel vIdInput = new JLabel("", SwingConstants.LEFT);
		vIdInput.setPreferredSize(new Dimension(110, 20));
		vIdInput.setText(this.vId);
		f_eighthRow.add(vIdInput);
		
		// Nineth panel for nineth row in the "Bill" panel which contains JLabel objects
		JPanel f_ninethRow = new JPanel();
		f_ninethRow.setBounds(10, 314, 242, 32);
		firstPanel.add(f_ninethRow);
		f_ninethRow.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel date_2_1 = new JLabel("Today's Date: ", SwingConstants.RIGHT);
		date_2_1.setPreferredSize(new Dimension(112, 20));
		f_ninethRow.add(date_2_1);
		JLabel dateInput = new JLabel("", SwingConstants.LEFT);
		dateInput.setPreferredSize(new Dimension(110, 20));
		dateInput.setText(this.date);
		f_ninethRow.add(dateInput);
		firstPanel.add(billText);
		
		
		
		
	}

	// getter method for the frame of the additional dialog box
	public JFrame getFrame() {
		return frame;
	}
}
