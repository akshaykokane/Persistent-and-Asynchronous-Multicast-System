import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Queue;
 import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.*; 
import java.util.*; 

public class Reciever extends Thread {
	ArrayList<ParticipantDetail> participants;
	int td;
 int count = 0;
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
          //   System.out.println("Waiting for participant");
				//		
			 }
			 socket = server.accept();
			 System.out.println("Particpant Connected");
			 ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
			 String message = (String) inputStream.readObject();
			 System.out.println("Message Recieved - > " + message);
			 messageQueue.add(message);
			 System.out.println("Message Added in messageQueue and ready to multicast");
       count++;
       writeMessage2LogFile(message);
   
			 socket.close();
		 }
		
	}catch (InterruptedException | IOException | ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

}

public void writeMessage2LogFile(String message){
 		
   BufferedWriter bw = null;
		FileWriter fw = null;
		try{
    		File file = new File("messageLog.txt");
        FileWriter fr = new FileWriter(file, true);
        BufferedWriter br = new BufferedWriter(fr);
        PrintWriter pr = new PrintWriter(br);
        pr.println(message);
        pr.close();
        br.close();
        fr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
   
   }
   
}
