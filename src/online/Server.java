package online;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {

    private ServerSocket server;
    private InetAddress ip;
    private ArrayList<ClientThread> clients;
    private Map<String, String> players;
    private ExecutorService threadPool;
    private boolean running;

    public Server() {
        try {
            ip = InetAddress.getLocalHost();
            System.out.println(ip);
            server = new ServerSocket(8080, 50, ip);
            clients = new ArrayList<>();
            players = new HashMap<>();
            threadPool = Executors.newCachedThreadPool();
            running = true;
        }
        catch (IOException e) {
            //TODO: handle
        }
    }

    public void shutdown() {
        running = false;
        try {
            if (!server.isClosed()) {
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Waiting for clients...");
            while (running) {
                Socket client = server.accept();
                ClientThread clientThread = new ClientThread(client);
                clients.add(clientThread);
                threadPool.execute(clientThread);
            }
        }
        catch (IOException e) {
            shutdown();
        }

    }

    class ClientThread implements Runnable {

        private Socket client;
        private BufferedReader in;
        private PrintWriter out;
        private String name;

        public ClientThread(Socket client) {
            this.client = client;
            running = true;
            try {
                in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out = new PrintWriter(client.getOutputStream(), true);
            } catch (IOException e) {
                shutdown();
            }
        }

        public void shutdown() {
            try {
                in.close();
                out.close();
                if (!client.isClosed()) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                name = in.readLine();
                String message;
                while ((message = in.readLine()) != null) {
                    if (message.startsWith("/pos")) {
                        String[] split = message.split(" ");
                        if (split.length == 2) {
                            players.put(split[0], split[1]);
                            System.out.println(players);
                        }
                    } else {
                        System.out.println(message);
                        out.println(name + ": " + message + " received");
                    }
                }
            }
            catch (Exception e) {
                shutdown();
            }
        }
    }

    public static void main(String[] args) {
        new Server().run();
    }

}