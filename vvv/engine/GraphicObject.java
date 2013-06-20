package vvv.engine;

public abstract class GraphicObject
{
	private Layer layer;
	
	public GraphicObject()
	{
		this.layer = null;
	}
	
	/* called from Layer only */
	void setLayer(Layer layer)
	{
		this.layer = layer;
	}
	
	Layer getLayer() { return this.layer; }		
}
