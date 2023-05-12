package application;

public class test {
	public static void main(String[] args) {
		//increment the existing IDs by 1 to get the next ID number
		int nextIdNumber = 2 + 1;

		// format the next ID number with leading zeros and prefix "US"
		String id = String.format("US%03d", nextIdNumber);

		// display the generated ID to the user
		System.out.println("Generated ID: " + id);
    }
} 
