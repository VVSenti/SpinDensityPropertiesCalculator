package models;

//import Controllers.Calculator;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import utils.CalculationManager;

@Component
public class FilesTableModel implements TableModel, CalculationManagerListener {
	@Autowired
	CalculationManager manager;

	static final int MIN_LINES_COUNT = 20;
	final String[] columnNames = new String[] { "File name", "Status", "Result" };
	final Class<?>[] columnClasses = new Class[] { String.class, String.class, Double.class };
	final Object[] blancLine = new Object[] { "", "", null };
	
	@Autowired
	Set<TableModelListener> listeners = new HashSet<>();

	@Override
	public int getRowCount() {
		return Integer.max(manager.getPlanSize(), MIN_LINES_COUNT);
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClasses[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (manager.getPlanSize() <= rowIndex) {
			return blancLine[columnIndex];
		} else {
			TaskBean taskBean = manager.getTaskBean(rowIndex);
			if (columnIndex == 0) {
				return taskBean.getFileName();
			} else if (columnIndex == 1) {
				return taskBean.getStatus();
			} else {
				return taskBean.getResult();
			}
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		listeners.add(l);
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		listeners.remove(l);
	}

	@Override
	public void planChanged(CalculationManager source) {
		for (TableModelListener listener : listeners) {
			listener.tableChanged(new TableModelEvent(this));
		}
	}

	@Override
	public void calculationStarted(CalculationManager source) {
	}

	@Override
	public void calculationStoped(CalculationManager source) {
	}

	public void setManager(CalculationManager manager) {
		this.manager = manager;
	}
}
