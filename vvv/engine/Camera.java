package vvv.engine;

import vvv.math.Matrix;
import vvv.math.Vec3;
import vvv.math.help;

public class Camera
{

    private float[] matrix_view = new float[16];
    private float[] matrix_projection = new float[16];
    private float[] matrix_viewProjection = new float[16];

    private enum PROJECTION_TYPE
    {

        ORTHO,
        PERSPECTIVE
    }
    private PROJECTION_TYPE projection;
    private Vec3 cam_position;
    private Vec3 body_direction_front;
    private Vec3 head_direction_front;
    private Vec3 body_upVector;
    private Vec3 head_upVector;
    private Vec3 body_direction_left;
    private float head_pitchAngle;
    private float persp_fieldOfView;
    private float persp_aspectRatio;
    private float persp_near;
    private float persp_far;
    private float ortho_top;
    private float ortho_bottom;
    private float ortho_left;
    private float ortho_right;
    private float ortho_zNear;
    private float ortho_zFar;
    private boolean updatedView;
    private boolean updatedProjection;
    private Vec3 tempVec3 = new Vec3();
    private float[] tempMat4 = new float[16];

    private void updateView()
    {
        Vec3.rotate(body_direction_front, body_direction_left,
                    head_pitchAngle, head_direction_front);
        head_upVector.cross(head_direction_front, body_direction_left);

        tempVec3.set(cam_position);
        tempVec3.add(head_direction_front);
        Matrix.lookAt(matrix_view,
                      cam_position, tempVec3, head_upVector);
        updatedView = false;
    }

    private void updateProjection()
    {
        switch (projection)
        {
            case PERSPECTIVE:
                Matrix.perspective(matrix_projection, persp_fieldOfView,
                                   persp_aspectRatio, persp_near, persp_far);
                break;
            case ORTHO:
                Matrix.ortho(matrix_projection, ortho_left, ortho_right, ortho_bottom, ortho_top, ortho_zNear, ortho_zFar);
                break;
        }
        updatedProjection = false;
    }

    public Camera()
    {
        Matrix.loadIdentityMat4(matrix_view);
        Matrix.loadIdentityMat4(matrix_projection);
        Matrix.loadIdentityMat4(matrix_viewProjection);
        cam_position = new Vec3(0.0f, 0.0f, -1.0f);
        body_direction_front = new Vec3(0.0f, 0.0f, 1.0f);
        head_direction_front = new Vec3(0.0f, 0.0f, 1.0f);
        body_upVector = new Vec3(0.0f, 1.0f, 0.0f);
        head_upVector = new Vec3(0.0f, 1.0f, 0.0f);
        body_direction_left = new Vec3(1.0f, 0.0f, 0.0f);
        head_pitchAngle = 0.0f;
        persp_fieldOfView = 45.0f;
        persp_aspectRatio = 1.0f;
        persp_near = 0.1f;
        persp_far = 10.0f;

        ortho_top = 1.0f;
        ortho_bottom = -1.0f;
        ortho_left = -1.0f;
        ortho_right = 1.0f;

        projection = PROJECTION_TYPE.ORTHO;
        updatedView = true;
        updatedProjection = true;
    }

    public float[] getView()
    {
        if (updatedView)
        {
            updateView();
        }
        return matrix_view;
    }

    public float[] getProjection()
    {
        if (updatedProjection)
        {
            updateProjection();
        }
        return matrix_projection;
    }

    public float[] getViewProjection()
    {
        if (updatedView)
        {
            updateView();
            if (updatedProjection)
            {
                updateProjection();
            }
            Matrix.multiplyMat4(matrix_projection, matrix_view, matrix_viewProjection);
        }
        else
        {
            if (updatedProjection)
            {
                updateProjection();
            }
            Matrix.multiplyMat4(matrix_projection, matrix_view, matrix_viewProjection);
        }
        return matrix_viewProjection;
    }

    public Vec3 getPos()
    {
        return cam_position;
    }

    public Vec3 getDirection()
    {
        if (updatedView)
        {
            updateView();
        }
        return head_direction_front;
    }

