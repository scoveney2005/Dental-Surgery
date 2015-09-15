import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Invoice implements Serializable {

	private int invoiceNo;
	private static int invoiceCount = 1;
	private boolean isPaid;
	private int year; // I will need the year of the invoice for validation
						// against payments
	private Calendar invoiceDate;
	private ArrayList<Procedure> in_procList;
	private ArrayList<Payment> in_paymentList;
	// have to format the date in the way I want it
	private SimpleDateFormat form = new SimpleDateFormat("dd MM yyyy");
	private String date;

	// taking ints and converting to a Calendar
	public Invoice(int d, int m, int y) {
		// had to minus one on month as date was adding 1 to month for some
		// unknown reason
		invoiceDate = new GregorianCalendar(y, m - 1, d);
		// formatting date
		date = form.format(invoiceDate.getTime());
		setPaid(false);
		in_procList = new ArrayList<Procedure>();
		in_paymentList = new ArrayList<Payment>();
		this.year = y;

		invoiceCount = getCounter();
		invoiceNo = invoiceCount;
	}

	public int getInvoiceNo() {
		return invoiceNo;
	}

	public boolean getIsPaid() {

		return isPaid;

	}

	public void setPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public String getInvoiceDate() {
		return date;
	}

	public void setInvoiceDate(Calendar invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public ArrayList<Procedure> getProcList() {
		return in_procList;
	}

	public void setProcList(ArrayList<Procedure> in_procList) {
		this.in_procList = in_procList;
	}

	public ArrayList<Payment> getPaymentList() {
		return in_paymentList;
	}

	public void setPaymentList(ArrayList<Payment> in_paymentList) {
		this.in_paymentList = in_paymentList;
	}

	public void addPayment(Payment p) {
		p.getPaymentAmt();
		in_paymentList.add(p);
		getTotalOwed(); // running method to force new calculation on Invoice
						// and maybe change isPaid boolean
	}

	public void addProcedure(Procedure p) {
		in_procList.add(p);
	}

	public void deleteProc(Procedure p) {
		in_procList.remove(p);
	}

	public double getInvoiceTotal() {
		double total = 0.0;

		for (int i = 0; i < in_procList.size(); i++) {
			total += in_procList.get(i).getProcCost();
		}

		return total;
	}

	public double getTotalPaid() {
		double total = 0.0;

		for (int i = 0; i < in_paymentList.size(); i++) {
			total += in_paymentList.get(i).getPaymentAmt();
		}

		return total;
	}

	public double getTotalOwed() {
		double total = 0.0;

		total = getInvoiceTotal() - getTotalPaid();

		// setting the invoice to paid if the total reaches zero
		if (getInvoiceTotal() > 0) {
			if (total == 0 || total < 0) {
				setPaid(true);
				total = 0;
			}
		}
		return total;
	}

	public int getCounter() {

		try {
			int a = 1;

			// runs through each patients invoice list starting from the end and
			// gets the last occurring invoice number.
			// otherwise starts invoice numbers from 1
			for (int i = MainApplication.pList.size() - 1; i >= 0; i--) {
				if (MainApplication.pList.get(i).getInvoiceList().size() != 0) {
					a = MainApplication.pList
							.get(i)
							.getInvoiceList()
							.get(MainApplication.pList.get(i).getInvoiceList()
									.size() - 1).getInvoiceNo() + 1;
				} else {
					return a;
				}

			}
			return a;
		} catch (ArrayIndexOutOfBoundsException e) {
			return 1;
		}

	}

	public int getInvoiceYear() {
		return year;
	}

	public String toString() {
		return "Invoice number:   " + getInvoiceNo() + "     Date:     "
				+ getInvoiceDate();// +"    Total Amount:    "+getInvoiceTotal()+"   Total Paid    "+getTotalPaid()+"     Total Owed:    "+getTotalOwed()+"      Is Invoice Paid: "+getIsPaid();
	}

	public void print() {
		System.out.println(toString());
	}

}
