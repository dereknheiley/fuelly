/**
 * @author Derek Neil B00163969
 * Special bare bone queue class with enqueue and dequeue
 * only! O(1) speed & add/remove efficiency with tail pointer.
 */
public class Q<T>{
	
	//instance variables
	private Node<T> head;
	private Node<T> tail;
	
	//constructor
	public Q(){
		head = tail = null;
	}
	
	//adds item to the end of the queue
	public void enqueue(T item){
		if(item !=null){
			Node<T> itemnode = new Node<T>(item, null); 
			if (head==null)
				head = tail= itemnode; 
			else{
				tail.setNext(itemnode);
				tail = itemnode;
			}
		}
	}
	
	public T peek(){
		if(head!=null)
			return head.getData();
		return null;
	}
	
	//returns data at given index and removes it from head
	public T dequeue(){
		if(head!=null){
			Node<T> pop = head;
			head = head.getNext();
			return pop.getData();
		}
		return null;
	}
}