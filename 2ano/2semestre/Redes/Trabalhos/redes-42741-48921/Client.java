import java.io.*;
import java.net.*;
import java.util.*;

public class Client
{
	private static DataInputStream in;
	private static DataOutputStream out;

	public static List<ClientFile> files;
	public static void main(String args[]) throws UnknownHostException, IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));				//lê as mensagens do cliente
		
		InetAddress ip = InetAddress.getByName("localhost");									//recebe o ip do cliente
		
		Socket s = new Socket(ip, 1234);														//cria o socket do cliente e recebe o ip do cliente e a porta à qual se deve ligar
		
		in = new DataInputStream(s.getInputStream());
		out = new DataOutputStream(s.getOutputStream());						
		while(true) {
        	System.out.println("Coloque o seu username:");
        	String username = br.readLine();														//lê o username especificado pelo cliente
        	out.writeUTF(username);																	//envia o username escolhido pelo cliente para o servidor
			boolean possibleUser = in.readBoolean();
			if(possibleUser == true)
				break;
		}
		Thread readMessage = new Thread(new Runnable()											//thread que serve para receber as mensagens do servidor
		{
			@Override
			public void run() {																
				String msg;
				try{
					while((msg = in.readUTF()) != null) {
						if(msg.startsWith("FILE")) {
							String[] aux = msg.split(" ");
							receiveFileFromServer(aux[1]);
						}
						else
							System.out.println(msg);
					}
				}
				catch(Exception IOException){
				}
			}
		});
		
		readMessage.start();																	//inicializa a thread para que o cliente esteja preparado para receber mensagens

		while (true) {																			//este ciclo serve para enviar as mensagens para o servidor
			try {
				String msg = br.readLine();												//lê a mensagem do cliente da consola/terminal
				out.writeUTF(msg);														//envia a mensagem para o servidor
				msg = msg.trim();														
				if(msg.startsWith("FILE")) {
					String[] temp = msg.split(" ");
					sendFileToServer(temp[2]);
				}
				else if(msg.startsWith("DOWNLOAD")) {
					String[] temp = msg.split(" ");
					receiveFileFromServer(temp[1]);
				}
				else if(msg.equals("EXIT")) {														//caso a mensagem introduzida seja 'Exit', este encerra o ciclo pois o cliente pretende desconectar-se do servidor
					break;
				}
			} catch (IOException e) {
			}
		}
																								//quando o cliente se desconecta, encerra os recursos necessários para enviar e receber mensagens e também o socket do cliente
		in.close();
		out.close();
		s.close();
	}

	private static void sendFileToServer(String fileName) throws IOException {
		int bytes = 0;
		File f = new File(fileName);
		FileInputStream fis = new FileInputStream(f);
		
		out.writeLong(f.length());

		byte[] buffer = new byte[256];
		while((bytes = fis.read(buffer)) != -1) {
			out.write(buffer, 0, bytes);
			out.flush();
		}
		fis.close();
	}

	private static void receiveFileFromServer(String fileName) throws IOException{
		try {
			int bytes = 0;
			FileOutputStream fos = new FileOutputStream(fileName);

			long size = in.readLong();
			byte[] buffer = new byte[256];
			while(size > 0 && (bytes = in.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
				fos.write(buffer, 0, bytes);
				size -= bytes;
			}
			fos.close();
			ClientFile cf = new ClientFile(buffer, fileName);
			Client.files.add(cf);
			
		} catch(FileNotFoundException e) {
		}
	}
}

class ClientFile {
	public byte[] buffer;
	String name;

	public ClientFile(byte[] buffer, String name) {
		this.buffer = buffer;
		this.name = name;
	}
}