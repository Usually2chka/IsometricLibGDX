package Network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import Network.Client.packets.AuthRequest;
import Network.Server.packets.AuthResponse;

public class Network {
    public static final int PORT = 54555;
    public static final int TIMEOUT = 5000;

    public static void registerClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(String.class);
        kryo.register(AuthRequest.class);
        kryo.register(AuthResponse.class);
    }
}
