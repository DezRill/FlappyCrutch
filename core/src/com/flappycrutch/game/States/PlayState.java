package com.flappycrutch.game.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.flappycrutch.game.Sprites.Bird;
import com.flappycrutch.game.Sprites.Tube;

/**
 * Created by lenovo on 25.03.2017.
 */

public class PlayState extends State {
    public static final int TUBE_SPACING=125;
    public static final int TUBE_COUNT=4;
    public static final int GROUND_Y_OFFSET=-30;

    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;

    private Array<Tube> tubes;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird=new Bird(50,300);
        bg=new Texture("bg.png");
        ground=new Texture("ground.png");
        groundPos1=new Vector2(camera.position.x-camera.viewportWidth/2,GROUND_Y_OFFSET);
        groundPos2=new Vector2((camera.position.x-camera.viewportWidth/2)+ground.getWidth(),GROUND_Y_OFFSET);
        camera.setToOrtho(false,480/2, 800/2);
        tubes=new Array<Tube>();
        for (int i=0;i<TUBE_COUNT;i++)
        {
            tubes.add(new Tube(i*(TUBE_SPACING+Tube.TUBE_WIDTH)));
        }
    }

    @Override
    protected void handleInput() {
        if(Gdx.input.justTouched())
        {
            bird.jump();
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        bird.update(dt);
        camera.position.x=bird.getPosition().x+80;
        for (int i=0;i<tubes.size;i++)
        {
            Tube tube=tubes.get(i);
            if (camera.position.x-(camera.viewportWidth/2)>tube.getPosTopTube().x+tube.getTopTube().getWidth())
            {
                tube.reposition(tube.getPosTopTube().x+((Tube.TUBE_WIDTH+TUBE_SPACING)*TUBE_COUNT));
            }
            if (tube.collides(bird.getBounds()) || bird.getBounds().y<30) gsm.set(new GameOver(gsm));
        }
        camera.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg,camera.position.x-(camera.viewportWidth/2),0);
        sb.draw(bird.getBird(),bird.getPosition().x,bird.getPosition().y);
        for (Tube tube:tubes)
        {
            sb.draw(tube.getTopTube(),tube.getPosBottomTube().x,tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(),tube.getPosBottomTube().x,tube.getPosBottomTube().y);
        }
        sb.draw(ground,groundPos1.x,groundPos1.y);
        sb.draw(ground,groundPos2.x,groundPos2.y);
        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        for (Tube tube:tubes) {
            tube.dispose();
        }
        ground.dispose();
    }

    private void updateGround()
    {
        if (camera.position.x-(camera.viewportWidth/2)>groundPos1.x+ground.getWidth()) groundPos1.add(ground.getWidth()*2,0);
        if (camera.position.x-(camera.viewportWidth/2)>groundPos2.x+ground.getWidth()) groundPos2.add(ground.getWidth()*2,0);
    }
}
