package ru.sentyurin.SpinDensityPropertiesCalculator.models;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;

import ru.sentyurin.SpinDensityPropertiesCalculator.utils.CalculationManager;
import ru.sentyurin.SpinDensityPropertiesCalculator.views.HelpWindow;
import ru.sentyurin.SpinDensityPropertiesCalculator.views.SettingsWindow;

public enum ControlButtons {
	RUN {
		public void eval(CalculationManager calculationManager) {
			calculationManager.startCalculation();
		}

		public void evalWithIntMessage(CalculationManager calculationManager, int message) {
		}
	},
	STOP {
		public void eval(CalculationManager calculationManager) {
			calculationManager.stopCalculation();
		}

		public void evalWithIntMessage(CalculationManager calculationManager, int message) {
		}
	},
	ADD {
		public void eval(CalculationManager calculationManager) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(true);
			int ret = fileChooser.showDialog(null, "Select files");
			if (ret == JFileChooser.APPROVE_OPTION) {
				File[] chosenFiles = fileChooser.getSelectedFiles();
				calculationManager.addTasks(chosenFiles);
			}
		}

		public void evalWithIntMessage(CalculationManager calculationManager, int message) {
		}
	},
	REMOVE {
		public void eval(CalculationManager calculationManager) {
		}

		public void evalWithIntMessage(CalculationManager calculationManager, int message) {
			calculationManager.removeTask(message);
		}
	},
	REMOVE_ALL {
		public void eval(CalculationManager calculationManager) {
			calculationManager.removeAllTasks();
		}

		public void evalWithIntMessage(CalculationManager calculationManager, int message) {
		}
	},
	MOVE_UP {
		public void eval(CalculationManager calculationManager) {
		}

		public void evalWithIntMessage(CalculationManager calculationManager, int message) {
			calculationManager.moveTaskUp(message);
		}
	},
	MOVE_DOWN {
		public void eval(CalculationManager calculationManager) {
		}

		public void evalWithIntMessage(CalculationManager calculationManager, int message) {
			calculationManager.moveTaskDown(message);
		}
	},
	EXPORT {
		public void eval(CalculationManager calculationManager) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.setDialogTitle("Saving results");
			int ret = fileChooser.showSaveDialog(null);
			if (ret == JFileChooser.APPROVE_OPTION) {
				File fileToSave = fileChooser.getSelectedFile();
				System.out.println(fileToSave.toString());
				try {
					fileToSave.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try (FileWriter writer = new FileWriter(fileToSave.getPath(), true)) {
					writer.append(calculationManager.getPlanTextPresentation());
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void evalWithIntMessage(CalculationManager calculationManager, int message) {
		}
	},
	COPY {
		public void eval(CalculationManager calculationManager) {
			String toClipboard = calculationManager.getPlanTextPresentation();
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(toClipboard), null);
		}

		public void evalWithIntMessage(CalculationManager calculationManager, int message) {
		}
	},
	SETTING {
		public void eval(CalculationManager calculationManager) {
			new SettingsWindow();
		}

		public void evalWithIntMessage(CalculationManager calculationManager, int message) {
		}
	},
	HELP {
		public void eval(CalculationManager calculationManager) {
			new HelpWindow();
		}

		public void evalWithIntMessage(CalculationManager calculationManager, int message) {
		}
	};

	public abstract void eval(CalculationManager calculationManager);

	public abstract void evalWithIntMessage(CalculationManager calculationManager, int message);
}