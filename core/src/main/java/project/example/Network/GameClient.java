package project.example.Network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static project.example.Network.Network.PORT;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;
import project.example.Network.Packets.ConnectPacket;
import project.example.Network.Packets.HandshakePacket;
import project.example.Network.Packets.CreateLobbyPacket;
import project.example.Network.Packets.LobbyPacket;
import project.example.Network.Packets.JoinToLobbyPacket;

public class GameClient {
    public enum ClientState {
        CONNECTED,
        DISCONNECTED,
        WAITING_RESPONSE,
        IN_LOBBY,
        ALLOWED,
        CANCELED
    }
    public ClientState state;
    private static Client client;
    private Array<Lobby> lobbies;
    public static Player player;
    public GameClient() {
        player = new Player("admin");
        client = new Client();
        lobbies = new Array<>();

        Network.RegisterClasses(client);

        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {

            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof LobbyPacket)
                    lobbies.add(((LobbyPacket) object).lobby);
                if (object instanceof HandshakePacket)
                    updateClient((HandshakePacket) object);
            }
        });

        client.start();

        try {
            client.connect(2000, "10.0.2.2", PORT); // localhost
            if (client.isConnected())
                state = ClientState.CONNECTED;
        } catch (IOException e) {
            Gdx.app.log("Отсутствует подключение к серверу", "");
        }
    }

    public void updateClient(Object object)
    {
        for (Lobby lobby: ((HandshakePacket) object).lobbies) {
            lobbies.add(lobby);
        }
    }

    public void createLobby(CreateLobbyPacket createLobbyPacket)
    {
        client.sendTCP(createLobbyPacket);
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

    public void connectToLobby(Lobby lobby, Consumer<Boolean> onResponse)
    {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof JoinToLobbyPacket) {
                    boolean allowed = ((JoinToLobbyPacket) object).isAllowed;
                    state = allowed ? ClientState.IN_LOBBY : ClientState.CANCELED;
                    Gdx.app.postRunnable(() -> onResponse.accept(allowed));
                    client.removeListener(this);
                }
            }
        });

        JoinToLobbyPacket request = new JoinToLobbyPacket();
        request.lobby = lobby;
        request.player = player;
        client.sendTCP(request);
        state = ClientState.WAITING_RESPONSE;
    }
}
