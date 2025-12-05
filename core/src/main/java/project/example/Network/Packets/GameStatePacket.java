package project.example.Network.Packets;

import java.util.ArrayList;

import project.example.Network.Entyties.Lobby;

public class GameStatePacket {
    public int lobbyId;
    public int positionX;
    public int positionY;
    public int hitPoint;
    public int damage;
    public int playerId;
    public boolean allowed;
    public ArrayList<int[]> playersPosition;
    public GameStatePacket()
    {

    }
    public GameStatePacket(Lobby lobby, int positionX, int positionY, int hitPoint, int damage, int playerId)
    {
        lobbyId = lobby.id;
        this.positionX = positionX;
        this.positionY = positionY;
        this.hitPoint = hitPoint;
        this.damage = damage;
        this.playerId = playerId;
    }
}
