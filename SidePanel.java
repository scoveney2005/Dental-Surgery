import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.io.IOException;

public class SidePanel extends JPanel {

	BottomPane bottom1;
	TopPane top1;
	TopRightPane top2;
	BottomRight bottom2;
	MainGui gui;

	public SidePanel(PayPanel pay, EditPane edit, MainGui gui) {
		super();

		setLayout(new GridLayout(2, 2));

		// this.gui = gui;

		// creating 4 panels to use on the one screen
		bottom1 = new BottomPane();

		top1 = new TopPane();

		top2 = new TopRightPane();

		bottom2 = new BottomRight();

		top1.link(bottom1); // have to create links to different classes to send
							// info the correct instance of the class
		bottom1.link(bottom2);
		top2.link(bottom1);
		top1.linkPay(pay);
		bottom2.link(bottom1);
		top2.link(bottom2);
		bottom1.linkTop(top1);
		bottom2.linkGui(gui);
		bottom2.linkTop(top2);
		top2.linkEdit(edit);
		top2.linkPay(pay);

		add(top1);
		add(top2);
		add(bottom1);
		add(bottom2);

		setPreferredSize(new Dimension(400, 800));
		setVisible(true);

	}
}
