package project.example.Network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

import static project.example.Network.Network.PORT;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Packets.HandshakePacket;
import project.example.Network.Packets.LobbyPacket;

public class GameClient {
    private Client client;
    private Server localServer;
    private Array<Lobby> lobbies;
    public GameClient() {

        client = new Client();
        lobbies = new Array<>();

        Network.RegisterClasses(client);

        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {

            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof HandshakePacket)
                    updateClient((HandshakePacket) object);
            }
        });

        client.start();

        try {
            client.connect(2000, "10.0.2.2", PORT); // localhost
        } catch (IOException e) {
            Gdx.app.log("Отсутствует подключение к серверу", "");
        }
    }

    public void updateClient(Object object)
    {
        for (Lobby lobby : ((HandshakePacket) object).lobbies) {
            lobbies.add(lobby);
        }
        //TODO Обновление клиентам, чтобы они обновили видимые лобаки
    }

    public void createLobby(LobbyPacket lobbyPacket)
    {
        client.sendTCP(lobbyPacket);
    }

    public Array<Lobby> getLobbies()
    {
        return new Array<>(lobbies);
    }
    public boolean getConnect()
    {
        return client.isConnected();
    }
    public void tryAgain() throws IOException {
        client.connect(1000, "10.0.2.2", PORT); // localhost
    }
}
