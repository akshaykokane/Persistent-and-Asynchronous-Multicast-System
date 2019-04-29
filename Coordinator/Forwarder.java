import java.util.ArrayList;
import java.util.*;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.UnknownHostException;
public class Forwarder extends Thread {
  ArrayList<ParticipantDetail> participants;
  ArrayList<String> messagelist;
  ArrayList<String> removedmessage;
  ArrayList<Long> removedtime;
  ArrayList<Long> timestamp;
   int td; 
  // Queue<String> messageQueue;
   	Socket socket = null;
	public Forwarder(ArrayList<ParticipantDetail> participants, int td, ArrayList<String> messagelist, ArrayList<String> removedmessage, ArrayList<Long> removedtime, ArrayList<Long> timestamp) {
		// TODO Auto-generated constructor stub
   
   this.participants = participants;
   this.td = td;
   this.messagelist = messagelist; 
   this.removedmessage = removedmessage;
   this.removedtime = removedtime;
   this.timestamp = timestamp;
	}
 
   public void run(){
   
   while(true){
    try{
   Thread.sleep(1);
   //System.out.println("waiting for participant");
  
     while(messagelist.size() > 0){
      
      
       String messageToCast = messagelist.get(messagelist.size()-1);//may be peek 
       messagelist.remove(messagelist.size()-1);
       removedmessage.add(messageToCast);
       long removetime = timestamp.get(timestamp.size()-1);
       removedtime.add(removetime);
       timestamp.remove(timestamp.size()-1);
       System.out.println("the removedmessagelist is "+removedmessage);
       System.out.println("the removed time stamp list is "+removedtime);
         for(ParticipantDetail p : participants){
         System.out.println("Pid : "+p.id+" P Status : "+p.status);
           if(p.status.equals("Active")){
           
           System.out.println("Pid : "+p.IP+" P Status : "+p.port);
           
             socket = new Socket(p.IP, p.port);
            System.out.println("Message Fwd : "+messageToCast); 
      	     ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
     		     outputStream.writeObject(messageToCast);
            socket.close();
           }
//           else if(p.status.equals("InActive")){
//           
//             if(p.offlineQueue.size() == td)
//               p.offlineQueue.poll(); //if td is reached, remove the oldest message and inset new in last
//               
//               p.offlineQueue.add(messageToCast);
           
 //          }
           
       
        }
       
       }
     }catch(InterruptedException | IOException e){
       e.printStackTrace();
     }
     
   }
   
   
 }
   
   }


