package CellularAutomata.parts;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

// TODO: Deprecated, turn this into an interface for grids at some point

public class Grid extends JPanel implements MouseListener, MouseMotionListener{
	
	private int _width;
	private int _height;
	private int _cell_size;
	private int _x;
	private int _y;
	private int _rows;
	private int _columns;
	private Color _cellColor;
	private boolean _drag_mode;
	private boolean _edit_mode;
	private SquareCell[][] _cells;
	
	public Grid(int width, int height, int cellSize) {
		
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(width, height));
	
		addMouseListener(this);
		addMouseMotionListener(this);
		
		_cell_size = cellSize;
		_rows = (int) (width/cellSize);
		_columns = (int) (height/cellSize);
		_width = cellSize*_rows;
		_height = cellSize*_columns;
		_x = 0;
		_y = 0;
		_cellColor = Color.BLACK;
		_drag_mode = false;
		_edit_mode = true;
		
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
		g2.fillRect(_x,_y, _width, _height);
		
		// Draw lines
		g2.setColor(Color.BLACK);
		for (int i=0; i<=_cell_size*_rows; i+=_cell_size) {
			g2.drawLine(_x, _y + i, _x + _width, _y + i);
		}
		for (int i=0; i<=_cell_size*_columns; i+=_cell_size) {
			g2.drawLine(_x + i, _y, _x + i, _y + _height);
		}
		
		// Color live cells
		g2.setColor(_cellColor);
		for (SquareCell[] row: _cells) {
			for (SquareCell cell: row) {
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

	public void setWidth(int n) {
		_width = n;
		_columns = n/_cell_size;
	}
	
	public void setHeight(int n) {
		_height = n;
		_rows = n/_cell_size;
	}
	
	public int getCellSize() {
		return _cell_size;
	}
	
	public void setCellSize(int n) {
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
	
	private SquareCell mouseCell(MouseEvent e) {
		try {
			return _cells[e.getX()/_cell_size][e.getY()/_cell_size];
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
						getCell(x-1,y),		/***(▰˘◡˘▰)***/		getCell(x+1,y),
						getCell(x-1,y+1),	getCell(x,y+1),		getCell(x+1,y+1)
				});
			}
		}
	}
}
