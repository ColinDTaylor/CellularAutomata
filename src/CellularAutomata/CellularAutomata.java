package CellularAutomata;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import CellularAutomata.automata.*;
import CellularAutomata.mvc.*;

public class CellularAutomata {
	
	public static void main(String[] args) {
		JFrame f = new JFrame("im a window lol");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		View view = new View(f);
		Controller con = new Controller(view);
		view.setController(con);
		con.setAutomata(new Conway());
		
		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BorderLayout());
		top_panel.add(view, BorderLayout.CENTER);
		
		top_panel.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		f.setContentPane(top_panel);
		f.pack();
		f.setVisible(true);
	}
}
