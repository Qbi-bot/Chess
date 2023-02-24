import javax.swing.*;
import java.awt.*;
import java.util.Arrays;


public class Pawn extends Figure{


    public Pawn(int[] position, int whiteOrBlack) {
        super(1, position, new int[6][2], whiteOrBlack);
        super.image = new ImageIcon(getClass().getClassLoader().getResource("graphics/" + (whiteOrBlack == -1 ? "whitePawn.png" : "blackPawn.png")));
        Image newImage = super.image.getImage().getScaledInstance(GamePanel.SPRITE_SIZE,GamePanel.SPRITE_SIZE, Image.SCALE_AREA_AVERAGING);
        super.image = new ImageIcon(newImage);
        super.figureJLabel = new JLabel(this.image, JLabel.CENTER);
        super.figureJLabel.setSize(GamePanel.SPRITE_SIZE,GamePanel.SPRITE_SIZE);
    }


    @Override
    public String toString() {
        return "Pawn{" +
                "position=" + Arrays.toString(position) +
                '}';
    }

    @Override
    public void checkMoves() {
        boolean canTakeOne = false;
        boolean canTakeTwo = false;
        boolean canSingleMove = true;
        boolean canDoubleMove = true;
        boolean canEnPassantOne = false;
        boolean canEnPassantTwo = false;

        for (Figure figure : Figure.figures) {
            //moves
            if(figure.position[0] == position[0] && figure.position[1] == position[1] + color) canSingleMove = false;
            if(!canSingleMove || (figure.position[0] == position[0] && figure.position[1] == position[1] + (2*color)) || movesCount != 0) canDoubleMove = false;
            //takes
            if(figure.position[0] == position[0] + 1 && figure.position[1] == position[1] + color && figure.color != this.color) canTakeOne = true;
            if(figure.position[0] == position[0] - 1 && figure.position[1] == position[1] + color && figure.color != this.color) canTakeTwo = true;
            //enPassant
            if(figure.movesCount == 1 && figure.position[0] == position[0] + 1 && figure.position[1] == (figure.color == -1 ? 4 : 3) && figure.color != color && figure.position[1] == this.position[1]) canEnPassantOne = true;
            if(figure.movesCount == 1 && figure.position[0] == position[0] - 1 && figure.position[1] == (figure.color == -1 ? 4 : 3) && figure.color != color && figure.position[1] == this.position[1]) canEnPassantTwo = true;
        }
        possibleMoves[0] = canTakeOne ? new int[]{position[0] + 1, position[1] + color} : GamePanel.OUT_OF_GAME;
        possibleMoves[1] = canSingleMove ? new int[]{position[0], position[1] + color} : GamePanel.OUT_OF_GAME;
        possibleMoves[2] = canTakeTwo? new int[]{position[0] - 1, position[1] + color} : GamePanel.OUT_OF_GAME;
        possibleMoves[3] = canDoubleMove ? new int[]{position[0], position[1] + (2*color)} : GamePanel.OUT_OF_GAME;
        possibleMoves[4] = canEnPassantOne ? new int[]{position[0] + 1, position[1] + color} : GamePanel.OUT_OF_GAME;
        possibleMoves[5] = canEnPassantTwo ? new int[]{position[0] - 1, position[1] + color} : GamePanel.OUT_OF_GAME;

        super.potentialCaptures = new int[][]{{position[0] + 1, position[1] + color},{position[0] - 1, position[1] + color}};

    }
}
