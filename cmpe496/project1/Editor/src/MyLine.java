import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;

public class MyLine extends JComponent {

	
	private int firstX;  // x coordinate of the start point
	private int secondX; // x coordinate of the end point
	private int firstY;  // y coordinate of the start point
	private int secondY; // y coordinate of the end point
	private int oX; // x coordinate of the clicked point
	private int oY; // y coordinate of the clicked point
	private int length = 90;  // length of line
	private Graphics graphic; // graphics object to be used for painting the shape 
	private Color color; // color of the shape

	// constructor
	public MyLine(int x, int y, Graphics g, Color _color) {
		oX = x;
		oY = y;
		firstX = x - length / 2;
		secondX = x + length / 2;
		firstY = y;
		secondY = y;
		graphic = g;
		color = _color;
		paintComponent(graphic);
	}

	// overridden paintComponent function to paint the shape
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		graphic.setColor(color);
		graphic.drawLine(firstX, firstY, secondX, secondY);
	}

	// overridden contains function to check whether the clicked point is in the shape
	@Override
	public boolean contains(Point p) {
		super.contains((Point) p);
		if (firstX - 2 <= p.getX() && p.getX() <= secondX + 2 && firstY - 2 <= p.getY() && p.getY() <= secondY + 2) {
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
		if (firstY == secondY) {
			firstX = (int) (p.getX() - length / 2);
			secondX = (int) (p.getX() + length / 2);
			firstY = (int) (p.getY());
			secondY = (int) (p.getY());
		} else {
			firstX = (int) (p.getX());
			secondX = (int) (p.getX());
			firstY = (int) (p.getY() - length / 2);
			secondY = (int) (p.getY() + length / 2);
		}
		paintComponent(graphic);
	}

	// delete function to delete the shape
	public void delete() {
		graphic.setColor(Color.WHITE);
		graphic.drawLine(firstX, firstY, secondX, secondY);
	}

	// enlarge function to enlarge the shape
	public void enlarge() {
		delete();
		if (firstY == secondY) {
			firstX -= 5;
			secondX += 5;
		} else {
			firstY -= 5;
			secondY += 5;
		}
		length += 10;
		paintComponent(graphic);
	}

	// shrink function to shrink the shape
	public void shrink() {
		delete();
		if (firstY == secondY) {
			firstX += 5;
			secondX -= 5;
		} else {
			firstY += 5;
			secondY -= 5;
		}
		length -= 10;
		paintComponent(graphic);
	}

	// rotate function to rotate the shape
	public void rotate() {
		delete();
		if (firstY == secondY) {
			firstX = oX;
			secondX = oX;
			firstY = oY - length / 2;
			secondY = oY + length / 2;
		} else {
			firstX = oX - length / 2;
			secondX = oX + length / 2;
			firstY = oY;
			secondY = oY;
		}

		paintComponent(graphic);
	}
}
