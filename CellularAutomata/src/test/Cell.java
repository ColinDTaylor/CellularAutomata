package test;

public class Cell {
	private Cell[] _neighbors = new Cell[8];
	
	
	public final Cell NORTH = _neighbors[1];
	public final Cell WEST = _neighbors[3];
	public final Cell EAST = _neighbors[4];
	public final Cell SOUTH = _neighbors[6];
	public final Cell NORTHWEST = _neighbors[0];
	public final Cell NORTHEAST = _neighbors[2];
	public final Cell SOUTHWEST = _neighbors[5];
	public final Cell SOUTHEAST = _neighbors[7];
	
	private int _age;
	private boolean _alive;
	private boolean _marked;
	public int x;
	public int y;
	
	public Cell(int cx, int cy) {
		x = cx;
		y = cy;
		_age = 0;
		_alive = false;
		
//		if (Math.random() > 0.5) {
//			_alive = true;
//		}
	}
	
	public Cell[] neighbors() {
		return _neighbors;
	}
	
	public void setNeighbors(Cell[] n) {
		_neighbors = n;
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

	public boolean isAlive() {
		return _alive;
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
	
	public int liveNeighbors() {
		int counter = 0;
		
		for (Cell cell: _neighbors) {
			if (cell == null) {
				continue;
			}
			
			if (cell.isAlive()) counter++;
		}
		
		return counter;
	}
}
