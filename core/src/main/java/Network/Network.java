package Network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {
    public static final int PORT = 54555; // Порт для P2P-подключения
    public static final int TIMEOUT = 5000; // Таймаут соединения

    // Регистрация классов для KryoNet
    public static void registerClasses(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(String.class);
        kryo.register(AuthRequest.class);
        kryo.register(AuthResponse.class);
    }
}
