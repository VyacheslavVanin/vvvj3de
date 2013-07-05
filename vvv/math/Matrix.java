package vvv.math;

import org.lwjgl.util.glu.GLU;



public class Matrix 
{
	public static void loadIdentityMat3( float[] mat)
	{
		mat[0] = 1.0f;
		mat[1] = 0.0f;
		mat[2] = 0.0f;
		mat[3] = 0.0f;
		mat[4] = 1.0f;
		mat[5] = 0.0f;
		mat[6] = 0.0f;
		mat[7] = 0.0f;
		mat[8] = 1.0f;
	}
	
	public static void loadIdentityMat4( float[] mat )
	{
		mat[0] = 1.0f;
		mat[1] = 0.0f;
		mat[2] = 0.0f;
		mat[3] = 0.0f;
		mat[4] = 0.0f;
		mat[5] = 1.0f;
		mat[6] = 0.0f;
		mat[7] = 0.0f;
		mat[8] = 0.0f;
		mat[9] = 0.0f;
		mat[10]= 1.0f;
		mat[11]= 0.0f;
		mat[12]= 0.0f;
		mat[13]= 0.0f;
		mat[14]= 0.0f;
		mat[15]= 1.0f;
	}
	
	public static void loadRotateMat3( final Vec3 axis, float angle, float[] out)
	{
		loadRotateMat3(out, angle, axis.x(), axis.y(), axis.z() );
	}
	
	public static void loadRotateMat3( float[] out, float angle, float ax, float ay, float az)
	{
		float c = FloatMath.cos(angle);
		float s = FloatMath.sin(angle);
		float al = FloatMath.sqrt(ax*ax+ay*ay+az*az);
		if( al !=0 )
		{
			ax /= al;
			ay /= al;
			az /= al;
		}
		float axcm = (1.0f - c) * ax;
		float azcm = (1.0f - c) * az;
		
		out[0] = c + axcm*ax;
		out[1] = axcm*ay + s*az;
		out[2] = axcm*az - s*ay;
		
		out[3] = axcm*ay - s*az;
		out[4] = c + (1.0f - c)*ay*ay;
		out[5] = azcm*ay + s*ax;
		
		out[6] = axcm*az + s*ay;
		out[7] = azcm*ay - s*ax;
		out[8] = c + azcm*az;	
	}
	
	public static void rotateMat3(float[] m, final Vec3 axis, float angle)
	{
		rotateMat3(m, angle, axis.x(), axis.y(), axis.z() );
	}
	
	public static void rotateMat3(float[] m,  float angle, float ax, float ay, float az)
	{
		float c = FloatMath.cos(angle);
		float s = FloatMath.sin(angle);
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
		
		final float m00 = m[0];
		final float m01 = m[1];
		final float m02 = m[2];
		final float m10 = m[3];
		final float m11 = m[4];
		final float m12 = m[5];
		final float m20 = m[6];
		final float m21 = m[7];
		final float m22 = m[8];
		
		m[0] = m00*r0 + m10*r1 + m20*r2;
		m[1] = m01*r0 + m11*r1 + m21*r2;
		m[2] = m02*r0 + m12*r1 + m22*r2;
		m[3] = m00*r3 + m10*r4 + m20*r5;
		m[4] = m01*r3 + m11*r4 + m21*r5;
		m[5] = m02*r3 + m12*r4 + m22*r5;
		m[6] = m00*r6 + m10*r7 + m20*r8;
		m[7] = m01*r6 + m11*r7 + m21*r8;
		m[8] = m02*r6 + m12*r7 + m22*r8;		
	}
	

