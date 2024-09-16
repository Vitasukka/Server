package main;

import online.Client;
import player.Player;

import java.util.HashMap;
import java.util.Map;

public class Game {

    private static Client client;
    private static Client client2;
    public static Player player;
    private static Map<String, String> players;

    public Game() {
        client = new Client("Lukas");
        client2 = new Client("Vita");
        player = new Player("Lukas", "0:0");
        players = new HashMap<>();
    }

    public static void main(String[] args) {
        new Game();
        client.run();
        client2.run();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayers(Map<String, String> players) {
        this.players = players;
    }
}
