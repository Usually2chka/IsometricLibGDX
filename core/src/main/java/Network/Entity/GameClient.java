package Network.Entity;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import Network.AuthRequest;
import Network.AuthResponse;
import Network.Network;

public class GameClient {
    private Client client;
    private int playerId;

    public void connect(String hostIp, String token, String playerName) throws IOException {
        this.client = new Client();
        Network.registerClasses(client);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof AuthResponse) {
                    AuthResponse response = (AuthResponse) object;
                    if (response.success) {
                        System.out.println("Успешное подключение к хосту!");
                    } else {
                        System.err.println("Ошибка: " + response.message);
                    }
                }
            }
        });

        client.start();
        client.connect(Network.TIMEOUT, hostIp, Network.PORT);

        // Отправляем запрос на аутентификацию
        AuthRequest request = new AuthRequest();
        request.token = token;
        request.playerName = playerName;
        client.sendTCP(request);
    }
}
