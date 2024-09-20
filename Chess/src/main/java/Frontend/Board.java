package Frontend;

import ChessCore.BoardFile;
import ChessCore.BoardInitializer;
import ChessCore.BoardRank;
import ChessCore.ChessBoard;
import ChessCore.ChessCaretaker;
import ChessCore.ChessMemento;
import ChessCore.ClassicBoardInitializer;
import ChessCore.ClassicChessGame;
import static ChessCore.GameStatus.BLACK_UNDER_CHECK;
import static ChessCore.GameStatus.BLACK_WON;
import static ChessCore.GameStatus.WHITE_UNDER_CHECK;
import static ChessCore.GameStatus.WHITE_WON;
import ChessCore.Move;
import static ChessCore.Player.BLACK;
import static ChessCore.Player.WHITE;
import javax.swing.JToolBar;

import ChessCore.Square;
import ChessCore.Utilities;
import Pieces.Bishop;
import Pieces.King;
import Pieces.Knight;
import Pieces.Pawn;
import Pieces.Piece;
import Pieces.Queen;
import Pieces.Rook;
import javax.swing.*;
import java.awt.*;
import static java.awt.BorderLayout.NORTH;
import static java.awt.BorderLayout.SOUTH;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class Board extends JFrame {
     ChessCaretaker caretaker = new ChessCaretaker();
    JButton[][] Squares = new JButton[8][8];
    ChessCore.ClassicChessGame game = new ClassicChessGame();
    private int buttonClicked = 0;
 private Square sourceSquare;  
    private Square destinationSquare;
    private ChessBoard board = game.getBoard();
    public Board() {
        initBoard();
    }
    public void initBoard()
    {
       BoardInitializer boardInitializer = ClassicBoardInitializer.getInstance();
JToolBar toolbar = new JToolBar();
JButton undo = new JButton("Undo");
toolbar.add(undo);
undo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                       
                        updateBoard();
                        
                    }
                });
