package vvv.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Screen
{

    private int width;
    private int height;
    private List< Layer> layers;

    public Screen()
    {
        width = 1;
        height = 1;
        layers = new ArrayList<>();
    }

    @Override
    protected void finalize() throws Throwable
    { 
        if (layers != null)
        {
            layers.clear();
            layers = null;
        }
        super.finalize();
    }

    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;

        for (Layer l : layers)
        {
            l.onResize();
        }

    }

    public int getHeight()
    {
        return this.height;
    }

    public int getWidth()
    {
        return this.width;
    }

    public boolean addLayer(Layer layer)
    {
        if (!layers.contains(layer))
        {
            layers.add(layer);
            Screen old = layer.getScreen();
            if (old != null)
            {
                old.removeLayer(layer);
            }

            layer.setScreen(this);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean removeLayer(Layer layer)
    {
        boolean ret = layers.remove(layer);
        if (ret)
        {
            layer.setScreen(null);
        }
        return ret;
    }

    public boolean contains(Layer layer)
    {
        return layers.contains(layer);
    }
    
    private Comparator<Layer> comparator = new Comparator<Layer>()
    {
        @Override
        public int compare(Layer l1, Layer l2)
        {
            float d1 = l1.getDepth();
            float d2 = l2.getDepth();
            if (d1 < d2)
            {
                return 1;
            }
            if (d2 < d1)
            {
                return -1;
            }
            return 0;
        }
    };

    public void sortLayers()
    {
        Collections.sort(layers, comparator);
    }

    public void draw()
    {
        for( int i =0; i < layers.size(); ++i)
        {
            try
            {
                layers.get(i).draw();
            }
            catch (Exception ex)
            {
                Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
