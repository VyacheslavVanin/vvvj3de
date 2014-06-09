/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package defaults;

import java.io.IOException;
import vvv.engine.shader.ModelShader;
import vvv.engine.widgets.WidgetLayer;

/**
 *
 * @author vvv
 */
public class DefaultWidgetLayer extends WidgetLayer
{
    public DefaultWidgetLayer() throws IOException
    {
        super();
        final ModelShader textShader = DefaultGui.getTextShader();
        final ModelShader imageShader = DefaultGui.getSpriteShader();
        
        setDepth(0.1f);
        setTextShader(textShader);
        setImageShader(imageShader);
    }
}
