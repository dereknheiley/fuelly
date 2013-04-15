public class Pair implements Comparable<Pair>{
	
	private double first;
	private int second ; 
	
	public Pair(double f, int s) {
		first= f;
		second = s; 
	}
	public double getFirst() {
		return first; 
	}
	public int getSecond() {
		return second; 
	}
	public int compareTo(Pair c){
		if(first < c.getFirst())
			return -1;
		if(first > c.getFirst())
			return 1;
		return 0;
	}
	public String toString(){
		return ""+first+"   "+second;
	}
}
