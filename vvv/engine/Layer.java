package vvv.engine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class Layer
{
    private Screen screen;
    private float depth;
    private List<GraphicObject> objects;

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

    public int getWidth()
    {
        return screen.getWidth();
    }

    public int getHeight()
    {
        return screen.getHeight();
    }

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

    abstract public void draw() throws Exception;

    abstract public void onResize();
    
    abstract public void init();

    abstract protected boolean onAddObject(final GraphicObject obj);

    abstract protected boolean onRemoveObject(final GraphicObject obj);

    public boolean addObject(GraphicObject obj)
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

    public boolean removeObject(GraphicObject obj)
    {
        obj.setLayer(null);
        onRemoveObject(obj);
        return objects.remove(obj);
    }

    protected List<GraphicObject> getObjects()
    {
        return objects;
    }
}
