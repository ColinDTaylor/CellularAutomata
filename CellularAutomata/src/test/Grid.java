package test;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

public class Grid extends JPanel implements MouseListener, MouseMotionListener{
	
	private int _width;
	private int _height;
	
	// size of each square in the grid
	private int _cell_size;
	
	// top left pixel to start drawing grid at
	private int _x;
	private int _y;
	
	// QoL
	private int _rows;
	private int _columns;
	
	private Color _cellColor;
	
	private boolean _drag_mode;
	private boolean _edit_mode;
	
	// 2D array of cell objects
	private Cell[][] _cells;
	
	public Grid(int width, int height, int squareSize) {
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(width, height));
	
		addMouseListener(this);
		addMouseMotionListener(this);
		
		_cell_size = squareSize;
		_rows = (int) (width/squareSize);
		_columns = (int) (height/squareSize);
		_width = squareSize*_rows;
		_height = squareSize*_columns;
		_x = 0;
		_y = 0;
		_cellColor = Color.BLACK;
		_drag_mode = false;
		_edit_mode = true;
		
		// Create the data structure
		_cells = new Cell[_rows][_columns];
		for (int x = 0; x < _rows; x++) {
			for (int y = 0; y < _columns; y++) {
				_cells[x][y] = new Cell(x, y);
			}
		}
		
		// Populate the neighbor arrays
		findNeighbors();
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
//		for (int x = 0; x < _rows; x++) {
//			for (int y = 0; y < _columns; y++) {
//				_cells[x][y] = new Cell(x, y);
//			}
//		}
		
		Graphics2D g2 = (Graphics2D) g;
		
		// Background
		g2.setColor(Color.WHITE);
		g2.fillRect(_x,_y, _width, _height);
		
		// Draw lines
		g2.setColor(Color.BLACK);
		for (int i=0; i<=_height; i+=_cell_size) {
			g2.drawLine(_x, _y + i, _x + _width, _y + i);
		}
		for (int i=0; i<=_width; i+=_cell_size) {
			g2.drawLine(_x + i, _y, _x + i, _y + _height);
		}
		
		// Color live cells
		g2.setColor(_cellColor);
		for (Cell[] row: _cells) {
			for (Cell cell: row) {
				if (cell.isAlive()) {
					g2.fillRect((cell.x*_cell_size) + _x, 
							(cell.y*_cell_size) + _y, 
							_cell_size, 
							_cell_size);
				}
			}
		}
	}
	
	// getters and setters
	
	public int getRows() {
		return _rows;
	}
	
	public int getColumns() {
		return _columns;
	}
	
	public Cell getCell(int x, int y) {
		try {
			return _cells[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public Cell[][] getCells() {
		return _cells;
	}

	public void setWidth(int n) {
		_width = n;
	}
	
	public void setHeight(int n) {
		_height = n;
	}
	
	public int getSquareSize() {
		return _cell_size;
	}
	
	public void setSquareSize(int n) {
		_cell_size = n;
	}
	
	public void setEditMode(boolean b) {
		_edit_mode = b;
	}

	/************** Events **************/
	
	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!_edit_mode) {
			return;
		}
		
		mouseCell(e).toggle();
		
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
		if (!_edit_mode) {
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
	
	private Cell mouseCell(MouseEvent e) {
		try {
			return _cells[e.getX()/_cell_size][e.getY()/_cell_size];
		} catch (ArrayIndexOutOfBoundsException ex) {
			return null;
		}
	} 

	private void findNeighbors() {
		for (Cell[] row: _cells) {
			for (Cell cell: row) {
				int x = cell.x;
				int y = cell.y;
				cell.setNeighbors(new Cell[] {
						getCell(x-1,y-1),	getCell(x,y-1), 	getCell(x+1,y-1),
						getCell(x-1,y),		/***(▰˘◡˘▰)***/		getCell(x+1,y),
						getCell(x-1,y+1),	getCell(x,y+1),		getCell(x+1,y+1)
				});
			}
		}
	}
}
