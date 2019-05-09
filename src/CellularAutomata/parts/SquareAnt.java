package CellularAutomata.parts;

import java.awt.Color;

/*** for use in automata such as Langston's ant ***/

public class SquareAnt {

	public enum Direction {NORTH, SOUTH, EAST, WEST}
	private Direction _dir;
	private int _x;
	private int _y;
	private Color _color;
	private Color _head_color;
	private int _id;
	
	public SquareAnt(int x, int y, int id) {
		_id = id;
		_color = Color.RED;
		_head_color = Color.decode("#8B0000");
		_dir = Direction.WEST;
		_x = x;
		_y = y;
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
	
	public int getId() {
		return _id;
	}
	
	public void rotateClockwise() {
		switch (_dir) {
		case NORTH:	_dir = Direction.EAST; break;
		case SOUTH: _dir = Direction.WEST; break;
		case EAST: _dir = Direction.SOUTH; break;
		case WEST: _dir = Direction.NORTH; break;
		}
	}
	
	public void rotateCounterClockwise() {
		switch (_dir) {
		case NORTH:	_dir = Direction.WEST; break;
		case SOUTH: _dir = Direction.EAST; break;
		case EAST: _dir = Direction.NORTH; break;
		case WEST: _dir = Direction.SOUTH; break;
		}
	}
	
	public void advance() {
		switch (_dir) {
		case NORTH:	_y--; break;
		case SOUTH: _y++; break;
		case EAST: _x++; break;
		case WEST: _x--; break;
		}
	}
	
	public void setColor(Color c) {
		_color = c;
	}
	
	public void setHeadColor(Color c) {
		_head_color = c;
	}
	
	public Color getColor() {
		return _color;
	}
	
	public Color getHeadColor() {
		return _head_color;
	}
	
	public Direction getDirection() {
		return _dir;
	}
}
