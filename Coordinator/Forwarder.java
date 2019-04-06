import java.util.ArrayList;
import java.util.*;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.UnknownHostException;
public class Forwarder extends Thread {
  ArrayList<ParticipantDetail> participants;
   int td; 
   Queue<String> messageQueue;
   	Socket socket = null;
	public Forwarder(ArrayList<ParticipantDetail> participants, int td, Queue<String> messageQueue) {
		// TODO Auto-generated constructor stub
   
   this.participants = participants;
   this.td = td;
   this.messageQueue = messageQueue; 

	}
 
   public void run(){
   
   while(true){
    try{
   Thread.sleep(1);
   //System.out.println("waiting for participant");
  
     while(messageQueue.size() > 0){
      
      
       String messageToCast = messageQueue.poll(); //may be peek 
         for(ParticipantDetail p : participants){
           if(p.status.equals("Active")){
             socket = new Socket(p.IP, p.port);
      	     ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
     		     outputStream.writeObject(messageToCast);
           }
           else if(p.status.equals("InActive")){
           
             if(p.offlineQueue.size() == td)
               p.offlineQueue.poll(); //if td is reached, remove the oldest message and inset new in last
               
               p.offlineQueue.add(messageToCast);
           
           }
           
           
       
         }
       }
     }catch(InterruptedException | IOException e){
       e.printStackTrace();
     }
     
   }
   
   
 }
   
   }


