import java.awt.EventQueue;
import java.awt.Point;
import javax.swing.JFrame;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import javax.swing.border.LineBorder;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.JLabel;

public class Editor implements ActionListener {

	private JFrame frame; // frame of the application
	private Color color; // selected color for drawing

	
	// Launching the application.
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Editor window = new Editor();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	// Creating the application
	public Editor() {
		initialize();
	}

	
	// Initializing the contents of the frame
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 975, 597);
		frame.setTitle("Drawing Editor");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		color = Color.RED;

		JPanel panel = new JPanel();
		panel.setOpaque(true);
		panel.setLayout(null);
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel.setBackground(Color.WHITE);
		panel.setBounds(83, 59, 868, 490);
		panel.setVisible(true);
		frame.getContentPane().add(panel);

		// adding action buttons
		JRadioButton rdbtnRectangle = new JRadioButton("Add Rectangle");
		rdbtnRectangle.setBounds(258, 29, 111, 23);
		frame.getContentPane().add(rdbtnRectangle);

		JRadioButton rdbtnCircle = new JRadioButton("Add Circle");
		rdbtnCircle.setBounds(83, 29, 87, 23);
		frame.getContentPane().add(rdbtnCircle);

		JRadioButton rdbtnLine = new JRadioButton("Add Line");
		rdbtnLine.setBounds(172, 29, 84, 23);
		frame.getContentPane().add(rdbtnLine);

		JRadioButton rdbtnMoveShape = new JRadioButton("Move Shape");
		rdbtnMoveShape.setBounds(597, 29, 97, 23);
		frame.getContentPane().add(rdbtnMoveShape);

		JRadioButton rdbtnDeleteShape = new JRadioButton("Delete Shape");
		rdbtnDeleteShape.setBounds(483, 29, 111, 23);
		frame.getContentPane().add(rdbtnDeleteShape);

		JRadioButton rdbtnSquare = new JRadioButton("Add Square");
		rdbtnSquare.setBounds(371, 29, 102, 23);
		frame.getContentPane().add(rdbtnSquare);

		JRadioButton rdbtnEnlarge = new JRadioButton("Enlarge");
		rdbtnEnlarge.setBounds(707, 29, 75, 23);
		frame.getContentPane().add(rdbtnEnlarge);

		JRadioButton rdbtnShrink = new JRadioButton("Shrink");
		rdbtnShrink.setBounds(784, 29, 75, 23);
		frame.getContentPane().add(rdbtnShrink);

		JRadioButton rdbtnRotate = new JRadioButton("Rotate");
		rdbtnRotate.setBounds(861, 29, 75, 23);
		frame.getContentPane().add(rdbtnRotate);

		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(rdbtnRectangle);
		bgroup.add(rdbtnCircle);
		bgroup.add(rdbtnLine);
		bgroup.add(rdbtnMoveShape);
		bgroup.add(rdbtnDeleteShape);
		bgroup.add(rdbtnSquare);
		bgroup.add(rdbtnEnlarge);
		bgroup.add(rdbtnShrink);
		bgroup.add(rdbtnRotate);

		// adding color buttons
		ButtonGroup bgroup2 = new ButtonGroup();
		JToggleButton redbtn = new JToggleButton();
		redbtn.setBounds(10, 69, 50, 20);
		redbtn.setBackground(Color.RED);
		redbtn.setName("red");
		JToggleButton grnbtn = new JToggleButton();
		grnbtn.setBounds(10, 100, 50, 20);
		grnbtn.setBackground(Color.GREEN);
		grnbtn.setName("green");
		JToggleButton ylwbtn = new JToggleButton();
		ylwbtn.setBounds(10, 131, 50, 20);
		ylwbtn.setBackground(Color.YELLOW);
		ylwbtn.setName("yellow");
		JToggleButton bluebtn = new JToggleButton();
		bluebtn.setBounds(10, 162, 50, 20);
		bluebtn.setBackground(Color.BLUE);
		bluebtn.setName("blue");
		JToggleButton blackbtn = new JToggleButton();
		blackbtn.setBounds(10, 192, 50, 20);
		blackbtn.setBackground(Color.BLACK);
		blackbtn.setName("black");
		JToggleButton ornbtn = new JToggleButton();
		ornbtn.setName("orange");
		ornbtn.setBackground(Color.ORANGE);
		ornbtn.setBounds(10, 223, 50, 20);
		JToggleButton pkbtn = new JToggleButton();
		pkbtn.setName("pink");
		pkbtn.setBackground(Color.PINK);
		pkbtn.setBounds(10, 254, 50, 20);
		JToggleButton graybtn = new JToggleButton();
		graybtn.setName("gray");
		graybtn.setBackground(Color.GRAY);
		graybtn.setBounds(10, 287, 50, 20);
		frame.getContentPane().add(redbtn);
		frame.getContentPane().add(grnbtn);
		frame.getContentPane().add(ylwbtn);
		frame.getContentPane().add(bluebtn);
		frame.getContentPane().add(graybtn);
		frame.getContentPane().add(blackbtn);
		frame.getContentPane().add(ornbtn);
		frame.getContentPane().add(pkbtn);
		bgroup2.add(redbtn);
		bgroup2.add(grnbtn);
		bgroup2.add(ylwbtn);
		bgroup2.add(bluebtn);
		bgroup2.add(graybtn);
		bgroup2.add(blackbtn);
		bgroup2.add(ornbtn);
		bgroup2.add(pkbtn);

		JLabel text = new JLabel("Welcome to Drawing Editor!");
		text.setBounds(21, 11, 235, 20);
		frame.getContentPane().add(text);

		// adding action listeners
		redbtn.addActionListener(this);
		bluebtn.addActionListener(this);
		graybtn.addActionListener(this);
		ylwbtn.addActionListener(this);
		grnbtn.addActionListener(this);
		blackbtn.addActionListener(this);
		ornbtn.addActionListener(this);
		pkbtn.addActionListener(this);

		// text message of add rectangle button
		rdbtnRectangle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("Click the drawing area to add rectangle");

			}
		});

		// text message of add circle button
		rdbtnCircle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("Click the drawing area to add circle");

			}
		});

		// text message of add line button
		rdbtnLine.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("Click the drawing area to add line");

			}
		});

		// text message of add square button
		rdbtnSquare.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("Click the drawing area to add square");

			}
		});

		// text message of move shape button
		rdbtnMoveShape.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("Click on the shape and drag it to move");

			}
		});

		// text message of delete shape button
		rdbtnDeleteShape.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("Click on the shape you want to delete");

			}
		});

		// text message of enlarge button
		rdbtnEnlarge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("Click on the shape you want to enlarge");

			}
		});

		// text message of shrink button
		rdbtnShrink.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("Click on the shape you want to shrink");

			}
		});

		// text message of rotate button
		rdbtnRotate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				text.setText("Click on the shape you want to rotate");

			}
		});

		// mouse listener for the panel
		panel.addMouseListener(new MouseAdapter() {
			private JComponent moved;
			private boolean isMoved = false;

			@Override
			public void mousePressed(MouseEvent e) {

				// adding circle to the panel
				if (rdbtnCircle.isSelected()) {
					MyCircle circle = new MyCircle(e.getX(), e.getY(), panel.getGraphics(), color);
					panel.add(circle);
			    // adding line to the panel		
				} else if (rdbtnLine.isSelected()) {
					MyLine line = new MyLine(e.getX(), e.getY(), panel.getGraphics(), color);
					panel.add(line);
				// adding rectangle to the panel	
				} else if (rdbtnRectangle.isSelected()) {
					MyRectangle rect = new MyRectangle(e.getX(), e.getY(), panel.getGraphics(), color);
					panel.add(rect);
				// adding square to the panel	
				} else if (rdbtnSquare.isSelected()) {
					MySquare sqr = new MySquare(e.getX(), e.getY(), panel.getGraphics(), color);
					panel.add(sqr);
				// finding which shape to be moved	
				} else if (rdbtnMoveShape.isSelected()) {
					panel.addMouseMotionListener(new MouseMotionAdapter() {
						public void mouseDragged(MouseEvent e) {
							for (Component c : panel.getComponents()) {
								if (c.contains(e.getPoint())) {
									moved = (JComponent) c;
									isMoved = true;
									break;
								}
							}
						}

					});
                // deleting the shape from panel 
				} else if (rdbtnDeleteShape.isSelected()) {
					for (Component c : panel.getComponents()) {
						if (c.contains(e.getPoint())) {
							panel.remove(c);
							if (c instanceof MyCircle) {
								((MyCircle) c).delete();
							} else if (c instanceof MyRectangle) {
								((MyRectangle) c).delete();
							} else if (c instanceof MySquare) {
								((MySquare) c).delete();
							} else if (c instanceof MyLine) {
								((MyLine) c).delete();
							}
							break;
						}
					}
				// finding which shape to enlarge	
				} else if (rdbtnEnlarge.isSelected()) {
					for (Component c : panel.getComponents()) {
						if (c.contains(e.getPoint())) {
							if (c instanceof MyCircle) {
								((MyCircle) c).enlarge();
							} else if (c instanceof MyRectangle) {
								((MyRectangle) c).enlarge();
							} else if (c instanceof MySquare) {
								((MySquare) c).enlarge();
							} else if (c instanceof MyLine) {
								((MyLine) c).enlarge();
							}
						}
					}
				// finding which shape to shrink	
				} else if (rdbtnShrink.isSelected()) {
					for (Component c : panel.getComponents()) {
						if (c.contains(e.getPoint())) {
							if (c instanceof MyCircle) {
								((MyCircle) c).shrink();
							} else if (c instanceof MyRectangle) {
								((MyRectangle) c).shrink();
							} else if (c instanceof MySquare) {
								((MySquare) c).shrink();
							} else if (c instanceof MyLine) {
								((MyLine) c).shrink();
							}
						}
					}
				// finding which shape to rotate	
				} else if (rdbtnRotate.isSelected()) {
					for (Component c : panel.getComponents()) {
						if (c.contains(e.getPoint())) {
							if (c instanceof MyRectangle) {
								((MyRectangle) c).rotate();
							} else if (c instanceof MyLine) {
								((MyLine) c).rotate();
							}
						}
					}
				}

			}

			// setting location of the moved shape
			@Override
			public void mouseReleased(MouseEvent e) {
				if (moved != null && isMoved) {
					Point moveP = new Point(e.getPoint());
					moved.setLocation(moveP);
					isMoved = false;
				}
			}
		});

	}

	// setting the color for drawing 
	@Override
	public void actionPerformed(ActionEvent e) {
		String btnName = ((Component) e.getSource()).getName();
		if (btnName.equals("red")) {
			color = Color.RED;
		} else if (btnName.equals("green")) {
			color = Color.GREEN;
		} else if (btnName.equals("yellow")) {
			color = Color.YELLOW;
		} else if (btnName.equals("blue")) {
			color = Color.BLUE;
		} else if (btnName.equals("gray")) {
			color = Color.GRAY;
		} else if (btnName.equals("black")) {
			color = Color.BLACK;
		} else if (btnName.equals("pink")) {
			color = Color.PINK;
		} else if (btnName.equals("orange")) {
			color = Color.ORANGE;
		}

	}
}