	public static void rotateMat4( float[] m, float angle, float ax, float ay, float az)
	{
		float c = FloatMath.cos(angle);
		float s = FloatMath.sin(angle);
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
		
		final float m00 = m[0];
		final float m01 = m[1];
		final float m02 = m[2];
		final float m10 = m[4];
		final float m11 = m[5];
		final float m12 = m[6];
		final float m20 = m[8];
		final float m21 = m[9];
		final float m22 = m[10];
		
		m[0] = m00*r0 + m10*r1 + m20*r2;
		m[1] = m01*r0 + m11*r1 + m21*r2;
		m[2] = m02*r0 + m12*r1 + m22*r2;
		m[4] = m00*r3 + m10*r4 + m20*r5;
		m[5] = m01*r3 + m11*r4 + m21*r5;
		m[6] = m02*r3 + m12*r4 + m22*r5;
		m[8] = m00*r6 + m10*r7 + m20*r8;
		m[9] = m01*r6 + m11*r7 + m21*r8;
		m[10] = m02*r6 + m12*r7 + m22*r8;		
	}
	
	
	public static void rotateMat4( float[] m, final Vec3 axis, float angle)
	{
		rotateMat4(m, angle, axis.x(), axis.y(), axis.z() );
	}
	
	
	public static void loadRotateMat4(  float[] out, float angle, float ax, float ay, float az)
	{
		float c = FloatMath.cos(angle);
		float s = FloatMath.sin(angle);
		float al = FloatMath.sqrt(ax*ax+ay*ay+az*az);
		if(al !=0 )
		{
			ax /= al;
			ay /= al;
			az /= al;
		}
		float axcm = (1.0f - c) * ax;
		float azcm = (1.0f - c) * az;
		
		out[0] = c + axcm*ax;
		out[1] = axcm*ay + s*az;
		out[2] = axcm*az - s*ay;
		out[3] = 0.0f;
		
		out[4] = axcm*ay - s*az;
		out[5] = c + (1-c)*ay*ay;
		out[6] = azcm*ay + s*ax;
		out[7] = 0.0f;
		
		out[8] = axcm*az + s*ay;
		out[9] = azcm*ay - s*ax;
		out[10]= c + azcm*az;
		out[11] = 0.0f;
		
		out[12] = 0.0f;
		out[13] = 0.0f;
		out[14] = 0.0f;
		out[15] = 1.0f;	
	}
	
	
	public static void loadRotateMat4( final Vec3 axis, float angle, float[] out)
	{
		loadRotateMat4(out, angle, axis.x(), axis.y(), axis.z() );
	}
	
	
	
	public static float[] newRotateMat4( final Vec3 axis, float angle)
	{
		float[] out = new float[16];
		loadRotateMat4(axis, angle, out);
		return out;
	}
	
	public static float[] newRotateMat3(final  Vec3 axis, float angle)
	{
		float[] out = new float[9];
		loadRotateMat3(axis, angle, out);
		return out;
	}
	
	public static void loadTranslateMat4(float[] mat, final Vec3 v)
	{
		mat[0] = 1.0f;
		mat[1] = 0.0f;
		mat[2] = 0.0f;
		mat[3] = 0.0f;
		mat[4] = 0.0f;
		mat[5] = 1.0f;
		mat[6] = 0.0f;
		mat[7] = 0.0f;
		mat[8] = 0.0f;
		mat[9] = 0.0f;
		mat[10]= 1.0f;
		mat[11]= 0.0f;
		mat[12]= v.x();
		mat[13]= v.y();
		mat[14]= v.z();
		mat[15]= 1.0f;
	}
	
	public static void loadTranslateMat4( float[] mat, float x, float y, float z)
	{
		mat[0] = 1.0f;
		mat[1] = 0.0f;
		mat[2] = 0.0f;
		mat[3] = 0.0f;
		mat[4] = 0.0f;
		mat[5] = 1.0f;
		mat[6] = 0.0f;
		mat[7] = 0.0f;
		mat[8] = 0.0f;
		mat[9] = 0.0f;
		mat[10]= 1.0f;
		mat[11]= 0.0f;
		mat[12]= x;
		mat[13]= y;
		mat[14]= z;
		mat[15]= 1.0f;
	}

	public static float[] newTranslateMat4( final Vec3 v)
	{
		float[] mat = new float[16];
		loadTranslateMat4(mat, v);
		return mat;
	}
	
