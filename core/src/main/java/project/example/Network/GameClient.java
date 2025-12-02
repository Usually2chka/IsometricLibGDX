package project.example.Network;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.util.function.Consumer;

import static project.example.Network.Network.PORT;

import project.example.Network.Entyties.Lobby;
import project.example.Network.Entyties.Player;
import project.example.Network.Packets.AllLobbiesPacket;
import project.example.Network.Packets.CreateLobbyPacket;
import project.example.Network.Packets.GamePacket;
import project.example.Network.Packets.HandshakePacket;
import project.example.Network.Packets.LeaveFromLobbyPacket;
import project.example.Network.Packets.JoinToLobbyPacket;
import project.example.Network.Packets.LobbyPacket;

public class GameClient {
    public enum ClientState {
        CONNECTED,
        DISCONNECTED,
        WAITING_RESPONSE,
        IN_LOBBY,
        IN_GAME,
        ALLOWED,
        CANCELED
    }
    public ClientState state;
    private static Client client;
    private Array<Lobby> lobbies;
    public final static Player player = new Player("Player");
    private final Array<Consumer<Array<Lobby>>> lobbySubscribers = new Array<>();
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
                    processedHandshakeData((HandshakePacket) object);
            }
        });

        client.start();

        try {
            client.connect(2000, "10.0.2.2", PORT); // localhost //to connect test server "26.135.168.236"
            if (client.isConnected())
                state = ClientState.CONNECTED;
        } catch (IOException e) {
            Gdx.app.log("Отсутствует подключение к серверу", "");
        }
        updateClient();
    }

    public void addLobbyListener(Consumer<Array<Lobby>> listener) {
        lobbySubscribers.add(listener);
        // Сразу отправляем текущее состояние
        listener.accept(lobbies);
    }

    public void removeLobbyListener(Consumer<Array<Lobby>> listener) {
        lobbySubscribers.removeValue(listener, true);
    }

    public void updateClient()
    {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof AllLobbiesPacket) {
                    Array<Lobby> lobby = new Array<>();
                    for (Lobby lb : ((AllLobbiesPacket) object).lobbies)
                        lobby.add(lb);
                    Gdx.app.postRunnable(() -> {
                        lobbies.clear();
                        lobbies.addAll(lobby);

                        for (Consumer<Array<Lobby>> subscriber : lobbySubscribers)
                            subscriber.accept(lobbies);
                    });
                }
            }
        });
    }

    public void createLobby(Lobby lobby, Consumer<Integer> onResponse)
    {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof CreateLobbyPacket)
                {
                    if (((CreateLobbyPacket) object).isAllowed) {
                        lobby.id = ((CreateLobbyPacket) object).id;
                        Gdx.app.postRunnable(() -> onResponse.accept(lobby.id));
                    }
                }
                client.removeListener(this);
            }
        });
        CreateLobbyPacket packet = new CreateLobbyPacket(lobby);
        client.sendTCP(packet);
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
        client.connect(1000, "10.0.2.2", PORT); // localhost //to connect test server "26.135.168.236"
    }

    public void startGame(Consumer<Boolean> consumer)
    {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof LobbyPacket) {
                    System.out.println("fkj");
                    client.removeListener(this);
                    consumer.accept(true);
                }
            }
        });
    }

    public void notifyServerStartGame(int lobbyId) { client.sendTCP(new LobbyPacket(lobbyId)); }

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
    public void leaveFromLobby(Lobby lobby)
    {
        lobby.leaveFromLobby(GameClient.player);
        client.sendTCP(new LeaveFromLobbyPacket(lobby));
        player.lobbyId = -1;
    }
    private void processedHandshakeData(HandshakePacket packet)
    {
        lobbies.clear();
        for (int i = 0; i < packet.lobbies.size(); i++)
            if (packet.lobbies.get(i) != null)
                lobbies.add(packet.lobbies.get(i));
        player.id = packet.playerId;
    }
}
