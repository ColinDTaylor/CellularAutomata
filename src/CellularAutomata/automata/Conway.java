package CellularAutomata.automata;

import java.awt.event.ActionEvent;

import CellularAutomata.mvc.Model;
import CellularAutomata.parts.*;

public class Conway implements Automata {

	private Model _model;
	private int[] _rule_birth, _rule_survival;
	
	
	public Conway() {
		_rule_birth = new int[8];
		_rule_survival = new int[8];
	}
	
	public Conway(Model model) {
		_model = model;
	}
	
	public void setModel(Model model) {
		_model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SquareCell[][] cells = (SquareCell[][]) _model.getCells();

		
		
		// we have to mark the cells in this loop, instead of straight up toggling them
		// otherwise we'd mess up neighbor calculations for the following cells
		for (SquareCell[] row: cells) {
			for (SquareCell cell: row) {
				switch (cell.liveNeighbors()) {
				case 2:
					// cell lives a healthy life
					break;
				case 3:
					// if cell is dead, it is going to be born
					if (!cell.isAlive()) { 
						cell.mark();
					}
					break;
				default:
					// 0-1 neighbors, cell marked for death to underpopulation
					// 4-8 neighbors, cell marked for death to overpopulation
					if (cell.isAlive()) {
						cell.mark();
					}
				}
			}
		}
		
		for (SquareCell[] row: cells) {
			for (SquareCell cell: row) {
				if (cell.isMarked()) {
					cell.toggle();
					cell.unmark();
				}
			}
		}
		
		_model.getGrid().repaint();
	}
	
	@Override
	public ID type() {
		return ID.CONWAY;
	}

	@Override
	public boolean setRule(String rule) {
		
		return false;
	}
}
