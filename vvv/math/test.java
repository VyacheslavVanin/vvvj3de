package vvv.math;

public class test 
{
	public static void main(String[] args) 
	{
		float[] m = new float[16];
		Matrix.loadIdentityMat4(m);
		Matrix.translate(m, new Vec3(1.0f,2.0f,3.0f) );
		Matrix.rotateMat4(m, new Vec3(-4,2,5), help.degToRadians(45.0f) );
		
		float[] m2 = new float[16];
		
		Matrix.inverseMat4(m, m2);
		
		Matrix.multiplyMat4(m, m2, m);
		
		for( float a: m)
		{
			System.out.println( a );
		}
		
	}

}
