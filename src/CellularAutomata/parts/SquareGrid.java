package CellularAutomata.parts;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.Timer;

import CellularAutomata.parts.SquareAnt;

// goal: create a gird which takes in some very large array, and creates
// a JPanel which is able to handle that large array and display it as
// a series of cells painted onto a white background which can then be 
// manipulated.


public class SquareGrid extends JPanel implements MouseListener, MouseMotionListener{
	
	private int _view_width;
	private int _view_height;
	private int _cell_size;
	private int _view_x;
	private int _view_y;
	private int _rows;
	private int _columns;
	private Color _cell_color;
	private boolean _drag_mode;
	private boolean _allow_edits;
	private SquareCell[][] _cells;
	private ArrayList<SquareAnt> _ants;
	private int _ant_pad;
	private boolean _place_ants;
	
	public SquareGrid(SquareCell[][] cells) {
		
		setLayout(new BorderLayout());
		_view_width = 645;
		_view_height = 645;
		setPreferredSize(new Dimension(_view_width, _view_height));
		
		addMouseListener(this);
		addMouseMotionListener(this);
		
		_cells = cells;
		_cell_size = 15;
		_ant_pad = 2;
		
		_cell_color = Color.BLACK;
		_drag_mode = false;
		_allow_edits = true;
		
		_rows = _cells.length;
		_columns = _cells[0].length;
		
		// x and y here refer to the top leftmost array indexes on the view
		
		_view_x = _rows/2;
		_view_y = _columns/2;
		
		// This is only used in rulesets that involve ants
		_ants = new ArrayList<SquareAnt>();
		
		// Create the data structure
		_cells = new SquareCell[_rows][_columns];
		for (int x = 0; x < _rows; x++) {
			for (int y = 0; y < _columns; y++) {
				_cells[x][y] = new SquareCell(x, y);
			}
		}
		// Populate the neighbor arrays
		findNeighbors();

	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		// Background
		g2.setColor(Color.WHITE);
		g2.fillRect(0,0, _view_width, _view_height);
		
		// horizontal lines
		g2.setColor(Color.BLACK);
		for (int i=0; i<=_view_height; i+= _cell_size) {
			g2.drawLine(0, 0 + i, 0 + _view_width, 0 + i);
		}
		
		// vertical lines
		for (int i=0; i<=_view_width; i+= _cell_size) {
			g2.drawLine(0 + i, 0, 0 + i, 0 + _view_height);
		}
		
		// These if statements should prevent trying to draw nonexistent cells by setting _view_y or _view_x too far in the corner
		if (_cells[0].length < _view_x+_columns) {
			_view_x = _cells[0].length - _columns;
		}
		if (_cells.length < _view_y+_rows) {
			_view_y = _cells.length - _rows;
		}
		
		// Color live cells
		g2.setColor(_cell_color);
		for (int x=0; x < _rows; x++) {
			for (int y=0; y<_columns; y++) {
				SquareCell cell = _cells[x+_view_x][y+_view_y];
				if (cell.isAlive()) {
					System.out.println("Painting Cell at " + cell.getX() + ", " + cell.getY());
					
					g2.fillRect((cell.x*_cell_size), 
							(cell.y*_cell_size), 
							_cell_size, 
							_cell_size);
				}

			}
		}
		
		// Draw ants
		for (SquareAnt ant: _ants) {
			SquareCell cell = getCell(ant.getX(), ant.getY());
			
			// I have no idea why this +1 makes things look right but it does so don't mess with it
			int ant_size = _cell_size - _ant_pad*2 + 1;
			
			// the +1 here is to account for the border
			int xloc = (cell.x*_cell_size) + _ant_pad;
			int yloc = (cell.y*_cell_size) + _ant_pad;
			
			// draw ant body
			g2.setColor(ant.getColor());
			g2.fillRect(xloc, 
					yloc,
					ant_size,
					ant_size);
			
			// draw ant head
			g2.setColor(ant.getHeadColor());
			switch (ant.getDirection()) {
			case NORTH:
				g2.fillRect(xloc, yloc, ant_size, ant_size/3);
				break;
			case SOUTH:
				g2.fillRect(xloc, yloc+ant_size/2, ant_size, ant_size/3);
				break;
			case EAST:
				g2.fillRect(xloc+ant_size/2, yloc, ant_size/3, ant_size);
				break;
			case WEST:
				g2.fillRect(xloc, yloc, ant_size/3, ant_size);
				break;
			}
		}
	}
	
	public void addAnt(SquareAnt ant) {
		_ants.add(ant);
	}
	
	// getters and setters
	
	public int getViewX() {
		return _view_x;
	}
	
	public int getViewY() {
		return _view_y;
	}
	
	public void setViewX(int n) {
		_view_x = n;
	}
	
	public void setViewY(int n) {
		_view_y = n;
	}
	
	public int getRows() {
		return _rows;
	}
	
	public int getColumns() {
		return _columns;
	}
	
	public ArrayList<SquareAnt> getAnts() {
		return _ants;
	}
	
	public SquareCell getCell(int x, int y) {
		try {
			return _cells[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public SquareCell[][] getCells() {
		return _cells;
	}

	public void setViewWidth(int n) {
		_view_width = n;
		_columns = n/_cell_size;
	}
	
	public void setViewHeight(int n) {
		_view_height = n;
		_rows = n/_cell_size;
	}
	
	public int getCellSize() {
		return _cell_size;
	}
	
	public void setCellSize(int n) {
		_cell_size = n;
	}
	
	public void setPlaceAnts(boolean b) {
		_place_ants = b;
	}
	
	public void allowEdits(boolean b) {
		_allow_edits = b;
	}

	/************** Events **************/
	
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!_allow_edits) {
			return;
		}
		
		SquareCell cell = mouseCell(e);
		
		if (_place_ants) {
			cell.setAnt(!cell.hasAnt());
			// TODO: for now, all ant IDs are just random 4 digit integers
			_ants.add(new SquareAnt(cell.x, cell.y, (int) Math.random()*10000));
		} else {
			cell.toggle();
		}
		
		
		repaint();
		_drag_mode = mouseCell(e).isAlive();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (!_allow_edits) {
			return;
		}
		
		if (mouseCell(e) == null) {
			return;
		}
		
		if (_drag_mode && !mouseCell(e).isAlive()) {
			mouseCell(e).toggle();
			repaint();
		} else if (!_drag_mode && mouseCell(e).isAlive()) {
			mouseCell(e).toggle();
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/*************** Helpers ***************/
	
	private SquareCell mouseCell(MouseEvent e) {
		try {
			return _cells[(e.getX()/_cell_size) + _view_x][(e.getY()/_cell_size) + _view_y];
		} catch (ArrayIndexOutOfBoundsException ex) {
			return null;
		}
	} 

	private void findNeighbors() {
		for (SquareCell[] row: _cells) {
			for (SquareCell cell: row) {
				int x = cell.x;
				int y = cell.y;
				cell.setNeighbors(new SquareCell[] {
						getCell(x-1,y-1),	getCell(x,y-1), 	getCell(x+1,y-1),
						getCell(x-1,y),		/***('u')***/		getCell(x+1,y),
						getCell(x-1,y+1),	getCell(x,y+1),		getCell(x+1,y+1)
				});
			}
		}
	}
}
