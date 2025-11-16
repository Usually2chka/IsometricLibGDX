package project.example.Network.Packets;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;

public class CreateLobbyPacket {
    public String lobbyName;
    public int maxPlayers;
    public boolean isPrivate;
    public int sizeWorld;
    public boolean isFallBlocks;
    public Player hostPlayer;
    public boolean isAllowed;
    public int id;

    public CreateLobbyPacket() { }

    public CreateLobbyPacket(Lobby lobby)
    {
        this.lobbyName = lobby.getLobbyName();
        this.maxPlayers = lobby.getMaxPlayers();
        this.isPrivate = lobby.getIsPrivate();
        this.sizeWorld = lobby.getSizeWorld();
        this.isFallBlocks = lobby.getIsFallBlocks();
        this.hostPlayer = lobby.hostPlayer;
    }
    //сделать просмотр статистики //playerinfo
}
