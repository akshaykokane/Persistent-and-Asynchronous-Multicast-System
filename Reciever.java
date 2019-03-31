import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;

public class Reciever extends Thread {
	ArrayList<ParticipantDetail> participants;
	int td;
	Queue<String> messageQueue;
	 private static ServerSocket server;
	 static Socket socket = null;
	public Reciever(ArrayList<ParticipantDetail> participants, int td, Queue<String> messageQueue, int incomingMessagePort) throws IOException {
		// TODO Auto-generated constructor stub
		System.out.println("In reciever constructor");
		this.participants = participants;
		this.td = td;
		this.messageQueue = messageQueue;
		server = new ServerSocket(incomingMessagePort);//waiting to recieve message
	}
	
	 @Override
		public void run() {
		try {
		 while(true) {
			 
			 while(participants.size() == 0){
            Thread.sleep(1000);
             System.out.println("Waiting for participant");
				//		
			 }
			 socket = server.accept();
			 System.out.println("Particpant Connected");
			 ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			 String message = (String) inputStream.readObject();
			 System.out.println("Message Recieved - > " + message);
			 messageQueue.add(message);
			 System.out.println("Message Added in messageQueue and ready to multicast");
			 socket.close();
		 }
		
	}catch (InterruptedException | IOException | ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}
}
