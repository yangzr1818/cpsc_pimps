package data;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;

import javax.swing.*;


public class Rec extends JPanel{    
	
	int xp;
	int yp;
	int width;
	int height;
	int barb;  
	double phi;  
	
    public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	this.setBackground(Color.WHITE);
    	g.drawRect(xp,yp,width,height);
    	
    }
    
  
    
   public void infoRec(int x, int y, int w, int g){
    	xp=x;
    	yp=y;
    	width=w;
    	height=g;
    }
   
   private void drawArrow(Graphics2D g2, double theta, double x0, double y0)  
   {  
       double x = x0 - barb * Math.cos(theta + phi);  
       double y = y0 - barb * Math.sin(theta + phi);  
       g2.draw(new Line2D.Double(x0, y0, x, y));  
       x = x0 - barb * Math.cos(theta - phi);  
       y = y0 - barb * Math.sin(theta - phi);  
       g2.draw(new Line2D.Double(x0, y0, x, y));  
   } 
}