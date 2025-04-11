package Network.Entity;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.UUID;

import Network.AuthRequest;
import Network.AuthResponse;
import Network.Network;


public class HostPlayer {
    private Server server; // KryoNet-сервер
    private String gameToken; // Токен для подключения

    public HostPlayer() throws IOException {
        this.server = new Server();
        Network.registerClasses(server);
        this.gameToken = UUID.randomUUID().toString(); // Генерация токена

        // Обработка событий
        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                System.out.println("Игрок подключился: " + connection.getID());
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof AuthRequest) {
                    handleAuth(connection, (AuthRequest) object);
                }
            }
        });

        server.bind(Network.PORT);
        server.start();
    }

    private void handleAuth(Connection connection, AuthRequest request) {
        AuthResponse response = new AuthResponse();

        if (request.token.equals(gameToken)) {
            response.success = true;
            response.message = "Добро пожаловать!";
        } else {
            response.success = false;
            response.message = "Неверный токен!";
            connection.close(); // Закрываем соединение
        }

        connection.sendTCP(response);
    }

    public String getGameToken() {
        return gameToken;
    }
}
