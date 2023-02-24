import javax.swing.*;
import java.awt.*;

public class King extends Figure{

    Figure shortRook;
    Figure longRook;

    public King(int[] position, int whiteOrBlack) {
        super(0, position, new int[10][2], whiteOrBlack);
        super.image = new ImageIcon(getClass().getClassLoader().getResource("graphics/" + (whiteOrBlack == -1 ? "whiteKing.png" : "blackKing.png")));
        Image newImage = super.image.getImage().getScaledInstance(GamePanel.SPRITE_SIZE,GamePanel.SPRITE_SIZE, Image.SCALE_AREA_AVERAGING);
        super.image = new ImageIcon(newImage);
        super.figureJLabel = new JLabel(this.image, JLabel.CENTER);
        super.figureJLabel.setSize(GamePanel.SPRITE_SIZE,GamePanel.SPRITE_SIZE);
    }

    @Override
    public void checkMoves() {
        int[][] moveGrid = new int[][]{{position[0] + 1, position[1]} , {position[0] + 1, position[1] +1} , {position[0], position[1] + 1}, {position[0] -1, position[1]}, {position[0] - 1, position[1] - 1}, {position[0], position[1] - 1}, {position[0] - 1, position[1] + 1}, {position[0] + 1, position[1] - 1}};
        super.potentialCaptures = moveGrid;
        super.checkMoves(moveGrid, this);

        // code for castle

        boolean canCastleShort = true;
        boolean canCastleLong = true;
        int rookMovesCountShort = -1;
        int rookMovesCountLong = -1;
        for (Figure figure : figures) {
            if (((figure.position[0] > position[0] && !(figure instanceof Rook)) && figure.position[1] == position[1]) || position[1] != (color == -1 ? 7 : 0) || !checkIfMoveInPossibleCaptures(this.position[0] + 1, position[1], this).isEmpty() || !checkIfMoveInPossibleCaptures(this.position[0] + 2, position[1], this).isEmpty()) canCastleShort = false;
            if (((figure.position[0] < position[0] && !(figure instanceof Rook)) && figure.position[1] == position[1]) || position[1] != (color == -1 ? 7 : 0) || !checkIfMoveInPossibleCaptures(this.position[0] - 1, position[1], this).isEmpty() || !checkIfMoveInPossibleCaptures(this.position[0] - 2, position[1], this).isEmpty()) canCastleLong = false;
            if(figure.position[0] > position[0] && figure instanceof Rook && figure.position[1] == position[1]){
                rookMovesCountShort = figure.movesCount;
                shortRook = figure;
            }
            if(figure.position[0] < position[0] && figure instanceof Rook && figure.position[1] == position[1]){
                rookMovesCountLong = figure.movesCount;
                longRook = figure;
            }
        }
        possibleMoves[8] = (canCastleShort && movesCount == 0 && rookMovesCountShort == 0) ? new int[]{position[0] + 2, position[1]} : GamePanel.OUT_OF_GAME;
        possibleMoves[9] = (canCastleLong && movesCount == 0 && rookMovesCountLong == 0) ? new int[]{position[0] - 2, position[1]} : GamePanel.OUT_OF_GAME;
    }
}
