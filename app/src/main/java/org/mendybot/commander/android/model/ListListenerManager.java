package org.mendybot.commander.android.model;

import org.mendybot.commander.android.model.list.ListListener;

import java.util.ArrayList;

public class ListListenerManager<M extends ListListener> {
    private ArrayList<M> listeners = new ArrayList<>();

    public void addListener(M listListener) {
        listeners.add(listListener);
    }

    public void removeListener(M listListener) {
        listeners.remove(listListener);
    }

    public void notifyAllChanged() {
        for (ListListener lis : listeners) {
            lis.listChanged();
        }
    }
}