Container pane = this.getContentPane();
pane.setLayout(new BorderLayout()); 
pane.add(toolbar, BorderLayout.NORTH); 
JPanel chessBoardPanel = new JPanel(new GridLayout(8, 8));
pane.add(chessBoardPanel, BorderLayout.CENTER); 
setTitle("Chess Board");
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setSize(600, 600);
         
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Squares[row][col] = new JButton();
                Squares[row][col].setBackground(getSquareColor(row, col));
                Squares[row][col].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleButtonClick(e);
                    }
                });
                chessBoardPanel.add(Squares[row][col]);
            }
        }

        initPieces();
        setVisible(true);
    }
    private Color getSquareColor(int row, int col) {
        if ((row + col) % 2 == 0) {
            return Color.WHITE;
        } else {
            return Color.BLUE.darker().darker();
        }
    }
     private Color getSquareColorforblack(int row, int col) {
        if ((row + col) % 2 != 0) {
            return Color.WHITE;
        } else {
            return Color.BLUE.darker().darker();
        }
    }
    public void initPieces()
    {
        for(int row = 0 ; row < 8; row++)
            for(int col = 0 ; col < 8; col++)
            {
                Square s = new Square(BoardFile.values()[col], BoardRank.values()[row]);
                if(game.getPieceAtSquare(s) != null)
                {
                    ImageIcon img = new ImageIcon(getName(s)+".png");
                    JLabel l = new JLabel(img);
                    Squares[7-row][col].add(l);
                }
            }
    }
    
    public String getName(Square s)
    {
        String pieceColor;
        String piece = null;
        if(game.getPieceAtSquare(s).getOwner() == WHITE)
        {
            pieceColor = "White";
        }
        else
        {
            pieceColor = "Black";
        }
        if(game.getPieceAtSquare(s) instanceof King)
        {
            piece = "King";
        }
         if(game.getPieceAtSquare(s) instanceof Queen)
        {
            piece = "Queen";
        }
          if(game.getPieceAtSquare(s) instanceof Rook)
        {
            piece = "Rook";
        }
         if(game.getPieceAtSquare(s) instanceof Bishop)
        {
            piece = "Bishop";
        }
          if(game.getPieceAtSquare(s) instanceof Knight)
        {
            piece = "Knight";
        }
         if(game.getPieceAtSquare(s) instanceof Pawn)
        {
            piece = "Pawn";
        }
        return pieceColor +""+piece;
    }
   
  public void updateBoard() {
    for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
            Squares[row][col].removeAll();

            Square s;
            if (game.getWhoseTurn() == WHITE) {
                s = new Square(BoardFile.values()[col], BoardRank.values()[7 - row]);
            } else {
                s = new Square(BoardFile.values()[7- col], BoardRank.values()[row]);
            }
            Squares[row][col].setBackground(getSquareColor(row, col));
            if (game.getPieceAtSquare(s) != null) {
                ImageIcon img = new ImageIcon(getName(s) + ".png");
                JLabel l = new JLabel(img);
                Squares[row][col].add(l);
            }
            Squares[row][col].revalidate();
            Squares[row][col].repaint();
        }
    }
    setVisible(true);
    //game.printBoard();
}


  private void handleButtonClick(ActionEvent e) {
    JButton clickedButton = (JButton) e.getSource();

    for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
            if (Squares[row][col] == clickedButton) {
                Square clickedSquare;
                if (game.getWhoseTurn() == WHITE) {
                    clickedSquare = new Square(BoardFile.values()[col], BoardRank.values()[7 - row]);
                } else {
                    clickedSquare = new Square(BoardFile.values()[7 - col], BoardRank.values()[row]);
                }

                if (buttonClicked == 0) {
                    sourceSquare = clickedSquare;
                    ArrayList<Square> validMoves = new ArrayList<>(game.getAllValidMovesFromSquare(sourceSquare));
                    for (Square square : validMoves) {
                        if (game.getWhoseTurn() == WHITE) {
                            Squares[7 - square.getRank().ordinal()][square.getFile().ordinal()].setBackground(Color.YELLOW);
                        } else {
                            Squares[square.getRank().ordinal()][7 - square.getFile().ordinal()].setBackground(Color.YELLOW);
                        }
                    }
                    buttonClicked = 1;
                } else {
                    destinationSquare = clickedSquare;

                    Move move = new Move(sourceSquare, destinationSquare);
                    ChessMemento memento = new ChessMemento(game.getBoard());
        caretaker.saveState(memento);
                    if (game.makeMove(move)) {
                        sourceSquare = null;
                        destinationSquare = null;
                        updateBoard();
                    } else {
                        System.out.println("Invalid move");
                    }
                    if(game.getGameStatus() == WHITE_WON)
                    {
                        JOptionPane.showMessageDialog(rootPane, "White won");
                    }
                    if(game.getGameStatus() == BLACK_WON)
                    {
                        JOptionPane.showMessageDialog(rootPane, "Black won");
                    }
                  
                    updateHighlight();

                    buttonClicked = 0;
                }
            }
        }
    }
}

private void updateHighlight() {
    resetBoardColors();

    if (game.getGameStatus() == WHITE_UNDER_CHECK) {
        Square kingSquare = Utilities.getKingSquare(WHITE, board);
        highlight(kingSquare);
    } else if (game.getGameStatus() == BLACK_UNDER_CHECK) {
        Square kingSquare = Utilities.getKingSquare(BLACK, board);
        highlight(kingSquare);
    }
}

private void resetBoardColors() {
    for (int row = 0; row < 8; row++) {
        for (int col = 0; col < 8; col++) {
            if (game.getWhoseTurn() == WHITE) {
                Squares[row][col].setBackground(getSquareColor(row, col));
            } else {
                Squares[row][col].setBackground(getSquareColorforblack(row, col));
            }
        }
    }
}

private void highlight(Square square) {
    int x, y;
    if (game.getWhoseTurn() == WHITE) {
        x = square.getFile().ordinal();
        y = 7 - square.getRank().ordinal();
    } else {
        x = 7 - square.getFile().ordinal();
        y = square.getRank().ordinal();
    }
    Squares[y][x].setBackground(Color.red);
}
}