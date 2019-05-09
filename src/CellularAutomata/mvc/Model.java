package CellularAutomata.mvc;
import CellularAutomata.parts.*;
import CellularAutomata.automata.*;

// Contains information about our grid

public class Model {

	private View _view;
	private SquareGrid _grid;
	
	public Model(View view) {
		_view = view;
	}
		
	public SquareCell[][] getCells() {
		if (_grid == null) {
			throw new RuntimeException("model never initialized a grid");
		}
		
		return _grid.getCells();
	}

	public void setGrid(SquareGrid grid) {
		_grid = grid;
	}
	
	public SquareGrid getGrid() {
		return _grid;
	}
	
	public void addAnt(SquareAnt ant) {
		_grid.addAnt(ant);
	}
}
