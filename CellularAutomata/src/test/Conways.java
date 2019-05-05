package test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class Conways {
	
	public static void main(String[] args) {
		
		JFrame f = new JFrame("im a window lol");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		View view = new View(f);
		
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BorderLayout());
		top_panel.add(view, BorderLayout.CENTER);
		
		top_panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		f.setContentPane(top_panel);
		f.pack();
		f.setVisible(true);
	}
}
