package weatherhandler;

import weatherhandler.data.MeasurementsCache;
import weatherhandler.parser.WeatherParser;
import weatherhandler.processor.Processor;
import weatherhandler.processor.BatchUpdatesProcessor;
import weatherhandler.processor.CompleteMissingProcessor;
import weatherhandler.processor.DBStorageProcessor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class TestServer implements Runnable {
    private Socket socket;
    private ServerSocket TCPsocket;

    @Override
    public void run() {
        try {
            MeasurementsCache.init();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println("Server started");
        try {
            TCPsocket = new ServerSocket(7789);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Processor processor = new BatchUpdatesProcessor(3000,
                new DBStorageProcessor("weather_measurements"));

        int clients = 0;
        while (true) {
            try {
                socket = TCPsocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Runnable thread = new ServerHandler(socket,
                new CompleteMissingProcessor(30, processor));
            new Thread(thread).start();
            clients++;
            if (clients % 50 == 0) {
                String time = new SimpleDateFormat("hh:mm:ss").format(new Date());
                System.out.println("[DEBUG " + time + "] New Client (" + clients + " connected)");
            }
        }
    }

    public void interrupt() {
        try {
            TCPsocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ServerHandler implements Runnable {

    private Socket socket;
    private Processor processor;

    public ServerHandler(Socket socket, Processor processor) {
        this.socket = socket;
        this.processor = processor;
    }

    @Override
    public void run() {
        String line;
        StringBuilder lines = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while ((line = in.readLine()) != null) {
                lines.append(line);
                if (line.contains("</WEATHERDATA>")) {
                    new WeatherParser(lines.toString(), this.processor);
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
