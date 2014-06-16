package vvv.engine.widgets;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @brief Screen object used to hold Layers of GraphicObjects. 
 * @author vvv
 */
public final class Screen
{
    private int width;
    private int height;
    private final List< Layer> layers; // List of attached layers
    private WidgetLayer guiLayer = null;

    public Screen()
    {
        width = 1;
        height = 1;
        layers = new ArrayList<>();
    }

   /**
    * @brief Set size of screen object (not real screen)
    * @param width - in pixels
    * @param height - in pixels */
    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;

        if (guiLayer != null)
        {
            guiLayer.onResize();
        }
        for (Layer l : layers)
        {
            l.onResize();
        }

    }

    /**
     * @brief Get height of Screen object
     * @return height in pixels */
    public int getHeight()
    {
        return this.height;
    }

    /**
     * @brief
     * @return width in pixels */
    public int getWidth()
    {
        return this.width;
    }

    /**
     * @brief Add layer to screen
     * @param layer - layer to add to Screen object 
     * @return true if layer added or false if layer already attached to this Screen */
    public boolean addLayer(Layer layer)
    {
        if( layer == null )
        {
            throw new InvalidParameterException( "layer should not be null" );
        }
        if( !layers.contains(layer))
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

    /**
     * @brief Remove specified Layer from list of attached Layers
     * @param layer - layer to remove from object
     * @return true if removed or <br>false if there no such layer in list of attached layers */
    public boolean removeLayer(Layer layer)
    {
        boolean ret = layers.remove(layer);
        if (ret)
        {
            layer.setScreen(null);
        }
        return ret;
    }

    /**
     * @brief Attach GUI layer to screen (Currently can be only one widget layer) 
     * @param layer - layer to attach
     * @return true if successful or false if failed to attach */
    public boolean setGuiLayer(WidgetLayer layer)
    {
        if( layer != null )
        {
            Screen oldScreen = layer.getScreen();
            if( oldScreen != null )
            {
                oldScreen.setGuiLayer(null);
            }
            layer.setScreen(this);
        }
        this.guiLayer = layer;
        return true;
    }
    
    /**
     * @brief Check if Screen object contain specified layer
     * @param layer
     * @return true if contain and false otherwise */
    public boolean contains(Layer layer)
    {
        return layers.contains(layer);
    }
    
    private final Comparator<Layer> comparator = new Comparator<Layer>()
    {
        @Override
        public int compare(Layer l1, Layer l2)
        {
            final float d1 = l1.getDepth();
            final float d2 = l2.getDepth();
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

    /**
     * @brief Sort layers by depth */
    public void sortLayers()
    {
        Collections.sort(layers, comparator);
    }

    /**
     * @brief draw all layers of screen, first all layers from list of attached layers and then GUI layer*/
    public void draw()
    {
        try
        {
            // draw regular layers
            for (int i = 0; i < layers.size(); ++i)
            {
                layers.get(i).draw();
            }

            // draw guiLayer
            if (guiLayer != null)
            {
                guiLayer.draw();
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(Screen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public WidgetLayer getGuiLayer()
    {
        return guiLayer;
    }
    
    
}