	public static void multiplyMat3(final float[] l, final  float[] r, float[] out )
	{
		final float l00 = l[0];
		final float l01 = l[1];
		final float l02 = l[2];
		final float l10 = l[3];
		final float l11 = l[4];
		final float l12 = l[5];
		final float l20 = l[6];
		final float l21 = l[7];
		final float l22 = l[8];
		
		final float r00 = r[0];
		final float r01 = r[1];
		final float r02 = r[2];
		final float r10 = r[3];
		final float r11 = r[4];
		final float r12 = r[5];
		final float r20 = r[6];
		final float r21 = r[7];
		final float r22 = r[8];
		
		out[0] = l00 * r00 + l10 * r01 + l20 * r02;
		out[1] = l01 * r00 + l11 * r01 + l21 * r02;
		out[2] = l02 * r00 + l12 * r01 + l22 * r02;
		out[3] = l00 * r10 + l10 * r11 + l20 * r12;
		out[4] = l01 * r10 + l11 * r11 + l21 * r12;
		out[5] = l02 * r10 + l12 * r11 + l22 * r12;
		out[6] = l00 * r20 + l10 * r21 + l20 * r22;
		out[7] = l01 * r20 + l11 * r21 + l21 * r22;
		out[8] = l02 * r20 + l12 * r21 + l22 * r22;		
	}

	public static float[] multiplyMat3( final float[] l, final float[] r )
	{
		float[] out = new float[9];
		multiplyMat3(l,r,out);
		return out;
	}
	
	public static void multiplyMat4(final float[] l, final float[] r, float[] out)
	{
		final float l00 = l[0];
		final float l01 = l[1];
		final float l02 = l[2];
		final float l03 = l[3];
		final float l10 = l[4];
		final float l11 = l[5];
		final float l12 = l[6];
		final float l13 = l[7];
		final float l20 = l[8];
		final float l21 = l[9];
		final float l22 = l[10];
		final float l23 = l[11];
		final float l30 = l[12];
		final float l31 = l[13];
		final float l32 = l[14];
		final float l33 = l[15];
				
		final float r00 = r[0];
		final float r01 = r[1];
		final float r02 = r[2];
		final float r03 = r[3];
		final float r10 = r[4];
		final float r11 = r[5];
		final float r12 = r[6];
		final float r13 = r[7];
		final float r20 = r[8];
		final float r21 = r[9];
		final float r22 = r[10];
		final float r23 = r[11];
		final float r30 = r[12];
		final float r31 = r[13];
		final float r32 = r[14];
		final float r33 = r[15];
		
		out[0] = l00*r00 + l10*r01 + l20*r02 + l30*r03;
		out[1] = l01*r00 + l11*r01 + l21*r02 + l31*r03;
		out[2] = l02*r00 + l12*r01 + l22*r02 + l32*r03;
		out[3] = l03*r00 + l13*r01 + l23*r02 + l33*r03;
		
		out[4] = l00*r10 + l10*r11 + l20*r12 + l30*r13;
		out[5] = l01*r10 + l11*r11 + l21*r12 + l31*r13;
		out[6] = l02*r10 + l12*r11 + l22*r12 + l32*r13;
		out[7] = l03*r10 + l13*r11 + l23*r12 + l33*r13;
		
		out[8] = l00*r20 + l10*r21 + l20*r22 + l30*r23;
		out[9] = l01*r20 + l11*r21 + l21*r22 + l31*r23;
		out[10]= l02*r20 + l12*r21 + l22*r22 + l32*r23;
		out[11]= l03*r20 + l13*r21 + l23*r22 + l33*r23;
		
		out[12]= l00*r30 + l10*r31 + l20*r32 + l30*r33;
		out[13]= l01*r30 + l11*r31 + l21*r32 + l31*r33;
		out[14]= l02*r30 + l12*r31 + l22*r32 + l32*r33;
		out[15]= l03*r30 + l13*r31 + l23*r32 + l33*r33;
	}
	
	public static float[] multiplyMat4( final float[] l, final float[] r )
	{
		float[] out = new float[16];
		multiplyMat4(l, r, out);
		return out;
	}
	
