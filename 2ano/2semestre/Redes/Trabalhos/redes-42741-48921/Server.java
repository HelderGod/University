import java.io.*;
import java.util.*;
import java.net.*;

public class Server {
    public static List<AllClients> clients;                                     //lista que guarda todos os clientes ligados ao servidor
    public static List<Tag> tags;                                               //lista que guarda todas as Tags criadas ou ainda abertas no servidor
    public static List<TheFile> files;                                          //lista que guarda todos os ficheiros que foram transferidos por clientes para o servidor
    private static ServerSocket ss;                                             //Socket do servidor

    public static void main(String[] args) throws IOException, SocketException{
        ss = new ServerSocket(1234);                           //cria o Socket do servidor na porta 1234
        Socket s;                                                               //Socket do(s) cliente(s) que se irá conectar ao Socket do servidor

        clients = new LinkedList<>();                                           //inicializa a lista de clientes
        tags = new LinkedList<>();                                              //inicializa a lista de Tags
        files = new LinkedList<>();                                             //inicializa a lista de ficheiros
        
        tags.add(new Tag("GLOBAL"));            //adiciona a Tag GLOBAL à lista de Tags, pois nesta Tag estarão todos os clientes conectados ao servidor

        while(true) {
            s = ss.accept();                                                    //o cliente é aceite pelo servidor
            Thread T = new Thread(new MultipleClients(s));                      //inicializa a Thread responsável pelo respetivo cliente
            T.start();                                                          //executa a Thread do cliente
        }
    }
}

class MultipleClients implements Runnable {                                     //esta é a classe que será responsável por ler as mensagens enviadas pelos clientes e executar os mesmos
    private DataInputStream in;
    private DataOutputStream out;
    Socket s;
    String username;

    public MultipleClients(Socket s) {
        this.s = s;
    }

    public static boolean checkUsername(String[] check) {                       //quando o cliente envia o seu username, este método verifica se a mensagem está no formato correto
		return check[0].equals("HELLO");
	}

	public static boolean clientAlreadyExists(String username) {                //método responsável por verificar se já existe um cliente conectado ao servidor com o nickname enviado por outro cliente
		for(AllClients ac : Server.clients) {
			if(username.equals(ac.username))
				return true;
		}
		return false;
	}

	public static void addToGlobal(AllClients ac) {                             //adiciona o cliente à Tag GLOBAL
		for(Tag temp : Server.tags) {
			if(temp.name.equals("GLOBAL")) {
				temp.subscribers.add(ac);
				break;
			}
		}
	}

    // //

    public boolean checkLength(String[] aux, int i) {                           //verifica se a mensagem enviada pelo cliente possui o tamanho mínimo necessário
        return aux.length < i;
    }

    private String getUsername() throws IOException{                            //obtém o username escolhido pelo cliente
        String username = new String();
        while(true) {
            username = in.readUTF();
            username = username.trim();
            String[] check = username.split(" ");
            if(check.length < 2) {
                System.out.println("ERR NICK");
                out.writeBoolean(false);
                continue;
            }
            username = check[1];
            if(checkUsername(check)) {
                if(clientAlreadyExists(username)) {
                    System.out.println("ERR NICK " + username);

                    out.writeBoolean(false);
                }
                else {
                    System.out.println("OK NICK " + username);

                    out.writeBoolean(true);

                    break;
                }
            }
            else {
                System.out.println("ERR NICK " + username);

                out.writeBoolean(false);
            }
        }
        return username;
    }

    public AllClients findClient(String name) {                                 //serve para encontrar o cliente desejado e no caso de não encontrar, devolve null
        AllClients ac = null;
        for(AllClients temp : Server.clients) {
            if(temp.username.equals(name)) {
                ac = temp;
                break;
            }
        }
        return ac;
    }

    public String getMessage(String[] aux) {                                    //obtém a mensagem enviada pelo cliente, já sem o comando escolhido e o destinatário desejado
		String message = new String();
		for(int i=2; i<aux.length; i++)
			message += " " + aux[i];
		message = message.trim();
		return message;
	}

