package Network.Server.packets;

public class AuthResponse {
    public boolean success; // Успешно ли подключение?
    public String message; // Сообщение об ошибке (если success=false)
}
