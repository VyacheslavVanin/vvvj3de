package vvv.math;



public class Vec4 
{
	private float vec[] = new float[4];
	private float x,y,z,w;
	private boolean changed;
	
	//constructors
	public Vec4( float x, float y, float z, float w) { this.x=x; this.y=y; this.z=z; this.w=w;}
	public Vec4( final Vec4 v) { this( v.x, v.y, v.z, v.w );  }
	public Vec4() { this( 0.0f, 0.0f, 0.0f, 0.0f ); }
	public Vec4( final Vec3 v, float w) { this(v.x(), v.y(), v.z(), w);}
	public Vec4( final Vec3 v) {this(v.x(), v.y(), v.z(), 1.0f );}
	public Vec4( final Vec2 v, float z, float w) { this(v.x(), v.y(), z, w);}
	public Vec4( final Vec2 v) { this(v.x(), v.y(), 1.0f, 1.0f); }
	
	// getters
	public float x() {return x;}
	public float y() {return y;}
	public float z() {return z;}
	public float w() {return w;}
	public float r() {return x;}
	public float g() {return y;}
	public float b() {return z;}
	public float a() {return w;}
	public final float[] data() 
	{ 
		if(changed)
		{
			vec[0] = x;
			vec[1] = y;
			vec[2] = z;
			vec[3] = w;
			changed = false;
		}
		return vec; 
	}
	
	public Vec3 xyz() { return new Vec3( x, y, z );}
	public Vec3 rgb() { return new Vec3( x, y, z );}
	
	//setters
	public void setX( float x ) 	{ this.x = x; changed =true; }
	public void setY( float y ) 	{ this.y = y; changed =true; }
	public void setZ( float z ) 	{ this.z = z; changed =true; }
	public void setW( float w )		{ this.w = w; changed =true; }
	public void setR( float r ) 	{ this.x = r; changed =true; }
	public void setG( float g ) 	{ this.y = g; changed =true; }
	public void setB( float b ) 	{ this.z = b; changed =true; }
	public void setA( float a )		{ this.w = a; changed =true; }
	
	public void set( float x, float y, float z, float w ) 	
	{ 
		this.x = x; 
		this.y = y; 
		this.z = z;
		this.w = w;
		changed = true;
	}
	
	public void set( final Vec4 v)
	{
		this.x = v.x; 
		this.y = v.y; 
		this.z = v.z;
		this.w = v.w;
		changed = true;
	}
	
	public void set( final Vec3 v, float w)
	{
		this.x = v.x(); 
		this.y = v.y(); 
		this.z = v.z();
		this.w = w;
		changed = true;
	}
	
	public void set( final Vec3 v)
	{
		this.x = v.x(); 
		this.y = v.y(); 
		this.z = v.z();
		this.w = 1.0f;
		changed = true;
	}
	
	
	// operations
	public void add( final Vec4 v)
	{
		this.x += v.x; 
		this.y += v.y; 
		this.z += v.z;
		this.w += v.w;
		changed = true;
	}
	
	public void sub( final Vec4 v)
	{
		this.x -= v.x; 
		this.y -= v.y; 
		this.z -= v.z;
		this.w -= v.w;
		changed = true;
	}
	
	public void mulScalar( float s)
	{
		this.x *= s;
		this.y *= s;
		this.z *= s;
		this.w *= s;
		changed = true;
	}
	
	public float lengthSqr()
	{
		return x*x + y*y + z*z + w*w;
	}
	
	public float length()
	{
		return FloatMath.sqrt( this.lengthSqr() );
	}
	
	public void normalize()
	{
		float tx = this.length();
		if( tx != 0 )
		{
			tx = 1.0f / tx;
			this.x *= tx;
			this.y *= tx;
			this.z *= tx;
			this.w *= tx;
		}	
		changed = true;
	}
	
	public static void normalize( final Vec4 in, Vec4 out)
	{
		out.set(in);
		out.normalize();
	}
	
	public static Vec4 newNormalize( final Vec4 v)
	{
		Vec4 ret = new Vec4(v);
		ret.normalize();
		return ret;
	}
	
	public float dot( final Vec4 v)
	{
		return 	x*v.x + y*v.y + z*v.z +	w*v.w;
	}
	
	public static float dot( final Vec4 l, final Vec4 r)
	{
		return 	l.x*r.x + l.y*r.y + l.z*r.z + l.w*r.w;
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
	
	
	
	public void applyMatrix( final float[] m)
	{
		final float tx = m[0]*x + m[4]*y + m[8]*z + m[12]*w;
		final float ty = m[1]*x + m[5]*y + m[9]*z + m[13]*w;
		final float tz = m[2]*x + m[6]*y + m[10]*z+ m[14]*w;
		final float tw = m[3]*x + m[7]*y + m[11]*z+ m[15]*w;
		this.x = tx;
		this.y = ty;
		this.z = tz;
		this.w = tw;
		changed = true;
	}
	
	public void applyMatrix( final Matrix4 m)
	{
		this.applyMatrix( m.getMatrix() );
	}
	
}
