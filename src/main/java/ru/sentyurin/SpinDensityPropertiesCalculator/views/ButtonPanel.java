package ru.sentyurin.SpinDensityPropertiesCalculator.views;

import java.awt.Dimension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.sentyurin.SpinDensityPropertiesCalculator.models.ButtonPanelModel;
import ru.sentyurin.SpinDensityPropertiesCalculator.models.ControlButtons;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

@Component
public class ButtonPanel extends JPanel implements ButtonPanelModelListener {
	private static final long serialVersionUID = 31126401022136278L;
	static final int buttonWidth = 130;
	static final int buttonHeight = 25;
	static final int hGap = 5;

	public final JButton runButton;
	public final JButton stopButton;
	public final JButton addButton;
	public final JButton removeButton;
	public final JButton removeAllButton;
	public final JButton moveUpButton;
	public final JButton moveDownButton;
	public final JButton exportButton;
	public final JButton copyButton;
	public final JButton helpButton;

	public final FilesTable filesTable;
	public final ButtonPanelModel buttonPanelModel;

	@Autowired
	public ButtonPanel(FilesTable filesTable, ButtonPanelModel buttonPanelModel) {
		this.filesTable = filesTable;
		this.buttonPanelModel = buttonPanelModel;

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		runButton = new JButton("Run");
		addButtonToPanel(runButton);
		addSpacer();
		stopButton = new JButton("Stop");
		addButtonToPanel(stopButton);
		addBigSpacer();

		addButton = new JButton("Add");
		addButtonToPanel(addButton);
		addSpacer();
		removeButton = new JButton("Remove");
		addButtonToPanel(removeButton);
		addSpacer();
		removeAllButton = new JButton("Remove all");
		addButtonToPanel(removeAllButton);
		addBigSpacer();

		moveUpButton = new JButton("Move up");
		addButtonToPanel(moveUpButton);
		addSpacer();
		moveDownButton = new JButton("Move down");
		addButtonToPanel(moveDownButton);
		addBigSpacer();

		exportButton = new JButton("Export");
		addButtonToPanel(exportButton);
		addSpacer();
		copyButton = new JButton("Copy");
		addButtonToPanel(copyButton);
		addBigSpacer();

		helpButton = new JButton("Help");
		addButtonToPanel(helpButton);
		addSpacer();

		runButton.addActionListener((x) -> buttonPanelModel.buttonPressed(ControlButtons.RUN));
		stopButton.addActionListener((x) -> buttonPanelModel.buttonPressed(ControlButtons.STOP));

		addButton.addActionListener((x) -> buttonPanelModel.buttonPressed(ControlButtons.ADD));

		removeButton.addActionListener((x) -> {
			int selectedRow = filesTable.getSelectedRow();
			buttonPanelModel.buttonPressedWithIntMessage(ControlButtons.REMOVE, selectedRow);
			filesTable.changeSelection(selectedRow, 0, false, false);
		});

		removeAllButton.addActionListener(
				(x) -> buttonPanelModel.buttonPressed(ControlButtons.REMOVE_ALL));

		moveUpButton.addActionListener((x) -> {
			int selectedRow = filesTable.getSelectedRow();
			buttonPanelModel.buttonPressedWithIntMessage(ControlButtons.MOVE_UP, selectedRow--);
			filesTable.changeSelection(selectedRow, 0, false, false);
		});

		moveDownButton.addActionListener((x) -> {
			int selectedRow = filesTable.getSelectedRow();
			buttonPanelModel.buttonPressedWithIntMessage(ControlButtons.MOVE_DOWN, selectedRow++);
			filesTable.changeSelection(selectedRow, 0, false, false);
		});

		exportButton
				.addActionListener((x) -> buttonPanelModel.buttonPressed(ControlButtons.EXPORT));
		copyButton.addActionListener((x) -> buttonPanelModel.buttonPressed(ControlButtons.COPY));
		helpButton.addActionListener((x) -> buttonPanelModel.buttonPressed(ControlButtons.HELP));

		setUnworkingView();
		disableRemoveButtons();
		buttonPanelModel.addListener(this);
	}

	private void addButtonToPanel(JButton btn) {
		btn.setMaximumSize(new Dimension(buttonWidth, buttonHeight));
		btn.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		this.add(btn);
	}

	@SuppressWarnings("serial")
	private void addSpacer() {
		this.add(new JLabel() {
			{
				setMaximumSize(new Dimension(10, hGap));
			}
		});
	}

	@SuppressWarnings("serial")
	private void addBigSpacer() {
		this.add(new JLabel() {
			{
				setMaximumSize(new Dimension(10, 4 * hGap));
			}
		});
	}

	private void enableRemoveButtons() {
		removeButton.setEnabled(true);
		removeAllButton.setEnabled(true);
	}

	private void disableRemoveButtons() {
		removeButton.setEnabled(false);
		removeAllButton.setEnabled(false);
	}

	private void setWorkingView() {
		runButton.setEnabled(false);
		stopButton.setEnabled(true);
	}

	private void setUnworkingView() {
		runButton.setEnabled(true);
		stopButton.setEnabled(false);
	}

	@Override
	public void calculationStarted() {
		setWorkingView();
	}

	@Override
	public void calculationStoped() {
		setUnworkingView();
	}

	@Override
	public void planIsEmpty() {
		disableRemoveButtons();
	}

	@Override
	public void planIsNotEmpty() {
		enableRemoveButtons();
	}
}