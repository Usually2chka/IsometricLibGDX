package project.example.Network.Packets;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;

public class LobbyPacket {
    public String lobbyName;
    public int maxPlayers;
    public boolean isPrivate;
    public short sizeWorld;
    public boolean isFallBlocks;
    public Player hostPlayer;
    public boolean isSuccess;

    public LobbyPacket() { }

    public LobbyPacket(boolean isSuccess)
    {
        this.isSuccess = isSuccess;
    }

    public LobbyPacket(String lobbyName, int maxPlayers, boolean isPrivate, short sizeWorld, boolean isFallBlocks, Player hostPlayer)
    {
        this.lobbyName = lobbyName;
        this.maxPlayers = maxPlayers;
        this.isPrivate = isPrivate;
        this.sizeWorld = sizeWorld;
        this.isFallBlocks = isFallBlocks;
        this.hostPlayer = hostPlayer;
    }
    //сделать просмотр статистики //playerinfo
}
