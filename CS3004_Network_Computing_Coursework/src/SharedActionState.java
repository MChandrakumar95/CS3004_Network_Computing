import java.net.*;
import java.io.*;

public class SharedActionState{
	
	private SharedActionState mySharedObj;
	private String myThreadName;
	private double mySharedVariable;
	private boolean accessing=false; // true a thread has a lock, false otherwise
	private int threadsWaiting=0; // number of waiting writers

// Constructor	
	
	SharedActionState(double SharedVariable) {
		mySharedVariable = SharedVariable;
	}

//Attempt to acquire a lock
	
	  public synchronized void acquireLock() throws InterruptedException{
	        Thread me = Thread.currentThread(); // get a ref to the current thread
	        System.out.println(me.getName()+" is attempting to acquire a lock!");	
	        ++threadsWaiting;
		    while (accessing) {  // while someone else is accessing or threadsWaiting > 0
		      System.out.println(me.getName()+" waiting to get a lock as someone else is accessing...");
		      //wait for the lock to be released - see releaseLock() below
		      wait();
		    }
		    // nobody has got a lock so get one
		    --threadsWaiting;
		    accessing = true;
		    System.out.println(me.getName()+" got a lock!"); 
		  }

		  // Releases a lock to when a thread is finished
		  
		  public synchronized void releaseLock() {
			  //release the lock and tell everyone
		      accessing = false;
		      notifyAll();
		      Thread me = Thread.currentThread(); // get a ref to the current thread
		      System.out.println(me.getName()+" released a lock!");
		  }
	
	
    /* The processInput method */

	public synchronized String processInput(String myThreadName, String theInput) {
    		System.out.println(myThreadName + " received "+ theInput);
    		String theOutput = null;
    		// Check what the client said
    		if (theInput.equalsIgnoreCase("Do my action!")) {
    			//Correct request
    			if (myThreadName.equals("ActionServerThread1")) {
    				/*  Add 20 to the variable
    					multiply it by 5
    					divide by 3.
    				 */
    				mySharedVariable = mySharedVariable + 20;
       				mySharedVariable = mySharedVariable * 5;
       				mySharedVariable = mySharedVariable / 3;
   				System.out.println(myThreadName + " made the SharedVariable " + mySharedVariable);
    				theOutput = "Do action completed.  Shared Variable now = " + mySharedVariable;
    			}
    			else if (myThreadName.equals("ActionServerThread2")) {
    				/*	Subtract 5 from the variable
    					Multiply it by 10 
    					Divide by 2.5
    					*/
       				mySharedVariable = mySharedVariable - 5;
       				mySharedVariable = mySharedVariable * 10;
       				mySharedVariable = mySharedVariable / 2.5;
    					
    				System.out.println(myThreadName + " made the SharedVariable " + mySharedVariable);
    				theOutput = "Do action completed.  Shared Variable now = " + mySharedVariable;

    			}
       			else if (myThreadName.equals("ActionServerThread3")) {
       				/*	Subtract 50
						Divide by 2
						Multiply by 33
       				 */
       				mySharedVariable = mySharedVariable - 50;
       				mySharedVariable = mySharedVariable / 2;
       				mySharedVariable = mySharedVariable * 33;
 
       				System.out.println(myThreadName + " made the SharedVariable " + mySharedVariable);
    				theOutput = "Do action completed.  Shared Variable now = " + mySharedVariable;

       			}
       			else if (myThreadName.equals("ActionServerThread4")) {
    				/*	Multiply by 20
						Divide by 10
						Subtract 1
    				 */
       				mySharedVariable = mySharedVariable * 20;
       				mySharedVariable = mySharedVariable / 10;
       				mySharedVariable = mySharedVariable - 1;
    				System.out.println(myThreadName + " made the SharedVariable " + mySharedVariable);
    				theOutput = "Do action completed.  Shared Variable now = " + mySharedVariable;
       			}
       			else {System.out.println("Error - thread call not recognised.");}
    		}
    		else { //incorrect request
    			theOutput = myThreadName + " received incorrect request - only understand \"Do my action!\"";
		
    		}
 
     		//Return the output message to the ActionServer
    		System.out.println(theOutput);
    		return theOutput;
    	}	
}

