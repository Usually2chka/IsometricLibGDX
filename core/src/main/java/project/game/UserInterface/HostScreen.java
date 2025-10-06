package project.game.UserInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.io.IOException;

import project.game.Utils.TextureManager;

public class HostScreen implements Screen {
    private Stage stage;
    private Game game;
    //private GameServer host;
    private Table table;
    private Group group;
    private HorizontalGroup firstCell;
    private VerticalGroup firtPartOfGroup;
    private VerticalGroup secondPartOfGroup;
    private Window window;

    private short quantityPlayersInRoom;
    private short sizeWorld; //
    private boolean isPressedPrivateRoom;
    private boolean isFallBlocks; //
    private String nameRoom; //
    public HostScreen(Game game) {
        this.game = game;
        window = new Window("", TextureManager.GetInstance().GetSkin());
        table = new Table();

        defineFirstCell();
        defineSecondCell();
        defineThirdCell();
        defineCreateButton();

        window.setSize(1500,1000);
        window.setPosition(
            (Gdx.graphics.getWidth()/2) - 730,
            (Gdx.graphics.getHeight()/2) - 500
        );


        window.center();
        window.add(table).expand().fill();

        stage = new Stage(new ScreenViewport());
        group = new Group();
    }

    private void createNetwork(){
//        try {
//            //host = new GameServer();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void defineFirstCell() {
        firstCell = new HorizontalGroup();
        firtPartOfGroup = new VerticalGroup();
        Label label = new Label("Private room", TextureManager.GetInstance().GetSkin());
        CheckBox checkBoxPrivateRoom = new CheckBox("", TextureManager.GetInstance().GetSkin());
        checkBoxPrivateRoom.padTop(30f);
        checkBoxPrivateRoom.setTransform(true);
        checkBoxPrivateRoom.scaleBy(1.01f);
        checkBoxPrivateRoom.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isPressedPrivateRoom == false)
                    isPressedPrivateRoom = true;
                else
                    isPressedPrivateRoom = false;
            }
        });
        firtPartOfGroup.addActor(label);
        firtPartOfGroup.padRight(400f);

        firtPartOfGroup.addActor(checkBoxPrivateRoom);
        firstCell.addActor(firtPartOfGroup);

        secondPartOfGroup = new VerticalGroup();
        secondPartOfGroup.addActor(new Label("Quantity players", TextureManager.GetInstance().GetSkin()));
        SelectBox<Short> list = new SelectBox<>(TextureManager.GetInstance().GetSkin());
        Array<Short> listWithQuantityPlayers = new Array<>();
        listWithQuantityPlayers.add(Short.parseShort("2"));
        listWithQuantityPlayers.add(Short.parseShort("3"));
        listWithQuantityPlayers.add(Short.parseShort("4"));
        list.setItems(listWithQuantityPlayers);
        list.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quantityPlayersInRoom = Short.parseShort("" + list.getSelectedIndex());
            }
        });
        secondPartOfGroup.addActor(list);
        firstCell.addActor(secondPartOfGroup);
        firstCell.padBottom(130f);
        table.add(firstCell).row();
    }

    private void defineSecondCell() {
        firstCell = new HorizontalGroup();
        firtPartOfGroup = new VerticalGroup();
        Label label = new Label("Fall blocks", TextureManager.GetInstance().GetSkin());
        CheckBox checkBoxFallBlocks = new CheckBox("", TextureManager.GetInstance().GetSkin());
        checkBoxFallBlocks.padTop(30f);
        checkBoxFallBlocks.setTransform(true);
        checkBoxFallBlocks.scaleBy(1.01f);
        checkBoxFallBlocks.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isFallBlocks == false)
                    isFallBlocks = true;
                else
                    isFallBlocks = false;
            }
        });
        firtPartOfGroup.addActor(label);
        firtPartOfGroup.padRight(500f);

        firtPartOfGroup.addActor(checkBoxFallBlocks);
        firstCell.addActor(firtPartOfGroup);

        secondPartOfGroup = new VerticalGroup();
        secondPartOfGroup.addActor(new Label("Size world", TextureManager.GetInstance().GetSkin()));
        SelectBox<Short> list = new SelectBox<>(TextureManager.GetInstance().GetSkin());
        Array<Short> listWithSizeWorld = new Array<>();
        listWithSizeWorld.add(Short.parseShort("16"));
        listWithSizeWorld.add(Short.parseShort("32"));
        listWithSizeWorld.add(Short.parseShort("64"));
        list.setItems(listWithSizeWorld);
        list.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sizeWorld = Short.parseShort("" + list.getSelectedIndex());
            }
        });
        secondPartOfGroup.addActor(list);
        firstCell.addActor(secondPartOfGroup);
        firstCell.padBottom(130f);
        table.add(firstCell).row();
    }

    private void defineThirdCell() {
        firstCell = new HorizontalGroup();
        secondPartOfGroup = new VerticalGroup();
        TextField textField = new TextField("", TextureManager.GetInstance().GetSkin());
        secondPartOfGroup.addActor(new Label("Name room", TextureManager.GetInstance().GetSkin()));
        secondPartOfGroup.addActor(textField);
        textField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nameRoom = textField.getMessageText();
            }
        });
        firstCell.addActor(secondPartOfGroup);
        //firstCell.padBottom(130f);
        table.add(firstCell).row();
    }

    private void defineCreateButton() {
        firstCell = new HorizontalGroup();
        secondPartOfGroup = new VerticalGroup();
        TextButton buttonToBack = new TextButton("Back", TextureManager.GetInstance().GetSkin());
        TextButton button = new TextButton("Create", TextureManager.GetInstance().GetSkin());
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        buttonToBack.addListener(new ClickListener() {
           @Override
           public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MultiplayerScreen(game));
           }
        });
        firstCell.addActor(buttonToBack);
        secondPartOfGroup.addActor(button);
        firstCell.padTop(230f).space(1100);
        firstCell.addActor(secondPartOfGroup);
        table.add(firstCell).row();

    }

    public void giveDataAboutServer(){}

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        table.setFillParent(true);

        group.addActor(window);
        stage.addActor(group);
        //stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.GRAY);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
