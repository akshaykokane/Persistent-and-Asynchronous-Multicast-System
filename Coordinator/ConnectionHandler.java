import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionHandler extends Thread{

	 private static ServerSocket server;
	 private static int serverPort = 6600;
	 static int count = 0;
	 static Socket socket = null;
	 public static ArrayList<ParticipantDetail> participants = new ArrayList<>();
	 public static int incomingMessagePort;
  
	 public ConnectionHandler(ArrayList<ParticipantDetail> participants, int incomingMessagePort) throws IOException {
		// TODO Auto-generated constructor stub
		
		server = new ServerSocket(serverPort);//server
		this.incomingMessagePort = incomingMessagePort;
     this.participants = participants;
	}
	 
	 @Override
		public void run() {
		 	try {
		 		
		 		System.out.println("Server Started..");
			
			// TODO Auto-generated method stub
			while(true) {
				
		 		socket = server.accept();
				count++; 
				System.out.println("Participant" + count +"connected");
				ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
				String particpantDetails = (String) inputStream.readObject();
				String[] particpantDetailsStr = particpantDetails.split(" ");
				participants.add(new ParticipantDetail(particpantDetailsStr[0],Integer.parseInt(particpantDetailsStr[1]), Integer.parseInt(particpantDetailsStr[2]),socket));
				// 0-id 1-IP 2-port
				outputStream.writeObject(incomingMessagePort);
				System.out.println("Participant id:" + particpantDetailsStr[1] +" added to multicase group");
		
		}
			
		 }catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
		}
	}

}
