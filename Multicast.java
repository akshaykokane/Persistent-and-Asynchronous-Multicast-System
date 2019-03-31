 import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.io.*;

public class Multicast extends Thread{
	ObjectInputStream inputStream = null;
	int threadBPort;
	String serverip="";
	Socket MulticastSocket=null;
	String filename;
  private static ServerSocket server;
  
	public Multicast(String serverIp, int threadBPort, String fileName) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
		this.serverip = serverIp;
		this.threadBPort = threadBPort;
    this.filename = fileName;
    server = new ServerSocket(threadBPort);//waiting to recieve message
	}
	public void run()
	{
 try{
    while(true){
    
      MulticastSocket = server.accept();
      
      inputStream = new ObjectInputStream(MulticastSocket.getInputStream());
      String message = (String)inputStream.readObject();
      System.out.println("There is an incoming message. Writing message " + message + " to output file");
      writeMessage2File(message, filename);
    }
 }catch(ClassNotFoundException | IOException e){
   e.printStackTrace();
 }
    

	}
 
   public void writeMessage2File(String message, String fileName){
 		
   BufferedWriter bw = null;
		FileWriter fw = null;
		try{
		File file = new File(fileName);
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
