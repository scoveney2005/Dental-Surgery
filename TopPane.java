import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TopPane extends JPanel {

	JLabel patLab, patAddLab, patAddPh, invoiceLab;
	JTextField patName, patAdd, patPh, date1, date2, date3;
	BottomPane b;
	MainApplication m;
	PayPanel pay;
	JPanel inPanel;
	Button addInvoice, add, clear;
	Patient p;

	public TopPane() {
		super();

		setLayout(new GridLayout(4, 1, 0, 15));

		patLab = new JLabel("Patient Name");
		patLab.setForeground(Color.black);
		add(patLab);

		patName = new JTextField();
		add(patName);

		patAddLab = new JLabel("Patient Address");
		patAddLab.setForeground(Color.black);
		add(patAddLab);

		patAdd = new JTextField();
		add(patAdd);

		patAddPh = new JLabel("Patient Phone Number");
		patAddPh.setForeground(Color.black);
		add(patAddPh);

		patPh = new JTextField();
		add(patPh);

		add = new Button("Add Patient");
		add(add);
		clear = new Button("Clear Details");
		add(clear);

		setPreferredSize(new Dimension(300, 250));

		setVisible(true);

		clear.addActionListener(new Clear());
		add.addActionListener(new Add());
	}

	private class Clear implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			patName.setText("");
			patPh.setText("");
			patAdd.setText("");
		}

	}

	private class Add implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String name = patName.getText();
			String add = patAdd.getText();
			String ph = patPh.getText();

			p = new Patient(name, add, ph);

			// adding patient to the list on screen
			b.setList(p);
			// adding to the main patient list
			m.pList.add(p);

			// sending the new Patient to the Payments window
			pay.listModelPat.addElement(p);
		}
	}

	// needed reference to send the data from one class to another
	public void link(BottomPane b) {
		this.b = b;
	}

	public void linkPay(PayPanel pay) {
		this.pay = pay;
	}

}
