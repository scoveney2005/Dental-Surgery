import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class EditPane extends JPanel {

	JList list;
	DefaultListModel listModel;
	JTextField nameBox;
	JTextField costBox;
	ObjectInputStream ois;
	FileInputStream fis;
	ArrayList<Procedure> procList;
	Procedure proc;
	TopRightPane top;
	BottomRight br;
	ObjectOutputStream oos;

	public EditPane() {
		super();

		procList = new ArrayList<Procedure>();

		setLayout(new FlowLayout());

		JPanel top = new JPanel(new BorderLayout());
		top.setPreferredSize(new Dimension(500, 250));

		// creates a buffer area
		this.add(Box.createRigidArea(new Dimension(50, 25)));

		listModel = new DefaultListModel();

		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);

		JScrollPane listScroller = new JScrollPane(list);

		top.add(listScroller);

		JPanel east = new JPanel(new GridLayout(2, 1, 0, 15));

		JPanel middle = new JPanel();
		JLabel procName = new JLabel("Procedure Name");
		nameBox = new JTextField(10);
		JLabel procCost = new JLabel("Procedure Cost");
		costBox = new JTextField(5);
		middle.add(procName);
		middle.add(nameBox);
		middle.add(procCost);
		middle.add(costBox);

		JPanel bottom = new JPanel();
		Button add = new Button("Add");
		add.addActionListener(new AddProc());
		Button edit = new Button("Edit");
		edit.addActionListener(new editProc());
		Button delete = new Button("Delete");
		delete.addActionListener(new deleteProc());

		bottom.add(add);
		bottom.add(edit);
		bottom.add(delete);

		add(top);
		east.add(middle);
		east.add(bottom);
		add(east);

		try {
			ois = new ObjectInputStream(new FileInputStream("Procedures.ser"));

			// reading Procedures Objects into arraylist
			while (true) {
				procList.add((Procedure) ois.readObject());

			}

		} catch (ClassNotFoundException f) {
			f.getMessage();
		} catch (IOException i) {
			i.getCause();
		}

		for (int i = 0; i < procList.size(); i++) {
			listModel.addElement(procList.get(i));
		}

		setPreferredSize(new Dimension(500, 250));

	}

	class AddProc implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			proc = new Procedure(nameBox.getText(), Integer.parseInt(costBox
					.getText()));
			listModel.addElement(proc);
			top.drop.addItem(proc);
			top.procList.add(proc);
			MainApplication.procedureList.add(proc);

			top.drop.removeAllItems();

			for (int i = 0; i < procList.size(); i++) {
				top.drop.addItem(procList.get(i));
			}
		}
	}

	class deleteProc implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			proc = (Procedure) listModel.get(list.getSelectedIndex());

			try {
				top.procList.remove(list.getSelectedIndex());
				MainApplication.procedureList.remove(list.getSelectedIndex());
				top.drop.removeItem(list.getSelectedIndex());
				listModel.removeAllElements();
				for (int i = 0; i < procList.size(); i++) {
					listModel.addElement(procList.get(i));
				}

			} catch (IndexOutOfBoundsException i) {
				i.getMessage();
			}

			top.drop.removeAllItems();

			for (int i = 0; i < procList.size(); i++) {
				top.drop.addItem(procList.get(i));
			}

		}
	}

	class editProc implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			try {
				proc = (Procedure) listModel.get(list.getSelectedIndex());

				proc.setProcName(nameBox.getText());
				proc.setProcCost(Integer.parseInt(costBox.getText()));
			} catch (ArrayIndexOutOfBoundsException a) {
				JOptionPane.showMessageDialog(null, "Please Select Procedure",
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			// outputting list with changes made
			listModel.removeAllElements();

			for (int i = 0; i < procList.size(); i++) {
				listModel.addElement(procList.get(i));
			}

			top.drop.removeAllItems();

			for (int i = 0; i < procList.size(); i++) {
				top.drop.addItem(procList.get(i));
			}

		}
	}

	public void link(TopRightPane top) {
		this.top = top;
	}

}
