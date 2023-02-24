import javax.swing.*;
import java.util.*;

public abstract class Figure{
    protected int value;
    protected ImageIcon image;
    protected int[][] possibleMoves;
    public int[] position;
    protected int[][] potentialCaptures;

    public JLabel figureJLabel;
    public int[] jLabelPosition;

    // white = -1    black = 1
    protected int color;
    int movesCount;
    public static List<Figure> figures = new ArrayList<>();


    public Figure(int value, int[] position, int[][] possibleMoves , int whiteOrBlack) {
        this.value = value;
        this.position = position;
        this.jLabelPosition = position;
        this.possibleMoves = possibleMoves;
        this.potentialCaptures = possibleMoves;
        this.color = whiteOrBlack;

        // Increasing size of both figures array and labels of figures

        figures.add(this);

    }
    public abstract void checkMoves();

    public int[][] checkMoves(int[][] possibleMoves){
        for (int i = 0; i < possibleMoves.length; i++) {
            Figure figureAtCoords = GamePanel.getFigureFromCoords(possibleMoves[i][0],possibleMoves[i][1]);
            this.possibleMoves[i] = (figureAtCoords == null || figureAtCoords.color != this.color) ? possibleMoves[i] : GamePanel.OUT_OF_GAME;
        }
        return this.possibleMoves;

    }
    // this method is made for king to check if he can go to some square where he might be under attack
    public void checkMoves(int[][] possibleMoves,Figure figure){
        if(figure instanceof King){
            for (int i = 0; i < possibleMoves.length; i++) {
                Figure figureAtCoords = GamePanel.getFigureFromCoords(possibleMoves[i][0],possibleMoves[i][1]);
                this.possibleMoves[i] = ((figureAtCoords == null || figureAtCoords.color != this.color) && checkIfMoveInPossibleCaptures(possibleMoves[i][0], possibleMoves[i][1], this).isEmpty()) ? possibleMoves[i] : GamePanel.OUT_OF_GAME;
            }

        }
    }

    // this method will exclude figure that we use it for and figures of same colour
    public static List<Figure> checkIfMoveInPossibleCaptures(int x, int y, Figure figureGiven){
        List<Figure> potentialFigures = new ArrayList<>();
        for (Figure figure : Figure.figures) {
            for (int[] potentialCapture : figure.potentialCaptures) {
                if(potentialCapture[0] == x && potentialCapture[1] == y && (figureGiven.color != figure.color)){
                    potentialFigures.add(figure);
                }
            }
        }
        return potentialFigures;
    }

    public static List<Figure> checkIfMoveInPossibleCaptures(int x, int y, Figure figureGiven, List<Figure> listGiven){
        List<Figure> potentialFigures = new ArrayList<>();
        for (Figure figure : listGiven) {
            for (int[] potentialCapture : figure.potentialCaptures) {
                if(potentialCapture[0] == x && potentialCapture[1] == y && (figureGiven.color != figure.color)){
                    potentialFigures.add(figure);
                }
            }
        }
        return potentialFigures;
    }




    @Override
    public String toString() {
        return "Figure{" +
                "position=" + Arrays.toString(position) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Figure figure = (Figure) o;
        return value == figure.value && color == figure.color && Objects.equals(image, figure.image) && Arrays.deepEquals(possibleMoves, figure.possibleMoves) && Arrays.equals(position, figure.position) && Objects.equals(figureJLabel, figure.figureJLabel) && Arrays.equals(jLabelPosition, figure.jLabelPosition);
    }
}
