package MainGame;



import javax.swing.*;

public class Player {
    private String name;

    public Player(String ordre) {
        JFrame frame = new JFrame("Entrez le nom");
        this.name = JOptionPane.showInputDialog(frame, "Entrez le nom du " + ordre + ":");
    }

    public String getName() {
        return name;
    }
}
