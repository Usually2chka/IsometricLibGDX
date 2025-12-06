package project.example.Network.Entyties;

public class Player {
    public int hitPoint;
    public int damage;
    public int positionX;
    public int positionY;
    public boolean isDied;

    public String name;

    public int id;
    public boolean isTurned;
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

    public int getId()
    {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    @Override
    public String toString() {
        return name;
    }
}
