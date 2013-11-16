package data;
//package data;
//
//import java.awt.*;
//import java.awt.event.*;
//import java.awt.geom.Line2D;
//
//import javax.swing.*;
//
//
//public class Rec extends JPanel{    
//	
//	int xp;
//	int yp;
//	int width;
//	int height;
//	int barb;  
//	double phi;  
//	
//    public void paintComponent(Graphics g){
//    	super.paintComponent(g);
//    	this.setBackground(Color.WHITE);
//    	g.drawRect(xp,yp,width,height);
//    	
//    }
//    
//  
//    
//   public void infoRec(int x, int y, int w, int g){
//    	xp=x;
//    	yp=y;
//    	width=w;
//    	height=g;
//    }
//   
//   private void drawArrow(Graphics2D g2, double theta, double x0, double y0)  
//   {  
//       double x = x0 - barb * Math.cos(theta + phi);  
//       double y = y0 - barb * Math.sin(theta + phi);  
//       g2.draw(new Line2D.Double(x0, y0, x, y));  
//       x = x0 - barb * Math.cos(theta - phi);  
//       y = y0 - barb * Math.sin(theta - phi);  
//       g2.draw(new Line2D.Double(x0, y0, x, y));  
//   } 
//}


import java.awt.*;  
import java.awt.font.*;  
import javax.swing.*;  
   
public class InnerText extends JPanel  
{  
    protected void paintComponent(Graphics g)  
    {  
        super.paintComponent(g);  
        Graphics2D g2 = (Graphics2D)g;  
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  
                            RenderingHints.VALUE_ANTIALIAS_ON); 
        int w = getWidth();  
        int h = getHeight();  
        int x = w/3;  
        int y = h/4;  
        int rectW = w*3/8;  
        int rectH = h/4;  
        g.setColor(Color.black);  
        g.drawRect(x, y, rectW, rectH);  
        Font font = g2.getFont().deriveFont(16f);  
        g2.setFont(font);  
        String text = "hello world";  
        FontRenderContext frc = g2.getFontRenderContext();  
        int textWidth = (int)font.getStringBounds(text, frc).getWidth();  
        LineMetrics lm = font.getLineMetrics(text, frc);  
        int textHeight = (int)(lm.getAscent() + lm.getDescent());  
        int sx = x + (rectW - textWidth)/2;  
        int sy = (int)(y + (rectH + textHeight)/2 - lm.getDescent());  
        g.setColor(Color.blue);  
        g.drawString(text, sx, sy);  
    }  
}