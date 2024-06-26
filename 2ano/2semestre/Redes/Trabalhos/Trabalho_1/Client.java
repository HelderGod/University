// Java implementation for multithreaded chat client
// Save file as Client.java

import java.io.*;
import java.net.*;
//import java.util.*;

public class Client
{

	public static void main(String args[]) throws UnknownHostException, IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		// getting localhost ip
		InetAddress ip = InetAddress.getByName("localhost");
		
		// establish the connection
		Socket s = new Socket(ip, 1234);
		
		// obtaining input and out streams
		DataInputStream in = new DataInputStream(s.getInputStream());
		DataOutputStream out = new DataOutputStream(s.getOutputStream());

        System.out.println("Coloque o seu username;");
        String username = br.readLine();
        out.writeUTF(username);

		
		// readMessage thread
		Thread readMessage = new Thread(new Runnable()
		{
			@Override
			public void run() {
				String msg;
				try{
					while ((msg = in.readUTF()) != null) {
							// read the message sent to this client
							System.out.println(msg);
						
					}
				}
				catch(Exception IOException){

				}
			}
		});
		
		readMessage.start();

		while (true) {
			try {
				// read the message to deliver.
				String msg = br.readLine();
				// write on the output stream
				out.writeUTF(msg);
				if(msg.equals("Exit")) {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		in.close();
		out.close();
		s.close();
	}
}