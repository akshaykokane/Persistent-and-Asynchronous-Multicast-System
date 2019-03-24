import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.net.Inet4Address;
import java.net.UnknownHostException;
public class Participant {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		Socket socket = null;
		ObjectOutputStream outputStream = null;
		ObjectInputStream inputStream = null;
		int Port = Integer.parseInt(args[3]);
		String serverip = args[2];
		Scanner s = new Scanner(System.in);
		String currentWorkingDirectory = "participant>>";
				
		String input = "";		
		String participantip="";
		outputStream = new  ObjectOutputStream(socket.getOutputStream());
		inputStream = new ObjectInputStream(socket.getInputStream());
		do {
			System.out.print(currentWorkingDirectory);
			input = s.nextLine();
			String[] commandAndValue = input.split(" ");
				
			switch(commandAndValue[0]) {
			
				case "register"://participant registers with the coordinator
					socket = new Socket(serverip, Port);
					participantip = Inet4Address.getLocalHost().getHostAddress();
					outputStream.writeObject(args[0]+participantip+commandAndValue[1]);
					Multicast object = new Multicast(serverip,Port);
					Thread thread = new Thread(object);
					//START THREAD HERE		
				case "deregister"://participant deregisters from the coordinator
					socket.close();
					//THREAD will stop here
					break;
				case "disconnect":
					socket.close();
					break;
				case "reconnect":
					socket = new Socket(serverip, Port);
					participantip = Inet4Address.getLocalHost().getHostAddress();
					outputStream.writeObject(args[0]+participantip+commandAndValue[1]);
					//THREAD will START AGAIN			
					break;
				case "msend":
					outputStream.writeObject(commandAndValue[1]);
					break;
				default:
					//Logic yet to be written
			}
					
		}while(!input.equals("deregister"));
			outputStream.close();
			inputStream.close();
			Thread.sleep(1);
		
			
		
		
	}

}

