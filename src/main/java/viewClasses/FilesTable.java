package viewClasses;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.springframework.stereotype.Component;

@Component
public class FilesTable extends JTable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public FilesTable(TableModel tm) {
		super(tm);
		getTableHeader().setReorderingAllowed(false);
		TableColumnModel tableColumnModel = this.getColumnModel();
		TableColumn firstColumn = tableColumnModel.getColumn(0);
		firstColumn.setPreferredWidth(300);
		firstColumn.setResizable(false);
		TableColumn secondColumn = tableColumnModel.getColumn(1);
		secondColumn.setResizable(false);
		TableColumn thirdColumn = tableColumnModel.getColumn(2);
		thirdColumn.setResizable(false);
	}
}
