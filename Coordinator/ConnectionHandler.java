import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.io.*; 
import java.util.*; 

public class ConnectionHandler extends Thread{

	 private static ServerSocket server;
	 private static int serverPort = 6600;
	 static int count = 0;
	 static Socket socket = null;
	 public static ArrayList<ParticipantDetail> participants = new ArrayList<>();
   public static ArrayList<Long> timestamp = new ArrayList<Long>();
	 public static ArrayList<String> messagelist = new ArrayList<String>();
   public static ArrayList<String> removedmessage = new ArrayList<String>();
   public static ArrayList<Long> removedtime = new ArrayList<Long>();
   public static int incomingMessagePort;
   public static int td;
   public static long timediff;
   public static long reconnecttime;
   public static int index;   
	 public ConnectionHandler(ArrayList<ParticipantDetail> participants, int td, int incomingMessagePort, ArrayList<Long> timestamp,ArrayList<String> messagelist, ArrayList<String> removedmessage, ArrayList<Long> removedtime) throws IOException {
		// TODO Auto-generated constructor stub
		
		server = new ServerSocket(serverPort);//server
		this.incomingMessagePort = incomingMessagePort;
     this.participants = participants;
     this.td = td;
     this.timestamp = timestamp;
     this.messagelist=messagelist;
     this.removedmessage = removedmessage;
     this.removedtime = removedtime;
	}
	 
	 @Override
		public void run() {
		 	try {
		 		
		 		System.out.println("Server Started..");
			
			// TODO Auto-generated method stub
			while(true) {
				
		 		socket = server.accept();
				count++;
				int tempid;
				System.out.println("Participant" + count +"connected");
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				String particpantDetails = (String) inputStream.readObject();
        System.out.println("The participant details string is "+particpantDetails);
				String[] particpantDetailsStr = particpantDetails.split(" ");
		    System.out.println(particpantDetailsStr[0]);
            
    		switch(particpantDetailsStr[0]){
       
				case "register":
				     participants.add(new ParticipantDetail(particpantDetailsStr[1],Integer.parseInt(particpantDetailsStr[2]), Integer.parseInt(particpantDetailsStr[3]),socket));
             outputStream.writeObject(incomingMessagePort);
		         System.out.println("Participant id:" + particpantDetailsStr[2] +" added to multicase group");
          break;
          
				case "disconnect":
					tempid = Integer.parseInt(particpantDetailsStr[1]);
					 for(ParticipantDetail p : participants)
					{
						if(p.id == tempid)
						{
							p.status = "InActive";
						}
					}
             System.out.println("Participant id:" + particpantDetailsStr[1] +" disconnected");
           break;
				case "deregister":
					tempid = Integer.parseInt(particpantDetailsStr[1]);
               for(ParticipantDetail p : participants)
					    {
						      if(p.id == tempid)
						      {
							        p.status = "Deregistered";
                      p.socket.close();
						      }
					    }
         System.out.println("Participant id:" + particpantDetailsStr[1] +" deregistered");
         break;
         
         case "reconnect":
         long reconnecttime = System.currentTimeMillis();
         reconnecttime = reconnecttime/1000;
         //td=td*1000;
       	 timediff = reconnecttime - td;
         int tempCount = 0;
         Socket socketTemp = null;
         Stack<String> tempStack = new Stack<>();
         tempid = Integer.parseInt(particpantDetailsStr[1]);
        // System.out.println("The reconnectime is "+reconnecttime);
         //System.out.println("The time difference is "+timediff);
         for(ParticipantDetail p : participants)
         {
        	 if(p.status.equals("InActive") && p.id==tempid)
        	 {
             p.status="Active";
             p.port = Integer.parseInt(particpantDetailsStr[2]);
             //System.out.println("This is before if");
        		 for(long time: removedtime)
        		 {
        			 if(time>=timediff)
        			 {
                // System.out.println("The time variable is"+time);
        				 index = removedtime.indexOf(time);
                 //System.out.println("index is "+index);
        				 //p.status="Active";
                 socket = new Socket(p.IP, p.port);
                 outputStream = new ObjectOutputStream(socket.getOutputStream());
                 String message = removedmessage.get(index);    
                 outputStream.writeObject(message);
        				 //break;
        			 }
        		 }
        		 //iterate through timestamps and check from reconnecttime to timediff for appending messages
        	 }
//        	 for(int i=index;i<removedmessage.size()-1;i++)
//        	 {
//        		 String message = removedmessage.get(i);
//              System.out.println(i);
//        		  socket = new Socket(p.IP, p.port);
//	               outputStream = new ObjectOutputStream(socket.getOutputStream());
//		             outputStream.writeObject(message);
 //               System.out.println(message);
//        	 }
         }
             
          
             
             break;
         
				}
				
				// 0-id 1-IP 2-port
						
		}
			
		 }catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
		}
	}

}