	public static void inverseMat3( final float[] in, float[] out)
	{
		final float in00 = in[0];
		final float in01 = in[1];
		final float in02 = in[2];
		final float in10 = in[3];
		final float in11 = in[4];
		final float in12 = in[5];
		final float in20 = in[6];
		final float in21 = in[7];
		final float in22 = in[8];
		
		final float OneDivDeterminant =
				         1.0f / ( in00 * (in11 * in22 - in21 * in12)
				                - in10 * (in01 * in22 - in21 * in02)
				                + in20 * (in01 * in12 - in11 * in02) );
		
		out[0] = (in11 * in22 - in21 * in12) * OneDivDeterminant;
        out[1] = (in12 * in20 - in22 * in10) * OneDivDeterminant;
        out[2] = (in10 * in21 - in20 * in11) * OneDivDeterminant;
        out[3] = (in02 * in21 - in01 * in22) * OneDivDeterminant;
        out[4] = (in00 * in22 - in02 * in20) * OneDivDeterminant;
        out[5] = (in01 * in20 - in00 * in21) * OneDivDeterminant;
        out[6] = (in12 * in01 - in11 * in02) * OneDivDeterminant;
        out[7] = (in10 * in02 - in12 * in00) * OneDivDeterminant;
        out[8] = (in11 * in00 - in10 * in01) * OneDivDeterminant;
	}
	
	public static float[] newInverseMat3( final float[] in)
	{
		float[] out = new float[9];
		inverseMat3(in, out);
		return out;
	}

	public  static void inverseMat4(final float[] in, float[] out)
	{
		final float epsilon = 0.0000005f;
		
		final float v00 = in[0];
		final float v01 = in[1];
		final float v02 = in[2];
		final float v03 = in[3];
		final float v10 = in[4];
		final float v11 = in[5];
		final float v12 = in[6];
		final float v13 = in[7];
		final float v20 = in[8];
		final float v21 = in[9];
		final float v22 = in[10];
		final float v23 = in[11];
		final float v30 = in[12];
		final float v31 = in[13];
		final float v32 = in[14];
		final float v33 = in[15];
		
		final float sf00 = v22*v33 - v32*v23;
		final float sf02 = v12*v33 - v32*v13;
		final float sf03 = v12*v23 - v22*v13;
		
		final float sf04 = v21*v33 - v31*v23;
		final float sf06 = v11*v33 - v31*v13;
		final float sf07 = v11*v23 - v21*v13;
		
		final float sf08 = v21*v32 - v31*v22;
		final float sf10 = v11*v32 - v31*v12;
		final float sf11 = v11*v22 - v21*v12;
		
		final float sf12 = v20*v33 - v30*v23;
		final float sf14 = v10*v33 - v30*v13;
		final float sf15 = v10*v23 - v20*v13;
		
		final float sf16 = v20*v32 - v30*v22;
		final float sf18 = v10*v32 - v30*v12;
		final float sf19 = v10*v22 - v20*v12;
		
		final float sf20 = v20*v31 - v30*v21;
		final float sf22 = v10*v31 - v30*v11;
		final float sf23 = v10*v21 - v20*v11;
		
		float inv00 = v11*sf00 - v12*sf04 + v13*sf08;	
		float inv10 = -(v10*sf00 - v12*sf12 + v13*sf16);	
		float inv20 = v10*sf04 - v11*sf12 + v13*sf20;	
		float inv30 = -(v10*sf08 - v11*sf16 + v12*sf20);
				
		float det = v00*inv00 + v01*inv10 + v02 * inv20 + v03*inv30;
		
		if( 1.0f - det <=  epsilon )
		{
			out[0] = inv00;
			out[1] = -(v01*sf00 - v02*sf04 + v03*sf08);
			out[2] = v01*sf02 - v02*sf06 + v03*sf10;
			out[3] = -(v01*sf03 - v02*sf07 + v03*sf11);
			
			out[4] = inv10;
			out[5] = v00*sf00 - v02*sf12 + v03*sf16;
			out[6] = -(v00*sf02 - v02*sf14 + v03*sf18);
			out[7] = v00*sf03 - v02*sf15 + v03*sf19;
			
			out[8] = inv20;
			out[9] = -(v00*sf04 - v01*sf12 + v03*sf20);
			out[10]= v00*sf06 - v01*sf14 + v03*sf22;
			out[11]= -(v00*sf07 - v01*sf15 + v03*sf23);
			
			out[12] = inv30;
			out[13] = v00*sf08 - v01*sf16 + v02*sf20;
			out[14] = -(v00*sf10 - v01*sf18 + v02*sf22);
			out[15] = v00*sf11 - v01*sf19 + v02*sf23;
		}
		else
		{
			out[0] = inv00 / det;
			out[1] = -(v01*sf00 - v02*sf04 + v03*sf08)/ det;
			out[2] = (v01*sf02 - v02*sf06 + v03*sf10) / det;
			out[3] = -(v01*sf03 - v02*sf07 + v03*sf11)/ det;
			
			out[4] = inv10 / det;
			out[5] = (v00*sf00 - v02*sf12 + v03*sf16) / det;
			out[6] = -(v00*sf02 - v02*sf14 + v03*sf18)/ det;
			out[7] = (v00*sf03 - v02*sf15 + v03*sf19) / det;
			
			out[8] = inv20 / det;
			out[9] = -(v00*sf04 - v01*sf12 + v03*sf20) / det;
			out[10]= (v00*sf06 - v01*sf14 + v03*sf22) / det;
			out[11]= -(v00*sf07 - v01*sf15 + v03*sf23) / det;
			
			out[12] = inv30 / det;
			out[13] = (v00*sf08 - v01*sf16 + v02*sf20) / det;
			out[14] = -(v00*sf10 - v01*sf18 + v02*sf22)/ det;
			out[15] = (v00*sf11 - v01*sf19 + v02*sf23) / det;
		}
	}
	
