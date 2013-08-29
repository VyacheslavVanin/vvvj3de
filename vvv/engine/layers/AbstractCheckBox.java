/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine.layers;

/**
 *
 * @author QwertyVVV
 */
public abstract class AbstractCheckBox extends AbstractButton
{
    private ActionListener onCheckListener = null;
    private ActionListener onUncheckListener = null;
    
    private boolean   checked = false;

    public void addOnCheckListener( ActionListener listener )
    {
        this.onCheckListener = listener;
    }
    
    public void addOnUncheckListener( ActionListener listener )
    {
        this.onUncheckListener = listener;
    }
    
    public boolean isChecked()
    {
        return checked;
    }
    
    private void onCheckBase()
    {
        onCheck();
        if(  onCheckListener != null)
        {
            onCheckListener.action();
        }
    }
    
    private void onUncheckBase()
    {
        onUncheck();
        if( onUncheckListener != null )
        {
            onUncheckListener.action();
        }
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
