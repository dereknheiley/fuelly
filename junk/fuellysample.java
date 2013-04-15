/**
 * @author Derek Neil B00163969
 * reads String input from file and does some processing */
import java.util.Scanner;
import java.io.*;

public class fuellysample{
	public static void main(String[] args){
		
		try{
			Scanner inputFile = new Scanner(new File("fuellyjavadone-cleaned-sample.txt"));
			FileWriter filewriter = new FileWriter(new File("fuellybikestylesdone-sample.csv"));
			
			//get fix dash's and spaces in user url's in column 9 (index 8)
			while (inputFile.hasNext()){
				String line = inputFile.nextLine();
				String[] a = line.split(",");
				
				//delete existing dash's
				a[8]=a[8].replaceAll("-", "").trim().replaceAll(" ", "-");
				
				print(a);
				write(a, filewriter);
			}
			inputFile.close();
			filewriter.close();
		}
		catch(IOException e){
			System.out.println("Problem reading file!\n");
		}
	}
	public static void write(String[] a, FileWriter filewriter){
		try{
			int n=a.length;
			for(int i=0; i<n; i++){
				if(i!=3 && i<n-1)
					filewriter.write(a[i]+",");
				else if(i==n-1)
					filewriter.write(a[i]+"\n");
			}
		}
		catch(IOException e){
			System.out.println("Problem writing file!");
		}
	}
	public static void print(String[] a){
		int n=a.length;
		for(int i=0; i<n; i++){
			if(i!=3 && i<n-1)
				System.out.print(a[i]+",");
			else if(i==n-1)
				System.out.print(a[i] );
		}
		System.out.println();
	}
}
