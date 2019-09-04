package CellularAutomata.automata;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import CellularAutomata.mvc.Model;
import CellularAutomata.parts.*;

public class Langston implements Automata {

	private Model _model;
	private ArrayList<SquareAnt> _ants;
	
	public Langston() {
	}

	public Langston(Model model) {
		_model = model;
	}
	
	@Override
	// the ants themselves should do this, actually
	public void actionPerformed(ActionEvent e) {
		if (_ants.isEmpty()) {
			return;
		}
		
		SquareCell[][] cells = _model.getCells();
		for (SquareAnt ant: _ants) {
			SquareCell cell = cells[ant.getX()][ant.getY()];
			if (cell.isAlive()) {
				ant.rotateCounterClockwise();
				ant.advance();
			}
			else {
				ant.rotateClockwise();
				ant.advance();
			}
			
			cell.toggle();
			cell.setAnt(false);
			
			switch (ant.getDirection()) {
			case NORTH: 
				_model.getGrid().getCell(cell.x, cell.y-1).setAnt(true);
				break;
			case SOUTH:
				_model.getGrid().getCell(cell.x, cell.y+1).setAnt(true);
				break;
			case EAST: 
				_model.getGrid().getCell(cell.x+1, cell.y).setAnt(true);
				break;
			case WEST: 
				_model.getGrid().getCell(cell.x-1, cell.y).setAnt(true);
				break;
			}
		}
		
		_model.getGrid().repaint();
	}

	@Override
	public void setModel(Model model) {
		_model = model;
		_ants = _model.getGrid().getAnts();
	}

	public ID type() {
		return ID.LANGSTON;
	}

	@Override
	public boolean setRule(String rule) {
		// TODO Langston setRule
		return true;
	}
}
