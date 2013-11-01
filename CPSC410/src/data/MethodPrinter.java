package data;


import javax.swing.*;

import data.Rec;



public class MethodPrinter {
	public static void main(String[] args) throws Exception {
        
        JFrame f = new JFrame("UML");
        	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	Rec r = new Rec();
        	r.infoRec(10,20,34,43);
        	r.infoRec(1,2,34,43);
        	f.add(r);
        	f.setSize(400,250);
        	f.setVisible(true);
 
    
   
    
   
    }
    
}