    public void removeSubFromTag(Tag t) {                                     //remove a subscrição do cliente em questão da Tag desejada
        for(AllClients ac : t.subscribers) {
            if(ac.username.equals(this.username))
                t.subscribers.remove(ac);
        }
    }

    public void removeSubFromAllTags() {                                    //remove a subscrição do cliente em questão de todas as Tags onde este se encontre subscrito
        for(Tag t : Server.tags) {
            for(AllClients ac : t.subscribers) {
                if(ac.username.equals(this.username))
                    t.subscribers.remove(ac);
            }
        }
    }

	public Tag findTag(String tagName) {                                    //encontra a tag com o nome dado como argumento e devolve essa mesma tag (caso não exista devolve null)
		Tag t = null;
		for(Tag temp : Server.tags) {
			if(temp.name.equals(tagName))
				t = temp;
		}
		return t;
	}

    public boolean clientAlreadyInTag(Tag t) {                              //verifica se o cliente já é subscrito na tag
		for(AllClients ac : t.subscribers) {
			if(ac.username.equals(this.username))
				return true;
		}
		return false;
	}

    public void addClientToTag(Tag t) {                                     //adiciona o cliente à tag pretendida
		for(AllClients ac : Server.clients) {
			if(ac.username.equals(this.username))
				t.subscribers.add(ac);
		}
	}

    public void messageToSubs(Tag t, String message) throws IOException {               //envia a mensagem para todos os subscritores da tag (excepto para o remetente)
		for(AllClients ac : t.subscribers) {
			if(!this.username.equals(ac.username))
				ac.out.writeUTF("Message: +" + t.name + " -" + this.username +  "-> " + message);
		}
	}

    public void printAllPosts(Tag t) throws IOException {                               //envia todos os posts da tag escolhida para o cliente
		out.writeUTF("+" + t.name);
		int postNum = 0;
		for(Post p : t.posts) {
			postNum++;
            if(p.text.size() == 1)
			    out.writeUTF("Post " + postNum + ": -" + p.user + "-> " + p.text.get(0));
            else {
                if(p.text.size() > 1) {
                    String message = "Post " + postNum + ": -" + p.user + "-> " + p.text.get(0);
                    out.writeUTF(message);
                    for(int i=1; i<p.text.size(); i++)
                        out.writeUTF(p.text.get(i));
                }
            }
		}
        printAllFileNames(t, postNum);
	}

    public void printAllFileNames(Tag t, int postNum) throws IOException {          //permite enviar o nome do ficheiro que esteja na tag como post
        for(Post p : t.posts) {
            if(p.postFiles[0] != null) {
                postNum++;
                out.writeUTF("Post " + postNum + ": -" + p.postFiles[0].user + "-> " + p.postFiles[0].fileName);
            }
        }
    }

    public void printAllTags() throws IOException {                                 //envia para o cliente o nome de todas as tags ativas no servidor
		for(Tag t : Server.tags)
			out.writeUTF("+" + t.name + ": nº de subs-> " + t.subscribers.size());
	}

    public void printAllUsers() throws IOException {                                //envia para o cliente o username de todos os clientes ativos no servidor
		for(AllClients ac : Server.clients) {
			out.writeUTF("-" + ac.username);
		}
	}

    public void printAllSubs(Tag t) throws IOException {                            //envia para o cliente o username de todos os subscritores de uma tag 
		out.writeUTF("+" + t.name);
		for(AllClients ac : t.subscribers)
			out.writeUTF("-" + ac.username);
	}

