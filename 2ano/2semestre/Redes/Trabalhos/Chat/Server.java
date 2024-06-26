import java.io.*;
import java.util.*;
import java.net.*;

public class Server
{
	static List<MultipleClients> clients = new LinkedList<>(); 		//lista para guardar todos os clientes que se conectem ao servidor
	
	static int numberOfClients = 0; 								//contar o número de clientes conectados ao servidor

	public static void main(String[] args) throws IOException
	{
		ServerSocket ss = new ServerSocket(1234); 					//cria o socket do servidor
		
		Socket s;
		
		while (true)
		{
			s = ss.accept();														//aceita o cliente

			DataInputStream in = new DataInputStream(s.getInputStream());			//serve para receber as mensagens dos clientes
			DataOutputStream out = new DataOutputStream(s.getOutputStream());		//serve para enviar as mensagens para os clientes

			String username = in.readUTF();											//recebe o nome de utilizador escolhido pelo cliente
			System.out.println(username + " connected");							

			MultipleClients mc = new MultipleClients(username, s, in, out);

			Thread t = new Thread(mc);												//inicializa a thread para cada cliente no servidor

			clients.add(mc);														//adiciona o cliente à lista

			t.start();

			numberOfClients++;
		}
	}
}

class MultipleClients implements Runnable {
	public String username;
	private final DataInputStream in;
	private final DataOutputStream out;
	Socket s;
	
	public MultipleClients(String username, Socket s, DataInputStream in, DataOutputStream out) {		//construtor para preparar o cliente a receber e enviar mensagens no servidor
		this.username = username;
		this.s = s;
		this.in = in;
		this.out = out;
	}

	@Override
	public void run() {

		String read = new String();
		while(true)
		{
			try
			{
				read = in.readUTF();											//recebe a mensagem enviada pelo cliente
				
				if(read.equals("Exit")){										//desconecta o cliente do servidor
					for(MultipleClients mc : Server.clients)					//envia uma mensagem a todos os restantes clientes a informar que o cliente se desconectou
						mc.out.writeUTF(this.username + " desligou");
					
					for(MultipleClients mc : Server.clients) {
						if(mc.username.equals(this.username))					//serve para procurar o cliente na lista e removê-lo da mesma		
						Server.clients.remove(mc);
					}
					Server.numberOfClients--;
					System.out.println(this.username + " desconectou-se");
					this.s.close();												//encerra o socket do cliente em específico
					break;
				}
				
				else if(String.valueOf(read.charAt(0)).equals("-")) {			//envia a mensagem para o cliente especificado pelo remetente
					String[] line = read.split(" ");							//parte a mensagem para separar o nome de utilizador da restante mensagem
					String user = line[0].substring(1, line[0].length());		//ignora o '-' para ficar só com o nome do utilizador especificado, de forma a facilitar a procura do mesmo
					String msg = new String();

					for(int i=1; i<line.length; i++) {							//serve para voltar a unir a mensagem
						msg += " " + line[i];									
					}
					msg.trim();													//retira espaços que possam estar atrás ou na frente da mensagem

					for (MultipleClients mc : Server.clients)	
					{
						if (mc.username.equals(user)) {							//encontra o utilizador pretendido para receber a mensagem
							mc.out.writeUTF("-" + this.username + " :" + msg);
							break;
						}
					}
				}

				else if(String.valueOf(read.charAt(0)).equals("+")) {			//envia a mensagem para todos os restantes utilizadores
					String[] line = read.split(" ");
					String msg = new String();

					for(int i=1; i<line.length; i++) {
						msg += " " + line[i];
					}
					msg.trim();

					for(MultipleClients mc : Server.clients) {
						if(!this.username.equals(mc.username))					//envia a mensagem para todos os utilizadores excepto para o remetente
							mc.out.writeUTF("+" + this.username + " :" + msg);
					}
				}

				else if(read.equals("#count")) {								//envia o número de utilizadores atualmente conectados ao servidor
					for(MultipleClients mc : Server.clients) {
						if(this.username.equals(mc.username)) {
							mc.out.writeUTF("Número de clientes conectados: " + Server.numberOfClients);
							break;
						}
					}
				}

				else if(read.equals("#show")) {									//envia os utilizadores que ainda se encontram conectados ao servidor
					for(MultipleClients mc : Server.clients) {
						if(this.username.equals(mc.username)) {
							mc.out.writeUTF("Clientes conectados:");
							for(MultipleClients a : Server.clients)
								mc.out.writeUTF(a.username);
							break;
						}
					}
				}

				else {															//se a mensagem não possuir o formato correto, envia ao remetente da mensagem essa mesma informação
					for(MultipleClients mc : Server.clients) {
						if(this.username.equals(mc.username)) {
							mc.out.writeUTF("Formato incorreto de mensagem!");
							break;
						}
					}
				}
			} catch (IOException e) {
			}
			
		}
																			//encerra os recursos de receber e enviar mensagens
		try																	
		{
			this.in.close();
			this.out.close();
			
		} catch(IOException e){
		}
	}
}
