package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private String name;

    public Client(String name) {
        this.name = name;
        try {
            //DESKTOP-E8FKEGM/192.168.56.1
            client = new Socket("192.168.56.1", 8080);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        }
        catch (IOException e) {
            shutdown();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void shutdown() {
        try {
            if (!client.isClosed()) {
                client.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            out.println(name);
            out.println("Hallo");
            String message;
            while ((message = in.readLine()) != null) {
                out.println("/pos "+ name + "," + "0:0" + ";");
                System.out.println(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}