package project.example.Network.Packets;

import java.util.HashMap;

import project.example.Network.Entyties.Player;

public class GameStatePacket {public HashMap<Integer, int[]> playerIdToCoordinate;
    public Player playerTurned;
    public Player enemyPlayer;
    public int lobbyId;
    public boolean isAllowed;
    public int currentTurns;
    public GameStatePacket()
    {

    }
}
