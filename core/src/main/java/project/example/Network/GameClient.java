package project.example.Network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

import static project.example.Network.Network.PORT;

import project.example.Network.Packets.HandshakePacket;
import project.example.Network.Packets.LobbyPacket;
import project.example.Network.Packets.SuccessPacket;

public class GameClient {
    private Client client;

    public GameClient() {

        client = new Client();

        Network.RegisterClasses(client);

        client.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof HandshakePacket);
                if (object instanceof  SuccessPacket)
                    updateClient((SuccessPacket) object);
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
        //TODO Раздумиями было понято, что лучше делать без флажка "isSuccess" и обрабатывать некоретное введение на клиете, и
        //TODO                                                                                              (по возможности) на сервере
        //if (((SuccessPacket) object).isSuccess)

        //TODOОбновление клиентам, чтобы они обновили видимые лобаки
    }

    public void createLobby(LobbyPacket lobbyPacket)
    {
        client.sendTCP(lobbyPacket);//TODO ОБРАБОТКА ЛОББИ
    }
}
