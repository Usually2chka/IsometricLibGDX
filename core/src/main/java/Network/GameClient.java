package Network;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;
import java.net.InetAddress;

import static Network.Network.PORT;

import Network.Packets.ChatMessage;

public class GameClient {
    private Client client;

    public GameClient() throws IOException {
        connectToServer();
//        client = new Client();
//
//        Network.RegisterClasses(client);
//
//        client.addListener(new Listener() {
//            @Override
//            public void connected(Connection connection) {
//                System.out.println("Подключено к серверу!");
//            }
//
//            @Override
//            public void received(Connection connection, Object object) {
//                if (object instanceof ChatMessage) {
//                }
//            }
//        });
//
//
//
//        client.start();
//        new Thread("Network") {
//            public void run() {
//                while (true) {
//                    try {
//                        client.update(100); // Обновляем клиент с таймаутом в 100 мс
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    // Для удобства можно добавить небольшой перерыв
//                    try {
//                        Thread.sleep(10);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
//        client.connect(5000, "10.0.2.2", PORT, PORT); // localhost
    }

    public void connectToServer(){

        client = new Client();
        client.start();

        InetAddress address = client.discoverHost(PORT, 5000);
        Gdx.app.log("address", String.valueOf(address));

        Network.RegisterClasses(client);

        ChatMessage request = new ChatMessage("Here is the request");

        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof ChatMessage) {
                    ChatMessage response = (ChatMessage) object;
                    Gdx.app.log("response", response.text);
                }
            }
        });

        new Thread(){
            public void run(){
                Gdx.app.log("here", "yeah");
                try {
                    //client.connect(50000, "10.0.2.2", 54557, 54777);
                    client.connect(5000, "10.0.2.2", PORT);// 10.0.2.2 is addres for connecting localhost from emulator.
                } catch (IOException e){
                    Gdx.app.log("expection", "");

                }
            }
        }.start();

        client.sendTCP(request);
    }

    public void sendMessage(String text) {
        if (client.isConnected()) {
            client.sendTCP(new ChatMessage(text));
        }
    }

    public Client getClient() {
        return client;
    }
}
