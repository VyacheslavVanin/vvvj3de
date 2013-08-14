package vvv.math;


public class help 
{
	public static float degToRadians(float deg)
	{
		return deg / 180.0f * 3.141592654f;
	}
	
	public static float radiansToDeg(float radians)
	{
		return radians / 3.141592654f * 180.0f;
	}
	
	public static final float PI = 3.141592654f;
	public static final float PI2= PI*PI;
	public static final float PId2 = PI / 2.0f;
	public static final float PId4 = PI / 4.0f;
	public static final float PId3 = PI / 3.0f;
	public static final float PId6 = PI / 6.0f;
	public static final float PIm2 = PI * 2.0f;
	public static final float EPSILON = 0.0000005f;
}
