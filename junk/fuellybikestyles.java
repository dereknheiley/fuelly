/**
 * @author Derek Neil B00163969
 * reads String input from file and counts freq of char
 * creates huffman encoding tree to compress message
 * compresses message, then uses huffman tree to decode
 */
import java.util.Scanner;
import java.io.*;

public class fuellybikestyles{
	public static void main(String[] args){
		
		try{
			Scanner inputFile = new Scanner(new File("fuellyjavadone-cleaned-sample.txt"));
			FileWriter filewriter = new FileWriter(new File("fuellybikestylesdone.csv"));
			
			//get fix dash's and spaces in user url's
			while (inputFile.hasNext()){
				String line = inputFile.nextLine();
				String[] a = line.split(",");
				if(a.length==8){
					if(a[3].matches("(.*[1-9][0-9][0-9]*.*)")){
						//if col 4 contains XX or XXX or XXXX digits, use them as CC
						int n = a[3].length();
						String cc ="";
						//get the numerical characters
						for(int i=0; i<n; i++){
							if(a[3].substring(i, i+1).matches("(.*[1-9][0-9].*)"))
								cc+=a[3].substring(i, i+1);
						}
						//add cell for CC number to be put into since split drops empty's
						a = new String[]{a[0],a[1],a[2],a[3],a[4],a[5],a[6],a[7],""};
						a[8]=cc;
					}
				}
				
				
				print(a);
				//write(a, filewriter);
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
