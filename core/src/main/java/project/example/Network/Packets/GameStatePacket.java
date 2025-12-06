package project.example.Network.Packets;

import java.util.HashMap;

import project.example.Network.Entyties.Lobby;

public class GameStatePacket {
    public HashMap<Integer, int[]> playerIdToCoordinate;
    public int lobbyId;
    public int positionX;
    public int positionY;
    public int hitPoint;
    public int damage;
    public int attackedPlayerId;
    public int attackerPlayerId;
    public boolean isTurned;
    public boolean isAllowed;
    public int[] queueTurns;
    public GameStatePacket()
    {

    }
    public GameStatePacket(Lobby lobby, int positionX, int positionY)
    {
        lobbyId = lobby.id;
        this.positionX = positionX;
        this.positionY = positionY;
    }
    public GameStatePacket(Lobby lobby, int damage, int attackedPlayerId, int attackerPlayerId)
    {
        lobbyId = lobby.id;
        this.damage = damage;
        this.attackedPlayerId = attackedPlayerId;
        this.attackerPlayerId = attackerPlayerId;
    }
}
