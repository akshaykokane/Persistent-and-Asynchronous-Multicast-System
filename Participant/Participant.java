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
		
		List<String> configFile = Files.readAllLines(Paths.get(args[0]));
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
		boolean disconnected = false;
    boolean registered = false;
  Multicast threadB = null;
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
					outputStream.writeObject(commandAndValue[0] + " "+ participantip + " " + id + " " + commandAndValue[1]);
					outgoingMessagePort = (int)inputStream.readObject();
          System.out.println("Outgoingmessage port is " + outgoingMessagePort);
          threadB = new Multicast(serverIP, threadBPort, fileName);
					threadB.start();
          registered = true;
           break;
				case "deregister"://participant deregisters from the coordinator
          if(registered){
  		    socket = new Socket(serverIP, serverPort);		
					outputStream = new  ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());
			    outgoingMessagePort = 0;
          outputStream.writeObject(commandAndValue[0] + " " + id);
					registered = false;
          threadB.close();
           threadB.interrupt();
           outputStream.close();
           inputStream.close();
           socket.close();
 
         }
         else
           System.out.println("Please note that you are not registerted in any multicast group. Thank you");
          break;
                
				case "disconnect":
        if(registered){
        		socket = new Socket(serverIP, serverPort);		
					outputStream = new  ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());
			
   	      outputStream.writeObject(commandAndValue[0] + " " + id);	
                disconnected = true;
        
        }
        else
         	System.out.println("Sorry, You cannot disconnect as you are not registerted with any multicast group.");
        
					break;
                
				case "reconnect":
        if(disconnected){
          socket = new Socket(serverIP, serverPort);		
					outputStream = new  ObjectOutputStream(socket.getOutputStream());
					inputStream = new ObjectInputStream(socket.getInputStream());
					participantip = Inet4Address.getLocalHost().getHostAddress();
					outputStream.writeObject("reconnect " + id + " "+ commandAndValue[1]);
          threadBPort = Integer.parseInt(commandAndValue[1]);
          threadB = new Multicast(serverIP, threadBPort, fileName);
          threadB.start();
          disconnected = false;
        }
        else
  		     	System.out.println("You are either already connected to multicast group or you are not part of any multicast group");
					break;
                
				case "msend":
        if(registered && !disconnected){
           socket = new Socket(serverIP, outgoingMessagePort);
          outputStream = new  ObjectOutputStream(socket.getOutputStream());
					outputStream.writeObject(input.substring(6));
           Thread.sleep(10);
				
        }
        else if (!registered){
        
          System.out.println("Please note that you are not registerted in any multicast group. Please register first to send message. Thank you");
        }
        else if (disconnected)
          System.out.println("Please note that you disconnected from multicast group. Please reconnect first to send message. Thank you");
        	break;
        
         
			}
					
		}while(!input.equals("exit"));
   System.out.println("Good Bye!");
			Thread.sleep(1);
		
			
		
		
	}

}

