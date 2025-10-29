package project.example.Network;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import static project.example.Network.Network.PORT;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Packets.HandshakePacket;
import project.example.Network.Packets.LobbyPacket;

public class GameClient {
    private Client client;
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
            client.connect(10000, "10.0.2.2", PORT); // localhost
        } catch (IOException e) {
            throw new RuntimeException(e);
            /*TODO Сделать new mainmenuscreen + уведомление, что у клиента нет интернета + тоже самое сделать при повторной попытке зайти в mpScreen*/
        }
    }

    public void updateClient(Object object)
    {
        for (Lobby lobby : ((HandshakePacket) object).lobbies) {
            lobbies.add(lobby);
        }
        //lobbies = ((HandshakePacket) object).lobbies;
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
}
