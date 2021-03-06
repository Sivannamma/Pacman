import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ReadFromFile {
	String name;
	boolean isFirstLine;
	int [] mat;

	public ReadFromFile(String name) {
		this.name = name;
		isFirstLine = true;
	}

	public int [] convertFileToIntArray() {

		try {
			File myObj = new File(name);
			Scanner myReader = new Scanner(myObj);
			int ind =0;
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if(isFirstLine)
				{
					int size = Integer.parseInt(data);
					mat = new int[size];
					isFirstLine= false;
				}
				else {
					for (int i = 0; i < data.length(); i++) {
						if(data.charAt(i) >= '0' && data.charAt(i)<= '9'){
							mat[ind++] = (int)(data.charAt(i) -48);
						}
					}
				}
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}

		return mat;

	}

}
