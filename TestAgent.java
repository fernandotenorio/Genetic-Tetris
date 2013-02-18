import javax.swing.*;
import java.awt.*;

public class TestAgent
{
 public static void main (String[] args)
 {
  JFrame f = new JFrame();
  int rows = 20, cols = 10, block = 32;
  boolean sleep = true;  //para atualizar o panel
  AIGame ai = new AIGame(rows, cols, block, sleep);
  ai.setBackground(Color.black);
  f.add(ai);
  f.pack();
  f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  f.setResizable(false);
  f.setVisible(true);
  new Thread(ai).start();
 }
}

