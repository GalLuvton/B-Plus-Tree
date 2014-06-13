/*Name: Leaf
propose: This class represents a leaf of the tree.
author: Gal Luvton and Daniel Sinaniev
Date Created: 19/5/2013
Last modification: 24/5/2013
*/

import java.util.Vector;

public class Leaf {
	
	/*Fields*/
	//size- holds the number of elements in this leaf
	private int size;
	//T- holds the T value of the tree. needed to ensure the leaf's size is legal
	private final int T;
	//next- points to the next leaf in the tree
	private Leaf next;
	//parent- points to the father of this leaf- a junction
	private Junction parent;
	//data- holds the numbers stored in this leaf
	private Vector<Link> data;
	
	
	/*Behavior*/
	/*Constructors*/
	//creates a new leaf from the given arguments
	public Leaf(Link key, int t, Junction parent, Leaf next, Leaf previous){
		this.T= t;
		this.size= 1;
		this.data= new Vector<Link>();
		this.next= next;
		this.parent= parent;
		if ((previous != null) && (next != null)){	//sets the new Link's 'next' and 'prev' values
			this.data.add(key);
			key.setNext(next.getFirst());
			key.setPrev(previous.getLast());
			previous.setNext(this);
		} 
		else if ((previous == null) && (next != null)){
			this.data.add(key);
			key.setNext(next.getFirst());
			key.setPrev(null);
		}
		else if ((previous != null) && (next == null)){
			this.data.add(key);
			key.setNext(null);
			key.setPrev(previous.getLast());
			previous.setNext(this);
		}
		else {
			this.data.add(key);
			key.setNext(null);
			key.setPrev(null);
		}
	}//Leaf(Link, int, Junction, Leaf, Leaf)
	
	
	//returns the last Link in this leaf
	protected Link getLast(){
		return this.data.lastElement();
	}//getLast()

	
	//gets the first Link in this leaf
	protected Link getFirst(){
		return this.data.firstElement();
	}//getFirst()
	
	
	//inserts a new key into the leaf. returns the minimal gap between this link and the adjacent ones
	protected int insert(Link key){
		int ans= Integer.MAX_VALUE;	//sets the value to a high number to ensure the corrent gap will register
		boolean stop= false;
		int i;	//will point to the location of the new key in this leaf
		for(i=0; ((i < this.data.size()) && !stop); i++){	//finds where to place the new key
			if ((this.data.elementAt(i)).getElement() > key.getElement())
				stop= true;	
		}
		if (stop)	//due to the way 'for' loops works, this is needed
			i= i-1;
		if((i != 0) && (i != this.data.size())){	//if the new link should anywhere but first or last
			key.setNext(this.data.elementAt(i));	//sets all of the link's pointers
			key.setPrev(data.elementAt(i-1));
			this.data.elementAt(i-1).setNext(key);
			this.data.elementAt(i).setPrev(key);
			this.data.add(i, key);
			int gap1= this.data.elementAt(i+1).getElement() - this.data.elementAt(i).getElement();	//mesures the gaps
			int gap2= this.data.elementAt(i).getElement() - this.data.elementAt(i-1).getElement();
			ans= Math.min(gap1, gap2);
		}
		else if((i == 0) && (i != this.data.size())){	//if the new link should be the first in this leaf
			key.setNext(this.data.elementAt(i));
			key.setPrev(this.data.elementAt(i).getPrev());
			if (this.data.elementAt(i).getPrev() != null)
				this.data.elementAt(i).getPrev().setNext(key);
			this.data.elementAt(i).setPrev(key);
			this.data.add(i, key);
			int gap1= this.data.elementAt(i+1).getElement() - this.data.elementAt(i).getElement();
			int gap2;
			if (this.data.elementAt(i).getPrev() != null)
				gap2= this.data.elementAt(i).getElement() - (this.data.elementAt(i).getPrev()).getElement();
			else
				gap2= Integer.MAX_VALUE;
			ans= Math.min(gap1, gap2);
		}
		else if((i != 0) && (i == this.data.size())){	//if the new link should be last in this leaf
			key.setNext(this.data.elementAt(i-1).getNext());
			key.setPrev(this.data.elementAt(i-1));
			this.data.elementAt(i-1).setNext(key);
			if (key.getNext() != null)
				(key.getNext()).setPrev(key);
			this.data.add(key);
			int gap1;
			if ((this.data.elementAt(i).getNext()) != null)
				gap1= (this.data.elementAt(i).getNext()).getElement() - this.data.elementAt(i).getElement();
			else
				gap1= Integer.MAX_VALUE;
			int gap2= this.data.elementAt(i).getElement() - this.data.elementAt(i-1).getElement();
			ans= Math.min(gap1, gap2);
		}	
		this.size= this.data.size();	//updates the leaf's size
		return ans;
	}//insert(Link)
	
	
	//returns 'true' if this leaf is too big
	protected boolean overflow(){
		return (this.size > this.T-1);
	}//overflow()
	

	//splits this leaf into 2- updates this leaf to be the left half, and returns the right half
	protected Leaf split(Junction parent){
		int half= this.size - this.size/2;
		Leaf newLeaf= new Leaf(this.data.remove(half), this.T, parent, this.next, this);	//creates a new leaf with the middle link of this leaf
		this.size= this.data.size();
		newLeaf.getFirst().setPrev(this.data.elementAt(half-1));	//lets the pointers for the new leaf's only key
		while (half < this.size){	//removes the rest of the right half elements in this leaf
			newLeaf.insert(this.data.remove(half));	//insertion will take care of the new link's pointers
			this.size= this.data.size();	//update the size of this leaf
		}
		this.next= newLeaf;	//sets this leaf's pointers right
		return newLeaf;
	}//split(Junction)
	
	
	//finds the location of x in this leaf- if not found, returns 'null'
	public Link find(int x){
		for (int i=0; i < this.data.size(); i++){	//searches the leaf
			if (x <= this.data.elementAt(i).getElement()){
				return this.data.elementAt(i);
			}
		}
		return null;	//in case x was not found- null tells us to insert x at the end of the Leaf
	}//find(int)
	
	
	//returns the links of this leaf
	protected Vector<Link> getData(){
		return this.data;
	}//getData()
	
	
	//returns the next leaf in the list
	protected Leaf getNext(){
		return this.next;
	}//getNext()
	
	
	//sets the next leaf in the list
	protected void setNext(Leaf next){
		this.next= next;
	}//setNext()
	
	
	//returns the parent of this leaf
	protected Junction getParent(){
		return this.parent;
	}//getParent()
	
	
	//sets the parent of this leaf
	protected void setParent(Junction parent){
		this.parent= parent;
	}//setParent
	
	
	//returns the size of this link
	protected int getSize(){
		return this.size;
	}//getSize()
	
	
	//overrides the toString from Object class
	//returns a string representation of this link
	public String toString(){
		String ans= "";
		for (int i= 0; i < this.data.size(); i++){
			ans+= this.data.elementAt(i) + ",";
		}
		return ans.substring(0, ans.length()-1);
	}//toString()
	
	

}//Leaf
