package sd;

import java.io.*;
import java.net.*;

public class EchoServer {

    private int serverPort;

    public EchoServer(int p) {
        serverPort = p;
    }

    public static void main(String[] args) {
        System.err.println("SERVER...");
        if (args.length < 1) {
            System.err.println("Missing parameter: port number");
            System.exit(1);
        }
        int p = 0;
        try {
            p = Integer.parseInt(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }

        EchoServer serv = new EchoServer(p);
        serv.servico();   // activa o servico
    }

    // activa o servidor no porto indicado em "serverPort"
    public void servico() {
        try {
            ServerSocket ss = new ServerSocket(serverPort);
            // ciclo de atendimento do serviço
            while(true) {
                // esperar por ligações
                Socket s = ss.accept();
                
                InputStream socketIn= s.getInputStream();
                byte[] b = new byte[256];
                int lidos = socketIn.read(b);
          
                String pedido = new String(b, 0, lidos);
                System.out.println("DO CLIENTE: " + pedido);
                
                // escrever a resposta
                OutputStream socketOut = s.getOutputStream();
                socketOut.write(b, 0, lidos);
                
                // terminar a ligação com o cliente
                s.close();
                
                if(pedido.equals("GOODBYE"))
                    break;
            }
            // terminar o serviço
            
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
