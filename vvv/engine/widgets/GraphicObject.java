package vvv.engine.widgets;

public abstract class GraphicObject
{
	private Layer layer;
	
	public GraphicObject()
	{
		this.layer = null;
	}
	
	/* called from Layer only */
	final void setLayer(Layer layer)
	{
		this.layer = layer;
	}
	
	public Layer getLayer() { return this.layer; }		
}
