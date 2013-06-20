package vvv.math;

public class Matrix3
{
	private float[] matrix;
	private Vec3    tmpVec = new Vec3();
	
	public Matrix3()
	{
		this(1.0f);
	}
	
	public Matrix3(final float[] mat)
	{
		matrix = new float[9];
		Matrix.copyMatrix3(mat, this.matrix);
	}
	
	public Matrix3(float val)
	{
		matrix = new float[9];
		matrix[0] = val;
		matrix[1] = 0.0f;
		matrix[2] = 0.0f;
		matrix[3] = 0.0f;
		matrix[4]=  val;
		matrix[5] = 0.0f;
		matrix[6] = 0.0f;
		matrix[7] = 0.0f;
		matrix[8] = val;
	}
	
	public Matrix3(final Matrix3 mat )
	{
		this( mat.matrix );
	}
	
	public Matrix3( final Quat q)
	{
		matrix = new float[9];
		q.mat3cast(matrix);
	}
	

	public float[] getMatrix() 
	{ 
		return matrix; 
	}

	
	public void setMatrix( final float[] mat)
	{
		Matrix.copyMatrix3( mat, this.matrix);
	}
	
	public void setMatrix( final Matrix3 mat)
	{
		Matrix.copyMatrix3( mat.matrix, this.matrix);
	}
	
	public void setMatrix( float val)
	{
		matrix[0] = val;
		matrix[1] = 0.0f;
		matrix[2] = 0.0f;
		matrix[3] = 0.0f;
		matrix[4]=  val;
		matrix[5] = 0.0f;
		matrix[6] = 0.0f;
		matrix[7] = 0.0f;
		matrix[8] = val;
	}
	
	public void setMatrix( Quat q )
	{
		q.mat3cast( matrix );
	}

	
	public void mulMatrix( final Matrix3 mat)
	{
		Matrix.multiplyMat3( matrix, mat.matrix, matrix);
	}
	
	public void mulMatrix( final Matrix3 l, final Matrix3 r)
	{
		Matrix.multiplyMat3(l.matrix, r.matrix, matrix);
	}
	
	
    public void loadRotate( float angle, Vec3 axis)
	{
		Matrix.loadRotateMat3( axis, angle, matrix );
	}
	
	public void loadRotate( float angle, float ax, float ay, float az)
	{
		Matrix.loadRotateMat3(matrix, angle, ax, ay, az);
	}
	
	
	public void rotate( float angle, float ax, float ay, float az )
	{
		Matrix.rotateMat3(matrix, angle, ax, ay, az);
	}
	
	public void rotate( float angle, Vec3 axis)
	{
		Matrix.rotateMat3(matrix, axis, angle);
	}
	
	public void rotate( final Quat q)
	{
		q.getAxis(tmpVec);
		Matrix.rotateMat3(matrix, tmpVec, q.getAngle());
	}
	
	public void scale( float s)
	{
		matrix[0] *= s;
		matrix[4] *= s;
		matrix[8] *= s;
	}
	
	public void scale( float sx, float sy, float sz)
	{
		matrix[0] *= sx;
		matrix[4] *= sy;
		matrix[8] *= sz;
	}
	
	
}
