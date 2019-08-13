package com.pt.osm.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author LongNT
 * 
 **/

public class Observer {

	private static final List<IObserver> LIST_CHAT = Collections.synchronizedList(new ArrayList<>());
	private static final int A_ADD = 1;
	private static final int A_DELETE = 2;

	public static void addListener(String shEventName, IObserver listener) {
			if (LIST_CHAT.indexOf(listener) == -1) {
				updateListChat(listener, A_ADD);
			}
		
	}

	public static synchronized void removeListener(String shEventName, IObserver listener) {
			updateListChat(listener, A_DELETE);
		
	}

	private static synchronized void updateListChat(IObserver listener, int action) {
		if (action == A_ADD) {
			LIST_CHAT.add(listener);
		} else if (action == A_DELETE) {
			LIST_CHAT.remove(listener);
		}
	}

	public static synchronized void fireChatEvent(String key, Object value) {
		List<IObserver> lsttmp = new ArrayList<>();
		for (int i = LIST_CHAT.size() - 1; i >= 0; i--) {
			if (!LIST_CHAT.get(i).isAlive()) {
				lsttmp.add(LIST_CHAT.get(i));
			}
		}
		LIST_CHAT.removeAll(lsttmp);
		for (IObserver IObserver : LIST_CHAT) {
			IObserver.onEvent(key, value);
		}
	}

}
