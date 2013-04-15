
public class percentilerange {
	public static void main(String[] args) {
		System.out.println("i\tlower\tupper\tlength");
		for(int i=1; i<=20; i++){
			int lower = i/4;
			int upper = 3*i/4;
			int length = upper-lower;
			System.out.println(i+"\t"+lower+"\t"+upper+"\t"+length);
		}

	}

}
