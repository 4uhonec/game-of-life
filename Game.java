import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.security.SecureRandom;

public class Game implements Constants{
    private int[][] currentState;
    private int[][] nextState;
    private final double CELL_SIZE;
    private GraphicsContext gc;
    private final double width;//width of canvas
    private final double height;//height of canvas
    private Mode mode;//TODO: with loop.pause() maybe we can remove mode variable
    private int gridSize;
    private int sleepTime;
    private int gridOffset;
    //TODO: 1) add support for recalculating grid when window size change
    //TODO: 2) add support for keyboard "Next"
    //TODO: 4) add cell size change
    //TODO: 5) add aging colors, maybe colorpicker?
    //TODO: 6) add count corners - we dont see walls, we keep counting cells from other side of grid[very optional, after 5) ]
    //TODO: 7) multithreading

    public Game(Canvas canvas){//game constructor
        this.gridSize = DEFAULT_GRID_SIZE;
        CELL_SIZE = canvas.getHeight()/(double)gridSize;
        this.currentState = new int[gridSize][gridSize];
        this.nextState = new int[gridSize][gridSize];
        this.gc = canvas.getGraphicsContext2D();
        this.width = canvas.getWidth();
        this.height = canvas.getHeight();
        this.mode = Mode.PAUSED;
        this.sleepTime = DEFAULT_SLEEP_TIME;
    }

    public void randomFill(){//filling game field with random values 0/1
        SecureRandom random = new SecureRandom();
        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){
                currentState[i][j] = (random.nextInt(2) == 1) ? 1 : 0;
            }
        }
        draw();
    }

    public void next(){//calculate next game state in nextState[][], copy it to currentState[][], draw it
        int neighbors;//TODO: is there more elegant solution for this? maybe 8 ifs? Is it better for 6) ?

        //calculate inner field
        for(int i = 1; i < gridSize - 1; i++){
            for(int j = 1; j < gridSize - 1; j++){
                neighbors = currentState[i - 1][j - 1] + currentState[i][j - 1] + currentState[i + 1][j - 1]
                        + currentState[i - 1][j]                                + currentState[i + 1][j]
                        + currentState[i - 1][j + 1] + currentState[i][j + 1] + currentState[i + 1][j + 1];
                nextState[i][j] = cellNextState(neighbors, i, j);
            }
        }

        //calculate edges
        for(int i = 1; i < gridSize - 1; i++){

            //added upper row
            neighbors = currentState[i - 1][gridSize - 1] + currentState[i][gridSize - 1] + currentState[i + 1][gridSize - 1] +
                     currentState[i - 1][0]                      + currentState[i + 1][0]
                    + currentState[i - 1][1] + currentState[i][1] + currentState[i + 1][1];
            nextState[i][0] = cellNextState(neighbors, i, 0);

            //added bottom row
            neighbors = currentState[i - 1][gridSize - 2] + currentState[i][gridSize - 2] + currentState[i + 1][gridSize - 2]
                    + currentState[i - 1][gridSize - 1]                                        + currentState[i + 1][gridSize - 1]
                    + currentState[i - 1][0] + currentState[i][0] + currentState[i + 1][0];
            nextState[i][gridSize - 1] = cellNextState(neighbors, i, gridSize - 1);

            //added left row
            neighbors = currentState[gridSize - 1][i - 1] + currentState[0][i - 1] + currentState[1][i - 1]
                        + currentState[gridSize - 1][i]                        + currentState[1][i]
                    + currentState[gridSize - 1][i + 1] + currentState[0][i + 1] + currentState[1][i + 1];
            nextState[0][i] = cellNextState(neighbors, 0, i);

            //left(actually bottom), added left row
            neighbors = currentState[gridSize - 2][i - 1] + currentState[gridSize - 1][i - 1] + currentState[0][i - 1]
                    + currentState[gridSize - 2][i]                                      + currentState[0][i]
                    + currentState[gridSize - 2][i + 1] + currentState[gridSize - 1][i + 1] + currentState[0][i + 1];
            nextState[gridSize - 1][i] = cellNextState(neighbors, gridSize - 1, i);
        }

        //calculate corners
        neighbors = currentState[1][0] + currentState[1][1] + currentState[0][1];//TODO: CONTINUE FROM HERE(CALCULATE NEIGHBOURS BEHIND WALLS)
        nextState[0][0] = cellNextState(neighbors, 0, 0);

        neighbors = currentState[gridSize - 2][0] + currentState[gridSize - 2][1] + currentState[gridSize - 1][1];
        nextState[gridSize - 1][0] = cellNextState(neighbors, gridSize - 1, 0);

        neighbors = currentState[1][gridSize - 1] + currentState[1][gridSize - 2] + currentState[0][gridSize - 2];
        nextState[0][gridSize - 1] = cellNextState(neighbors, 0, gridSize - 1);

        neighbors = currentState[gridSize - 2][gridSize - 1] + currentState[gridSize - 2][gridSize - 2] + currentState[gridSize - 1][gridSize - 2];
        nextState[gridSize - 1][gridSize - 1] = cellNextState(neighbors, gridSize - 1, gridSize - 1);

        //copy next state to current state
        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){
                currentState[i][j] = nextState[i][j];
            }
        }

        draw();
    }

    private void draw(){//draw game field
        gc.setFill(Color.WHITE);
        gc.clearRect(0, 0, width, height);
        //gc.fillRect(0, 0, width, height);


        for(int i = 0; i < gridSize; i++){
            for(int j = 0; j < gridSize; j++){
                if(currentState[i][j] == 1){
                    gc.setFill(Color.BLACK);
                    gc.fillRect((double)j * CELL_SIZE, (double)i * CELL_SIZE, ((double)j + 1) * CELL_SIZE, ((double)i + 1) * CELL_SIZE);
                }
                if(currentState[i][j] == 0){
                    gc.setFill(Color.WHITE);
                    gc.fillRect((double)j * CELL_SIZE , (double)i * CELL_SIZE , ((double)j + 1) * CELL_SIZE , ((double)i + 1) * CELL_SIZE );
                }
            }
        }
        for(int i = 0; i < gridSize; i++){
//                Line line1 = new Line(0.0, (double)i * CELL_SIZE, (double)gridSize,  (double)i * CELL_SIZE);
//                Line line2 = new Line((double)i * CELL_SIZE, 0.0, (double)i * CELL_SIZE,  (double)gridSize );
//                line1.setStrokeWidth(0.5);
//                line2.setStroke(Color.WHITE);
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1.0);
            gc.strokeLine(0.0, (double)i * CELL_SIZE - 0.5, (double)gridSize * CELL_SIZE,  (double)i * CELL_SIZE - 0.5);
            gc.strokeLine((double)i * CELL_SIZE - 0.5, 0.0, (double)i * CELL_SIZE - 0.5,  (double)gridSize * CELL_SIZE);
        }
    }

    private int cellNextState(int neighbors, int i, int j){//method returns 1 if cell will be alive or born, and 0 if cell will be blank or dead
        if((neighbors == 3 && currentState[i][j] == 0) ||
          ((neighbors == 3 || neighbors == 2) && currentState[i][j] == 1)) return 1;//alive or born
        else
            return 0;//dead or blank
    }

    public void setMode(Mode mode){
        this.mode = mode;
    }

    public Mode getMode() {
        return mode;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public int getGridSize() {
        return gridSize;
    }
}
