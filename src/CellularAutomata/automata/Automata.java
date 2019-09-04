package CellularAutomata.automata;

import java.awt.event.ActionListener;

import CellularAutomata.mvc.Model;


public interface Automata extends ActionListener {
	
	public enum ID {
			CONWAY,
			LANGSTON
	}
	
	public void setModel(Model model);
	
	// setRule takes in a string in the Golly format, parses it, and
	// adjust the ruleset accordingly
	public boolean setRule(String rule);
	
	public ID type();
}
