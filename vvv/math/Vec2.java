package vvv.math;


public class Vec2 
{
	private float vec[] = new float[2];
	private float x,y;
	private boolean changed;
	
	public Vec2(float x, float y) { this.x=x; this.y=y; changed=true; }
	public Vec2(Vec2 v) { this(v.x, v.y); }
	public Vec2() { this(0,0); }
	
	public float x() {return x;}
	public float y() {return y;}
	public final float[] data() 
	{
		if(changed)
		{
			vec[0] = x;
			vec[1] = y;
			changed = false;
		}
		return vec;
	}
	
	public void setX(float x){ this.x = x; changed = true; }
	public void setY(float y){ this.y = y; changed = true; }
	public void set(float x, float y) { this.x = x; this.y=y; changed = true; }
	public void set(final Vec2 v) { this.x = v.x; this.y = v.y; changed = true; }
	
	public void add(final Vec2 v)	{ this.x +=v.x; this.y +=v.y; 	changed = true; }
	public void sub(final Vec2 v)	{ this.x -=v.x; this.x -=v.x; 	changed = true; }
	public void mulScalar(float s)	{ this.x *=s; 	this.x *=s; 	changed = true; }
	
	public float lengthSqr()	{ return this.x*this.x + this.y*this.y; }
	public float length()		{ return FloatMath.sqrt( lengthSqr() ); }
	
	public void  rotate( float angle ) 
	{
		final float tc = FloatMath.cos(angle);
		final float ts = FloatMath.sin(angle);
		final float tx = tc*this.x - ts*this.y;
		final float ty = ts*this.x + tc*this.y;
		this.x  = tx;
		this.x  = ty;
		changed = true;
	}
	
	public static void rotate( final Vec2 in, float angle , Vec2 out)
	{
		out.set(in);
		out.rotate(angle);
	}
	
	public static Vec2 newRotate( final Vec2 in, float angle)
	{
		Vec2 ret = new Vec2(in);
		ret.rotate(angle);
		return ret;
	}
	
	public float dot( final Vec2 v )
	{
		return this.x*v.x + this.y*v.y;
	}
	
	public static float dot( final Vec2 right, final Vec2 left )
	{
		return right.x*left.x + right.y*left.y;
	}
	
	public void normalize()
	{
		final float tx = this.length();
		if( tx != 0 )
		{
			this.x /=tx;
			this.y /=tx;
		}	
		changed = true;
	}
	
	public static void normalize( final Vec2 in, Vec2 out)
	{
		out.set(in);
		out.normalize();
	}
	
}
