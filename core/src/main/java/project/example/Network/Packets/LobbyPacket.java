package project.example.Network.Packets;

import project.example.Network.Entyties.Lobby;

public class LobbyPacket {
    public int lobbyId;

    public LobbyPacket() { }
    public LobbyPacket(int id) { lobbyId = id; }
}
