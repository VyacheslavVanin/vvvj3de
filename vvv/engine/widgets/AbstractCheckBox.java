package vvv.engine.widgets;

/**
 *
 * @author QwertyVVV
 */
public abstract class AbstractCheckBox extends AbstractButton
{
    private ListenerContainer onCheckListener = new ListenerContainer();
    private ListenerContainer onUncheckListener = new ListenerContainer();
    
    private boolean   checked = false;

    public void addOnCheckListener( ActionListener listener )
    {
        this.onCheckListener.addListener(listener);
    }
    
    public void addOnUncheckListener( ActionListener listener )
    {
        this.onUncheckListener.addListener(listener);
    }
    
    public boolean isChecked()
    {
        return checked;
    }
    
    private void onCheckBase()
    {
        onCheck();
        onCheckListener.action();
    }
    
    private void onUncheckBase()
    {
        onUncheck();
        onUncheckListener.action();
    }
    
    protected abstract void onCheck();
    protected abstract void onUncheck();
    
    @Override
    protected void onClick()
    {
        if( checked )
        {
            checked = false;
            onUncheckBase();
        }
        else
        {
            checked = true;
            onCheckBase();
        }
    } 
}
