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
import project.example.Network.Packets.HandshakePacket;
import project.example.Network.Packets.LeaveFromLobbyPacket;
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
    public final static Player player = new Player("admin");
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
                if (object instanceof AllLobbiesPacket)
                    processedAllLobbyPacket((AllLobbiesPacket) object);
                //if (object instanceof LobbyPacket) {
                    //lobbies.add(((LobbyPacket) object).lobby);
                //}
            }

            @Override
            public void disconnected(Connection connection) {
                Gdx.app.log("Работает?", "" + Player.inCurrentLobby);
                leaveFromLobby(Player.inCurrentLobby);
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

    public void updateClient()//Consumer<Array<Lobby>> onResponse)
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
                    Gdx.app.log("а", "" + allowed);
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
        client.sendTCP(new LeaveFromLobbyPacket(lobby));
    }
    private void processedHandshakeData(HandshakePacket packet)
    {
        lobbies.clear();
        for (int i = 0; i < packet.lobbies.size(); i++)
            if (packet.lobbies.get(i) != null)
                lobbies.add(packet.lobbies.get(i));
        player.id = packet.playerId;
    }
    private void processedAllLobbyPacket(AllLobbiesPacket packet)
    {
        lobbies.clear();
        for (int i = 0; i < packet.lobbies.size(); i++)
            if (packet.lobbies.get(i) != null)
                lobbies.add(packet.lobbies.get(i));
    }
}
