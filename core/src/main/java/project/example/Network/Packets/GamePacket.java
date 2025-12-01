package project.example.Network.Packets;

import project.example.Network.Entyties.Lobby;

public class GamePacket {
    public int lobbyId;
    public int positionX;
    public int positionY;
    public int hitPoint;
    public int damage;
    public GamePacket()
    {

    }
    public GamePacket(Lobby lobby)
    {
        lobbyId = lobby.id;
    }
}
