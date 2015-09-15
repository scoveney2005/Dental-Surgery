import java.io.Serializable;
import java.util.ArrayList;

public class Patient implements Serializable, Cloneable, Comparable {

	private int patientId;
	private static int patNum = 1; // used to keep track of the patient ID if
									// removing patients
	private String patientName;
	private String patientAdd;
	private String patientPhone;
	private ArrayList<Invoice> p_invoiceList;

	public Patient(String name, String address, String num) {
		setPatientName(name);
		setPatientPhone(num);
		setPatientAdd(address);

		patNum = getCounter();
		patientId = patNum;

		p_invoiceList = new ArrayList<Invoice>();
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientAdd() {
		return patientAdd;
	}

	public void setPatientAdd(String patientAdd) {
		this.patientAdd = patientAdd;
	}

	public String getPatientPhone() {
		return patientPhone;
	}

	public void setPatientPhone(String patientPhone) {
		this.patientPhone = patientPhone;
	}

	public void addInvoice(Invoice in) {
		p_invoiceList.add(in);
	}

	public ArrayList<Invoice> getInvoiceList() {
		return p_invoiceList;
	}

	public int getPatientId() {
		return patientId;
	}

	// getting surname for comapare method
	private String getSurname() {
		int nameInd = this.getPatientName().lastIndexOf(" ");

		String name = this.getPatientName().substring(nameInd);

		return name;
	}

	// sorting patients by surname
	public int compareTo(Object o1) {

		return this.getSurname().compareTo(((Patient) o1).getSurname());

	}

	// creating clone of patient
	public Object clone() throws CloneNotSupportedException {

		Patient clone = (Patient) super.clone();
		return clone;
	}

	public int getCounter() {
		// checks PatientId of the last entered patient and will add one to this
		try {
			int a = MainApplication.pList.get(MainApplication.pList.size() - 1)
					.getPatientId();
			return a + 1;
		} catch (ArrayIndexOutOfBoundsException e) {
			return 1;
		}

	}

	public String toString() {
		return getPatientId() + " " + getPatientName() + " " + getPatientAdd()
				+ " " + getPatientPhone() + "\n";
	}

	public void print() {
		System.out.println(toString());
	}

}
