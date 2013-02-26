import java.util.Random;

public class PSOParticle
{
	private int dims;
	private float[] bestPosition;
	private float[] position;
	private float[] velocity;

	public PSOParticle(int dims)
	{
		this.dims = dims;
		this.bestPosition = new float[dims];
		this.velocity = new float[dims];
		this.position = new float[dims];
	}

	public float[] getVelocity()
	{
		return this.velocity;
	}

	public float[] getPosition()
	{
		return this.position;
	}

	public float[] getBestPosition()
	{
		return this.bestPosition;
	}
}
