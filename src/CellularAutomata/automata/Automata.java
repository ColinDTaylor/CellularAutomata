package CellularAutomata.automata;

import java.awt.event.ActionListener;

import CellularAutomata.mvc.Model;


public interface Automata extends ActionListener {
	
	public enum ID {
			CONWAY,
			LANGSTON
	}
	
	public void setModel(Model model);
	
	public ID type();
}
