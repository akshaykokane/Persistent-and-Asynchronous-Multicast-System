import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Participant {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		
		List<String> configFile = Files.readAllLines(Paths.get("config.txt"));
		Socket socket = null;
		ObjectOutputStream outputStream = null;
		ObjectInputStream inputStream = null;
		
		int id = Integer.parseInt(configFile.get(0));
		String fileName = configFile.get(1);
		String serverIP = configFile.get(2).split(" ")[0];
		int serverPort = Integer.parseInt(configFile.get(2).split(" ")[1]);
	  int outgoingMessagePort = 0;
		int threadBPort = 0;
		
		Scanner s = new Scanner(System.in);
		String currentWorkingDirectory = "participant >>";
				
		String input = "";		
		String participantip="";
		
		do {
			System.out.print(currentWorkingDirectory);
			input = s.nextLine();
			String[] commandAndValue = input.split(" ");
				
			switch(commandAndValue[0]) {
			
				case "register"://participant registers with the coordinator
					socket = new Socket(serverIP, serverPort);
					
					outputStream = new  ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());
					threadBPort = Integer.parseInt(commandAndValue[1]);
					participantip = Inet4Address.getLocalHost().getHostAddress();
					outputStream.writeObject(participantip+" "+id+" "+commandAndValue[1]);
					outgoingMessagePort = (int)inputStream.readObject();
          System.out.println("Outgoingmessage port is " + outgoingMessagePort);
			    Multicast threadB = new Multicast(serverIP, threadBPort, fileName);
					threadB.start();
					//Thread thread = new Thread(object);
					//START THREAD HERE		
				case "deregister"://participant deregisters from the coordinator
					socket = new Socket(serverIP, serverPort);
					outputStream = new  ObjectOutputStream(socket.getOutputStream());
					outputStream.writeObject(commandAndValue[0] + id);
					socket.close();
					//THREAD will stop here
					break;
				case "disconnect":
					socket = new Socket(serverIP, serverPort);
					outputStream = new  ObjectOutputStream(socket.getOutputStream());
					outputStream.writeObject(commandAndValue[0] + id);
					socket.close();
					break;
				case "reconnect":
					//socket = new Socket(serverip, Port);
					participantip = Inet4Address.getLocalHost().getHostAddress();
					outputStream.writeObject(args[0]+participantip+commandAndValue[1]);
					//THREAD will START AGAIN			
					break;
				case "msend":
          socket = new Socket(serverIP, outgoingMessagePort);
          outputStream = new  ObjectOutputStream(socket.getOutputStream());
					outputStream.writeObject(commandAndValue[1]);
           Thread.sleep(1000);
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

