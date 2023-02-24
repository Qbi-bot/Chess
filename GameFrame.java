import javax.swing.*;

public class GameFrame extends JFrame {
    public static GamePanel gamePanel = new GamePanel();
    public GameFrame(){
        this.add(gamePanel);
        this.setTitle("Chess");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
