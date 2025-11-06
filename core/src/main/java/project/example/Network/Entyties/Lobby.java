package project.example.Network.Entyties;

import java.util.ArrayList;

public class Lobby {
    public Player hostPlayer;

    private String lobbyName;
    private int maxPlayers;
    private boolean isPrivate;
    private int sizeWorld;
    private boolean isFallBlocks;
    private ArrayList<Player> players;

    public Lobby()
    {

    }

    public Lobby(String lobbyName, int maxPlayers, boolean isPrivate, Player hostPlayer, int sizeWorld, boolean isFallBlocks)
    {
        players = new ArrayList<>();
        this.lobbyName = lobbyName;
        this.maxPlayers = maxPlayers;
        this.isPrivate = isPrivate;
        this.hostPlayer = hostPlayer;
    }

    public void joinToLobby(Player player)
    {
        players.add(player);
    }

    @Override
    public String toString()
    {
        return lobbyName + " " + maxPlayers + " " + isPrivate + " " + hostPlayer + " " + sizeWorld + " " + isFallBlocks + " " + players.size() + "/4";
    }
}
