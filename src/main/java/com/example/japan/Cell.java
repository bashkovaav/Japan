package com.example.japan;

public class Cell {
    public enum State {
        EMPTY,       // Белая
        FILLED,      // Черная
        MARKED       // Белая с точкой
    }

    private State state;
    private boolean locked;
    private final int row;
    private final int col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.state = State.EMPTY;
        this.locked = false;
    }

    // Геттеры и сеттеры
    public State getState() { return state; }
    public void setState(State state) { this.state = state; }

    public boolean isLocked() { return locked; }
    public void setLocked(boolean locked) { this.locked = locked; }

    public int getRow() { return row; }
    public int getCol() { return col; }

    // Переключение состояния по левому клику
    public void toggleState() {
        if (!locked) {
            switch (state) {
                case EMPTY:
                    state = State.FILLED;
                    break;
                case FILLED:
                    state = State.EMPTY;
                    break;
                case MARKED:
                    state = State.FILLED;
                    break;
            }
        }
    }

    // Переключение блокировки по правому клику
    public void toggleLock() {
        if (state != State.FILLED) {
            locked = !locked;
            if (locked && state == State.EMPTY) {
                state = State.MARKED;
            } else if (!locked && state == State.MARKED) {
                state = State.EMPTY;
            }
        }
    }
}