    public void setOrtho(float top, float bottom, float left, float right, float zNear, float zFar)
    {
        ortho_top = top;
        ortho_bottom = bottom;
        ortho_left = left;
        ortho_right = right;
        ortho_zNear = zNear;
        ortho_zFar = zFar;
        projection = PROJECTION_TYPE.ORTHO;
        updatedProjection = true;
    }

    /**
     * @param fov in degrees
     */
    public void setPerspective(float fov, float aspect, float zNear, float zFar)
    {
        persp_fieldOfView = fov;
        persp_aspectRatio = aspect;
        persp_near = zNear;
        persp_far = zFar;
        projection = PROJECTION_TYPE.PERSPECTIVE;
        updatedProjection = true;
    }

    /**
     * @param fov in degrees
     */
    public void setPerspective(float fov, int sizeX, int sizeY, float zNear, float zFar)
    {
        if (sizeY == 0)
        {
            sizeY = 1;
        }
        persp_aspectRatio = (float) sizeX / (float) sizeY;
        persp_fieldOfView = fov;
        persp_near = zNear;
        persp_far = zFar;
        projection = PROJECTION_TYPE.PERSPECTIVE;
        updatedProjection = true;
    }

    public void setPos(float x, float y, float z)
    {
        cam_position.set(x, y, z);
        updatedView = true;
    }

    public void setPos(final Vec3 pos)
    {
        setPos(pos.x(), pos.y(), pos.z());
    }

    public boolean setBodyForward(final Vec3 forward, final Vec3 up)
    {
        float dot = Vec3.dot(forward, up);
        if (dot < help.EPSILON && dot > -help.EPSILON)
        {
            Vec3.normalize(forward, body_direction_front);
            Vec3.normalize(up, body_upVector);
            body_direction_left.cross(body_direction_front, body_upVector);
            updatedView = true;
            return true;
        }
        return false;
    }

    public void lookAt(float x, float y, float z)
    {
        tempVec3.set(x, y, z);
        tempVec3.sub(cam_position);
        tempVec3.normalize();
        if (tempVec3.equals(body_upVector))
        {
            head_pitchAngle = help.PId2;
        }
        else
        {
            body_direction_left.cross(tempVec3, body_upVector);
            body_direction_left.normalize();
            body_direction_front.cross(body_upVector, body_direction_left);
            head_pitchAngle = (float) Math.acos((float) Vec3.dot(body_direction_front, tempVec3));
        }
        updatedView = true;
    }

    public void lookAt(final Vec3 target)
    {
        lookAt(target.x(), target.y(), target.z());
    }

    public void move(float x, float y, float z, float distance)
    {
        tempVec3.set(x, y, z);
        tempVec3.normalize();
        tempVec3.mulScalar(distance);
        cam_position.add(tempVec3);
        updatedView = true;
    }

    public void move(final Vec3 direction, float distance)
    {
        move(direction.x(), direction.y(), direction.z(), distance);
    }

    public void moveForward(float distance)
    {
        tempVec3.set(body_direction_front);
        tempVec3.mulScalar(distance);
        cam_position.add(tempVec3);
        updatedView = true;
    }

    public void moveBackward(float distance)
    {
        moveForward(-distance);
    }

    public void moveLeft(float distance)
    {
        tempVec3.set(body_direction_left);
        tempVec3.mulScalar(distance);
        cam_position.add(tempVec3);
        updatedView = true;
    }

    public void moveRight(float distance)
    {
        moveLeft(-distance);
    }

    public void moveUp(float distance)
    {
        tempVec3.set(body_upVector);
        tempVec3.mulScalar(distance);
        cam_position.add(tempVec3);
        updatedView = true;
    }

    public void moveDown(float distance)
    {
        moveUp(-distance);
    }

    public void turnLeft(float angle)
    {
        Matrix.loadRotateMat4(body_upVector, angle, tempMat4);
        body_direction_front.applyMatrix4(tempMat4);
        body_direction_left.applyMatrix4(tempMat4);
        updatedView = true;
    }

    public void turnRight(float angle)
    {
        turnLeft(-angle);
    }

    public void turnUp(float angle)
    {
        head_pitchAngle += angle;
        updatedView = true;
    }

    public void turnDown(float angle)
    {
        turnUp(-angle);
    }

    public float getAspect()
    {
        return persp_aspectRatio;
    }
}
