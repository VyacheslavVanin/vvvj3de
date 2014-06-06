/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.sprite;

import vvv.engine.texture.Texture;
import vvv.engine.widgets.RectangularGraphicObject;

/**
 *
 * @author QwertyVVV
 */
public abstract class Sprite extends RectangularGraphicObject
{
    abstract public Texture getTexture();
}
