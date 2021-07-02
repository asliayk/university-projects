import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;

public class MySquare extends JComponent {

	private int leftX;  // x coordinate of the left edge of the square
	private int rightX; // x coordinate of the right edge of the square
	private int topY;   // y coordinate of the top edge of the square
	private int bottomY; // y coordinate of the bottom edge of the square
	private int length = 50; // length of each edge of the square
	private Graphics graphic; // graphics object to be used for painting
	private Color color; // color of the shape

	// constructor
	public MySquare(int x, int y, Graphics g, Color _color) {
		leftX = x - length / 2;
		rightX = x + length / 2;
		topY = y - length / 2;
		bottomY = y + length / 2;
		graphic = g;
		color = _color;
		paintComponent(graphic);
	}

	// overridden paintComponent function to paint the shape
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		graphic.setColor(color);
		graphic.fillRect(leftX, topY, length, length);
	}

	// overridden contains function to check whether the clicked point is in the shape
	@Override
	public boolean contains(Point p) {
		super.contains(p);
		if (leftX <= p.getX() && p.getX() <= rightX && topY <= p.getY() && p.getY() <= bottomY) {
			return true;
		} else {
			return false;
		}

	}

	// overridden setLocation function to set new location for moved shape
	@Override
	public void setLocation(Point p) {
		super.setLocation(p);
		delete();
		leftX = (int) (p.getX() - length / 2);
		rightX = (int) (p.getX() + length / 2);
		topY = (int) (p.getY() - length / 2);
		bottomY = (int) (p.getY() + length / 2);
		paintComponent(graphic);
	}

	// delete function to delete shape
	public void delete() {
		graphic.setColor(Color.WHITE);
		graphic.fillRect(leftX, topY, length, length);
	}

	// enlarge function to enlarge the shape
	public void enlarge() {
		delete();
		leftX -= 5;
		rightX += 5;
		topY -= 5;
		bottomY += 5;
		length += 10;
		paintComponent(graphic);
	}

	// shrink function to shrink the shape
	public void shrink() {
		delete();
		leftX += 5;
		rightX -= 5;
		topY += 5;
		bottomY -= 5;
		length -= 10;
		paintComponent(graphic);
	}

}
