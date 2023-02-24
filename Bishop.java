import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Bishop extends Figure{

    public Bishop(int[] position, int whiteOrBlack) {
        super(3, position, new int[28][2], whiteOrBlack);
        super.image = new ImageIcon(getClass().getClassLoader().getResource("graphics/" + (whiteOrBlack == -1 ? "whiteBishop.png" : "blackBishop.png")));
        Image newImage = super.image.getImage().getScaledInstance(GamePanel.SPRITE_SIZE,GamePanel.SPRITE_SIZE, Image.SCALE_AREA_AVERAGING);
        super.image = new ImageIcon(newImage);
        super.figureJLabel = new JLabel(this.image, JLabel.CENTER);
        super.figureJLabel.setSize(GamePanel.SPRITE_SIZE,GamePanel.SPRITE_SIZE);
    }

    @Override
    public String toString() {
        return "Bishop{" +
                "position=" + Arrays.toString(position) +
                '}';
    }

    @Override
    public void checkMoves() {
        // 0 == left-up     1 == right-up   2 == left-down  3 == right-down
        int direction = 0;
        boolean metOtherFigure = false;
        for (int i = 0; i < 28; i++) {
            if(i % 7 == 0 && i != 0){
                ++direction;
                metOtherFigure = false;
            }
            //if direction equals 0 or 2 its left. If direction equals 0 or 1 its up
            int xDirection = (position[0] + (direction % 2 == 0 ? -1 : 1)*((i%7) + 1));
            int yDirection = (position[1] +(direction < 2 ? -1 : 1)*((i%7) + 1));
            Figure figureAtDirections = GamePanel.getFigureFromCoords(xDirection,yDirection);
            if((figureAtDirections == null || figureAtDirections.color != this.color) && !metOtherFigure){
                possibleMoves[i] = new int[]{xDirection, yDirection};
                if(figureAtDirections != null && figureAtDirections.color != this.color) metOtherFigure = true;
            } else if (figureAtDirections != this){
                metOtherFigure = true;
                possibleMoves[i] = GamePanel.OUT_OF_GAME;
            } else  possibleMoves[i] = GamePanel.OUT_OF_GAME;

        }

        super.potentialCaptures = this.possibleMoves;

    }
}
