import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class BottomRight extends JPanel implements Serializable {

	double height;
	Dimension screen;
	Component rigid;
	JPanel top, bottom, buttons;
	JLabel lab;
	JTextArea area;
	BottomPane bp;
	Patient patient;
	Invoice invoice;
	ArrayList<Invoice> invoices;
	JList invoiceList, procList;
	DefaultListModel invListModel, procModel;
	JScrollPane listScroller, listScroller2;
	ObjectOutputStream oos;
	FileOutputStream fos;
	MainApplication main;
	MainGui gui;
	TopRightPane trp;

	public BottomRight() {

		super();

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// getting size of screen
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		height = (screen.getHeight() - 100) / 2;

		top = new JPanel();
		top.setPreferredSize(new Dimension(450, 50));
		lab = new JLabel("Patient Invoice Information");
		lab.setSize(450, 100);
		top.add(lab);

		bottom = new JPanel(new BorderLayout());
		bottom.setMinimumSize(new Dimension(450, 450));

		// list that will show patients invoices
		invListModel = new DefaultListModel();
		invoiceList = new JList(invListModel);

		invoiceList
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		invoiceList.setLayoutOrientation(JList.VERTICAL);
		invoiceList.setVisibleRowCount(-1);
		invoiceList.setModel(invListModel);
		listScroller = new JScrollPane(invoiceList);

		invoiceList.addListSelectionListener(new invoiceShow());

		bottom.add("West", listScroller);

		// list that will show the procedures on a selected invoice with money
		// paid and owing
		procModel = new DefaultListModel();

		procList = new JList(procModel);

		procList.setMinimumSize(new Dimension(200, 200));

		procList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		procList.setLayoutOrientation(JList.VERTICAL);
		procList.setVisibleRowCount(-1);
		procList.setModel(procModel);
		listScroller2 = new JScrollPane(procList);

		bottom.add("East", listScroller2);

		// buttons to save the details of patients to a file and one to not save
		buttons = new JPanel();
		Button quit = new Button("Quit Without Saving");
		quit.addActionListener(new close());
		Button save = new Button("Save & Quit");
		save.addActionListener(new saveDetails());
		buttons.add(save);
		buttons.add(quit);

		add(top);
		add(bottom);
		// creates a buffer of nothing to organize layout better
		rigid = Box.createRigidArea(new Dimension(50, 50));
		add(rigid);
		add(buttons);

	}

	class close implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// closes frame
			gui.frame.dispose();
		}
	}

	class saveDetails implements ActionListener, Serializable {
		public void actionPerformed(ActionEvent e) {
			try {

				oos = new ObjectOutputStream(new FileOutputStream("Object.ser"));

				// writing out all patients to file
				for (int i = 0; i < MainApplication.pList.size(); i++) {
					oos.writeObject(MainApplication.pList.get(i));
				}

				// writing out in case any changes have been made to procedures
				oos = new ObjectOutputStream(new FileOutputStream(
						"Procedures.ser"));
				for (int k = 0; k < trp.procList.size(); k++) {
					oos.writeObject(trp.procList.get(k));
				}

				gui.frame.dispose();
				oos.close();
			} catch (FileNotFoundException f) {

				f.getMessage();
			} catch (IOException i) {

				i.getMessage();
			}

		}

	}

	class invoiceShow implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent l) {

			// calling method instead of having code inside here as i want to
			// access code from outside this class too
			try {
				if (invoiceList.getSelectedIndex() != -1) {
					/*
					 * Was having major trouble with adding an invoice to the
					 * list as i had to remove all the elements first(for
					 * display reasons) which was causing a problem as i was
					 * displaying information from the selected invoice on
					 * another list. as clearSelection causes the listener to
					 * act twice I was getting an exception using it so i've
					 * made the selected invoice equal a instance variable and
					 * sent the same invoice to another class that needed it too
					 * via a link!!! It works but obviously inefficient.
					 */
					invoice = (Invoice) invListModel.get(invoiceList
							.getSelectedIndex());

					procDetails();

					// sending the selected invoice to the top pane to use as it
					// will be cleared further down
					trp.getSelectedInvoice((Invoice) invListModel
							.get(invoiceList.getSelectedIndex()));

				}
				// invoiceList.clearSelection();
			} catch (ArrayIndexOutOfBoundsException a) {
				a.getMessage();
			}

		}
	}

	public void procDetails() {

		procModel.removeAllElements();

		// displaying procedures for the invoice
		try {
			if (!invoice.getProcList().isEmpty()) {
				for (int i = 0; i < invoice.getProcList().size(); i++) {
					procModel.addElement(invoice.getProcList().get(i));
				}

			}
		} catch (NullPointerException n) {
			n.getMessage();
		}

		try {

			// calculations
			procModel.addElement("Invoice Total: " + invoice.getInvoiceTotal());
			procModel.addElement("Amount Paid:  " + invoice.getTotalPaid());
			procModel.addElement("Total Owed:   " + invoice.getTotalOwed());
		} catch (NullPointerException n) {
			n.getMessage();
		}

	}

	public void showInvoices() {

		invListModel.removeAllElements();
		patient = (Patient) bp.listModel.get(bp.list.getSelectedIndex());

		for (int i = 0; i < patient.getInvoiceList().size(); i++) {
			invListModel.addElement(patient.getInvoiceList().get(i));

		}

	}

	// links to connect two instances of classes
	public void link(BottomPane bp) {
		this.bp = bp;
	}

	public void linkGui(MainGui gui) {
		this.gui = gui;
	}

	public void linkTop(TopRightPane trp) {
		this.trp = trp;
	}
}
