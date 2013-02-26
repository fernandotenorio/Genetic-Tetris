import java.util.Random;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.io.*;

public class RunPSO implements PSOFitnessDelegate
{
	/* Tetris params */
	int gamesAg = 4;
	int rows = 20;
	int cols = 10;
	int blockSize = 1;
	
	/* PSO params */
	int maxiter = 100;
	int popSize = 50;
	float[] min = new float[6];
	float[] max = new float[6];
	float omega = 0.12f;
	float phil = 0.9f;
	float phig = 0.9f;
	Random random = new Random();
	int seed = random.nextInt();
	
	float bestGlobal = 0;
	
	public void run()
	{
		for (int i = 0; i < min.length; i++)
		{
			min[i] = -5.0f;
			max[i] = 5.0f;
		}
		PSOAlgorithm pso = new PSOAlgorithm(popSize, min, max, this, omega, phil, phig);
		for (int k = 0; k < maxiter; k++)
			pso.step();
			
		float[] best = pso.getBestGlobal();
		for (int i = 0; i < best.length; i++)
			System.out.println(best[i]);
	}
	
	public float fitness(float[] x)
	{
		 TetrisAgent agent = new TetrisAgent();
		 
		 for (int i = 0; i < agent.genes.length; i++)
			agent.genes[i] = x[i];
		
		CountDownLatch doneSignal = new CountDownLatch(gamesAg);
		for (int i = 0; i < gamesAg; i++)
		{
			AIGame game = new AIGame(rows, cols, blockSize, i + seed, doneSignal); 
			game.agent = agent;
			new Thread(game).start();
		}
		
		 try
		{
			doneSignal.await();   
		}
		catch(InterruptedException ex){}
   
		float average = 0.0f;
		for (Integer cleared : agent.clearedRowsArray)
			average += cleared;
			
		agent.fitness = 1.0f * average/gamesAg;
		
		if (-agent.fitness < bestGlobal)
			bestGlobal = -agent.fitness;
			
		System.out.println(bestGlobal);
		
		return -agent.fitness;
	}
	
	public static void main (String[] args)
	{
		RunPSO alg = new RunPSO();
		alg.run();
		
	}
}