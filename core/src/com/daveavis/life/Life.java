package com.daveavis.life;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Life extends ApplicationAdapter {
    private final int WIDTH = 40; // number of cells wide
    private final int HEIGHT = 30;  // number of cells high
    private final int CELL_SIZE = 10;  // width of a cell in pixels
    private final double INITIAL_PCT = 0.15; // percentage of cells alive initially
    private final float RATE = 0.2f; // seconds between updates

    private SpriteBatch batch;
    private Pixmap pixmap;
    private Texture texture;
    private Sprite sprite;
    private BitmapFont font;

    private boolean[][] alive;
    private int[][] liveNeighbors;
    private float timeSinceCollision = 0f;
    private int generation = 0;
    private int debugCount = 0;

    @Override
    public void create() {
        font = new BitmapFont();
        font.setColor(Color.YELLOW);
        populate();
        draw();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timeSinceCollision += Gdx.graphics.getDeltaTime();
        if (timeSinceCollision > RATE) {
            countNeighbors();
            nextGen();
            timeSinceCollision = 0f;
            generation++;
        }
        draw();
        batch.begin();
        // position the grid in the middle of the window
        sprite.setPosition(Gdx.graphics.getWidth() / 2 - pixmap.getWidth() / 2, Gdx.graphics.getHeight() / 2 - pixmap.getHeight() / 2);

        sprite.draw(batch);
        font.draw(batch, "Generation " + generation, 10, 20);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        texture.dispose();
        font.dispose();
    }

    private void populate() {
        alive = new boolean[HEIGHT][WIDTH];
        liveNeighbors = new int[HEIGHT][WIDTH];

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                alive[row][col] = Math.random() < INITIAL_PCT;
            }
        }
    }

    private void countNeighbors() {
        int rowUp;
        int rowDown;
        int colLeft;
        int colRight;

        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                int neighbors = 0;

                rowUp = (row - 1);
                if (rowUp < 0) rowUp += HEIGHT;

                rowDown = (row + 1) % (HEIGHT - 1);

                colLeft = col - 1;
                if (colLeft < 0) colLeft += WIDTH;

                colRight = (col + 1) % (WIDTH - 1);

                if (alive[rowUp][colLeft]) neighbors++;
                if (alive[rowUp][col]) neighbors++;
                if (alive[rowUp][colRight]) neighbors++;
                if (alive[row][colLeft]) neighbors++;
                if (alive[row][colRight]) neighbors++;
                if (alive[rowDown][colLeft]) neighbors++;
                if (alive[rowDown][col]) neighbors++;
                if (alive[rowDown][colRight]) neighbors++;

                liveNeighbors[row][col] = neighbors;
                //System.out.println("neighbors = " + neighbors);
                //System.out.println("liveNeighbors[" + row + "][" + col + "] = " + liveNeighbors[row][col]);
            }
        }
        //if (debugCount < 2) {
        //    debug();
        //}
    }

    private void nextGen() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (alive[row][col]) {
                    if (liveNeighbors[row][col] < 2)
                        alive[row][col] = false;
                    else if (liveNeighbors[row][col] == 2 || liveNeighbors[row][col] == 3)
                        alive[row][col] = true;
                    else if (liveNeighbors[row][col] > 3)
                        alive[row][col] = false;
                } else {
                    if (liveNeighbors[row][col] == 3)
                        alive[row][col] = true;
                }
            }
        }
    }

    private void draw() {
        batch = new SpriteBatch();
        pixmap = new Pixmap(WIDTH * CELL_SIZE, HEIGHT * CELL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();


        pixmap.setColor(Color.BLACK);
        // Draw the vertical lines of the grid
        for (int i = 0; i < WIDTH * CELL_SIZE; i += CELL_SIZE) {
            pixmap.drawLine(i, 0, i, HEIGHT * CELL_SIZE);
        }
        // Draw the horizontal lines of the grid
        for (int i = 0; i < HEIGHT * CELL_SIZE; i += CELL_SIZE) {
            pixmap.drawLine(0, i, WIDTH * CELL_SIZE, i);
        }

        // Draw the live cells
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                if (alive[row][col]) {
                    pixmap.setColor(Color.BLACK);
                    pixmap.fillCircle(col * CELL_SIZE + CELL_SIZE / 2, row * CELL_SIZE + CELL_SIZE / 2, (CELL_SIZE / 2) - 1);
                }
            }
        }

        // create a texture from the pixmap
        texture = new Texture(pixmap);

        // the texture has the grid now, dispose of the pixmap
        pixmap.dispose();

        sprite = new Sprite(texture);
    }

    private void debug() {
        debugCount++;
        System.out.println("Generation " + generation);
        // print out the liveNeighbors array
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                System.out.print(liveNeighbors[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
