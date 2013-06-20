package vvv.math;


public class Vec3 
{
	private float vec[] = new float[3];
	private float x,y,z;
	private boolean changed;
	
	//constructors
	public Vec3( float x, float y , float z ) 	{ this.x=x; this.y=y; this.z=z; changed = true;}
	public Vec3( final Vec3 v) 					{ this( v.x, v.y, v.z );  }
	public Vec3()                           	{ this(0.0f,0.0f,0.0f); }
	public Vec3( float x) { this(x,x,x); }
	// getters
	public float x() 				{ return x; }
	public float y() 				{ return y; }
	public float z()	 			{ return z; }
	public float r()	 			{ return x; }
	public float g() 				{ return y; }
	public float b() 				{ return z; }
	public final float[] data() 	
	{ 
		if(changed)
		{
			vec[0]=x;
			vec[1]=y;
			vec[2]=z;
			changed = false;
		}
		return vec; 
	}
	
	// setters
	public void setX( float x ) 	{ this.x = x; changed = true; }
	public void setY( float y ) 	{ this.y = y; changed = true; }
	public void setZ( float z ) 	{ this.z = z; changed = true; }
	public void setR( float r ) 	{ this.x = r; changed = true; }
	public void setG( float g ) 	{ this.y = g; changed = true; }
	public void setB( float b ) 	{ this.z = b; changed = true; }
	
	public void set( float x, float y, float z ) 	
	{ 
		this.x = x; 
		this.y = y; 
		this.z = z; 
		changed = true;
	}
	
	public void set( final Vec3 v) 	
	{  
		this.x = v.x; 
		this.y = v.y; 
		this.z = v.z;  
		changed = true;
	}
	
	public void set( final Vec2 v, float s)
	{
		this.x = v.x();
		this.y = v.y();
		this.z = s;
		changed = true;
	}
	
	public void set( final Vec2 v )
	{
		this.x = v.x();
		this.y = v.y();
		this.z = 0;
		changed = true;
	}
		
	// operations
	public void add( final Vec3 v) 
	{
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
		changed = true;
	}
	
	public void add( float x, float y, float z)
	{
		this.x += x;
		this.y += y;
		this.z += z;
		changed = true;
	}
	
	public void sub( final Vec3 v)
	{
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
		changed = true;
	}
	
	public void sub( float x, float y, float z)
	{
		this.x -= x;
		this.y -= y;
		this.z -= z;
		changed = true;
	}
	
	public void mulScalar( float s)
	{
		this.x *= s;
		this.y *= s;
		this.z *= s;
		changed = true;
	}
	
	public void mulScalar( float sx, float sy, float sz)
	{
		this.x *= sx;
		this.y *= sy;
		this.z *= sz;
		changed = true;
	}
	
	public float lengthSqr()
	{
		return this.x*this.x + this.y*this.y + this.z*this.z;
	}
	
	public float length()
	{
		return FloatMath.sqrt( this.lengthSqr() );
	}
	
	public void normalize()
	{
		final float tx = this.length();
		if( tx != 0 )
		{
			this.x /= tx;
			this.y /= tx;
			this.z /= tx;
			changed = true;
		}	
	}
	
	public static void normalize( final Vec3 in, final Vec3 out)
	{
		out.set(in);
		out.normalize();
	}
	
	public static Vec3 newNormalize(final Vec3 v)
	{
		Vec3 ret= new Vec3(v);
		ret.normalize();
		return ret;
	}
	
	public float dot( final Vec3 v)
	{
		return x*v.x + y*v.y + z*v.z;
	}
	
	public static float dot( final Vec3 l, final Vec3 r)
	{
		return l.x*r.x + l.y*r.y + l.z*r.z;
	}

	public Vec3 newCross(final Vec3 v)
	{
		return new Vec3( y*v.z - z*v.y, 
						 z*v.x - x*v.z,
						 x*v.y - y*v.x );
	}
	
