package project.example.Network.Packets;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;
import project.example.Network.GameClient;

public class ConnectPacket {
    public Player player;
    public Lobby lobby;
    public boolean isSuccess;
    public ConnectPacket()
    {

    }
}
