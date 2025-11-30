package project.example.UserInterface;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import project.example.Network.GameClient;
import project.example.Utils.TextureManager;

public class MainMenuScreen implements Screen {
    private Stage stage;
    private Table table;

    private TextButton singlePlayerButton;
    private TextButton multiPlayerButton;
    private TextButton exitButton;
    private Game game;
    private GameClient client;
    public MainMenuScreen(Game game, GameClient client)
    {
        this.game = game;
        this.client = client;
        stage = new Stage(new ScreenViewport());

        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        table.padRight(228); //ширина кнопки 114, затем мы умнажаем размер кнопки на 2 ".scaleBy(2)"
        stage.addActor(table);
        table.defaults();

        DefineGroupButton().setPosition(0, 0);
        table.add(DefineGroupButton());
    }
    private VerticalGroup DefineGroupButton()
    {
        VerticalGroup group = new VerticalGroup();

        singlePlayerButton = new TextButton("Singleplayer", TextureManager.GetInstance().GetSkin());
        singlePlayerButton.setTransform(true);
        singlePlayerButton.scaleBy(2);

        multiPlayerButton = new TextButton("Multiplayer", TextureManager.GetInstance().GetSkin());
        multiPlayerButton.setTransform(true);
        multiPlayerButton.scaleBy(2);

        exitButton = new TextButton("Exit", TextureManager.GetInstance().GetSkin());
        exitButton.setTransform(true);
        exitButton.scaleBy(2);

        group.addActor(singlePlayerButton);
        group.addActor(multiPlayerButton);
        group.addActor(exitButton);

        group.fill();
        group.space(60);
        return group;
    }

    @Override
    public void show() {
        singlePlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new SingleplayerScreen(game));
            }
        });

        multiPlayerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (client.getConnect())
                    game.setScreen(new MultiplayerScreen(game, client)); //inputName();
                else {
                    showConnectionErrorDialog();
                }
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLUE);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    private void showConnectionErrorDialog() {
        Dialog dialog = new Dialog("", TextureManager.GetInstance().GetSkin()) {
            @Override
            protected void result(Object object) {
                // Используем понятные строковые константы для проверки
                if ("TRY_AGAIN".equals(object)) {
                    System.out.println("Попытка переподключения...");

                    // Пытаемся подключиться в отдельном потоке
                    new Thread(() -> {
                        boolean connected = false;
                        try {
                            // Пытаемся подключиться
                            client.tryAgain();
                            connected = client.getConnect();
                        } catch (Exception e) {
                            System.out.println("Ошибка переподключения: " + e.getMessage());
                        }

                        final boolean finalConnected = connected;

                        // Возвращаемся в UI-поток для обновления интерфейса
                        Gdx.app.postRunnable(() -> {
                            if (finalConnected) {
                                // Если подключились успешно - переходим на экран мультиплеера
                                System.out.println("Подключение установлено!");
                                game.setScreen(new MultiplayerScreen(game, client));
                            } else {
                                // Если не подключились - снова показываем диалог
                                System.out.println("Подключение не удалось, показываем диалог снова");
                                showConnectionErrorDialog();
                            }
                        });
                    }).start();

                }
                else if ("OK".equals(object))
                    hide();
            }
        };

        // Настройка содержимого диалога
        Label label = new Label("There is no internet connection", TextureManager.GetInstance().GetSkin());
        label.setFontScale(2f);
        dialog.getContentTable().add(label).pad(20);

        // Создаем кнопки с увеличенным шрифтом
        TextButton tryAgainButton = new TextButton("TRY AGAIN", TextureManager.GetInstance().GetSkin());
        tryAgainButton.getLabel().setFontScale(2.5f);

        TextButton okButton = new TextButton("OK", TextureManager.GetInstance().GetSkin());
        okButton.getLabel().setFontScale(2.5f);

        // Используем строковые константы вместо boolean значений
        dialog.getButtonTable().defaults().space(350);
        dialog.button(tryAgainButton, "TRY_AGAIN");
        dialog.button(okButton, "OK");

        // Настройки размера и позиции
        dialog.show(stage);
        dialog.setSize(700, 200);
        dialog.setPosition(
            stage.getWidth() / 2f - dialog.getWidth() / 2f,
            stage.getHeight() / 2f - dialog.getHeight() / 2f
        );

        // Настройка внешнего вида
        dialog.setColor(1, 1, 1, 1);
        dialog.setBackground(TextureManager.GetInstance().GetSkin().newDrawable("white", Color.DARK_GRAY));
    }
    public void inputName()
    {
        Dialog dialog = new Dialog("", TextureManager.GetInstance().GetSkin()) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    // Действия при нажатии OK
                    String name = ((TextField) getContentTable().getCells().first().getActor()).getText();
                    if (!name.isEmpty()) {
                        GameClient.player.setName(name);
                        //game.setScreen(new MultiplayerScreen(game, client));
                    }
                    else
                        System.out.println("name null");
                }
            }
        };

        TextField textField = new TextField("your nickname?", TextureManager.GetInstance().GetSkin());
        textField.setScale(2.5f);
        TextButton cancelButton = new TextButton("CANCEL", TextureManager.GetInstance().GetSkin());
        cancelButton.getLabel().setFontScale(2.5f);

        TextButton okButton = new TextButton("OK", TextureManager.GetInstance().GetSkin());
        okButton.getLabel().setFontScale(2.5f);

        dialog.getButtonTable().defaults().space(350);
        dialog.getContentTable().add(textField);
        dialog.button(cancelButton, false);
        dialog.button(okButton, true);

        dialog.setColor(1, 1, 1, 1);
        dialog.getContentTable().add(textField).width(400).height(80).pad(20);
        dialog.setBackground(TextureManager.GetInstance().GetSkin().newDrawable("white", Color.DARK_GRAY));
        dialog.setSize(700, 200);


        dialog.show(stage);
    }
}
