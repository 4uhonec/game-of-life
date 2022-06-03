import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.util.Duration;



public class GameOfLifeController implements Constants{
    private Game game;
    private boolean started = false;
    private Timeline loop = null;


    @FXML
    private Canvas canvas;

    @FXML
    void newGameButtonPressed(ActionEvent event) {
        startNewGame();
        if(loop == null){
            loop = new Timeline(new KeyFrame(Duration.millis(game.getSleepTime()), e -> {
                game.next();
            }));
        }else loop.pause();
    }

    @FXML
    void nextStepButtonPressed(ActionEvent event) {
        if(game!=null){
            game.setMode(Mode.PAUSED);
            loop.pause();
            game.next();
        }
        else{
            startNewGame();
        }
    }

    @FXML
    void autoplayButtonPressed(ActionEvent event) {
        if(started){
            if(game.getMode() == Mode.AUTO){
                game.setMode(Mode.PAUSED);
                loop.pause();
            }else{
                game.setMode(Mode.AUTO);
                loop.setCycleCount(loop.INDEFINITE);
                loop.play();
            }
        }
    }

    @FXML
    void biggerButtonPressed(ActionEvent event) {
        if(started){

        }
    }

    @FXML
    void smallerButtonPressed(ActionEvent event) {
        if(started){

        }
    }

    @FXML
    void fasterButtonPressed(ActionEvent event) {
        if(started){
            if(game.getSleepTime() > MIN_SLEEP_TIME){
                game.setSleepTime(game.getSleepTime() - DELTA_SLEEP_TIME);
                double rate = loop.getRate();
                loop.setRate(rate + ((double)DELTA_SLEEP_TIME/1000.0)/rate);
            }
        }
    }

    @FXML
    void slowerButtonPressed(ActionEvent event) {
        if(started){
            if(game.getSleepTime() < MAX_SLEEP_TIME) {
                game.setSleepTime(game.getSleepTime() + DELTA_SLEEP_TIME);
                double rate = loop.getRate();
                loop.setRate(rate - ((double)DELTA_SLEEP_TIME/1000.0)/rate);
            }
        }
    }

    private void startNewGame(){
        game = new Game(canvas);
        game.randomFill();
        started = true;
    }
}
