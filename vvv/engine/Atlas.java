package vvv.engine;

import java.util.*;
import org.lwjgl.util.vector.Vector4f;

public class Atlas
{

    public Atlas(float height, float width)
    {
        if (height <= 0)
        {
            height = MAX_TEXTURE_SIZE;
        }
        if (width <= 0)
        {
            width = MAX_TEXTURE_SIZE;
        }

        height = (float) Math.floor(height);
        width = (float) Math.floor(width);

        this.height = height;
        this.width = width;
    }

    public Atlas(float height, float width, float border)
    {
        this(height, width);
        this.border = border;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public void pack(List<Image> in, List<Image> notPlaced)
    {
        list = simplePacker.pack(width, height, border, in, notPlaced);
    }

    public List<textureData> getList()
    {
        return list;
    }

    static public class textureData
    {
        String name = new String();
        TexCoordData data = new TexCoordData();
        private Image img = null;

        public String getName()
        {
            return name;
        }

        public TexCoordData getData()
        {
            return data;
        }

        public Image getImg()
        {
            return img;
        }

        public void setImg(Image img)
        {
            this.img = img;
        }
    }

    static private class MyComparator implements Comparator<Image>
    {
        @Override
        public int compare(Image i1, Image i2)
        {
            float dh = i2.getHeight() - i1.getHeight();
            float dw = i2.getWidth() - i1.getWidth();

            if (dh > 0)
            {
                return 1;
            }

            if (dh < 0)
            {
                return -1;
            }

            if (dw > 0)
            {
                return 1;
            }

            if (dw < 0)
            {
                return -1;
            }

            return 0;
        }
    }

    static private class simplePackerNode
    {
        private simplePackerNode leftChild = null;
        private simplePackerNode rightChild = null;
        private Image image = null;
        private float posX = 0;
        private float posY = 0;
        private float width = 0;
        private float height = 0;
        private float border = 0;

        public simplePackerNode(float x, float y, float width, float height)
        {
            this.posX = x;
            this.posY = y;
            this.height = height;
            this.width = width;
        }

        public simplePackerNode(float x, float y, float width, float height, float border)
        {
            this(x, y, width, height);
            this.border = border;
        }

        public boolean placeImages(List<Image> list)
        {
            Image im = list.get(0);
            float imageHeight = im.getHeight() + 2 * border;
            float imageWidth = im.getWidth() + 2 * border;

            if (image == null)
            {
                if (imageHeight <= (height) && imageWidth <= (width))
                {
                    if (imageWidth != width)
                    {
                        rightChild = new simplePackerNode(posX + imageWidth, posY,
                                                          width - imageWidth, imageHeight, border);
                    }

                    if (imageHeight != height)
                    {
                        leftChild = new simplePackerNode(posX, posY + imageHeight,
                                                         width, height - imageHeight, border);
                    }

                    image = list.remove(0);
                    return true;
                }
                else
                {
                    return false;
                }
            }
            else // image != null
            {
                if (rightChild != null)
                {
                    boolean ret = rightChild.placeImages(list);
                    if (ret == true)
                    {
                        return true;
                    }
                }

                if (leftChild != null)
                {
                    return leftChild.placeImages(list);
                }

                return false;
            }
        }

        public void collect(List<textureData> list)
        {
            if (image != null)
            {
                textureData data = new textureData();
                data.name = image.getName();
                data.img = image;
                data.data.set(posX + border, posY + border,
                              image.getWidth(), image.getHeight());
                list.add(data);

                if (rightChild != null)
                {
                    rightChild.collect(list);
                }

                if (leftChild != null)
                {
                    leftChild.collect(list);
                }
            }
        }
    }

    static private class simplePacker
    {
        static public List<textureData> pack(float width, float height, float border,
                                             List<Image> in, List<Image> notPlaced)
        {
            List<Image> sortedin = new LinkedList<>(in);
            Collections.sort(sortedin, new MyComparator());

            simplePackerNode root = new simplePackerNode(0, 0, width, height, border);

            while (sortedin.size() > 0)
            {
                boolean ret = root.placeImages(sortedin);
                if (ret == false)
                {
                    Image im = sortedin.remove(0);
                    notPlaced.add(im);
                }
            }

            float horizontalMultiplyer = 1.0f / width;
            float verticalMultiplyer = 1.0f / height;

            List<textureData> ret = new ArrayList<>();

            root.collect(ret);
            for (textureData td : ret)
            {
                TexCoordData data = td.data;
                Vector4f v = data.get();
                data.set(v.x * horizontalMultiplyer, v.y * verticalMultiplyer, 
                         v.z * horizontalMultiplyer, v.w * verticalMultiplyer);
            }

            return ret;
        }
    }
    public final float MAX_TEXTURE_SIZE = 2048;
    private float height;
    private float width;
    private float border = 0;
    private List<textureData> list = new ArrayList<>();
}