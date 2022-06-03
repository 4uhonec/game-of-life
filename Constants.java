public interface Constants {
    int DEFAULT_GRID_SIZE = 50;//number of cells in a grid's row, grid is a square
    int MAX_GRID_SIZE = 400;
    int MIN_GRID_SIZE = 10;

    int DEFAULT_SLEEP_TIME = 500;//default sleep, milliseconds
    int MAX_SLEEP_TIME = 2500;
    int MIN_SLEEP_TIME = 100;
    int DELTA_SLEEP_TIME = 100;

    int DELTA_GRID_CHANGE = 5;//when up/downscale button pressed, we are expanding/shrinking grid from each side by this value

    enum Mode{PAUSED, AUTO}
}
