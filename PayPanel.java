import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PayPanel extends JPanel {

	JList patList, payList;
	DefaultListModel listModelPat, listModelPay;
	JTextField payAmt, date1, date2, date3, invoiceNo;
	JLabel payAmtLab, date, invoiceNoLab;
	JPanel patientPanel, payPanel, lists, lower;
	JScrollPane listScroller, listScroller2;
	Button addPay, payReport;
	Payment p;
	Patient pat;
	ArrayList<Payment> payments;
	ArrayList<Patient> patients;
	ArrayList<Invoice> invoices;
	MainApplication main;
	Invoice inv;
	DateFormat format;

	public PayPanel() {
		super();
		setLayout(new GridLayout(2, 1));

		// creating patient list
		patientPanel = new JPanel(new BorderLayout());
		listModelPat = new DefaultListModel();

		patList = new JList(listModelPat);
		patList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		patList.setLayoutOrientation(JList.VERTICAL);
		patList.setVisibleRowCount(-1);

		listScroller = new JScrollPane(patList);
		patientPanel.add(listScroller);

		// populating list with patients
		for (int i = 0; i < main.pList.size(); i++) {
			listModelPat.addElement(main.pList.get(i));
		}

		patList.addListSelectionListener(new getPaymentList());

		// creating payment jlist
		payPanel = new JPanel(new BorderLayout());
		listModelPay = new DefaultListModel();
		listModelPay.addElement("Payment list");

		payList = new JList(listModelPay);
		payList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		payList.setLayoutOrientation(JList.VERTICAL);
		payList.setVisibleRowCount(-1);

		listScroller2 = new JScrollPane(payList);
		payPanel.add(listScroller2);

		// main upper panel
		lists = new JPanel(new GridLayout(1, 2));
		lists.add(patientPanel);
		lists.add(payPanel);

		// lower panel with buttons
		lower = new JPanel(new FlowLayout());

		payAmt = new JTextField(10);
		payAmtLab = new JLabel("      Payment Amount:");

		date = new JLabel("       Date of Payment:");
		date1 = new JTextField(2);
		date2 = new JTextField(2);
		date3 = new JTextField(4);

		addPay = new Button("Make Payment");
		addPay.addActionListener(new makePay());

		payReport = new Button("Print Arrears Report");
		payReport.addActionListener(new printArrears());

		lower.add(date);
		lower.add(date1);
		lower.add(date2);
		lower.add(date3);
		lower.add(payAmtLab);
		lower.add(payAmt);
		lower.add(addPay);
		lower.add(payReport);

		add(lists);
		add(lower);

		setPreferredSize(new Dimension(400, 800));
		setVisible(true);
	}

	class makePay implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// getting the patient selected
			pat = (Patient) listModelPat.get(patList.getSelectedIndex());

			// creating payment with what user entered
			int day = Integer.parseInt(date1.getText());
			int month = Integer.parseInt(date2.getText());
			int year = Integer.parseInt(date3.getText());
			double payment = Double.parseDouble(payAmt.getText());

			// creating new payment
			p = new Payment(day, month, year, payment);

			try {
				// the user must select which invoice to pay against
				inv = (Invoice) listModelPay.get(payList.getSelectedIndex());

				if (inv.getIsPaid()) {
					JOptionPane.showMessageDialog(null, "Invoice Paid in Full",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
				// if no procedures have been added to the invoice yet
				else if (inv.getTotalOwed() == 0) {
					JOptionPane.showMessageDialog(null, "Invoice is Empty",
							"Error", JOptionPane.ERROR_MESSAGE);

				}
				// if payment is made before invoice date
				else if (year < inv.getInvoiceYear()) {
					JOptionPane.showMessageDialog(null,
							"Payment Date is before Invoice Date", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					inv.addPayment(p);
					showPaymentList();
				}
			} catch (ArrayIndexOutOfBoundsException a) {
				JOptionPane.showMessageDialog(null, "Please Select Invoice",
						"Error", JOptionPane.ERROR_MESSAGE);

			} catch (ClassCastException c) {
				JOptionPane.showMessageDialog(null, "Please Select Invoice",
						"Error", JOptionPane.ERROR_MESSAGE);

			}

		}
	}

	class getPaymentList implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent e) {
			// doing this way so outside class can call the display
			showPaymentList();
		}

	}

	public void showPaymentList() {
		listModelPay.removeAllElements();

		pat = (Patient) listModelPat.get(patList.getSelectedIndex());

		payments = new ArrayList<Payment>();
		invoices = pat.getInvoiceList();

		// outputting all invoices
		for (int i = 0; i < pat.getInvoiceList().size(); i++) {

			payments = invoices.get(i).getPaymentList();
			listModelPay.addElement(invoices.get(i));

			// outputting all payments under each invoice
			for (int k = 0; k < payments.size(); k++) {

				listModelPay.addElement(payments.get(k));

			}

			listModelPay.addElement("Total Owed on Invoice:  "
					+ invoices.get(i).getTotalOwed());

			if (invoices.get(i).getIsPaid()) {
				listModelPay.addElement("Invoice Paid in Full");
			}
			// creating a space between invoices
			listModelPay.addElement(" ");
		}
	}

	class printArrears implements ActionListener {
		public void actionPerformed(ActionEvent a) {

			try {
				FileWriter file = new FileWriter("ArrearsReport.txt");
				PrintWriter output = new PrintWriter(file);

				// running through patient list
				for (int i = 0; i < MainApplication.pList.size(); i++) {
					// running through patients invoices
					for (int k = 0; k < MainApplication.pList.get(i)
							.getInvoiceList().size(); k++) {
						// if any invoice is unpaid
						if (MainApplication.pList.get(i).getInvoiceList()
								.get(k).getIsPaid() == false) {
							// getting position of last payment
							int lastPayment = MainApplication.pList.get(i)
									.getInvoiceList().get(k).getPaymentList()
									.size() - 1;

							// getting the current date
							format = new SimpleDateFormat("yyyy/MM/dd");
							Calendar cal = Calendar.getInstance();
							format.format(cal.getTime());

							// getting current year and month
							int currentYear = Integer.parseInt(format.format(
									cal.getTime()).substring(0, 4));
							int currentMonth = Integer.parseInt(format.format(
									cal.getTime()).substring(5, 7));

							// month and year of last payment
							int month = MainApplication.pList.get(i)
									.getInvoiceList().get(k).getPaymentList()
									.get(lastPayment).getMonth();
							int year = MainApplication.pList.get(i)
									.getInvoiceList().get(k).getPaymentList()
									.get(lastPayment).getYear();

							// if the payment is more than 6 months old from the
							// current date
							if ((currentMonth >= month - 5 && currentYear > year)
									|| (currentYear == year && currentMonth > month + 6)) {

								output.println(MainApplication.pList.get(i));
								output.println(MainApplication.pList.get(i)
										.getInvoiceList().get(k));
								output.println(MainApplication.pList.get(i)
										.getInvoiceList().get(k)
										.getPaymentList());

							}

						}

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
