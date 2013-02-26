import java.util.Random;

public class PSOAlgorithm
{
	private float omega;
	private float phiLocal;
	private float phiGlobal;
	private int size;
	private int dims;
	private float[] min;	
	private float[] max;

	private PSOParticle[] particles;
	private Random random = new Random();
	private PSOFitnessDelegate delegate;
	private float[] bestGlobal;
	
	public PSOAlgorithm(int size, float[] min, float[] max, PSOFitnessDelegate delegate, float omega, float phiLocal, float phiGlobal)
	{
		this.size = size;
		this.dims = min.length;
		this.omega = omega;
		this.phiLocal = phiLocal;
		this.phiGlobal = phiGlobal;
		this.min = min;
		this.max = max;
		this.delegate = delegate;
			
		initSwarm();
	}

	private void initSwarm()
	{
		particles = new PSOParticle[size];
		
		bestGlobal = new float[dims];
		for (int d = 0; d < dims; d++)
			bestGlobal[d] = min[d] + (max[d] - min[d]) * random.nextFloat();

		for (int i = 0; i < size; i++)
		{
			PSOParticle particle = new PSOParticle(dims);
			particles[i] = particle;
			
			for (int d = 0; d < dims; d++)
			{
				particle.getPosition()[d] = min[d] + (max[d] - min[d]) * random.nextFloat();
				particle.getBestPosition()[d] = particle.getPosition()[d];
				float lo = -Math.abs(max[d] - min[d]);
				float hi = Math.abs(max[d] - min[d]);
				particle.getVelocity()[d] = lo + (hi - lo) * random.nextFloat();

			}

			if (delegate.fitness(particle.getBestPosition()) < delegate.fitness(bestGlobal))
			{
				for (int d = 0; d < dims; d++)
					bestGlobal[d] = particle.getBestPosition()[d];
			}
			
		}
		
	}

	public void step()
	{
		for (int i = 0; i < particles.length; i++)
		{
			PSOParticle particle = particles[i];
			float[] bestPos = particle.getBestPosition();
			float[] pos = particle.getPosition();
			
			for (int dim = 0; dim <  dims; dim++)
			{
				float rp = random.nextFloat();
				float rg = random.nextFloat();
				
				float[] vel = particle.getVelocity();
				float dxLocal = bestPos[dim] - pos[dim];
				float dxGlobal = bestGlobal[dim] - pos[dim];
				vel[dim] = omega * vel[dim] + phiLocal * rp * dxLocal + phiGlobal * rg * dxGlobal;
				pos[dim] += vel[dim];
				
				if (pos[dim] < min[dim]) 
					pos[dim] = min[dim];
				if (pos[dim] > max[dim]) 
					pos[dim] = max[dim];

			}
		
			float bestFit = delegate.fitness(bestPos);
			float newFit = delegate.fitness(pos);
			
			if (newFit < bestFit)
			{
				for (int dim = 0; dim <  dims; dim++)
					bestPos[dim] = pos[dim];
				
				if (newFit < delegate.fitness(bestGlobal))
					for (int dim = 0; dim <  dims; dim++)
						bestGlobal[dim] = bestPos[dim];
			}
			
		}
	}
	
	public float[] getBestGlobal()
	{
		return this.bestGlobal;
	}
	
	public static void main (String[] args)
	{
		PSOAlgorithm swarm = new PSOAlgorithm(50, 
		new float[]{-100.0f, -100.0f, -100.0f, -100.0f, -100.0f}, 
		new float[]{100.0f, 100.0f, 100.0f, 100.0f, 100.0f},
		
		new PSOFitnessDelegate(){
			public float fitness(float[] position)
			{
				float sum = 0.0f;
				for (int i = 0; i < position.length; i++)
					sum += position[i];
				return sum;
			}
		}, 0.6f, 1.3f, 1.3f);
		
		for (int k = 0; k < 1000; k++)
			swarm.step();
	
		for (int i = 0; i < swarm.bestGlobal.length; i++)
			System.out.println(swarm.bestGlobal[i]);
	}

}
