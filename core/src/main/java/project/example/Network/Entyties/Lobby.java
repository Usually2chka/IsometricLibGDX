package project.example.Network.Entyties;

import java.util.ArrayList;

import project.example.Network.GameClient;

public class Lobby {
    public Player hostPlayer;
    public int id;
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
        this.sizeWorld = sizeWorld;
        this.isFallBlocks = isFallBlocks;
        this.hostPlayer = hostPlayer;
        players.add(hostPlayer);
        if (lobbyName == null)
            this.lobbyName = hostPlayer.getName();
    }

    public void joinToLobby(Player player)
    {
        players.add(player);
    }
    public void leaveFromLobby(Player player) {
        players.remove(player);
    }

    @Override
    public String toString()
    {
        return lobbyName + " " + isPrivate + " " + hostPlayer + " " + sizeWorld + " " + isFallBlocks + " " + players.size() + "/" + maxPlayers;
    }

    public String getLobbyName()
    {
        return lobbyName;
    }

    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public boolean getIsPrivate()
    {
        return isPrivate;
    }

    public int getSizeWorld()
    {
        return sizeWorld;
    }

    public int getQuantityPlayers() { return players.size(); }

    public boolean getIsFallBlocks()
    {
        return isFallBlocks;
    }

    public ArrayList<Player> getPlayers()
    {
        return new ArrayList<>(players);
    }

    public long getId() {
        return id;
    }
}
