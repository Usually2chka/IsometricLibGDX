package Network.Server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer {
    private Server server;

    public GameServer() throws IOException
    {
        server = new Server();
        server.start();
        server.bind(54555, 54777);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof FrameworkMessage.KeepAlive)
                {
                    //Logic
                }
                else
                {
                    //logic
                }
            }
        });
    }
}
