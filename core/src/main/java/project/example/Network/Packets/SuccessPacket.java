package project.example.Network.Packets;


import com.badlogic.gdx.utils.Array;

import project.example.Network.Entyties.Lobby;

public class SuccessPacket {
    public boolean isSuccess;
    public Array<Lobby> lobbies;
}
