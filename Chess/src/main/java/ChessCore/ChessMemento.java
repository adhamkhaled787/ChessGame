
package ChessCore;


public class ChessMemento {
    private final ChessBoard board;
    public ChessMemento(ChessBoard board)
    {
        this.board = board;
    }

   

    public ChessBoard getBoard() {
        return board;
    }
    
}
