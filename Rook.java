import javax.swing.*;
import java.awt.*;

public class Rook extends Figure{
    public Rook(int[] position, int whiteOrBlack) {
        super(5, position, new int[28][2], whiteOrBlack);
        super.image = new ImageIcon(getClass().getClassLoader().getResource("graphics/" + (whiteOrBlack == -1 ? "whiteRook.png" : "blackRook.png")));
        Image newImage = super.image.getImage().getScaledInstance(GamePanel.SPRITE_SIZE,GamePanel.SPRITE_SIZE, Image.SCALE_AREA_AVERAGING);
        super.image = new ImageIcon(newImage);
        super.figureJLabel = new JLabel(this.image, JLabel.CENTER);
        super.figureJLabel.setSize(GamePanel.SPRITE_SIZE,GamePanel.SPRITE_SIZE);
    }

    @Override
    public void checkMoves() {
        // 0 == left     1 == up   2 == right  3 == down
        int direction = 0;
        boolean metOtherFigure = false;

        for (int i = 0; i < possibleMoves.length; i++) {
            if (i % 7 == 0 && i != 0){
                ++direction;
                metOtherFigure = false;
            }
            if(!metOtherFigure){

                int x = direction == 0 ? -1 : 1;
                int y = direction == 1 ? -1 : 1;

                //we check here if we should even take x or y into consideration

                int doesXExist = direction % 2 == 0 ? ((i%7) + 1) : 0;
                int doesYExist = direction % 2 != 0 ? ((i%7) + 1) : 0;

                int[] finalCoords = new int[]{position[0]+(doesXExist*x),position[1]+(doesYExist*y)};
                Figure potentialFigureAtCoords = GamePanel.getFigureFromCoords(finalCoords[0],finalCoords[1]);
                possibleMoves[i] = finalCoords;
                if(potentialFigureAtCoords != null){
                    metOtherFigure = true;
                    if(potentialFigureAtCoords.color == this.color) possibleMoves[i] =  GamePanel.OUT_OF_GAME;
                }
            }else possibleMoves[i] =  GamePanel.OUT_OF_GAME;

        }
        super.potentialCaptures = this.possibleMoves;
    }
}
