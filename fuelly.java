/**
 * @author Derek Neil B00163969
 * reads String input from file and computes average of interquartile range for similar items
 */
import java.util.Scanner;
import java.io.*;

public class fuelly{
	public static void main(String[] args){
		try{
			//these two files are keyed together on the Make & Model string
			Scanner userdata = new Scanner(new File("fuelly20130414.csv"));
			/* userdata needs to be sorted by make & model to match order in bikeinfo 
			 * which it should be scrapping from the motorcycle page that lists models
			 * in this order */
			Scanner bikeinfo = new Scanner(new File("fuellybikeinfo.csv"));
			int noCC=0;
			int noMileageOrFuelups=0;
			int mileageAnnomaly=0;
			int calculated=0;
			int added=0;
			int wrote=0;
			
			String[] curr; //holds data for current bikeinfo to be used over serveral loops
			
			//create Ordered List of averages for current bike model
			OrderedList<Pair> averages = new OrderedList<Pair>(1000);
			
			//temp output
			FileWriter filewriter = new FileWriter(new File("fuelly.json"));
			filewriter.write("{ \"data\": ["+"\n"); //open json format
			//all the different bike models are tagged with what style they are
			//each file will be a data series so you can toggle them on and off on the graph
			//NOTE: each style can only have 999 data points or the graph won't load that data series
//			FileWriter[] filewrite = new FileWriter[] {
//					new FileWriter(new File("sport.txt")),
//					new FileWriter(new File("sporttouring.txt")),
//					new FileWriter(new File("touring.txt")),
//					new FileWriter(new File("street.txt")),
//					new FileWriter(new File("scooter.txt")),
//					new FileWriter(new File("offroad.txt")),
//					new FileWriter(new File("cruiser.txt")),
//					new FileWriter(new File("chopper.txt")),
//					new FileWriter(new File("threewheel.txt"))
//			};
			
			if(bikeinfo.hasNext()){
				curr = bikeinfo.nextLine().split(",");
			}
			else{
				System.out.println("No bikeinfo data to get started!");
				return;
			}
			int k=0;
			usrLoop: while (userdata.hasNext()){
				k++;
				String line = userdata.nextLine().replaceAll("\"", "");
				String[] a = line.split(",");
				/* scraping the site v5, this returns;
				 * a[0] "YEAR MAKE MULTI WORD MODEL AND POSSIBLY CC RATING"
				 * a[1] "Added MTH YYYY &bull; ## fuel-ups Property of USERNAME ."
				 * a[2] "DDDD.DD"
				*/
				
				try{
					//step 0: make sure mileage is within range
					double mileage = Double.parseDouble(a[2]);
					mileage = 100/mileage; //convert from L/100KM to KM/L i think...
					if(mileage > 7 && mileage < 90){
						
						//step 1: get rid of year
						String makeModel = a[0].substring(5);
						//make sure " &bull; Gas " wasn't accidentally read while scrapping
						makeModel = makeModel.replaceAll("&bull; Gas", "").trim();
						
						//step 2: extract fuel ups
						String temp[] = a[1].split(" ");
						int fuelups = Integer.parseInt(temp[4]);
						
						int num = averages.size();
						
						//step 3: 
						//if make & model are current, add mileage to linked list
						if(makeModel.equalsIgnoreCase(curr[0])){
							averages.insert(new Pair(mileage,fuelups));
							num = averages.size();
							added++;
							continue usrLoop;
						}
						
						/* else this is another bike, so compute average of interquartile
						 * range and write row for last bike model
						 * then reset list of averages and add data for this new bike model*/
						
						else if(num>0){
							calculated++;
							//step 4: get average & put into format for highcharts script
							boolean write = false;
							
							int sum =0;
							double avg = 0;
							
							if(num >=8){
								write = true;
								int low = num/4-1; //index of 25th percentile
								int high = 3*num/4; //index of 75th percentile
								for(int i=low; i<high; i++)
									sum+=averages.get(i).getFirst();
								avg = (double)sum/(high-low); //average of interquartile range
							}
							else if (num >= 4){
								//compute avg
								int low = num/4-1; //index of 25th percentile
								int high = 3*num/4; //index of 75th percentile
								for(int i=low; i<high; i++)
									sum+=averages.get(i).getFirst();
								avg = (double)sum/(high-low); //average of interquartile range
								
								//check std dev to make sure values aren't wildly different
								double var=0;
								for(int i=0; i<num; i++){
									double tmp = averages.get(i).getFirst();
									var = (avg - tmp)*(avg - tmp);
								}
								var /= (num-1);
								double stdDev = Math.sqrt(var);
								if( stdDev/avg < 0.15 )
									write=true;	
							}
							else if (num >= 2){
								//compute avg
								for(int i=0; i<num; i++)
									sum+=averages.get(i).getFirst();
								avg = (double)sum/num;
								
								//check std dev to make sure values aren't wildly different
								double var=0;
								for(int i=0; i<num; i++){
									double tmp = averages.get(i).getFirst();
									var = (avg - tmp)*(avg - tmp);
								}
								var /= (num-1);
								double stdDev = Math.sqrt(var);
								if( stdDev/avg < 0.15 )
									write=true;	
							}
							else if(num==1 && averages.get(0).getSecond()>=7){ //fuelups of that last bike, not this one! store pairs??
								avg = averages.get(0).getFirst();//this should be averages.get(0); and remove it afterwards!
								write=true;
							}
							
							//reject data on models that only have on bike and less than 7 fuelups
							
							if(write){
								int size = 0; //size of data point marker
								if(num<7)
									size=2;
								else if(num<25)
									size=4;
								else if(num<75)
									size=6;
								else if(num<125)
									size=8;
								else
									size=10;
								
								//trim to one decimal place
								avg = ((int)(avg*10))/10.0;
								
								//fuel , CC, Make & Model, URL
								//{x:38, y:250, m:'Bike Make & Model', url: 'http://fuelly.com/motorcycle/honda/cbr250r', marker:{radius:4, states:{hover:{radius:10}}}},
								String out = "{\"x\":"+avg+", \"y\":"+curr[1]+", \"m\":\""+curr[0]+"\", \"url\": \""+curr[3]+"\", \"marker\":{\"radius\":"+size+", \"states\":{\"hover\":{\"radius\":"+(size+2)+"}}}},";
								
								//temp filewriter
								try{
									int cc = Integer.parseInt(curr[1]);
									if(cc>0){
										filewriter.write(out+"\n");
										wrote++;
									}
								}catch (NumberFormatException e){
									noCC++;
								}
								
								//use style of bike to write to correct file
	//							if(curr[2].equalsIgnoreCase("sport"))
	//								filewrite[0].write(out+"\n");
	//							if(curr[2].equalsIgnoreCase("sporttouring"))
	//								filewrite[1].write(out+"\n");
	//							if(curr[2].equalsIgnoreCase("touring"))
	//								filewrite[2].write(out+"\n");
	//							if(curr[2].equalsIgnoreCase("street"))
	//								filewrite[3].write(out+"\n");
	//							if(curr[2].equalsIgnoreCase("scooter"))
	//								filewrite[4].write(out+"\n");
	//							if(curr[2].equalsIgnoreCase("offroad"))
	//								filewrite[5].write(out+"\n");
	//							if(curr[2].equalsIgnoreCase("cruiser"))
	//								filewrite[6].write(out+"\n");
	//							if(curr[2].equalsIgnoreCase("chopper"))
	//								filewrite[7].write(out+"\n");
	//							if(curr[2].equalsIgnoreCase("threewheel"))
	//								filewrite[8].write(out+"\n");
							}
							
							//even if we didn't write it out, we're still done with this model and need to clear to track next one
							averages.clear();
						}
							
						//load next bike model which should match next bike
						while(makeModel.compareToIgnoreCase(curr[0]) > 0){
							if(bikeinfo.hasNext())
								curr = bikeinfo.nextLine().split(",");
							else{
								System.out.println("Ran out of bikeinfo data after "+k+" user data: "+makeModel);
								userdata.close();
								bikeinfo.close();
								filewriter.close();
//								int n=filewrite.length;
//								for(int i=0; i<n; i++)
//									filewriter[i].close();
								return;
							}
						}
						if(makeModel.equalsIgnoreCase(curr[0]))
							averages.insert(new Pair(mileage,fuelups));
						else {
							System.out.println("User data without bikeinfo match: "+makeModel+" w/ "+mileage+" fuel usage");
							continue usrLoop;
						}
					}
					else
						mileageAnnomaly++;
				}catch (NumberFormatException e){
					//System.out.println("Skipping row "+k+", Can't parse mileage or fuelups to int value!");
					noMileageOrFuelups++;
				}
			}
			filewriter.write("}"+"\n"); //close json format
			userdata.close();
			bikeinfo.close();
			filewriter.close();
			//close all the file's in the filewrite array
//			int n=filewrite.length;
//			for(int i=0; i<n; i++)
//				filewriter[i].close();
			
			//print results
			System.out.println();
			System.out.println("Number input data points: "+k);
			System.out.println("Number of Models compiled: "+wrote);
			System.out.println("added: "+added);
			System.out.println("calculated: "+calculated);
			System.out.println("Individual bikes with mileage annomalies: "+mileageAnnomaly);
			System.out.println("Models with no CC rating: "+noCC);
			System.out.println("Individual null data points (no mileage / fuelups): "+noMileageOrFuelups);
			
		}
		catch(IOException e){
			System.out.println("Problem reading file!\n");
		}
	}

	
	//some temp worker methods
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
