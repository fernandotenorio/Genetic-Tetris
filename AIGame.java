import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.*;

import java.io.*;

public class AIGame extends GamePanel
{
 private CountDownLatch doneSignal;
 private boolean doSleep = false;
 TetrisAgent agent = new TetrisAgent();
 int cleared = 0;
 
 public AIGame(int r, int c, int bs, int seed, CountDownLatch signal)
 {
  this(r, c, bs, seed, false);
  this.doneSignal = signal;
 }
 public AIGame(int r, int c, int bs, int seed, boolean doSleep)
 {
  super(r, c, bs, seed);
  this.doSleep = doSleep;
 }
 
 public AIGame(int r, int c, int bs, boolean doSleep)
 {
  super(r, c, bs);
  this.doSleep = doSleep;
 }
     
 public void run()
 {
  fallingPiece = randomPiece();
  
  while (!gameOver)
  {
   if (board.canMoveDown(fallingPiece))
   {
    TetrisMove bestMove = MoveSearcher.getBestMove(board, fallingPiece.clonePiece(), agent);
    
    fallingPiece.position = bestMove.position;
    fallingPiece.rotation = bestMove.rotation;
    
    board.updateBoard(fallingPiece);
    
   }
   else 
   {
    board.updateBoard(fallingPiece);
    cleared +=board.checkCompleteRows();
    addNewPiece();  
   }
   
   if (doSleep)
   {
    try 
    {
     Thread.sleep(20L);
    }
    catch(InterruptedException ex){}
   }
   
   repaint();
  }
  
  agent.clearedRowsArray.add(cleared);  //acessado por varias threads!
  if (doneSignal != null)
   doneSignal.countDown();
   
   //System.out.println(cleared);
 }
 
}  //fim da classe AIGame

