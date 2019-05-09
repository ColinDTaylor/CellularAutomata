package CellularAutomata.parts;

public interface Cell {
	
	// getters and setters
	int getState();
	int getAge();
	int getX();
	int getY();
	Cell[] getNeighbors();
	boolean isAlive();
	boolean isMarked();
	boolean hasAnt();
	
	void mark();
	void unmark();
	void kill();
	void reset();
	
	void setAge(int age);
	void setState(int state);
	void setNeighbors(Cell[] neighbors);
	
}
