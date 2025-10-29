package project.example.Network.Entyties;

import java.util.ArrayList;

public class Lobby {
    private String lobbyName;
    private int maxPlayers;
    private boolean isPrivate;
    private Player hostPlayer;
    private int sizeWorld;
    private boolean isFallBlocks;
    private ArrayList<Player> players;

    public Lobby()
    {

    }
    public Lobby(String lobbyName, int maxPlayers, boolean isPrivate, Player hostPlayer, int sizeWorld, boolean isFallBlocks)
    {
        this.lobbyName = lobbyName;
        this.maxPlayers = maxPlayers;
        this.isPrivate = isPrivate;
        this.hostPlayer = hostPlayer;
    }
}
