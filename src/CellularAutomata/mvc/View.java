package CellularAutomata.mvc;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import CellularAutomata.parts.*;

import java.awt.*;
import java.awt.geom.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.awt.Graphics;
import java.awt.Canvas;
import java.awt.Graphics2D;
import java.util.*;
import javax.script.SimpleBindings;
import javax.swing.event.*;
import java.awt.event.*;
import CellularAutomata.automata.*;

// TODO: cell borders option (on/off)
// TODO: Langston's Ant
// TODO: drag to move
// TODO: scroll to zoom
// TODO: slider value display
// TODO: turn cell size slider into a menu with zoom levels

@SuppressWarnings("serial")
public class View extends JPanel {
	
	private SquareGrid _grid_panel;
	private JPanel _control_panel;
//	private int _cell_size;
//	private JFrame _main_frame;
//	
	private JButton _but_play;
	private JButton _but_reset;
	private JSlider _sli_rate;
	private JSlider _sli_cells;
	
	private JLabel _label_rate;
	private JLabel _label_cells;
	private JLabel _rulestring;
	
	// birth and survive text for the rulestring
	private JFormattedTextField _text_b;
	private JFormattedTextField _text_s;
	
	private JMenuBar _topbar;
	private JMenu _menu_automata;
	
	private static final String[] automatachoices = {"Conway", "Langston"};
	private JComboBox<String> _cb_automata;
	
	private static final String[] directions = {"up", "down", "left", "right"};
	private JComboBox<String> _cb_ant_direction;
	
	private JCheckBox _check_ant;
	
	private JCheckBox _check_gridlines;
	
	private boolean _on;
	private Controller _controller;
	
	
	public View(JFrame main_frame) {
		// parameters
//		_main_frame = main_frame;
		_on = false;
		setLayout(new BorderLayout());
		
		_grid_panel = null;
		
		_topbar = new JMenuBar();
		
		_menu_automata = new JMenu("Select Automata");
		_menu_automata.add(new JMenuItem("Conway's Game of Life"));
		_menu_automata.add(new JMenuItem("Langston's Ant"));
		_menu_automata.add(new JMenuItem("Simulate the actual universe"));
		
		_topbar.add(_menu_automata);
		main_frame.setJMenuBar(_topbar);
		
		_control_panel = new JPanel(new GridLayout(0, 1));
		_control_panel.setBorder(new EmptyBorder(10,10,10,10));
		
		_cb_automata = new JComboBox<String>(automatachoices);
		_cb_automata.addItemListener(new ItemListener( ) {

			
			// TODO: this should really be in the controller
			@Override
			public void itemStateChanged(ItemEvent e) {
				String item = (String) _cb_automata.getSelectedItem();
				_controller.setAutomata(item);
			}
			
		});;
		
		
		_control_panel.add(_cb_automata);
		
		_but_play = new JButton("Run");
		_control_panel.add(_but_play);
		_but_reset = new JButton ("Reset");
		_control_panel.add(_but_reset);
		
		_label_rate = new JLabel("Tick rate");
		_sli_rate = new JSlider(10, 1000);
		var rate_labels = new Hashtable<Integer, JLabel>();
		rate_labels.put(10, new JLabel("too fast"));
		rate_labels.put(1000, new JLabel("grandma"));
		_sli_rate.setLabelTable(rate_labels);
		var rate_panel = new JPanel(new BorderLayout());
		rate_panel.add(_label_rate, BorderLayout.WEST);
		rate_panel.add(_sli_rate, BorderLayout.CENTER);
		_sli_rate.setPaintLabels(true);
		_sli_rate.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_controller.setRefreshRate(_sli_rate.getValue());
				if (_grid_panel != null) {
					_grid_panel.repaint();
				}
			}
		});
		_control_panel.add(rate_panel);
		
		_label_cells = new JLabel("Cell size");
		_sli_cells = new JSlider(1, 100);
		var cellsize_labels = new Hashtable<Integer, JLabel>();
		cellsize_labels.put(1, new JLabel("smol"));
		cellsize_labels.put(100, new JLabel("too big"));
		_sli_cells.setLabelTable(cellsize_labels);
		var cellsize_panel = new JPanel(new BorderLayout());
		cellsize_panel.add(_label_cells, BorderLayout.WEST);
		cellsize_panel.add(_sli_cells, BorderLayout.CENTER);
		_sli_cells.setPaintLabels(true);
		_sli_cells.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				_controller.setCellSize(_sli_cells.getValue());
				if (_grid_panel != null) {
					_grid_panel.repaint();
				}
			}
		});
		_control_panel.add(cellsize_panel);
		
		_check_ant = new JCheckBox("Place ants", false);
		_check_ant.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (_check_ant.isSelected()) {
					_controller.setPlaceAnts(true);
				} else {
					_controller.setPlaceAnts(false);
				}
			}
			
		});
		_control_panel.add(_check_ant);
		
		_cb_ant_direction = new JComboBox<String>(directions);
		_cb_ant_direction.addItemListener(new ItemListener( ) {
			@Override
			public void itemStateChanged(ItemEvent e) {
				String item = (String) _cb_ant_direction.getSelectedItem();
				switch(item) {
				case("left"): 	_grid_panel.setAntDir(SquareAnt.Direction.WEST);
							  	break;
				case("right"): 	_grid_panel.setAntDir(SquareAnt.Direction.EAST);
							   	break; 
				case("up"): 	_grid_panel.setAntDir(SquareAnt.Direction.NORTH);
								break;
				case("down"): 	_grid_panel.setAntDir(SquareAnt.Direction.SOUTH);
							  	break;
				}
			}
			
		});;
		
		_control_panel.add(_cb_ant_direction);
		
		_check_gridlines = new JCheckBox("Show grid", true);
		_check_gridlines.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (_check_gridlines.isSelected()) {
					_controller.setGridlines(true);
				} else {
					_controller.setGridlines(false);
				}
			}
		});
		_control_panel.add(_check_gridlines);
		
		_text_b = new JFormattedTextField(3);
		_text_s = new JFormattedTextField(23);
		_rulestring = new JLabel();
		generateRulestring();
		_text_b.addPropertyChangeListener(new PropertyChangeListener( ) {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (_controller.setRule(_rulestring.getText())) {
					generateRulestring();
				}
			}
			
		});
		
		JPanel ruletext = new JPanel(new GridLayout(0,2));
		ruletext.add(new JLabel("Current Rule: "));
		ruletext.add(_rulestring);
		_control_panel.add(ruletext);
		
		add(_control_panel, BorderLayout.SOUTH);
		
		
		
		/********** Buttons **********/
		
		_but_play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_on) {
					_controller.stopTimer();
					_but_play.setText("Run");
					_on = false;
				} else {
					_controller.startTimer();
					_but_play.setText("Stop");
					_on = true;
				}
			}
		});
		
		_but_reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_on) {
					_controller.stopTimer();
					_on = false;
					_but_play.setText("Run");
				}
				_controller.reset();
			}
		});
		
		main_frame.pack();
	}

	public void setController(Controller c) {
		_controller = c;
	}
	
	public void setGrid(SquareGrid g) {
		_grid_panel = g;
		add(_grid_panel, BorderLayout.CENTER);
		_grid_panel.addComponentListener(new ComponentListener() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				_grid_panel.setViewWidth(_grid_panel.getWidth());
				_grid_panel.setViewHeight(_grid_panel.getHeight());
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public String generateRulestring() {
		String out = "B" + _text_b.getValue() + "/S" + _text_s.getValue();
		_rulestring.setText(out);
		return out;
	}
}
