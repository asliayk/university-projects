import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JComponent;

public class MyRectangle extends JComponent {
	
	private Graphics graphic; // graphics object to be used for painting the shape 
	private Color color; // color of the shape
	private int leftX; // x coordinate of the left edge of the rectangle
	private int rightX; // x coordinate of the right edge of the rectangle
	private int topY; // y coordinate of the top edge of the rectangle
	private int bottomY; // y coordinate of the bottom edge of the rectangle
	private int oX; // x coordinate of the clicked point
	private int oY; // y coordinate of the clicked point
	private int width = 100; // width of the rectangle
	private int height = 50; // height of the rectangle
	
	// constructor
	public MyRectangle(int x,int y, Graphics g, Color _color) {
		graphic = g;
		oX = x;
		oY = y;
		color = _color;
		leftX = x-width/2;
		rightX = x+width/2;
		topY = y-height/2;
		bottomY = y+height/2;
		paintComponent(graphic);
	}
	
	// overridden paintComponent function to paint the shape
    @Override
	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
		graphic.setColor(color);
		graphic.fillRect(leftX,topY,width,height);
	}
	
    // overridden contains function to check whether the clicked point is in the shape
	@Override
	public boolean contains(Point p) {
		super.contains(p);
		if(leftX<=p.getX() && p.getX()<=rightX && topY<=p.getY() && p.getY()<=bottomY) {
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
	    graphic.fillRect(leftX,topY,width,height);
	    leftX = (int) (p.getX()-width/2);
		rightX = (int) (p.getX()+width/2);
		topY = (int) (p.getY()-height/2);
		bottomY = (int) (p.getY()+height/2);
		oX = (leftX+rightX)/2;
		oY = (topY+bottomY)/2;
		paintComponent(graphic);
	}
	
	// delete function to delete the shape
	public void delete() {
		graphic.setColor(Color.WHITE);
	    graphic.fillRect(leftX,topY,width,height);
	}
	
	// enlarge function to enlarge the shape
	public void enlarge() {
		delete();
		leftX -=10;
		rightX +=10;
		width+=20;
		topY-=5;
		bottomY+=5;
		height+=10;
		paintComponent(graphic);
	}
	
	// shrink function to shrink the shape
	public void shrink() {
		delete();
		leftX +=10;
		rightX -=10;
		width-=20;
		topY+=5;
		bottomY-=5;
		height-=10;
		paintComponent(graphic);
	}
	
	// rotate function to rotate the shape
	public void rotate() {
		delete();
		leftX = oX-height/2;
		rightX = oX+height/2;
		topY = oY-width/2;
		bottomY = oY+width/2;
		int twidth = width;
		width = height;
		height = twidth;		
		paintComponent(graphic);
	}

}
