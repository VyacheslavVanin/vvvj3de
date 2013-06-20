package vvv.math;

public class Matrix4
{
	private float[] matrix;
	private Vec3    tmpVec = new Vec3();
	
	public Matrix4()
	{
		this(1.0f);
	}

	public Matrix4( float val)
	{
		matrix = new float[16];
		matrix[0] =  val;
		matrix[1] = 0.0f;
		matrix[2] = 0.0f;
		matrix[3] = 0.0f;
		matrix[4] = 0.0f;
		matrix[5] =  val;
		matrix[6] = 0.0f;
		matrix[7] = 0.0f;
		matrix[8] = 0.0f;
		matrix[9] = 0.0f;
		matrix[10]=  val;
		matrix[11]= 0.0f;
		matrix[12]= 0.0f;
		matrix[13]= 0.0f;
		matrix[14]= 0.0f;
		matrix[15]=  val;
	}
	
	public Matrix4( final float[] mat)
	{
		matrix = new float[16];
		Matrix.copyMatrix4(mat, this.matrix);
	}
	
	public Matrix4( final Matrix4 mat)
	{
		this( mat.matrix );
	}
	
	public Matrix4( final Quat q)
	{
		matrix = new float[16];
		q.mat4cast(matrix);
	}
	
	public Matrix4( final Matrix3 mat)
	{
		matrix = new float[16];
		Matrix.Mat3ToMat4( mat.getMatrix(), this.matrix);
	}
	
	
	public float[] getMatrix() 
	{
		return matrix;
	}
	
	
	public void setMatrix( final float[] mat)
	{
		Matrix.copyMatrix4(mat, matrix);
	}
	
	public void setMatrix( final Matrix4 mat)
	{
		Matrix.copyMatrix4(mat.matrix, matrix);
	}
	
	public void setMatrix( float val )
	{
		matrix[0] =  val;
		matrix[1] = 0.0f;
		matrix[2] = 0.0f;
		matrix[3] = 0.0f;
		matrix[4] = 0.0f;
		matrix[5] =  val;
		matrix[6] = 0.0f;
		matrix[7] = 0.0f;
		matrix[8] = 0.0f;
		matrix[9] = 0.0f;
		matrix[10]=  val;
		matrix[11]= 0.0f;
		matrix[12]= 0.0f;
		matrix[13]= 0.0f;
		matrix[14]= 0.0f;
		matrix[15]=  val;
	}
	
	public void setMatrix( final Quat q)
	{
		q.mat4cast(matrix);
	}
	
	public void setMatrix( final Matrix3 mat)
	{
		Matrix.Mat3ToMat4( mat.getMatrix(), this.matrix);
	}
	
	
	public void mulMatrix( final Matrix4 mat)
	{
		Matrix.multiplyMat4(matrix, mat.matrix, matrix);
	}
	
	public void mulMatrix( final Matrix4 l, final Matrix4 r)
	{
		Matrix.multiplyMat4(l.matrix, r.matrix, matrix);
	}
	
	
	public void loadRotate( float angle, Vec3 axis)
	{
		Matrix.loadRotateMat4( axis, angle, matrix );
	}
	
	public void loadRotate( float angle, float ax, float ay, float az)
	{
		Matrix.loadRotateMat4(matrix, angle, ax, ay, az);
	}
	
	public void rotate(float angle, float ax, float ay, float az)
	{
		Matrix.rotateMat4(matrix, angle, ax, ay, az);
	}
	
	public void rotate( float angle, Vec3 axis)
	{
		Matrix.rotateMat4(matrix, axis, angle);
	}
	
	public void rotate( final Quat q )
	{
		q.getAxis(tmpVec);
		Matrix.rotateMat4( matrix, tmpVec, q.getAngle() );
	}
	
	public void scale( float s )
	{
		//matrix[0] *= s;
		//matrix[5] *= s;
		//matrix[10]*= s;
		Matrix.scale(matrix, s);
	}
	
	public void scale( float sx, float sy, float sz)
	{
		//matrix[0] *= sx;
		//matrix[5] *= sy;
		//matrix[10]*= sz;
		Matrix.scale(matrix, sx, sy, sz);
	}
	
	public void scale( final Vec3 vec)
	{
		scale(vec.x(), vec.y(), vec.z() );
	}
	
	public void translate( float x, float y, float z)
	{
		matrix[12] += matrix[0]*x + matrix[4]*y + matrix[8 ]*z ;
		matrix[13] += matrix[1]*x + matrix[5]*y + matrix[9 ]*z ;
		matrix[14] += matrix[2]*x + matrix[6]*y + matrix[10]*z;
		matrix[15] += matrix[3]*x + matrix[7]*y + matrix[11]*z;
	}
	
	public void translate( final Vec3 vec)
	{
		translate( vec.x(), vec.y(), vec.z() );
	}
	
	public void loadTranslate( float x, float y, float z)
	{
		matrix[0] = 1.0f;
		matrix[1] = 0.0f;
		matrix[2] = 0.0f;
		matrix[3] = 0.0f;
		matrix[4] = 0.0f;
		matrix[5] = 1.0f;
		matrix[6] = 0.0f;
		matrix[7] = 0.0f;
		matrix[8] = 0.0f;
		matrix[9] = 0.0f;
		matrix[10]= 1.0f;
		matrix[11]= 0.0f;
		matrix[12]= x;
		matrix[13]= y;
		matrix[14]= z;
		matrix[15]= 1.0f;
	}
	
	public void loadTranslate( final Vec3 vec)
	{
		loadTranslate( vec.x(), vec.y(), vec.z() );
	}
	
	
	
}
