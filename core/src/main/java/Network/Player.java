package Network;

import com.badlogic.gdx.utils.Array;

import project.game.Game.Entitys.Unit;

public class Player {
    private Array<Unit> units;
    private short startPosUnitX;
    private short startPosUnitY;
    public Player(short startPosUnitX, short startPosUnitY)
    {
        this.startPosUnitX = startPosUnitX;
        this.startPosUnitY = startPosUnitY;
        //this.units.add(new Unit());
    }
    public Array<Unit> GetUnits(){return null;} //TODO!
    public void CreateNewUnit() {} //TODO!
}
