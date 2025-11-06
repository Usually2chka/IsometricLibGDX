package project.example.Network.Packets;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;

public class JoinToLobbyPacket {
    public Lobby lobby;
    public Player player;
    public boolean isAllowed;
    public String reason;

    public JoinToLobbyPacket() { }
}
