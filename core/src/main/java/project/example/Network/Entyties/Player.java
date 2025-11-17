package project.example.Network.Entyties;

public class Player {
    private String name;
    public int id;
    public int lobbyId;

    public Player()
    {

    }

    public Player(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public long getId()
    {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
