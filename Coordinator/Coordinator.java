import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.io.*; 
import java.util.*; 

class ParticipantDetail{
	//details of thread-b of participant
	String IP;
	int id;
	int port;
	String status;
	static Socket socket = null;
  static Queue<String> offlineQueue = new LinkedList<>();
 
	public ParticipantDetail(String ip, int id, int port, Socket socket) {
		
		this.IP = ip;
		this.id = id;
		this.port = port;
		this.status ="Active"; //Active, Inactive or End
		this.socket = socket;
   
	}
}
public class Coordinator {

	public static ArrayList<ParticipantDetail> participants = new ArrayList<>();
	public static Queue<String> messageQueue = new LinkedList<>();
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		int incomingMessagePort = 0;
		int td = 0;
    
		List<String> configFile = Files.readAllLines(Paths.get("config.txt"));
		
		incomingMessagePort = Integer.parseInt(configFile.get(0));
		td = Integer.parseInt(configFile.get(1));
		
		//System.out.println(incomingMessagePort);
		//System.out.println(td);
		
		//Thread to accept new connections and making entry in participant table
		ConnectionHandler ch = new ConnectionHandler(participants, td, incomingMessagePort);
		
		//Thread to accept a message from participant
		Reciever r = new Reciever(participants, td, messageQueue, incomingMessagePort);
		
		//Thread to forward message to multicast group
		Forwarder fwd = new Forwarder(participants, td, messageQueue);
		
		r.start();
		fwd.start();
		ch.start();
	
	
		

	}

}