	public static float[] newInverseMat4(final float[] in) 
	{
		float[] out = new float[16];
		inverseMat4(in, out);
		return out;
	}
	
	public static void Mat3ToMat4( final float[] in, float[] out)
	{
		out[0] = in[0];
		out[1] = in[1];
		out[2] = in[2];
		out[3] = 0.0f;
		out[4] = in[3];
		out[5] = in[4];
		out[6] = in[5];
		out[7] = 0.0f;
		out[8] = in[6];
		out[9] = in[7];
		out[10]= in[8];
		out[11]= 0.0f;
		out[12]= 0.0f;
		out[13]= 0.0f;
		out[14]= 0.0f;
		out[15]= 1.0f;
	}
	
	public static float[] newMat4FromMat3(final float[] in)
	{
		float[] out = new float[16];
		Mat3ToMat4(in,out);
		return out;
	}
	
	public static void Mat4ToMat3( final float[] in, float[] out)
	{
		out[0] = in[0];
		out[1] = in[1];
		out[2] = in[2];
		out[3] = in[4];
		out[4] = in[5];
		out[5] = in[6];
		out[6] = in[8];
		out[7] = in[9];
		out[8] = in[10];
	}
	
	public static float[] newMat3FromMat4( final float[] in)
	{
		float[] out = new float[9];
		Mat4ToMat3(in,out);
		return out;
	}
	
	public static void translate( float[] mat, final Vec3 v)
	{
		final float x = v.x();
		final float y = v.y();
		final float z = v.z();

		mat[12] += mat[0]*x + mat[4]*y + mat[8 ]*z ;
		mat[13] += mat[1]*x + mat[5]*y + mat[9 ]*z ;
		mat[14] += mat[2]*x + mat[6]*y + mat[10]*z;
		mat[15] += mat[3]*x + mat[7]*y + mat[11]*z;
		
	}
	
