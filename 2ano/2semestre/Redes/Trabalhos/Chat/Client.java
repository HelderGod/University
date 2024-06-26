import java.io.*;
import java.net.*;

public class Client
{

	public static void main(String args[]) throws UnknownHostException, IOException
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));				//lê as mensagens do cliente
		
		InetAddress ip = InetAddress.getByName("localhost");									//recebe o ip do cliente
		
		Socket s = new Socket(ip, 1234);														//cria o socket do cliente e recebe o ip do cliente e a porta à qual se deve ligar
		
		DataInputStream in = new DataInputStream(s.getInputStream());
		DataOutputStream out = new DataOutputStream(s.getOutputStream());						

        System.out.println("Coloque o seu username:");
        String username = br.readLine();														//lê o username especificado pelo cliente
        out.writeUTF(username);																	//envia o username escolhido pelo cliente para o servidor

		Thread readMessage = new Thread(new Runnable()											//thread que serve para receber as mensagens do servidor
		{
			@Override
			public void run() {																
				String msg;
				try{
					while((msg = in.readUTF()) != null)											//recebe a mensagem proveniente do servidor que foi enviada por ouro cliente					
						System.out.println(msg);
				}
				catch(Exception IOException){
				}
			}
		});
		
		readMessage.start();																	//inicializa a thread para que o cliente esteja preparado para receber mensagens

		while (true) {																			//este ciclo serve para enviar as mensagens para o servidor
			try {
				String msg = br.readLine();														//lê a mensagem do cliente da consola/terminal

				out.writeUTF(msg);																//envia a mensagem para o servidor
				if(msg.equals("Exit")) {														//caso a mensagem introduzida seja 'Exit', este encerra o ciclo pois o cliente pretende desconectar-se do servidor
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
}
