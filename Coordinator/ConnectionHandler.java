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
	 public static int incomingMessagePort;
   public static int td;
	 public ConnectionHandler(ArrayList<ParticipantDetail> participants, int td, int incomingMessagePort) throws IOException {
		// TODO Auto-generated constructor stub
		
		server = new ServerSocket(serverPort);//server
		this.incomingMessagePort = incomingMessagePort;
     this.participants = participants;
     this.td = td;
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
         int tempCount = 0;
         Socket socketTemp = null;
         Stack<String> tempStack = new Stack<>();
         tempid = Integer.parseInt(particpantDetailsStr[1]);
         for(ParticipantDetail p : participants)
				 {
					  if(p.id == tempid && !p.status.equals("Deregistered"))
					  {
                      
						    
 						  	p.status="Active";
                p.port = Integer.parseInt(particpantDetailsStr[2]);
                System.out.println("Now port " +p.port);
                System.out.println("Participant id:" + particpantDetailsStr[1] +" reconnected in multicase group");
							  //add td seconds of messages
                 while(!p.offlineQueue.isEmpty()){
                   //on reconnecting send all message in offlineQueue of that participant
                    String message = p.offlineQueue.poll();
                    socket = new Socket(p.IP, p.port);
     	               outputStream = new ObjectOutputStream(socket.getOutputStream());
     		             outputStream.writeObject(message);
                     System.out.println(message);
                 
                   }
						  
		  	   }
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
