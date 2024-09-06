package ru.sentyurin.SpinDensityPropertiesCalculator.models;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.sentyurin.SpinDensityPropertiesCalculator.utils.CalculationManager;

@Component
public class FilesTableModel implements TableModel, CalculationManagerListener {

	final int MIN_LINES_COUNT = 20;
	final String[] columnNames = new String[] { "File name", "Status", "Result" };
	final Class<?>[] columnClasses = new Class[] { String.class, String.class, Double.class };
	final Object[] blancLine = new Object[] { "", "", null };

	Set<TableModelListener> listeners = new HashSet<>();
	CalculationManager manager;

	@Autowired
	public FilesTableModel(CalculationManager manager) {
		this.manager = manager;
		manager.addListener(this);
	}

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
			TaskBean task = manager.getTaskBean(rowIndex);
			if (columnIndex == 0) {
				return task.getFileName();
			} else if (columnIndex == 1) {
				return task.getStatus();
			} else {
				return task.getResult();
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

}
