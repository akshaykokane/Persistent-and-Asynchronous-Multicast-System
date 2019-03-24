 import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Multicast implements Runnable{
	ObjectInputStream inputStream = null;
	int port;
	String serverip="";
	Socket Multicastsocket=null;
//	public Multicast(String serverip,int port) throws UnknownHostException, IOException{
//		this.serverip=serverip;
//		this.port=port;
//		Multicastsocket=new Socket(serverip,port);
//	}
	public Multicast(String serverip2, int port2) throws UnknownHostException, IOException {
		// TODO Auto-generated constructor stub
		this.serverip=serverip;
		this.port=port;
		Multicastsocket=new Socket(serverip,port);
	}
	public void run()
	{
		try{
			inputStream = new ObjectInputStream(Multicastsocket.getInputStream());
			//STORE the message in file
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
