package project.example.Network.Packets;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;
import project.example.Network.GameClient;

public class LeaveFromLobbyPacket {
    public Player player = GameClient.player;
    public Lobby lobby;
    public LeaveFromLobbyPacket() { }
    public LeaveFromLobbyPacket(Lobby lobby) { this.lobby = lobby; }
}
