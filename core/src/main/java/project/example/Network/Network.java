package project.example.Network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;
import project.example.Network.Packets.AllLobbiesPacket;
import project.example.Network.Packets.CreateLobbyPacket;
import project.example.Network.Packets.HandshakePacket;
import project.example.Network.Packets.JoinToLobbyPacket;
import project.example.Network.Packets.LeaveFromLobbyPacket;
import project.example.Network.Packets.LobbyPacket;

public class Network {
    public static final int PORT = 55555;

    public static void RegisterClasses(EndPoint endPoint)
    {
        Kryo kryo = endPoint.getKryo();
        kryo.register(AllLobbiesPacket.class);
        kryo.register(CreateLobbyPacket.class);
        kryo.register(Player.class);
        kryo.register(LobbyPacket.class);
        kryo.register(ArrayList.class);
        kryo.register(Lobby.class);
        kryo.register(JoinToLobbyPacket.class);
        kryo.register(LeaveFromLobbyPacket.class);
        kryo.register(HandshakePacket.class);
    }
}
