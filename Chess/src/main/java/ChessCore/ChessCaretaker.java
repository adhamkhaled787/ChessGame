package ChessCore;

import java.util.Stack;

public class ChessCaretaker {
    public Stack<ChessMemento> history = new Stack<>();
    private ChessMemento currentState;

    public void saveState(ChessMemento memento) {
        currentState = memento;
        history.push(currentState);
    }

    // Add a method to get the current state
    public ChessMemento getCurrentState() {
        return currentState;
    }
}
