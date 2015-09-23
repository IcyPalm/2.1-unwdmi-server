package weatherhandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer implements Runnable {
    private Socket socket;
    private ServerSocket TCPsocket;
    
    @Override
    public void run() {
        System.out.println("Server started");
        try {
            TCPsocket = new ServerSocket(7789);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true){
            try {
                socket = TCPsocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ServerHandler handler = new ServerHandler(socket);
            handler.run();
            System.out.println("New Client");
        }
    }
    
    public void interrupt(){
        try {
            TCPsocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


class ServerHandler implements Runnable {

    private Socket socket;
    
    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String line;
        StringBuffer lines = new StringBuffer() ;
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
            while( (line = in.readLine()) != null){
                if(line =="<MEASUREMENT>\n")System.out.println("HOERA!");
                lines.append(line);
                new WeatherParser(lines.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();

           // Close connection
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }

}
