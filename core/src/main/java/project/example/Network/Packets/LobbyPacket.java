package project.example.Network.Packets;

import project.example.Network.Entyties.Lobby;

public class LobbyPacket {
    public Lobby lobby;

    public LobbyPacket() {}

    public LobbyPacket(Lobby lobby)
    {
        this.lobby = lobby;
    }
}
