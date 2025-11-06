package project.example.Network.Entyties;

public class Player {
    private String name;
    private final long id = 0;

    public Player()
    {

    }

    public Player(String name)
    {
        this.name = name;
        generateId();
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

    private long generateId()
    {
        return id + 1;
    }
}
