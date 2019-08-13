package com.pt.osm.common;

/**
 *
 * 
 * @author LongNT
 * 
 **/

public interface IObserver {

	public void addEventListener();

	public void removeEventListener();

	public void onEvent(String key, Object value);

	public boolean isAlive();
}
