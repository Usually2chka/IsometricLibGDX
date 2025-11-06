package project.example.Network.Packets;

import project.example.Network.Entyties.Player;

public class CreateLobbyPacket {
    public String lobbyName;
    public int maxPlayers;
    public boolean isPrivate;
    public int sizeWorld;
    public boolean isFallBlocks;
    public Player hostPlayer;
    public boolean isSuccess;

    public CreateLobbyPacket() { }

    public CreateLobbyPacket(boolean isSuccess)
    {
        this.isSuccess = isSuccess;
    }

    public CreateLobbyPacket(String lobbyName, int maxPlayers, boolean isPrivate, int sizeWorld, boolean isFallBlocks, Player hostPlayer)
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
