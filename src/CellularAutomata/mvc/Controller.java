package CellularAutomata.mvc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import CellularAutomata.parts.*;
import CellularAutomata.automata.*;

public class Controller {

	private View _view;
	private Model _model;
	private Timer _timer;
	private int _refresh_rate;
//	private boolean _on;
	private Automata _automata;
	private boolean _place_ants;
	
//	private int _cell_size;
//	private int _grid_width;
//	private int _grid_height;
	
	public Controller(View view) {
		_view = view;
		_model = new Model(_view);
		_refresh_rate = 17; // in ms
//		_on = false;
//		_grid_width = 500;
//		_grid_height = 500;
//		_cell_size = 25;
		
		
		_model.setGrid(new SquareGrid(new SquareCell[1000][1000]));
		_view.setGrid(_model.getGrid());
		
		// the action listener for the "tick" is the chosen automata ruleset
	}
	
	public void setView(View v) {
		_view = v;
	}
	
	public void stopTimer() {
		if (_timer == null) {
			return;
		}
		_timer.stop();
//		_on = false;
	}
	
	public void startTimer() {
		if (_timer == null) {
			return;
		}
		_timer.start();
//		_on = true;
	}
	
	public void reset() {
		for (Cell[] row: _model.getCells()) {
			for (Cell cell: row) {
				cell.reset();
			}
		}
		_model.getGrid().allowEdits(true);
		_model.getGrid().repaint();
	}
	
	public void setAutomata(Automata a) {
		_automata = a;
		_automata.setModel(_model);
		_timer = new Timer(_refresh_rate, _automata);
	}
	
	public void setAutomata(String s) {
		switch (s) {
		case "Conway":
			setAutomata(new Conway(_model));
			break;
		case "Langston":
			setAutomata(new Langston(_model));
			break;
		}
	}
	
	public void setPlaceAnts(boolean b) {
		_model.getGrid().setPlaceAnts(b);
		_place_ants = b;
	}
	
	public void setRefreshRate(int r) {
		_refresh_rate = r;
		_timer.setDelay(r);
	}
	
	public void setCellSize(int s) {
//		_cell_size = s;
		_model.getGrid().setCellSize(s);
		_model.getGrid().repaint();
	}
}
