
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;

public class MyCircle extends JComponent {

	private int xCoord; // calculated x coordinate to draw circle
	private int yCoord; // calculated y coordinate to draw circle
	private int oX; // x coordinate of the clicked point
	private int oY; // y coordinate of the clicked point
	private Graphics graphic; // graphics object to be used for painting the shape 
	private int radius;  // radius of the circle
	private Color color; // color of the shape

	// constructor
	public MyCircle(int x, int y, Graphics g, Color _color) {
		radius = 40;
		oX = x;
		oY = y;
		xCoord = x - radius / 2;
		yCoord = y - radius / 2;
		graphic = g;
		color = _color;
		paintComponent(g);

	}

	// overridden paintComponent function to paint the shape
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(graphic);

		graphic.setColor(color);
		graphic.fillOval(xCoord, yCoord, radius, radius);

	}

	// overridden contains function to check whether the clicked point is in the shape
	@Override
	public boolean contains(Point p) {
		super.contains((Point) p);
		double distance = Math.sqrt(Math.pow((p.getX() - xCoord), 2) + Math.pow((p.getY() - yCoord), 2));
		if (distance <= radius) {
			return true;
		} else {
			return false;
		}

	}

	// overridden setLocation function to set new location for moved shape
	@Override
	public void setLocation(Point p) {
		super.setLocation(p);
		graphic.setColor(Color.WHITE);
		graphic.fillOval(xCoord, yCoord, radius, radius);
		xCoord = (int) (p.getX() - radius / 2);
		yCoord = (int) (p.getY() - radius / 2);
		paintComponent(graphic);
	}

	// delete function to delete the shape
	public void delete() {
		graphic.setColor(Color.WHITE);
		graphic.fillOval(xCoord, yCoord, radius, radius);
	}

	// enlarge function to enlarge the shape
	public void enlarge() {
		delete();
		radius = radius + 10;
		xCoord = oX - radius / 2;
		yCoord = oY - radius / 2;
		paintComponent(graphic);
	}

	// shrink function to shrink the shape
	public void shrink() {
		delete();
		radius = radius - 10;
		xCoord = oX - radius / 2;
		yCoord = oY - radius / 2;
		paintComponent(graphic);
	}

}
