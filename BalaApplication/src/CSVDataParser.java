import java.util.*;
import java.io.*;

public class CSVDataParser {

	public static void main(String[] args) {
		String filePath = "ticket.csv";
		CSVDataParser csv = new CSVDataParser();
		csv.writeCsv(filePath);
		System.out.println("CSV write completed");
		csv.csvLookup(filePath, 0, "Balaji");
	}

	List<Guest> guestList = new ArrayList<>();

	public void writeCsv(String filePath) {
		Guest guest = new Guest("Balaji", "10");
		guestList.add(guest);
		guest = new Guest("Jayma", "20");
		guestList.add(guest);
		guest = new Guest("Deeptha", "30");
		guestList.add(guest);
		guest = new Guest("Mayura", "40");
		guestList.add(guest);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			guestList.forEach(elem -> {
				try {
					writer.write(elem.getName() + "," + elem.getTickets());
					writer.newLine();
				} catch (IOException e) {
					System.out.println("Inner try catch exception while csv file writing");
				}
			});
			writer.close();
		}
		catch (Exception ex ) {
			System.out.println("Outer try catch exception while csv file writing");
		}
	}

	public void csvLookup(String filePath, int targetColumnIndex, String lookupValue) {
		String line;
		boolean found = false;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			while ((line = reader.readLine()) != null) {
				String[] columns = line.split(",");
				if (targetColumnIndex < columns.length && 
						columns[targetColumnIndex].trim().equalsIgnoreCase(lookupValue)) {
					System.out.println("Lookupvalue found in csv file for lookup value " + columns);
					found = true;
					break;
				}
			}
			if (!found) {
				System.out.println("Lookupvalue NOT found in csv file for lookup value  " + lookupValue);
			}
		} catch (Exception e) {
			System.out.println("Try catch exception while csv file reading");
		}
	}
}

class Guest {

	private String name;
	private String tickets;

	public Guest() {}
	public Guest(String name, String tickets) {
		this.name = name;
		this.tickets = tickets;
	}
	public String getName() { return this.name; }
	public String getTickets() { return this.tickets; }
}
