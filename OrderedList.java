/**
 * @author Derek
 * ordered list class that accepts duplicates
 * used to store players in an order and print them out
 */
import java.util.ArrayList;
public class OrderedList <T extends Comparable<T>>{
	//instance variables
	private ArrayList<T> elements;
	
	//constructor
	public OrderedList(){
		elements = new ArrayList<T>();
	}
    public OrderedList(int size){
    	elements = new ArrayList<T>(size);
    }
    
    public void enumerate(){
    	int n = elements.size();
    	for(int i=0; i<n; i++)
    		System.out.println(elements.get(i));
    }
    
    //general methods
    public int size(){
    	return elements.size();
    }
    public void clear(){
    	elements.clear();
    }
    public boolean isEmpty(){
    	return elements.isEmpty();
    }
    public T get(int pos){
    	if(pos<0||pos>elements.size()-1){
    		System.out.println("Get index out of bounds");
    		return null;
    	}
    	else
    		return elements.get(pos);
    }
    public T remove(int pos){
    	if(pos<0||pos>elements.size()-1){
    		System.out.println("Remove index out of bounds");
    		return null;
    	}
    	else
    		return elements.remove(pos);
    }
    
    //insert an item at the correct position
    public int insert(T item){
    	if( elements.size() ==0 ){
    	    elements.add(item);
    	    return 0;
    	}
    	else{
			int pos = binarySearch(item);
			//regardless if item is found or not
			//use result from binary search as insert point
			//since we're allowing duplicates
			elements.add(pos,item);
			return pos;
    	}
    }
    
    //binary search returns just lo not -(lo+1)
    //because intended use is for an ordered list w/ duplicates
    public int binarySearch(T item){
        if(elements.size() ==0)
            return 0;
        int lo=0, hi= elements.size()-1, mid =0;
	    while (lo<=hi){
		    mid = (lo+hi)/2;
		    int c = item.compareTo(elements.get(mid));
		    if( c==0 ) return mid;
		    else if ( c<0 ) hi = mid -1;
		    else if( c>0 ) lo = mid + 1;
	    }
        return lo; 
    }
}
