package vvv.engine.widgets;

import java.util.ArrayList;
import java.util.List;

/**
 * @brief Layer used to contain GraphicObjects <br>
 *        Layers can be attached to Screen object.
 * @author vvv */
public abstract class Layer
{
    private Screen screen;
    private float depth;
    private final List<GraphicObject> objects;

    public Layer()
    {
        objects = new ArrayList<>();
        screen = null;
        depth = 1.0f;
    }

    void setScreen(Screen screen)
    {
        this.screen = screen;
    }

    Screen getScreen()
    {
        return this.screen;
    }

    /**
     * @return Width of screen in pixels */
    public int getWidth()
    {
        return screen.getWidth();
    }

    /**
     * @return Height of screen in pixels */
    public int getHeight()
    {
        return screen.getHeight();
    }

    /**
     * @brief Set Layer Depth. Depth describes an order layers drawn
     * @param depth  */
    public void setDepth(float depth)
    {
        this.depth = depth;
        if (screen != null)
        {
            screen.sortLayers();
        }
    }

    public float getDepth()
    {
        return depth;
    }

    /**
     * @brief Implement how to draw Objects which belong to this layer
     * @throws Exception */
    abstract public void draw() throws Exception;

    /**
     * @brief implement what to do when resize (for example, change camera parameters )  */
    abstract public void onResize();
    
    /**
     * @brief This method call before add object to list.<br> 
     *        If method return true then object obj will be added to list of objects<br>
     *        You can override this to filter objects
     * @param obj
     * @return  */
    protected boolean onAddObject(final GraphicObject obj) {return true;}

    /**
     * @brief This method called before removal object obj from list of objects.<br>
     *        You can override this if you need.
     * @param obj  */
    protected void onRemoveObject(final GraphicObject obj) {};

    /**
     * @brief Add GraphicObject to layer
     * @param obj - object to add
     * @return true if added, false otherwise */
    public final boolean addObject(GraphicObject obj)
    {
        if (!objects.contains(obj))
        {
            if (onAddObject(obj) == false)
            {
                return false;
            }

            Layer oldlayer = obj.getLayer();

            if (oldlayer != null)
            {
                oldlayer.objects.remove(obj);
            }

            obj.setLayer(this);
            objects.add(obj);

            return true;
        }
        return false;
    }

    /**
     * @brief Remove object from list
     * @param obj
     * @return  */
    public final boolean removeObject(GraphicObject obj)
    {
        obj.setLayer(null);
        onRemoveObject(obj);
        return objects.remove(obj);
    }

    /**
     * @brief get list of objects in layer
     * @return  */
    protected final List<GraphicObject> getObjects()
    {
        return objects;
    }
}