    public void receiveFile(String fileName, AllClients ac, Tag t) throws IOException{          //permite receber o ficheiro de um cliente através de um array de bytes
        try {
            int bytes = 0;
            FileOutputStream fos = new FileOutputStream("files/" + fileName);

            long size = in.readLong();
            byte[] buffer = new byte[256];
            while(size > 0 && (bytes = in.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
                fos.write(buffer, 0, bytes);
                size -= bytes;
            }
            TheFile f = new TheFile(buffer, this.username, fileName);
            Server.files.add(f);
            fos.close();
            if(ac != null) {
                sendFile(fileName, ac);
            }
            else if(t != null) {
                Post p = new Post(this.username);
                p.addText(fileName);
                p.addFile(f);
                t.posts.add(p);
            }

        } catch(FileNotFoundException e) {
            System.out.println("ERR FILE " + this.username);
        }
    }

    private static void sendFile(String fileName, AllClients ac) throws IOException {           //envia para o cliente desejado o array de bytes que contém o conteúdo do ficheiro pretendido
		int bytes = 0;
		File f = new File(fileName);
		FileInputStream fis = new FileInputStream(f);
		
		ac.out.writeLong(f.length());

		byte[] buffer = new byte[256];
		while((bytes = fis.read(buffer)) != -1) {
			ac.out.write(buffer, 0, bytes);
			ac.out.flush();
		}
		fis.close();
	}

    @Override
    public void run(){                                                                      //recorrendo ao uso de threads, recebe as mensagens dos clientes, executa o comando pretendido pelo cliente e, se necessário, envia mensagens para o(s) cliente(s) (consoante o comando executado)
        try {
            in = new DataInputStream(s.getInputStream());
            out = new DataOutputStream(s.getOutputStream());

            username = getUsername();
            AllClients temp = new AllClients(username, in, out, s);
            addToGlobal(temp);
            Server.clients.add(temp);

            String read = new String();
            while(true) {
               read = in.readUTF();
               read = read.trim();
               String[] aux = read.split(" ");

               if(read.equals("EXIT")) {
                   for(AllClients ac : Server.clients) {
                       if(!ac.username.equals(this.username))
                            ac.out.writeUTF(this.username + " desligou");
                        else
                            Server.clients.remove(ac);
                   }
                   removeSubFromAllTags();
                   System.out.println("EXIT NICK " + this.username);
                   this.s.close();
                   break;
               }

               else if(aux[0].equals("MSG")) {
                   if(checkLength(aux, 2))
                       System.out.println("ERR MSG " + this.username);
                   else {
                       Tag t = findTag(aux[1]);
                       String message = getMessage(aux);

                       if(t != null) {
                           if(clientAlreadyInTag(t)) {
                                messageToSubs(t, message);
                                System.out.println("OK MSG " + aux[1] + " " + this.username);
                           }
                           else
                                System.out.println("ERR MSG " + aux[1] + " " + this.username);
                       }

                       else {
                           AllClients ac = findClient(aux[1]);
                           if(ac != null) {
                               ac.out.writeUTF("Message: -" + this.username + "-> " + message);
                               System.out.println("OK MSG " + this.username);
                           }
                           else
                                System.out.println("ERR MSG " + this.username);
                       }
                   }
               }

               else if(aux[0].equals("POST")) {
                   if(checkLength(aux, 3))
                        System.out.println("ERR POST " + this.username);
                    
                    else {
                        Tag t = findTag(aux[1]);
                        String message = getMessage(aux);

                        if(t != null) {
                            if(clientAlreadyInTag(t)) {
                                Post p = new Post(this.username);
                                p.addText(message);
                                t.posts.add(p);
                                System.out.println("OK POST " + aux[1] + " " + this.username);
                            }
                            else
                                System.out.println("ERR POST " + aux[1] + " " + this.username);
                        }

                        else
                            System.out.println("ERR POST " + this.username);

                    }
               }

               else if(aux[0].equals("MULTIPOST")) {
                   if(checkLength(aux, 3))
                        System.out.println("ERR MULTIPOST " + this.username);
                    else {
                        Tag t = findTag(aux[1]);
                        if(t != null) {
                            if(clientAlreadyInTag(t)) {
                                String message = getMessage(aux);
                                Post p = new Post(this.username);
                                p.addText(message);
                                do {
                                    read = in.readUTF();
                                    read = read.trim();
                                    if(!read.equals("END"))
                                        p.addText(read);
                                } while (!read.equals("END"));
                                t.posts.add(p);
                                System.out.println("OK MULTIPOST " + aux[1] + " " + this.username);
                            }
                            else
                                System.out.println("ERR MULTIPOST " + aux[1] + " " + this.username);
                        }
                        else
                            System.out.println("ERR MULTIPOST " + aux[1] + " " + this.username);
                    }
               }

               else if(aux[0].equals("READ")) {
                   if(checkLength(aux, 2))
                        System.out.println("ERR READ " + this.username);
                    
                    else {
                        Tag t = findTag(aux[1]);
                        if(t != null) {
                            if(clientAlreadyInTag(t)) {
                                printAllPosts(t);

                                System.out.println("OK READ " + aux[1] + " " + this.username);
                            }
                            else
                                System.out.println("ERR READ " + aux[1] + " " + this.username);
                        }
                        else
                            System.out.println("ERR READ " + this.username);
                    }
               }

               else if(aux[0].equals("SUB")) {
                   if(checkLength(aux, 2))
                        System.out.println("ERR SUB " + this.username);
                    
                    else {
                        Tag t = findTag(aux[1]);

                        if(t != null) {
                            if(!clientAlreadyInTag(t)) {
                                addClientToTag(t);
                                System.out.println("OK SUB " + aux[1] + " " + this.username);
                            }
                            else
                                System.out.println("ERR SUB " + aux[1] + " " + this.username);
                        }
                        else
                            System.out.println("ERR SUB " + this.username);
                    }
               }

               else if(aux[0].equals("CREATE")) {
                   if(checkLength(aux, 2))
                        System.out.println("ERR CREATE " + this.username);
                    
                    else {
                        Tag t = findTag(aux[1]);
                        if(t == null) {
                            t = new Tag(aux[1]);
                            Server.tags.add(t);
                            addClientToTag(t);
                            System.out.println("OK CREATE " + aux[1] + " " + this.username);
                        }
                        else
                            System.out.println("ERR CREATE " + aux[1] + " " + this.username);
                    }
               }

               else if(aux[0].equals("SHOW")) {
                   if(checkLength(aux, 2))
                        System.out.println("ERR SHOW " + this.username);
                    else {
                        if(aux[1].equals("TAGS")) {
                            printAllTags();
                            System.out.println("OK SHOW TAGS " + this.username);
                        }

                        else if(aux[1].equals("USERS")) {
                            printAllUsers();
                            System.out.println("OK SHOW USERS " + this.username);
                        }

                        else {
                            Tag t = findTag(aux[1]);
                            if(t != null) {
                                printAllSubs(t);
                                System.out.println("OK SHOW " + aux[1] + " " + this.username);
                            }
                            else
                                System.out.println("ERR SHOW " + aux[1] + " " + this.username);
                        }
                    }
               }

               else if(aux[0].equals("COUNT")) {
                   if(checkLength(aux, 2))
                        System.out.println("ERR COUNT " + this.username);

                    else {
                        if(aux[1].equals("TAGS")) {
                            out.writeUTF("Nº de Tags: " + Server.tags.size());
                            System.out.println("OK COUNT TAGS " + this.username);
                        }
                        else if(aux[1].equals("USERS")) {
                            out.writeUTF("Nº de utilizadores: " + Server.clients.size());
                            System.out.println("OK COUNT USERS " + this.username);
                        }
                        else {
                            Tag t = findTag(aux[1]);
                            if(t != null) {
                                out.writeUTF("+" + t.name + " Nº de subscritores: " + t.subscribers.size());
                                System.out.println("OK COUNT " + aux[1] + " SUBS " + this.username);
                            }
                            else
                                System.out.println("ERR COUNT " + aux[1] + " " + this.username);
                        }
                    }
               }

               else if(aux[0].equals("UNSUB")) {
                   if(checkLength(aux, 2))
                        System.out.println("ERR UNSUB " + this.username);
                    else {
                        Tag t = findTag(aux[1]);
                        if(t != null) {
                            if(clientAlreadyInTag(t)) {
                                if(t.name.equals("GLOBAL"))
                                    System.out.println("ERR UNSUB GLOBAL " + this.username);
                                else {
                                    removeSubFromTag(t);
                                    System.out.println("OK UNSUB " + aux[1] + " " + this.username);
                                }
                            }
                            else
                                System.out.println("ERR UNSUB " + aux[1] + " " + this.username);
                        }
                        else
                            System.out.println("ERR UNSUB " + aux[1] + " " + this.username);
                    }
               }

               else if(aux[0].equals("FILE")) {
                   if(checkLength(aux, 4))
                        System.out.println("ERR FILE " + this.username);
                    else {
                        AllClients ac = findClient(aux[1]);
                        if(ac != null) {
                            receiveFile(aux[2], ac, null);
                            ac.out.writeUTF("FILE " + aux[2]);
                            sendFile(aux[2], ac);
                            System.out.println("OK FILE " + aux[1] + " " + this.username);
                        }
                        else {
                            Tag t = findTag(aux[1]);
                            if(t != null) {
                                if(clientAlreadyInTag(t)) {
                                    receiveFile(aux[2], null, t);
                                    System.out.println("OK FILE " + aux[1] + " " + this.username);
                                }
                                else
                                    System.out.println("ERR FILE " + aux[1] + " " + this.username);
                            }
                            else
                                System.out.println("ERR FILE " + this.username);
                        }
                    }
               }

               else if(aux[0].equals("DOWNLOAD")) {
                   if(checkLength(aux, 2)) {
                       System.out.println("ERR DOWNLOAD " + this.username);
                   }
                   else {
                       AllClients ac = findClient(this.username);
                       Tag t = null;
                       for(Tag a : Server.tags) {
                           for(Post p : a.posts) {
                               if(p.postFiles[0] == null)
                                    continue;
                               if(p.postFiles[0].fileName.equals(aux[1])) {
                                   t = a;
                                   break;
                               }
                           }
                       }
                       if(t != null) {
                           sendFile(aux[1], ac);
                           System.out.println("OK DOWNLOAD " + this.username);
                       }
                       else
                            System.out.println("ERR DOWNLOAD " + this.username);
                   }
               }

               else
                   System.out.println("ERR MESSAGE FORMAT " + this.username);
            }
        } catch(IOException e) {
        }

        try {
            this.in.close();
            this.out.close();
        } catch(IOException e) {
        }
    }
}

class AllClients {                                                  //classe responsável por guardar as informações de cada cliente (username, socket e recursos de envio e receção de mensagens)
    String username;
    public final DataInputStream in;
    public final DataOutputStream out;
    Socket s;

