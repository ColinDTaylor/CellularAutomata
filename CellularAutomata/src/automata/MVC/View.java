package test;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.Graphics;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JPanel implements ActionListener {

	private int _grid_width;
	private int _grid_height;
	private int _refresh_rate;
	private Grid _grid_panel;
	private JPanel _button_panel;
	private int _cell_size;
	private JFrame _main_frame;
	private JButton _but_play;
	private boolean _on;
	
	public View(JFrame main_frame) {
		// parameters
		_main_frame = main_frame;
		_grid_width = 500;
		_grid_height = 500;
		_refresh_rate = 500; // just under 60 frames per second
		_cell_size = 10;
		_on = false;
		setLayout(new BorderLayout());
		
		_grid_panel = new Grid(_grid_width, _grid_height, _cell_size);
		add(_grid_panel, BorderLayout.CENTER);
		
		_button_panel = new JPanel(new BorderLayout());
		_but_play = new JButton("Run");
		_button_panel.add(_but_play);
		add(_button_panel, BorderLayout.SOUTH);

		
		Timer timer = new Timer(_refresh_rate, this);
		
		_but_play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_on) {
					timer.stop();
					_but_play.setText("Run");
					_on = false;
					_grid_panel.setEditMode(true);
				} else {
					timer.start();
					_but_play.setText("Stop");
					_on = true;
					_grid_panel.setEditMode(false);
				}
			}
		});
		
		main_frame.pack();
	}

	
	// actionPerformed is our "tick" for conway's game
	// TODO: by updating the number of living neighbors whenever a cell dies, I could make this significantly less time complex
	@Override
	public void actionPerformed(ActionEvent e) {
		Cell[][] cells = _grid_panel.getCells();
		
		// we have to mark the cells in this loop, instead of straight up toggling them
		// otherwise we'd mess up neighbor calculations for the following cells
		for (Cell[] row: cells) {
			for (Cell cell: row) {
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
		
		for (Cell[] row: cells) {
			for (Cell cell: row) {
				if (cell.isMarked()) {
					cell.toggle();
					cell.unmark();
				}
			}
		}
		
		_grid_panel.repaint();
	}
	
	public Grid getGrid() {
		return _grid_panel;
	}
}