	public void cross( Vec3 l, Vec3 r )
	{
		final float tx = l.y*r.z - l.z*r.y;
		final float ty = l.z*r.x - l.x*r.z;
		final float tz = l.x*r.y - l.y*r.x;
		this.x = tx;
		this.y = ty;
		this.z = tz;
		changed= true;
	}
	
	public static Vec3 newCross( final Vec3 l, final Vec3 r)
	{
		return new Vec3(	l.y*r.z - l.z*r.y,
							l.z*r.x - l.x*r.z,
							l.x*r.y - l.y*r.x);
	}

	public void rotateX( float angle)
	{
		final float c = FloatMath.cos(angle);
		final float s = FloatMath.sin(angle);
		final float ty = y*c - z*s;
		final float tz = y*s + z*c;
		y = ty;
		z = tz;
		changed = true;
	}
	
	public void rotateY( float angle)
	{
		final float c = FloatMath.cos(angle);
		final float s = FloatMath.sin(angle);
		final float tx =  x*c + z*s;
		final float tz = -x*s + z*c;
		x = tx;
		z = tz;
		changed = true;
	}
	
	public void rotateZ( float angle )
	{
		final float c = FloatMath.cos(angle);
		final float s = FloatMath.sin(angle);
		final float tx =  x*c - y*s;
		final float ty =  x*s + y*c;
		x = tx;
		y = ty;
		changed = true;
	}
	
	public void rotate(final Vec3 axis, float angle)
	{
		float c = FloatMath.cos(angle);
		float s = FloatMath.sin(angle);
		float ax = axis.x();
		float ay = axis.y();
		float az = axis.z();
		float al = FloatMath.sqrt(ax*ax+ay*ay+az*az);
		if(al !=0 )
		{
			ax /= al;
			ay /= al;
			az /= al;
		}
		float axcm = (1.0f - c) * ax;
		float azcm = (1.0f - c) * az;
		
		final float r0 = c + axcm*ax;
		final float r1 = axcm*ay + s*az;
		final float r2 = axcm*az - s*ay;
		
		final float r3 = axcm*ay - s*az;
		final float r4 = c + (1-c)*ay*ay;
		final float r5 = azcm*ay + s*ax;
		
		final float r6 = axcm*az + s*ay;
		final float r7 = azcm*ay - s*ax;
		final float r8 = c + azcm*az;
		
		final float tx = r0*x + r3*y + r6*z;
		final float ty = r1*x + r4*y + r7*z;
		final float tz = r2*x + r5*y + r8*z;
		this.x = tx;
		this.y = ty;
		this.z = tz;
		changed = true;
	}
	
	public static void rotate( final Vec3 in, final Vec3 axis, float angle, Vec3 out)
	{
		out.set(in);
		out.rotate(axis, angle);
	}
	
	public void applyMatrix3( final float[] m)
	{
		final float tx = m[0]*x + m[3]*y + m[6]*z;
		final float ty = m[1]*x + m[4]*y + m[7]*z;
		final float tz = m[2]*x + m[5]*y + m[8]*z;
		this.x = tx;
		this.y = ty;
		this.z = tz;
		changed = true;
	}
	
	public void applyMatrix( final Matrix3 m)
	{
		this.applyMatrix3( m.getMatrix() );
	}
	
	
	public void applyMatrix4( final float[] m)
	{
		final float tx = m[0]*x + m[4]*y + m[8]*z;
		final float ty = m[1]*x + m[5]*y + m[9]*z;
		final float tz = m[2]*x + m[6]*y + m[10]*z;
		this.x = tx;
		this.y = ty;
		this.z = tz;
		changed = true;
	}
	
	public void applyMatrix( final Matrix4 m)
	{
		this.applyMatrix4( m.getMatrix() );
	}
	
	public boolean equals(Vec3 v)
	{
		if( v.x != x || v.y != y || v.z != z )
			return false;
		else
			return true;
	}
	
}