    public AllClients(String username, DataInputStream in, DataOutputStream out, Socket s) {
        this.username = username;
        this.in = in;
        this.out = out;
        this.s = s;
    }
}

class Tag {                                                     //guarda todas as informações de cada tag presente no servidor (nome da tag, os posts e os subscritores)
    String name;                                                //nome da tag
    List<Post> posts;                                           //lista responsável por guardar todos os posts de determinada tag
    List<AllClients> subscribers;                               //lista que guarda as informações de todos os subscritores

    public Tag(String name) {
        this.name = name;
        posts = new LinkedList<>();
        subscribers = new LinkedList<>();
    }
}

class Post {                                                    //classe responsável por guardar as informações cada post das tags(mensagem, remetente e, no caso de ser ficheiro, o próprio ficheiro)
    List<String> text;                                          //lista que guarda a mensagem de cada post (usamos lista de forma a implementar os multiposts)
    String user;                                                //remetente do post
    TheFile[] postFiles;                                        //se for ficheiro, permite guardar as informações do ficheiro

    public Post(String user) {
        text = new LinkedList<>();
        this.user = user;
        postFiles = new TheFile[1];
        postFiles[0] = null;
    }

    public void addText(String message) {                       //permite adicionar uma linha de mensagem à lista text
        this.text.add(message);
    }

    public void addFile(TheFile f) {                            //permite adicionar as informações do ficheiro
        postFiles[0] = f;
    }
}

class TheFile {                                                 //permite guardar as informações acerca de ficheiros
    byte[] buffer;                                              //guarda o conteúdo do ficheiro, transformado num array de bytes
    String user;                                                //remetente do ficheiro
    String fileName;                                            //nome do ficheiro

    public TheFile(byte[] buffer, String user, String fileName) {
        this.buffer = buffer;
        this.user = user;
        this.fileName = fileName;
    }
}