	public static void lookAt( float[] mat, final Vec3 eye, final Vec3 center, final Vec3 up)
	{
		final float eye0 = eye.x();
		final float eye1 = eye.y();
		final float eye2 = eye.z();
		
               // Vec3 f = normalize(Vec3.sub(center, eye));
		float f0 = center.x() - eye0;
		float f1 = center.y() - eye1;		 
		float f2 = center.z() - eye2;     
		float len = 1.0f / FloatMath.sqrt(f0*f0 + f1*f1 + f2*f2);
		f0 *= -len;
		f1 *= -len;
		f2 *= -len;

                // Vec3 u = normalize(up);
		float u0 = up.x();
		float u1 = up.y();
		float u2 = up.z();
		len = 1.0f / FloatMath.sqrt( u0*u0 + u1*u1 + u2*u2);
		u0 *= len;
		u1 *= len;
		u2 *= len;

		// Vec3 s = normalize(cross(f, u));
		float s0 = f1*u2 - f2*u1;
		float s1 = f2*u0 - f0*u2;
		float s2 = f0*u1 - f1*u0;
		len = 1.0f / FloatMath.sqrt( s0*s0 + s1*s1 + s2*s2);
		s0 *= len;
		s1 *= len;
		s2 *= len;
		
		// u = cross(s, f);
		u0 = s1*f2 - s2*f1;
		u1 = s2*f0 - s0*f2;
		u2 = s0*f1 - s1*f0;
		
		mat[0] = s0;
                mat[4] = s1;
                mat[8] = s2;
                
		mat[1] = u0;
                mat[5] = u1;
                mat[9] = u2;
                
		mat[2] = -f0;
                mat[6] = -f1;
                mat[10]= -f2;
                
		mat[3] = 0.0f;
		mat[7] = 0.0f;	
		mat[11]= 0.0f;
		
		mat[15]= 1.0f;	
                
                mat[12]= -(s0*eye0 + s1*eye1 + s2*eye2);
		mat[13]= -(u0*eye0 + u1*eye1 + u2*eye2);
		mat[14]=  f0*eye0 + f1*eye1 + f2*eye2;
	}
	
	/** @param fovy in degrees */
	public static void perspective( float[] mat, float fovy, float aspect, float zNear, float zFar)
	{
		float ta = (fovy * 0.5f)*3.141592654f*0.0055555555f;//to radians
		float r = FloatMath.sin(ta) / FloatMath.cos(ta) * zNear;
				
		mat[0] = zNear / (r*aspect);
		mat[1] = 0.0f;
		mat[2] = 0.0f;
		mat[3] = 0.0f;
		mat[4] = 0.0f;
		mat[5] = zNear / r;
		mat[6] = 0.0f;
		mat[7] = 0.0f;
		mat[8] = 0.0f;
		mat[9] = 0.0f;
		mat[10]= -(zFar + zNear) / (zFar - zNear);
		mat[11]= -1.0f;
		mat[12]= 0.0f;
		mat[13]= 0.0f;
		mat[14]= -2.0f * zFar * zNear / ( zFar - zNear);
		mat[15]= 0.0f;
	}
	
	public static void perspective( float[] mat, float fovy, float width, float height, float zNear, float zFar )
	{
		float ta = (fovy * 0.5f)*3.141592654f*0.0055555555f;//to radians
		float h = FloatMath.sin( ta ) / FloatMath.cos( ta ) ;
		float w = h * height / width;
		
		mat[0] = w;
		mat[1] = 0.0f;
		mat[2] = 0.0f;
		mat[3] = 0.0f;
		mat[4] = 0.0f;
		mat[5] = h;
		mat[6] = 0.0f;
		mat[7] = 0.0f;
		mat[8] = 0.0f;
		mat[9] = 0.0f;
		mat[10]= (zFar + zNear)/(zFar - zNear);
		mat[11]= -1.0f;
		mat[12]= 0.0f;
		mat[13]= 0.0f;
		mat[14]= -2.0f * zFar * zNear / ( zFar - zNear);
		mat[15]= 0.0f;
	}
	
	public static void ortho( float[] mat, float left, float right, float bottom, float top, float zNear, float zFar)
	{
		mat[0] = 2.0f / ( right - left);
		mat[1] = 0.0f;
		mat[2] = 0.0f;
		mat[3] = 0.0f;
		mat[4] = 0.0f;
		mat[5] = 2.0f / ( top - bottom);
		mat[6] = 0.0f;
		mat[7] = 0.0f;
		mat[8] = 0.0f;
		mat[9] = 0.0f;
		mat[10]= -2.0f / (zFar - zNear);
		mat[11]= 0.0f;
		mat[12]= -(right + left) / ( right - left);
		mat[13]= -(top + bottom) / ( top - bottom);
		mat[14]= -(zFar + zNear) / ( zFar - zNear);
		mat[15] = 1.0f;
	}
	
