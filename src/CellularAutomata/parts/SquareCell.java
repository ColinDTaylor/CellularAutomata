package CellularAutomata.parts;

public class SquareCell implements Cell {
	private SquareCell[] _neighbors = new SquareCell[8];
	
	public final SquareCell NORTH = _neighbors[1];
	public final SquareCell WEST = _neighbors[3];
	public final SquareCell EAST = _neighbors[4];
	public final SquareCell SOUTH = _neighbors[6];
	public final SquareCell NORTHWEST = _neighbors[0];
	public final SquareCell NORTHEAST = _neighbors[2];
	public final SquareCell SOUTHWEST = _neighbors[5];
	public final SquareCell SOUTHEAST = _neighbors[7];
	
	private int _age;
	private boolean _alive;
	private boolean _marked;
	private boolean _has_ant;
	public int x;
	public int y;
	
	public SquareCell(int cx, int cy) {
		x = cx;
		y = cy;
		_age = 0;
		_alive = false;
		
//		if (Math.random() > 0.5) {
//			_alive = true;
//		}
	}
	
	public SquareCell[] getNeighbors() {
		return _neighbors;
	}
	
	public SquareCell[] neighbors() {
		return _neighbors;
	}
	
	public void setNeighbors(Cell[] n) {
		_neighbors = (SquareCell[]) n;
	}
	
	public void toggle() {
		_alive = !_alive;
	}
	
	public int getAge() {
		return _age;
	}

	public void setAge(int _age) {
		this._age = _age;
	}

	public void setState(int s) {
		if (s == 0) {
			_alive = false;
		} 
		else if (s == 1) {
			_alive = true;
		} 
		else {
			throw new IllegalArgumentException("invalid cell state");
		}
	}
	
	public boolean hasAnt() {
		return _has_ant;
	}
	
	public void setAnt(boolean b) {
		_has_ant = b;
	}
	
	public int getState() {
		return isAlive() ? 1 : 0;
	}
	
	public boolean isAlive() {
		return _alive;
	}
	
	public void kill() {
		_alive = false;
		_age = 0;
	}
	
	public boolean isMarked() {
		return _marked;
	}
	
	public void mark() {
		_marked = true;
	}
	
	public void unmark() {
		_marked = false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void reset() {
		_marked = false;
		_alive = false;
		_age = 0;
		
	}
	
	public int liveNeighbors() {
		int counter = 0;
		
		for (SquareCell cell: _neighbors) {
			if (cell == null) {
				continue;
			}
			
			if (cell.isAlive()) counter++;
		}
		
		return counter;
	}
}
