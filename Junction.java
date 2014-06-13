/*Name: Junction
propose: This class represents a junction of the tree.
author: Gal Luvton and Daniel Sinaniev
Date Created: 19/5/2013
Last modification: 24/5/2013
*/
import java.util.Vector;


public class Junction {
	
	/*Fields*/
	//size- holds the number of links in this junction
	private int size;
	//T- holds the T value of the tree. needed to ensure the leaf's size is legal
	private final int T;
	//parent- points to the father of this junction
	private Junction parent;
	//elements- holds the links this junction points to (as stated in the assigment)
	private Vector<Link> elements;
	//pointers- points to the sons of this junction
	private Vector<Object> pointers;
	//numOfElements- holds the amount of elements under this junction
	private int numOfElements;
	
	
	/*Behavior*/
	/*Constructors*/
	//creates a new junction from the given arguments
	public Junction (Vector<Link> keys, int t, Junction parent, Vector<Object> sons){
		this.parent= parent;
		this.elements= keys;
		this.pointers= sons;
		this.numOfElements= 0;
		this.size= keys.size();
		this.T= t;
		for (int i= 0; (i < sons.size()); i++){	//counts the elements under this junction to update numOfElements
			Object temp= sons.elementAt(i);
			if (temp instanceof Leaf)
				this.numOfElements+= ((Leaf)(temp)).getSize();
			else
				this.numOfElements+= ((Junction)(temp)).numOfElements();
		}
	}//Junction(Vector<Link>, int, Junction, Vector<Object>)
	
	
	//returns 'true' if this leaf is too big
	protected boolean overflow(){
		return this.size > this.T-1;
	}//overflow()
	
	
	//splits this junction into 2- updates this junction to be the left half, and returns the right half
	protected Junction split(Junction parent){
		int halfPointers;	//sets pointers to middle of this junction
		if (this.pointers.size()%2 == 0)
			 halfPointers= this.pointers.size() - this.pointers.size()/2;
		else
			 halfPointers= this.pointers.size() - this.pointers.size()/2 - 1;
		int halfElements= this.elements.size() - this.elements.size()/2;
		Vector<Link> newElements= new Vector<Link>();
		Vector<Object> newPointers= new Vector<Object>();
		while (halfElements < this.elements.size()){	//removes the links and adds them to the new juction
			newElements.add(this.elements.remove(halfElements));
		}
		while (halfPointers < this.pointers.size()){	//removes the sons and adds them to the new juction
			newPointers.add(this.pointers.remove(halfPointers));
		}
		this.numOfElements= 0;
		for (int i=0; i < this.pointers.size(); i++){	//sums all the elements under this junction and updates numOfElements
			if (this.pointers.elementAt(i) instanceof Leaf)
				this.numOfElements+= ((Leaf)this.pointers.elementAt(i)).getSize();
			else
				this.numOfElements+= ((Junction)this.pointers.elementAt(i)).numOfElements();
		}
		Junction newJunction= new Junction(newElements, this.T, parent, newPointers);
		for (int i=0; i < newPointers.size(); i++){	//sets the sons 'parent' to this
			Object temp= newPointers.elementAt(i);
			if (temp instanceof Leaf){	//sets the 'parent' value of all the sons of this junction
				Leaf tempLeaf= (Leaf)temp;
				tempLeaf.setParent(newJunction);
			}
			else {
				Junction tempJunction= (Junction)temp;
				tempJunction.setParent(newJunction);
			}
		}
		return newJunction;
	}//split(Junction)
	
	
	//splits the son sent to this method
	protected void splitSon(Object node){
		int location= 0;
		boolean found= false;
		while (!found){	//locates the son that needs to be split
			if (this.pointers.elementAt(location) == node)
				found= true;
			else
				location++;
		}
		if (this.pointers.elementAt(location) instanceof Leaf)	//calls for a help method to split the son
			leafSplit(location);
		else
			junctionSplit(location);			
		this.size= this.elements.size();	//updates the size of this junction after the split
	}// splitSon(Object)
	
	
	//splits the leaf that is kept in the 'pointers' vector at intex 'location'
	private void leafSplit(int location){
		Leaf toSplit= (Leaf)(this.pointers.elementAt(location));
		Leaf newLeaf= toSplit.split(this);	//splits the given leaf
		if (location < this.elements.size()){	//adds the new link in it's right place in this junction
			this.elements.add(location, toSplit.getLast());
			this.pointers.add(location+1, newLeaf);
			this.elements.setElementAt(newLeaf.getLast(), location+1);
		}
		else {
			this.elements.add(toSplit.getLast());
			this.pointers.add(newLeaf);
		}
	}//splitSon(int)
	
	
	//splits the son sent to this method
	private void junctionSplit(int location){
		Junction toSplit= (Junction)this.pointers.elementAt(location);
		Junction newJunction= toSplit.split(this);	//splits the given junction
		if (location < this.elements.size()){	//adds the new link in it's right place in this junction
			this.elements.add(location, toSplit.getLast());
			this.pointers.add(location+1, newJunction);			
		}
		else{
			this.elements.add(toSplit.getLast());
			this.pointers.add(newJunction);
		}
		toSplit.setSize(toSplit.getElements().size());
		newJunction.setSize(toSplit.getElements().size());
		toSplit.removeLastElement();
	}//junctionSplit(int)
	
	
	//returns the size of this junction
	protected int getSize(){
		return this.size;
	}//getSize()
	
	
	//returns the elemets of this junction
	protected Vector<Link> getElements(){
		return this.elements;
	}//getElement()

	
	//returns the sons of this junction
	protected Vector<Object> getPointers(){
		return this.pointers;
	}//getPointers()
	
	
	//returns the last link in this junction
	protected Link getLast(){
		return this.elements.lastElement();
	}//getLast()
	
	
	//retunrs the parent of this junction
	protected Junction getParent(){
		return this.parent;
	}//getParent()
	
	
	//sets the parent of this junction
	protected void setParent(Junction parent){
		this.parent= parent;
	}//setParent(Junction)
	

	//sets the parent of this junction
	protected void setSize(int size){
		this.size= size;
	}//setParent(Junction)
	
	
	//removes the last element of this junction
	protected void removeLastElement(){
		if (this.pointers.size() == this.elements.size()){	//will not allow a gap to form
			this.elements.remove(this.elements.size()-1);
		}
		this.size= this.elements.size();	//updates the size of this junction
	}//removeLastElement()
	
	
	//finds x in this junction. if used while inserting, increases the size of this junction's numOfElemets
	protected Object find(int x, boolean isInserting){
		if (isInserting)	//for insertion uses of this method
			this.numOfElements++;
		for (int i=0; i < this.elements.size(); i++){	//scans the junction for the leaf's location
			if (this.elements.elementAt(i).getElement() >= x)
				return this.pointers.elementAt(i);
		}
		return this.pointers.lastElement();	//if not found, returns the last elements
	}//find(int, boolean)
	
	
	//returns the number of elements under this junction
	protected int numOfElements(){
		return this.numOfElements;
	}//numOfElements()
	
	
	//overrides the toString from Object class
	//returns a string representation of this link
	public String toString(){
		String ans="";
		for(int i=0; i < this.elements.size(); i++){
			ans+= this.elements.elementAt(i)+ ",";
		}
		return ans.substring(0, ans.length()-1);
	}//toString()
	
	
	
}//Junction