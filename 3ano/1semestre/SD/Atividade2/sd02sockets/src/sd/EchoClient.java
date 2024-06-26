package sd;
import java.io.*;
import java.net.*;

public class EchoClient {

    private String address = null;
    private int sPort = 0;

    public EchoClient(String add, int p) {
        address = add;
        sPort = p;
    }

    public static void main(String[] args) {
        // exigir os argumentos necessarios
        if (args.length < 3) {
            System.err.println("Argumentos insuficientes:  java EchoClient ADDRESS PORT MESSAGE");
            System.exit(1);
        }

        try {
            String addr = args[0];
            int p = Integer.parseInt(args[1]);

            EchoClient cl = new EchoClient(addr, p);

            // ler o texto a enviar ao servidor
            String s = "mensagem_que_deve_vir_como_argumento";

            cl.go(args[2]);   // faz o pretendido
        } catch (Exception e) {
            System.out.println("Problema no formato dos argumentos");
            e.printStackTrace();
        }
    }

    public void go(String msg) {

        // exercicio 1: mostrar a mensagem que vai ser enviada
        System.out.println("MENSAGEM: " + msg);

        // ... exercicio 3
        try {
	  Socket s= new Socket(address, sPort);
	  // ja esta connected
	  
	  // escrever a mensagem?
	  OutputStream socketOut= s.getOutputStream();
          socketOut.write(msg.getBytes());
          
	  InputStream socketIn= s.getInputStream();
          byte[] b = new byte[256];
          int lidos = socketIn.read(b);
          
          String resp = new String(b, 0, lidos);
          System.out.println("MENSAGEM: " + resp);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
    }

}
