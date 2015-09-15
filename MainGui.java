import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class MainGui extends JPanel {

	Dimension screen;
	JTabbedPane tpane;
	JFrame frame;
	double width, height;
	File file;
	PayPanel pay;
	EditPane edit;

	public MainGui() throws FileNotFoundException {

		super();

		// panel for payments
		pay = new PayPanel();
		// panel for maintenance
		edit = new EditPane();

		frame = new JFrame("My Application");

		tpane = new JTabbedPane();

		// getting size of screen
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		width = screen.getWidth() - 20;
		height = screen.getHeight() - 75;

		// adding the panes to the different tabs. SidePanel will have more
		// panes created within it
		tpane.addTab("Patient", null, new SidePanel(pay, edit, this));
		tpane.addTab("Payments", null, pay);
		tpane.addTab("Maintenance", null, edit);

		setLayout(new GridLayout(1, 1));
		add(tpane);

		// setting frame to size of the screen
		frame.setPreferredSize(new Dimension((int) width, (int) height));
		frame.setContentPane(this);
		frame.pack();
		frame.setVisible(true);

	}

}
