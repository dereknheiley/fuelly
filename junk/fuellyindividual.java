/**
 * @author Derek Neil B00163969
 * reads String input from file and does some processing */
import java.util.Scanner;
import java.io.*;

public class fuellyindividual{
	public static void main(String[] args){
		
		try{
			Scanner inputFile = new Scanner(new File("fuellysample.txt"));
			FileWriter filewriter = new FileWriter(new File("fuellyindividualLPK.txt"));
			
			while (inputFile.hasNext()){
				String line = inputFile.nextLine();
				String[] a = line.split(",");
				
				//fuel , CC, Year & Make & Model, URL
				//{x:38, y:250, m:'Bike Make & Model Here', url: 'http://fuelly.com/motorcycle/honda/cbr250r'},
				String out = "{x:"+a[6]+", y:"+a[7]+", m:'"+a[0]+" "+a[1]+" "+a[2]+"', url: '"+a[8]+"'},";
				
				System.out.println(out);
				filewriter.write(out+"\n");
			}
			inputFile.close();
			filewriter.close();
		}
		catch(IOException e){
			System.out.println("Problem reading file!\n");
		}
	}
}
