package weatherhandler.parser;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Supplier;

import weatherhandler.Logger;
import weatherhandler.parser.Parser;
import weatherhandler.parser.server.SocketHandler;
import weatherhandler.processor.Processor;

public class ServerParser implements Parser, AutoCloseable {
    private int port;
    private ServerSocket socket;
    private Supplier<Processor> output;

    private Logger logger = new Logger("Server");

    public ServerParser(int port, Supplier<Processor> output) {
        this.port = port;
        this.output = output;
    }

    public void process() {
        try {
            this.socket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.logger.info("Listening on " + port);

        int clients = 0;
        Socket client;
        while (true) {
            try {
                client = socket.accept();
                Runnable thread = new SocketHandler(client, this.output.get());
                new Thread(thread).start();
                clients++;
                if (clients % 50 == 0) {
                    this.logger.debug("New Client (" + clients + " connected)");
                }
            } catch (IOException e) {
                this.logger.error("Socket error:");
                e.printStackTrace();
            }
        }
    }

    public void close() throws IOException {
        this.socket.close();
    }
}