	public static void ortho( float[] mat, float left, float right, float bottom, float top)
	{
		mat[0] = 2.0f / (right - left);
		mat[1] = 0.0f;
		mat[2] = 0.0f;
		mat[3] = 0.0f;
		mat[4] = 0.0f;
		mat[5] = 2.0f / (top - bottom);
		mat[6] = 0.0f;
		mat[7] = 0.0f;
		mat[8] = 0.0f;
		mat[9] = 0.0f;
		mat[10]= -1.0f;
		mat[11]= 0.0f;
		mat[12]= -(right + left) / (right - left);
		mat[13]= -(top + bottom) / (top - bottom);
		mat[14] = 0.0f;
		mat[15]= 1.0f;
	}
	
	public static void frustrum( float[] mat, float left, float right, float bottom, float top, float zNear, float zFar)
	{
		mat[0] = 2.0f * zNear / ( right - left);
		mat[1] = 0.0f;
		mat[2] = 0.0f;
		mat[3] = 0.0f;
		mat[4] = 0.0f;
		mat[5] = 2.0f * zNear / ( top - bottom);
		mat[6] = 0.0f;
		mat[7] = 0.0f;
		mat[8] = (right + left) / ( right - left);
		mat[9] = (top + bottom) / ( top - bottom);
		mat[10]= -(zFar + zNear)/ (zFar - zNear);
		mat[11] = -1.0f;
		mat[12] = 0.0f;
		mat[13] = 0.0f;
		mat[14] = -2.0f * zFar * zNear / ( zFar - zNear);
		mat[15] = 0.0f;
	}

	public static void scale(  float[] in, float s) 
	{
		scale(in,s,s,s);
	}
	
	public static void scale( float[] in, float sx, float sy, float sz)
	{
		in[0] *= sx;
		in[1] *= sx;
		in[2] *= sx;
		in[3] *= sx;
		
		in[4] *= sy;
		in[5] *= sy;
		in[6] *= sy;
		in[7] *= sy;
		
		in[8] *= sz;
		in[9] *= sz;
		in[10]*= sz;
		in[11]*= sz;
	}
	
	public static void loadScale( float[] in, float sx, float sy, float sz)
	{
		in[0]  = sx;
		in[1]  = 0;
		in[2]  = 0;
		in[3]  = 0;
		in[4]  = 0;
		in[5]  = sy;
		in[6]  = 0;
		in[7]  = 0;
		in[8]  = 0;
		in[9]  = 0;
		in[10] = sz;
		in[11] = 0;
		in[12] = 0;
		in[13] = 0;
		in[14] = 0;
		in[15] = 1;
	}
	
	public static void copyMatrix4(final float[] in, float[] out)
	{
		out[  0 ] = in[  0 ];
		out[  1 ] = in[  1 ];
		out[  2 ] = in[  2 ];
		out[  3 ] = in[  3 ];
		out[  4 ] = in[  4 ];
		out[  5 ] = in[  5 ];
		out[  6 ] = in[  6 ];
		out[  7 ] = in[  7 ];
		out[  8 ] = in[  8 ];
		out[  9 ] = in[  9 ];
		out[ 10 ] = in[ 10 ];
		out[ 11 ] = in[ 11 ];
		out[ 12 ] = in[ 12 ];
		out[ 13 ] = in[ 13 ];
		out[ 14 ] = in[ 14 ];
		out[ 15 ] = in[ 15 ];
	}
	
	public static void copyMatrix3(final float[] in, float[] out)
	{
		out[  0 ] = in[  0 ];
		out[  1 ] = in[  1 ];
		out[  2 ] = in[  2 ];
		out[  3 ] = in[  3 ];
		out[  4 ] = in[  4 ];
		out[  5 ] = in[  5 ];
		out[  6 ] = in[  6 ];
		out[  7 ] = in[  7 ];
		out[  8 ] = in[  8 ];
	}
	
}
