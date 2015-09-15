import java.awt.Button;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class TopRightPane extends JPanel {

	private String[] procs;
	private String[] procCost;
	ArrayList<Procedure> procList;
	Scanner input;
	File file;
	JLabel costLab, invoiceLab, title;
	JPanel bottom, inPanel, top;
	Button add, delete, addInvoice;
	JComboBox<Procedure> drop;
	JScrollPane scroll;
	Procedure p;
	Patient patient;
	BottomPane bp;
	BottomRight br;
	EditPane edit;
	PayPanel pay;
	Invoice invoice;
	JTextField date1, date2, date3;
	ObjectInputStream ois;
	FileInputStream fis;

	public TopRightPane() {
		super();

		title = new JLabel("Invoice and Procedures");
		this.add(title);

		Component rigid = Box.createRigidArea(new Dimension(50, 50));
		this.add(rigid);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		procList = new ArrayList<Procedure>();

		top = new JPanel();
		top.setPreferredSize(new Dimension(200, 125));
		JLabel op = new JLabel("Add Procedure:       ");

		// method to read in Procedure List
		bringInProcs();

		// only Procedure Objects can go in drop menu
		drop = new JComboBox<Procedure>();

		// reading procedures into menu//Have to work on output as I have a cost
		// field too,rewrite code with proc.getCost()?
		for (int i = 0; i < procList.size(); i++) {
			drop.addItem(procList.get(i));
		}

		scroll = new JScrollPane(drop);

		top.add(op);
		top.add(scroll);

		inPanel = new JPanel();
		invoiceLab = new JLabel("Enter Date of Invoice ");
		inPanel.add(invoiceLab);
		date1 = new JTextField(3);
		date2 = new JTextField(3);
		date3 = new JTextField(5);
		inPanel.add(date1);
		inPanel.add(date2);
		inPanel.add(date3);

		addInvoice = new Button("Add Invoice");
		addInvoice.addActionListener(new addInvoice());

		this.add(inPanel);
		this.add(addInvoice);
		this.add(Box.createRigidArea(new Dimension(50, 25)));
		this.add(top);

		this.add(Box.createRigidArea(new Dimension(50, 25)));

		bottom = new JPanel(new GridLayout(1, 2));

		add = new Button("Add Procedure");
		bottom.add("West", add);
		add.addActionListener(new addProc());

		delete = new Button("Delete Procedure");
		delete.addActionListener(new deleteProc());
		bottom.add("Center", delete);

		this.add(bottom);
		setPreferredSize(new Dimension(200, 250));
		setVisible(true);

	}

	private class addProc implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// getting procedure from drop menu
			Procedure p = (Procedure) drop.getSelectedItem();

			try {
				// getting the patient that is selected on the patient list
				patient = (Patient) bp.listModel
						.get(bp.list.getSelectedIndex());

			} catch (ArrayIndexOutOfBoundsException a) {
				if (patient == null) {
					// if no patient has been selected to add the procedure to
					JOptionPane.showMessageDialog(null,
							"Choose Patient from list", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}

			try {

				// will only add procedure if invoice is still open(not paid in
				// full)

				if (!invoice.getIsPaid()) {
					invoice.addProcedure(p);

				}

				// will appear if all invoices are paid!!
				else {
					JOptionPane.showMessageDialog(null,
							"Please create new invoice");
				}

				// if patient has no invoices
				if (patient.getInvoiceList().isEmpty()) {
					JOptionPane.showMessageDialog(null,
							"Please create new invoice");
				}

			}

			// error messages if no invoice is selected
			catch (NullPointerException n) {

				JOptionPane.showMessageDialog(null, "Please Select Invoice",
						"Error", JOptionPane.ERROR_MESSAGE);
			} catch (ArrayIndexOutOfBoundsException out) {
				JOptionPane.showMessageDialog(null, "Please Select Invoice",
						"Error", JOptionPane.ERROR_MESSAGE);

			} catch (IndexOutOfBoundsException o) {
				JOptionPane.showMessageDialog(null, "Please Select invoice",
						"Error", JOptionPane.ERROR_MESSAGE);

			}

			try {
				// displaying details in window with upgraded procedures and
				// costs
				br.procDetails();
				pay.showPaymentList();

			} catch (ArrayIndexOutOfBoundsException a) {
				a.getMessage();
			}

		}
	}

	class addInvoice implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {

				patient = (Patient) bp.listModel
						.get(bp.list.getSelectedIndex());
				// getting date entered by user
				int a = Integer.parseInt(date1.getText());
				int b = Integer.parseInt(date2.getText());
				int c = Integer.parseInt(date3.getText());

				// creating invoice
				Invoice in = new Invoice(a, b, c);
				patient.addInvoice(in);
				// refreshing invoice view
				br.showInvoices();
				// refreshing payment list
				pay.showPaymentList();
				// clearing dates entered
				date1.setText("");
				date2.setText("");
				date3.setText("");

			} catch (ArrayIndexOutOfBoundsException p) {
				if (patient == null) {

					JOptionPane.showMessageDialog(null,
							"Choose Patient from list", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException n) {
				JOptionPane.showMessageDialog(null,
						"Please Enter Date for invoice", "Error",
						JOptionPane.ERROR_MESSAGE);

			}

		}

	}

	class deleteProc implements ActionListener {
		public void actionPerformed(ActionEvent a) {

			p = (Procedure) drop.getSelectedItem();

			if (!invoice.getIsPaid()) {
				invoice.deleteProc(p);
				br.procDetails();
			} else {
				JOptionPane.showMessageDialog(null, "Invoice is closed",
						"Error", JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	public void link(BottomRight br) {
		this.br = br;
	}

	public void link(BottomPane b) {
		bp = b;
	}

	// solution to a problem occurring in BottomRightPane class (see that class
	// for more details)
	public void getSelectedInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public void linkEdit(EditPane edit) {
		this.edit = edit;
		edit.link(this);
	}

	public void linkPay(PayPanel pay) {
		this.pay = pay;
	}

	public void bringInProcs() {

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

	}

}
