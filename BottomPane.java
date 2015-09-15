import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class BottomPane<E> extends JPanel {

	JList list;
	DefaultListModel listModel;
	Button print, remove;
	JPanel bottom, middle, top;
	JLabel pat;
	JScrollPane listScroller;
	BottomRight br;
	TopPane t;
	Patient patient;
	List<Invoice> invoices;
	List<Patient> pListClone;
	MainApplication main;
	ObjectInputStream ois;

	public BottomPane() {

		super();

		// getting size of screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		double height = (screen.getHeight() - 100) / 2;

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setPreferredSize(new Dimension(400, (int) height));

		top = new JPanel();
		top.setMaximumSize(new Dimension(450, 50));
		pat = new JLabel("Patient List");
		pat.setMaximumSize(new Dimension(450, 50));
		top.add(pat);

		listModel = new DefaultListModel();

		// populating list with patients. Sorting the list again because
		for (int i = 0; i < main.pList.size(); i++) {

			listModel.addElement(main.pList.get(i));
		}

		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);

		list.setModel(listModel);

		listScroller = new JScrollPane(list);

		list.addListSelectionListener(new ListSelect());

		middle = new JPanel(new BorderLayout());
		middle.setPreferredSize(new Dimension(450, 300));
		middle.add(listScroller);

		bottom = new JPanel();
		bottom.setMaximumSize(new Dimension(450, 300));
		bottom.setLayout(new GridLayout(1, 2));

		print = new Button("Print Patient Report");
		print.addActionListener(new printReport());
		remove = new Button("Remove Patient");
		remove.addActionListener(new removePatient());

		bottom.add(print);
		bottom.add(remove);

		add(top);
		add(middle);
		add(bottom);

	}

	public void setList(Patient p) {
		listModel.addElement(p);

	}

	public void link(BottomRight b) {
		this.br = b;
	}

	public void linkTop(TopPane t) {
		this.t = t;
	}

	class removePatient implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// getting the selected patient and deleting it from records

			try {
				main.pList.remove(list.getSelectedIndex());
				listModel.remove(list.getSelectedIndex());
			} catch (ArrayIndexOutOfBoundsException a) {
				a.getMessage();
			}
			t.patName.removeAll();
			t.patPh.removeAll();
			t.patAdd.removeAll();

		}
	}

	class ListSelect implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {

			// using try catch as if patient is deleted, program is also trying
			// to display its details at the same time
			try {

				patient = (Patient) listModel.get(list.getSelectedIndex());
				t.patName.setText(patient.getPatientName());
				t.patAdd.setText(patient.getPatientAdd());
				t.patPh.setText(patient.getPatientPhone());

				br.showInvoices();
				br.procModel.removeAllElements();
			} catch (ArrayIndexOutOfBoundsException f) {
				f.getMessage();
			}

		}

	}

	class printReport implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// using a clone of the arraylist so sorting for the report will not
			// affect look of the list in the GUI
			pListClone = new ArrayList<Patient>();

			for (Patient p : MainApplication.pList) {
				try {
					pListClone.add((Patient) p.clone());
				} catch (CloneNotSupportedException c) {

				}
			}

			Collections.sort(pListClone);

			try {
				FileWriter file = new FileWriter("PatientReport.txt");
				PrintWriter output = new PrintWriter(file);

				for (int i = 0; i < pListClone.size(); i++) {

					output.println(pListClone.get(i));

					for (int p = 0; p < pListClone.get(i).getInvoiceList()
							.size(); p++) {
						output.println(pListClone.get(i).getInvoiceList()
								.get(p));

						output.println(pListClone.get(i).getInvoiceList()
								.get(p).getProcList());

						output.println(pListClone.get(i).getInvoiceList()
								.get(p).getPaymentList());

					}

					// just having a space between patient reports
					output.println("    ");

				}

				output.flush();
				output.close();

			} catch (IOException io) {

				io.getMessage();
			}

		}
	}

}
