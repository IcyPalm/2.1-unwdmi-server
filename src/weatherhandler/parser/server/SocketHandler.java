package weatherhandler.parser.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import weatherhandler.parser.XMLParser;
import weatherhandler.processor.Processor;

public class SocketHandler implements Runnable {
    /**
     * Socket receiving data.
     */
    private Socket socket;
    /**
     * Output processor.
     */
    private Processor processor;

    public SocketHandler(Socket socket, Processor processor) {
        this.socket = socket;
        this.processor = processor;
    }

    @Override
    public void run() {
        String line;
        StringBuilder lines = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Read data client sends to server
            while ((line = in.readLine()) != null) {
                lines.append(line);
                // if </WEATHERDATA> is encountered close document and create
                // new Parser.
                if (line.contains("</WEATHERDATA>")) {
                    new XMLParser(lines.toString(), this.processor);
                    lines.setLength(0);
                }
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
