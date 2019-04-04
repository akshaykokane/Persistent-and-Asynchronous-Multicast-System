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
		  Multicast threadB;
			switch(commandAndValue[0]) {
			
				case "register"://participant registers with the coordinator
					socket = new Socket(serverIP, serverPort);		
					outputStream = new  ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());
					threadBPort = Integer.parseInt(commandAndValue[1]);
					participantip = Inet4Address.getLocalHost().getHostAddress();
					outputStream.writeObject(commandAndValue[0] + " "+ participantip + " " + id + " " + commandAndValue[1]);
					outgoingMessagePort = (int)inputStream.readObject();
          System.out.println("Outgoingmessage port is " + outgoingMessagePort);
          threadB = new Multicast(serverIP, threadBPort, fileName);
					threadB.start();
           break;
				case "deregister"://participant deregisters from the coordinator
  		    socket = new Socket(serverIP, serverPort);		
					outputStream = new  ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());
			
          outputStream.writeObject(commandAndValue[0] + " " + id);	
					break;
                
				case "disconnect":
        		socket = new Socket(serverIP, serverPort);		
					outputStream = new  ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());
			
   	      outputStream.writeObject(commandAndValue[0] + " " + id);	
					break;
                
				case "reconnect":
  		     socket = new Socket(serverIP, serverPort);		
					outputStream = new  ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());
					participantip = Inet4Address.getLocalHost().getHostAddress();
					outputStream.writeObject("reconnect " + id);	
					break;
                
				case "msend":
          socket = new Socket(serverIP, outgoingMessagePort);
          outputStream = new  ObjectOutputStream(socket.getOutputStream());
					outputStream.writeObject(input.substring(6));
           Thread.sleep(1000);
					break;
         
			}
					
		}while(!input.equals("deregister"));
			outputStream.close();
			inputStream.close();
			Thread.sleep(1);
		
			
		
		
	}

}

