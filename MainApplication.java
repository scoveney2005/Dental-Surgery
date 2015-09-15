import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainApplication implements Serializable {

	static ArrayList<Patient> pList;
	static ArrayList<Procedure> procedureList;
	static MainGui gui;
	static ObjectInputStream ois;
	FileInputStream fis;
	static ObjectOutputStream oos;
	static File procedureFile = new File("Procedures.ser");
	static DateFormat format;

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub

		pList = new ArrayList<Patient>();

		// list of procedures at the start of program
		// will run independently of these for maintenance once program is
		// running
		Procedure p1 = new Procedure("extraction", 50);
		Procedure p2 = new Procedure("filling", 90);
		Procedure p3 = new Procedure("cleaning", 55);
		Procedure p4 = new Procedure("crown", 200);
		Procedure p5 = new Procedure("X-Ray", 30);

		procedureList = new ArrayList<Procedure>();
		procedureList.add(p1);
		procedureList.add(p2);
		procedureList.add(p3);
		procedureList.add(p4);
		procedureList.add(p5);

		try {
			ois = new ObjectInputStream(new FileInputStream("Object.ser"));

			// reading PatientList back in from file
			while (true) {
				pList.add((Patient) ois.readObject());

			}

		} catch (ClassNotFoundException f) {
			f.getMessage();
		} catch (IOException i) {
			i.getCause();
		}

		// writing out procedures so can be read in from the GUI
		try {

			// used this check, so if edits are made to procedures in the GUI
			// the file will not be overwrote again.
			if (!procedureFile.exists()) {
				oos = new ObjectOutputStream(
						new FileOutputStream(procedureFile));

				for (int i = 0; i < procedureList.size(); i++) {

					oos.writeObject(procedureList.get(i));

				}

			}
		} catch (IOException e) {

			e.printStackTrace();
		}

		gui = new MainGui();

	}

}
