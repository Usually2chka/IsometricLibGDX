package project.example.Network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import static project.example.Network.Network.PORT;

import project.example.Network.Packets.HandshakePacket;

public class GameClient {
    private Client client;

    public GameClient() throws IOException {
        client = new Client();

        Network.RegisterClasses(client);

        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof HandshakePacket) {

                }
            }
        });

        client.start();
        client.connect(10000, "10.0.2.2", PORT); // localhost
    }
}
