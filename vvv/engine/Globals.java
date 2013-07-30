/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vvv.engine;

/**
 *
 * @author Вячеслав
 */
public class Globals 
{
    static public class Time
    {
        static private long currentTime = System.currentTimeMillis();
        
        static public  void update()
        {
            currentTime = System.currentTimeMillis();
        }
        
        static public long get()
        {
            return currentTime;
        }
    }
}
