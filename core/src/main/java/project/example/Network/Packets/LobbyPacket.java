package project.example.Network.Packets;

import project.example.Network.Entyties.Lobby;

public class LobbyPacket {
    public int lobbyId;
    public int sizeWorld;
    private int maxPlayers;
    private boolean isPrivate;

    public LobbyPacket() { }
    public LobbyPacket(int id, int size, int maxPlayers, boolean isPrivate) {
        lobbyId = id;
        sizeWorld = size;
        this.maxPlayers = maxPlayers;
        this.isPrivate = isPrivate;
    }
}
