// Java implementation of Server side
// It contains two classes : Server and ClientHandler
// Save file as Server.java

import java.io.*;
import java.util.*;
import java.net.*;

// Server class
public class Server
{

	// Vector to store active clients
	static List<MultipleClients> clients = new LinkedList<>();
	
	// counter for clients
	static int numberOfClients = 0;

	public static void main(String[] args) throws IOException
	{
		// server is listening on port 1234
		ServerSocket ss = new ServerSocket(1234);
		
		Socket s;
		
		// running infinite loop for getting
		// client request
		while (true)
		{
			// Accept the incoming request
			s = ss.accept();

			//System.out.println("New client request received : " + s);
			
			// obtain input and output streams
			DataInputStream in = new DataInputStream(s.getInputStream());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			
			//System.out.println("Creating a new handler for this client...");

			String username = in.readUTF();
			System.out.println(username + " connected");

			// Create a new handler object for handling this request.
			MultipleClients mc = new MultipleClients(username, s, in, out);

			// Create a new Thread with this object.
			Thread t = new Thread(mc);

			// add this client to active clients list
			clients.add(mc);

			// start the thread.
			t.start();

			// increment i for new client.
			// i is used for naming only, and can be replaced
			// by any naming scheme
			numberOfClients++;

		}
	}
}

// ClientHandler class
class MultipleClients implements Runnable {
	Scanner scn = new Scanner(System.in);
	public String username;
	private final DataInputStream in;
	private final DataOutputStream out;
	Socket s;
	private boolean login;
	
	// constructor
	public MultipleClients(String username, Socket s, DataInputStream in, DataOutputStream out) {
		this.username = username;
		this.s = s;
		this.in = in;
		this.out = out;
		this.login = true;
	}

	@Override
	public void run() {

		String read = new String();
		while(true)
		{
			try
			{
				// receive the string
				read = in.readUTF();
				
				if(read.equals("Exit")){
					login = false;
					for(MultipleClients mc : Server.clients)
					mc.out.writeUTF(this.username + " desligou");
					
					for(MultipleClients mc : Server.clients) {
						if(mc.username.equals(this.username))
						Server.clients.remove(mc);
					}
					s.close();
					break;
				}
				
				// break the string into message and recipient part
				else if(String.valueOf(read.charAt(0)).equals("-")) {
					String[] line = read.split(" ");
					String user = line[0].substring(1, line[0].length());
					String msg = new String();

					for(int i=1; i<line.length; i++) {
						msg += " " + line[i];
					}
					msg.trim();

					// search for the recipient in the connected devices list.
					// ar is the vector storing client of active users
					for (MultipleClients mc : Server.clients)
					{
						// if the recipient is found, write on its
						// output stream
						if (mc.username.equals(user) && mc.login == true)
						{
							mc.out.writeUTF("-" + this.username + " :" + msg);
							break;
						}
					}
				}

				else if(String.valueOf(read.charAt(0)).equals("+")) {
					String[] line = read.split(" ");
					String msg = new String();

					for(int i=1; i<line.length; i++) {
						msg += " " + line[i];
					}
					msg.trim();

					for(MultipleClients mc : Server.clients) {
						if(!this.username.equals(mc.username))
							mc.out.writeUTF("+" + this.username + " :" + msg);
					}
				}
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
		}
		try
		{
			// closing resources
			this.in.close();
			this.out.close();
			
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}