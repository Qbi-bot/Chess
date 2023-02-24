    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.util.ArrayList;
    import java.util.List;


    public class GamePanel extends JPanel implements MouseListener, MouseMotionListener{

        static final int SCREEN_EDGE = 800;
        static final int UNIT_SIZE = 100;
        static final int MARGIN = 5;
        public static final int SPRITE_SIZE = 90;
        public static final int[] OUT_OF_GAME = new int[]{-9999,-9999};
        static Figure currentFigure;
        private static King whiteKing;
        private static King blackKing;
        private static List<Figure> potentialCheckingFigure;
        private static boolean checked = false;
        public static int allMovesCount = 0;
        public static int whoseMove = -1;
        public GamePanel() {
            this.setPreferredSize(new Dimension(SCREEN_EDGE, SCREEN_EDGE));
            this.setFocusable(true);
            createAllFigures();
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
            repaint();
        }

        // You have to initialize figures in createAllFigures() method or otherwise game won't function correctly

        public void createAllFigures(){
            //white

            whiteKing = new King(new int[]{4,7}, -1);

            new Queen(new int[]{3,7}, -1);

            for (int i = 0; i < 2; i++) {
                new Rook(new int[]{(i * 7),7}, -1);
            }

            for (int i = 0; i < 2; i++) {
                new Knight(new int[]{1 +(i*5),7}, -1);
            }

            for (int i = 0; i < 2; i++) {
                new Bishop(new int[]{2 +(i*3),7}, -1);
            }
            for (int i = 0; i < 8; i++) {
                new Pawn(new int[]{i,6}, -1);
            }

            //black

            blackKing = new King(new int[]{4,0}, 1);

            new Queen(new int[]{3,0}, 1);

            for (int i = 0; i < 2; i++) {
                new Rook(new int[]{(i * 7),0}, 1);
            }

            for (int i = 0; i < 2; i++) {
                new Knight(new int[]{1 +(i*5),0}, 1);
            }

            for (int i = 0; i < 2; i++) {
                new Bishop(new int[]{2 +(i*3),0}, 1);
            }
            for (int i = 0; i < 8; i++) {
                new Pawn(new int[]{i,1}, 1);
            }

            for (Figure figure : Figure.figures) {
                figure.checkMoves();
            }

        }

        public void draw(Graphics g){

            // Drawing board

            for (int y = 0; y < SCREEN_EDGE/UNIT_SIZE; y++) {

                // 1 == "white"   2 == "black"
                int color = (y % 2 == 0) ? 1 : 2;

                for (int x = 0; x < SCREEN_EDGE/UNIT_SIZE; x++) {
                    g.setColor(color == 1 ? new Color(239,217, 181) : new Color(180, 136,98));
                    g.fillRect(x*UNIT_SIZE, y*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    color = color == 1 ? 2 : 1;
                }
            }

            drawFigures();

            // Drawing moves if there is currentFigure

            if (currentFigure != null && currentFigure.color == whoseMove && (!checked || currentFigure instanceof King)){
                g.setColor(Color.black);
                for (int i = 0; i < currentFigure.possibleMoves.length; i++) {
                    g.drawOval((currentFigure.possibleMoves[i][0] * UNIT_SIZE) + MARGIN, (currentFigure.possibleMoves[i][1] * UNIT_SIZE) + MARGIN, SPRITE_SIZE,SPRITE_SIZE);
                }
            }
        }

        public void drawFigures(){
            for (Figure figure : Figure.figures) {
                if(figure != null){
                    this.add(figure.figureJLabel);
                    figure.figureJLabel.setLocation(figure.position[0]*UNIT_SIZE + MARGIN ,figure.position[1]*UNIT_SIZE + MARGIN);
                }
            }
        }

        public void move(int x, int y){
            if(currentFigure != null){
                x /= UNIT_SIZE;
                y /= UNIT_SIZE;
                boolean isLegal = false;
                for (int i = 0; i < currentFigure.possibleMoves.length; i++) {
                    if (x == currentFigure.possibleMoves[i][0] && y == currentFigure.possibleMoves[i][1] && currentFigure.color == whoseMove && (!checked || currentFigure instanceof King)) {
                        isLegal = true;
                        break;
                    }
                }
                if (isLegal){
                    currentFigure.position[0] = x;
                    currentFigure.position[1] = y;
                    currentFigure.movesCount++;
                    allMovesCount++;

                    // checks for en passant

                    if (currentFigure instanceof Pawn && (currentFigure.possibleMoves[4][0] == x && currentFigure.possibleMoves[4][1] == y || currentFigure.possibleMoves[5][0] == x && currentFigure.possibleMoves[5][1] == y)){
                        capture(getFigureFromCoords(x, y - currentFigure.color));
                    } else capture(getFigureFromCoords(x,y));

                    // checks for castle

                    if (currentFigure instanceof King) {
                        if(((King) currentFigure).shortRook != null){
                            ((King) currentFigure).shortRook.position = (currentFigure.possibleMoves[8][0] == x && currentFigure.possibleMoves[8][1] == y) ? new int[]{currentFigure.position[0] - 1, currentFigure.position[1]} : ((King) currentFigure).shortRook.position;
                        }
                        if(((King) currentFigure).longRook != null){
                            ((King) currentFigure).longRook.position = (currentFigure.possibleMoves[9][0] == x && currentFigure.possibleMoves[9][1] == y) ? new int[]{currentFigure.position[0] + 1, currentFigure.position[1]} : ((King) currentFigure).longRook.position;
                        }
                    }
                    for (Figure figure : Figure.figures) {
                        figure.checkMoves();
                    }
                    whoseMove *= -1;
                    checked = false;
                }
                currentFigure = null;
            }
        }
        public void capture(Figure figure){
            if(figure != null){
                figure.figureJLabel.setLocation(OUT_OF_GAME[0], OUT_OF_GAME[1]);
                Figure.figures.remove(figure);
            }
        }

        // If it discovers check returns true otherwise false
        public boolean wouldDiscoverCheck(Figure figure){
            List<Figure> updatedFigures = new ArrayList<>();
            updatedFigures.addAll(Figure.figures);
            updatedFigures.remove(figure);
            return !findChecks(updatedFigures).isEmpty();
        }



        public static Figure getFigureFromCoords(int x, int y){
            for (Figure figure : Figure.figures) {
                if(currentFigure == null){
                    if(figure.position[0] == x && figure.position[1] == y) return figure;
                }else {
                    if(figure.position[0] == x &&  figure.position[1] == y && figure != currentFigure) return figure;
                }
            }
            return null;
        }
        // This method checks if king of player who moves is under a check
        public static List<Figure> findChecks(){
            King currentKing = (whoseMove == -1 ? whiteKing : blackKing);
            List<Figure> potentialCheckingFigure = Figure.checkIfMoveInPossibleCaptures(currentKing.position[0], currentKing.position[1], currentKing);
            return potentialCheckingFigure;
        }

        //

        public static List<Figure> findChecks(List<Figure> figuresGiven){
            King currentKing = (whoseMove == -1 ? whiteKing : blackKing);
            List<Figure> potentialCheckingFigure = Figure.checkIfMoveInPossibleCaptures(currentKing.position[0], currentKing.position[1], currentKing, figuresGiven);
            return potentialCheckingFigure;
        }

        // MouseListener methods

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e){
            potentialCheckingFigure = findChecks();
            checked = !potentialCheckingFigure.isEmpty();
            currentFigure = getFigureFromCoords(e.getX()/UNIT_SIZE,e.getY()/UNIT_SIZE);
            if(currentFigure != null){
                currentFigure.checkMoves();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            move(e.getX(),e.getY());
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        // MouseMotionListener methods

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }