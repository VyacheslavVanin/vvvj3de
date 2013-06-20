package vvv.math;


public class Quat 
{
	private float x,y,z,w;
	
	public Quat() { x=0; y=0; z=0; w=1;}
	public Quat( float w, float x, float y, float z) 
	{ 
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	} 

	public Quat( final Quat q)
	{
		this.w = q.w;
		this.x = q.x;
		this.y = q.y;
		this.z = q.z;
	}
	
	
	public void set( float w, float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void set( final Quat q)
	{
		this.x = q.x;
		this.y = q.y;
		this.z = q.z;
		this.w = q.w;
	}
	
	
	/** xyz has to be axis values of normalized vector,
	 *  angle - in radians  */
	public void setAngleAxis( float angle, float x, float y, float z) 
	{
		float tmp2 = angle * 0.5f;
		float tmp1 = FloatMath.sin( tmp2 );
		
		this.w =  FloatMath.cos( tmp2 );
		this.x = x * tmp1;
		this.y = y * tmp1;
		this.z = z * tmp1;
	}
	
	public void setAngleAxis( float angle, final Vec3 v) 
	{
		float tmp2 = angle * 0.5f;
		float tmp1 = FloatMath.sin( tmp2 );
		
		this.w =  FloatMath.cos( tmp2 );
		this.x = v.x() * tmp1;
		this.y = v.y() * tmp1;
		this.z = v.z() * tmp1;
	};
	
	public float getAngle()
	{
		return (float)Math.acos( this.w ) * 2.0f;
	}
	
	public Vec3 getAxis()
	{
		float tmp1 = 1.0f - w*w;
		if( tmp1 <= 0.0f )
		{
			return new Vec3( 0.0f, 0.0f, 1.0f );
		}
		float tmp2 = 1.0f / FloatMath.sqrt( tmp1 );
		return new Vec3( x*tmp2, y*tmp2, z*tmp2);
	}
	
	public void getAxis( Vec3 out )
	{
		float tmp1 = 1.0f - w*w;
		if( tmp1 <= 0.0f )
		{
			out.set( 0.0f, 0.0f, 1.0f );
			return;
		}
		float tmp2 = 1.0f / FloatMath.sqrt( tmp1 );
		out.set( x*tmp2, y*tmp2, z*tmp2);
	}
	
	public void setEuler( float pitch, float yaw, float roll )
	{
		float tmp1 = FloatMath.cos( pitch * 0.5f );
		float tmp2 = FloatMath.cos( yaw * 0.5f );
		float tmp3 = FloatMath.cos( roll * 0.5f );
		float tmp4 = FloatMath.sin( pitch * 0.5f );
		float tmp5 = FloatMath.sin( yaw * 0.5f );
		float tmp6 = FloatMath.sin( roll * 0.5f );
		this.x = tmp4*tmp2*tmp3 + tmp1*tmp5*tmp6;
		this.y = tmp1*tmp5*tmp3 + tmp4*tmp2*tmp6;
		this.z = tmp1*tmp2*tmp6 + tmp4*tmp5*tmp3;
		this.w = tmp1*tmp2*tmp3 + tmp4*tmp5*tmp6;
	}
	
	public Quat mul( final Quat p )
	{
		float tmp1 = w*p.w - x*p.x - y*p.y - z*p.z;
		float tmp2 = w*p.x + x*p.w + y*p.z - z*p.y;
		float tmp3 = w*p.y + y*p.w + z*p.x - x*p.z;
		float tmp4 = w*p.z + z*p.w + x*p.y - y*p.x;
		this.x = tmp2;
		this.y = tmp3;
		this.z = tmp4;
		this.w = tmp1;
		return this;
	}
	
	public Quat rotate( float angle, float ax, float ay, float az)
	{
		float tmp2 = angle * 0.5f;
		float tmp1 = FloatMath.sin( tmp2 );
		
		float tmpw =  FloatMath.cos( tmp2 );
		float tmpx = ax * tmp1;
		float tmpy = ay * tmp1;
		float tmpz = az * tmp1;
		
		tmp1 = w*tmpw - x*tmpx - y*tmpy - z*tmpz;
		tmp2 = w*tmpx + x*tmpw + y*tmpz - z*tmpy;
		float tmp3 = w*tmpy + y*tmpw + z*tmpx - x*tmpz;
		float tmp4 = w*tmpz + z*tmpw + x*tmpy - y*tmpx;
		this.x = tmp2;
		this.y = tmp3;
		this.z = tmp4;
		this.w = tmp1;
		
		return this;
	}
	
	
	public Quat cross( final Quat p)
	{
		float tmp1 = w*p.w - x*p.x - y*p.y - z*p.z;
		float tmp2 = w*p.x + x*p.w + y*p.z - z*p.y;
		float tmp3 = w*p.y + y*p.w + z*p.x - x*p.z;
		float tmp4 = w*p.z + z*p.w + x*p.y - y*p.x;
		this.x = tmp2;
		this.y = tmp3;
		this.z = tmp4;
		this.w = tmp1;
		return this;
	}
	
	public static void mul( final Quat q, final Quat p, Quat out)
	{
		float tmpw = q.w*p.w - q.x*p.x - q.y*p.y - q.z*p.z;
		float tmpx = q.w*p.x + q.x*p.w + q.y*p.z - q.z*p.y;
		float tmpy = q.w*p.y + q.y*p.w + q.z*p.x - q.x*p.z;
		float tmpz = q.w*p.z + q.z*p.w + q.x*p.y - q.y*p.x;
		out.x = tmpx;
		out.y = tmpy;
		out.z = tmpz;
		out.w = tmpw;
	}
	
	public static void cross( final Quat q, final Quat p, Quat out)
	{
		float tmp1 = q.w*p.w - q.x*p.x - q.y*p.y - q.z*p.z;
		float tmp2 = q.w*p.x + q.x*p.w + q.y*p.z - q.z*p.y;
		float tmp3 = q.w*p.y + q.y*p.w + q.z*p.x - q.x*p.z;
		float tmp4 = q.w*p.z + q.z*p.w + q.x*p.y - q.y*p.x;
		out.x = tmp2;
		out.y = tmp3;
		out.z = tmp4;
		out.w = tmp1;
	}
	
	public static Quat newMul( final Quat q, final Quat p)
	{
		return new Quat( q.w*p.w - q.x*p.x - q.y*p.y - q.z*p.z, 
						 q.w*p.x + q.x*p.w + q.y*p.z - q.z*p.y, 
						 q.w*p.y + q.y*p.w + q.z*p.x - q.x*p.z, 
						 q.w*p.z + q.z*p.w + q.x*p.y - q.y*p.x);
	}
	
	public static Quat newCross( final Quat q, final Quat p)
	{
		return new Quat( q.w*p.w - q.x*p.x - q.y*p.y - q.z*p.z, 
						 q.w*p.x + q.x*p.w + q.y*p.z - q.z*p.y, 
						 q.w*p.y + q.y*p.w + q.z*p.x - q.x*p.z, 
						 q.w*p.z + q.z*p.w + q.x*p.y - q.y*p.x);
	} 
	
	public Quat mul(float s)
	{
		x *= s;
		y *= s;
		z *= s;
		w *= s;
		return this;
	}
	
	public Quat div(float s)
	{
		x /= s;
		y /= s;
		z /= s;
		w /= s;
		return this;
	}
	
	public static void mul( final Quat q, float s, Quat out)
	{
		out.x = q.x * s;
		out.y = q.y * s;
		out.z = q.z * s;
		out.w = q.w * s;
	}
	
	public static void div( final Quat q, float s, Quat out)
	{
		out.x = q.x / s;
		out.y = q.y / s;
		out.z = q.z / s;
		out.w = q.w / s;
	}
	
	public static Quat newMul( final Quat q, float s)
	{
		return new Quat( q.w*s, q.x*s, q.y*s, q.z*s );
	}
	
	public static Quat newDiv( final Quat q, float s)
	{
		return new Quat( q.w/s, q.x/s, q.y/s, q.z/s );
	}
	
	public float dot(final Quat q)
	{
		return x*q.x + y*q.y + z*q.z + w*q.w;
	}
	
	public static float dot( final Quat q1, final Quat q2)
	{
		return q1.x*q2.x + q1.y*q2.y + q1.z*q2.z + q1.w*q2.w;
	}
	
	public float length2()
	{
		return x*x + y*y + z*z + w*w;
	}
	
	public float length()
	{
		return FloatMath.sqrt( x*x + y*y + z*z + w*w );
	}
	
	public void normalize()
	{	
		float len = length();
		if(len <= 0)
		{
			x = 0;
			y = 0;
			z = 0;
			w = 1;
		}
		len = 1.0f / len;
		x *=len;
		y *=len;
		z *=len;
		w *=len;
	}
	
	public static void normalize( final Quat q, Quat out)
	{
		out.set( q );
		out.normalize();
	}
	
	public static Quat newNormalize( final Quat q)
	{
		Quat ret = new Quat(q);
		ret.normalize();
		return ret;
	}
	
	public void add( final Quat q)
	{
		x += q.x;
		y += q.y;
		z += q.z;
		w += q.w;
	}
	
	public static void add( final Quat q1, final Quat q2, Quat out )
	{
		out.x = q1.x + q2.x;
		out.y = q1.y + q2.y;
		out.z = q1.z + q2.z;
		out.w = q1.w + q2.w;
	}
	
	public static Quat newAdd( final Quat q1, final Quat q2 )
	{
		return new Quat( q1.w + q2.w, 
						 q1.x + q2.x, 
						 q1.y + q2.y, 
						 q1.z + q2.z );
	}
	
	public void conjugate()
	{
		x = -x;
		y = -y;
		z = -z;
	}
	
	public void inverse()
	{
		float len = 1.0f / length2();
		x = -x * len;
		y = -y * len;
		z = -z * len;
		w *= len;
	}
	
	public static void inverse( final Quat q, Quat out)
	{
		float len = 1.0f / q.length2();
		out.x = -q.x * len;
		out.y = -q.y * len;
		out.z = -q.z * len;
		out.w = q.w * len;
	}
	
	public static Quat newInverse( final Quat q)
	{
		float len = 1.0f / q.length2();
		return new Quat( q.w*len, -q.x*len, -q.y*len, -q.z*len );
	}
	
	
	public void mat3cast( float[] mat)
	{
		mat[0] = 1.0f - 2.0f*y*y - 2.0f*z*z;
        mat[1] = 2.0f*x*y + 2.0f*w*z;
        mat[2] = 2.0f*x*z - 2.0f*w*y;

        mat[3] = 2.0f*x*y - 2.0f*w*z;
        mat[4] = 1.0f - 2.0f*x*x - 2.0f*z*z;
        mat[5] = 2.0f*y*z + 2.0f*w*x;

        mat[6] = 2.0f*x*z + 2.0f*w*y;
        mat[7] = 2.0f*y*z - 2.0f*w*x;
        mat[8] = 1.0f - 2.0f*x*x - 2.0f*y*y;
	}
	
	public void mat4cast( float[] mat )
	{
		mat[0] = 1.0f - 2.0f*y*y - 2.0f*z*z;
        mat[1] = 2.0f*x*y + 2.0f*w*z;
        mat[2] = 2.0f*x*z - 2.0f*w*y;
        mat[3] = 0.0f;
        mat[4] = 2.0f*x*y - 2.0f*w*z;
        mat[5] = 1.0f - 2.0f*x*x - 2.0f*z*z;
        mat[6] = 2.0f*y*z + 2.0f*w*x;
        mat[7] = 0.0f;
        mat[8] = 2.0f*x*z + 2.0f*w*y;
        mat[9] = 2.0f*y*z - 2.0f*w*x;
        mat[10] = 1.0f - 2.0f*x*x - 2.0f*y*y;
        mat[11] = 0.0f;
        mat[12] = 0.0f;
        mat[13] = 0.0f;
        mat[14] = 0.0f;
        mat[15] = 1.0f;
	}
	
	public float[] mat3cast()
	{
		float[] mat = new float[9];
		mat[0] = 1.0f - 2.0f*y*y - 2.0f*z*z;
        mat[1] = 2.0f*x*y + 2.0f*w*z;
        mat[2] = 2.0f*x*z - 2.0f*w*y;

        mat[3] = 2.0f*x*y - 2.0f*w*z;
        mat[4] = 1.0f - 2.0f*x*x - 2.0f*z*z;
        mat[5] = 2.0f*y*z + 2.0f*w*x;

        mat[6] = 2.0f*x*z + 2.0f*w*y;
        mat[7] = 2.0f*y*z - 2.0f*w*x;
        mat[8] = 1.0f - 2.0f*x*x - 2.0f*y*y;
		return mat;
	}
	
	public float[] mat4cast()
	{
		float[] mat = new float[16];
		mat[0] = 1.0f - 2.0f*y*y - 2.0f*z*z;
        mat[1] = 2.0f*x*y + 2.0f*w*z;
        mat[2] = 2.0f*x*z - 2.0f*w*y;
        mat[3] = 0.0f;
        mat[4] = 2.0f*x*y - 2.0f*w*z;
        mat[5] = 1.0f - 2.0f*x*x - 2.0f*z*z;
        mat[6] = 2.0f*y*z + 2.0f*w*x;
        mat[7] = 0.0f;
        mat[8] = 2.0f*x*z + 2.0f*w*y;
        mat[9] = 2.0f*y*z - 2.0f*w*x;
        mat[10] = 1.0f - 2.0f*x*x - 2.0f*y*y;
        mat[11] = 0.0f;
        mat[12] = 0.0f;
        mat[13] = 0.0f;
        mat[14] = 0.0f;
        mat[15] = 1.0f;
		return mat;
	}
	
	public Vec3 mul(final Vec3 v)
	{
		Vec3 q = new Vec3(x,y,z);
		Vec3 uv = Vec3.newCross(q, v);
		Vec3 uuv = Vec3.newCross(q, uv);
		uv.mulScalar( 2.0f * w );
		uuv.mulScalar( 2.0f );
		Vec3 ret = new Vec3(v);
		ret.add(uv);
		ret.add(uuv);
		return ret;
	}
	
	public void mul( final Vec3 v, Vec3 out )
	{
		Vec3 q = new Vec3(x,y,z);
		Vec3 uv = Vec3.newCross(q, v);
		Vec3 uuv = Vec3.newCross(q, uv);
		uv.mulScalar( 2.0f * w );
		uuv.mulScalar( 2.0f );
		out.set(v);
		out.add(uv);
		out.add(uuv);
	}
	
	public void castFromMat3(float[] m)
	{
		float x2m1 = m[0] - m[4] - m[8];
		float y2m1 = m[4] - m[0] - m[8];
		float z2m1 = m[8] - m[0] - m[4];
		float w2m1 = m[0] + m[4] + m[8];
		int i=0;
		float b = w2m1;
		if( x2m1 > b )
		{
			b = x2m1;
			i = 1;
		}
		if( y2m1 > b)
		{
			b = y2m1;
			i = 2;
		}
		if( z2m1 > b)
		{
			b = z2m1;
			i = 3;
		}
		
		b = FloatMath.sqrt( b + 1.0f ) * 0.5f;
		float mult = 0.25f / b;
		
		switch( i )
		{
			case 0:
				w = b;
				x = (m[5] - m[7]) * mult;
				y = (m[6] - m[2]) * mult;
				z = (m[1] - m[3]) * mult;
				break;
			case 1:
				w = (m[5] - m[7]) * mult;
				x = b;
				y = (m[1] + m[3]) * mult;
				z = (m[6] + m[2]) * mult;
				break;
			case 2:
				w = ( m[6] - m[2]) * mult;
				x = ( m[1] + m[3]) * mult;
				y = b;
				z = ( m[5] - m[7]) * mult;
				break;
			case 3:
				w = (m[1] - m[3]) * mult;
				x = (m[6] + m[2]) * mult;
				y = (m[5] + m[7]) * mult;
				z = b;
				break;
		}
		
	}
	
	public void castFromMat4(float[] m)
	{
		/*
		 *   0   1   2       0  1  2  3
		 *   3   4   5       4  5  6  7
		 *   6   7   8       8  9  10 11
		 *                   12 13 14 15  
		 * */
		
		float x2m1 = m[0] - m[5] - m[10];
		float y2m1 = m[5] - m[0] - m[10];
		float z2m1 = m[10] - m[0] - m[5];
		float w2m1 = m[0] + m[5] + m[10];
		int i=0;
		float b = w2m1;
		if( x2m1 > b )
		{
			b = x2m1;
			i = 1;
		}
		if( y2m1 > b)
		{
			b = y2m1;
			i = 2;
		}
		if( z2m1 > b)
		{
			b = z2m1;
			i = 3;
		}
		
		b = FloatMath.sqrt( b + 1.0f ) * 0.5f;
		float mult = 0.25f / b;
		
		switch( i )
		{
			case 0:
				w = b;
				x = (m[6] - m[9]) * mult; //+
				y = (m[8] - m[2]) * mult; //+
				z = (m[1] - m[4]) * mult; //+
				break;
			case 1:
				w = (m[6] - m[9]) * mult; //+
				x = b;
				y = (m[1] + m[4]) * mult; //+
				z = (m[8] + m[2]) * mult; //+
				break;
			case 2:
				w = ( m[8] - m[2]) * mult; //+
				x = ( m[1] + m[4]) * mult; //+
				y = b;
				z = ( m[6] - m[9]) * mult; //+
				break;
			case 3:
				w = (m[1] - m[4]) * mult; //+
				x = (m[8] + m[2]) * mult; //+
				y = (m[6] + m[9]) * mult; //+
				z = b;
				break;
		}
	}
	
}
