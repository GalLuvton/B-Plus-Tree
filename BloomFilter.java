/*Name: BloomFilter
propose: This class is a bloom filter
author: Gal Luvton and Daniel Sinaniev
Date Created: 19/5/2013
Last modification: 24/5/2013
*/

public class BloomFilter {
	
	/*Fields*/
	//arr- the bloomfilter's array of bits
	private boolean[] arr;
	//
	private final int K= 16;
	//N- the size of elements the bloom filter will hold
	private final int N;
	//NUM1- a random number that will be used in the hash functions
	private final int NUM1;
	//NUM2- a random number that will be used in the hash functions
	private final int NUM2;
	//primes= an array of prime numbers that will be used in the hash functions
	private int[] primes;
	
	
	/*Behavior*/
	/*Constructors*/
	//creates a new bloom filter
	public BloomFilter(int n){
		N= n;
		arr= new boolean[N*K];
		NUM1= (int)(Math.random()*10)+1;	//sets the random numbers and the prime's array
		NUM2= (int)(Math.random()*10)+1;
		int[] temp= {11, 23, 43, 79, 101, 139, 163, 181, 199, 229, 293};
		primes= temp;
	}//BloomFilter(int)
	
	
	//inserts a new value into the bloom filter
	public void insert(int x){
		for (int i=0; i<10; i++)	//runs in through 10 hash functions
			arr[hash(x,i)]= true;
	}//insert(int)
	
	
	//checks of 'x' was inserted to the bloom filter
	public boolean find(int x){
		boolean ans= true;
		for (int i=0; i<10; i++)
			ans= ans && arr[hash(x,i)];
		return ans;
	}//find(int)
	
	
	//the hash function's base form
	private int hash(int x, int i){
		return ((NUM1*x+NUM2)%primes[i])%(N*K);
	}//hash(int, int)
	
	
}//BloomFilter
