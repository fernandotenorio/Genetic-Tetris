import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TetrisAgent implements Comparable
{
 //float[] genes = new float[9];
 //float[] genes = new float[]{ 0.6f, 0.3f, 0.05f, 0.1f, 0.1f, 0.1f, 0.2f};
 //float[] genes = new float[]{ 0.95856917f, 0.08560386f, 0.9476838f, 0.58525276f, 0.0291457f, 0.18112998f, 0.17345366f};
  float[] genes = new float[]{1.4578092f, -0.16867723f, -0.16530013f, -2.921182f, -0.9268366f, -0.32727492f, -0.34364402f, -0.15645486f, 0.66300744f};

 
 List<Integer> clearedRowsArray = Collections.synchronizedList(new ArrayList<Integer>());
 float fitness;
 
 public float eval(int[][]board, int y)
 {
  float sum = 0.0f;
  
  sum += genes[0] * BoardEvaluator.clearedRows(board);
  sum += genes[1] * (2 * board.length - y + BoardEvaluator.pileHeight(board) * 0.5f);
  sum += genes[2] * BoardEvaluator.countSingleHoles(board);
  sum += genes[3] * BoardEvaluator.countConnectedHoles(board);
  sum += genes[4] * BoardEvaluator.rowTransitions(board);
  sum += genes[5] * BoardEvaluator.colTransitions(board);
  sum += genes[6] * BoardEvaluator.blocksAboveHoles(board);
  sum += genes[7] * BoardEvaluator.countWells(board);
  sum += genes[8];
  //sum += genes[6] * BoardEvaluator.bumpiness(board);
  
  return sum;
 }
 
 public int compareTo(Object obj)
 {
  if (obj instanceof TetrisAgent) 
  {
   TetrisAgent other = (TetrisAgent) obj;
   if (this.fitness > other.fitness)
    return 1;
   else if (this.fitness < other.fitness)
    return -1;
  }
  return 0;
 }
 public static TetrisAgent randomAgent()
 {
  TetrisAgent agent = new TetrisAgent();
  Random random = new Random();
  
  for (int i = 0; i < agent.genes.length; i++)
   agent.genes[i] = (float)random.nextGaussian();
  
  return agent;
 }
 
 public TetrisAgent cloneAgent()
 {
  TetrisAgent cloneAgent = new TetrisAgent();
  
  for (int i = 0; i < this.genes.length; i++)
   cloneAgent.genes[i] = this.genes[i];
  
  return cloneAgent;
 }
}

