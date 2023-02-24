import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class Knight extends Figure{

    public Knight(int[] position, int whiteOrBlack) {
        super(3, position, new int[8][2], whiteOrBlack);
        super.image = new ImageIcon(getClass().getClassLoader().getResource("graphics/" + (whiteOrBlack == -1 ? "whiteKnight.png" : "blackKnight.png")));
        Image newImage = super.image.getImage().getScaledInstance(GamePanel.SPRITE_SIZE,GamePanel.SPRITE_SIZE, Image.SCALE_AREA_AVERAGING);
        super.image = new ImageIcon(newImage);
        super.figureJLabel = new JLabel(this.image, JLabel.CENTER);
        super.figureJLabel.setSize(GamePanel.SPRITE_SIZE,GamePanel.SPRITE_SIZE);
    }

    @Override
    public void checkMoves() {
        super.checkMoves(new int[][]{{position[0] + 2,position[1] + 1}, {position[0] - 2,position[1] + 1}, {position[0] + 2,position[1] - 1}, {position[0] - 2,position[1] - 1}, {position[0] + 1,position[1] + 2}, {position[0] - 1,position[1] + 2}, {position[0] + 1,position[1] - 2}, {position[0] - 1,position[1] - 2}});
        super.potentialCaptures = this.possibleMoves;
    }

    @Override
    public String toString() {
        return "Knight{" +
                "position=" + Arrays.toString(position) +
                '}';
    }
}
