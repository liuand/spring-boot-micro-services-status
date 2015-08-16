package org.poop.reporter.registry;

import org.poop.reporter.domain.Application;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MapStore {
    private Map<Integer, Application> map = new ConcurrentHashMap<>();


    public Application find(Application app) {
        return map.get(app.hashCode());
    }

    public Application put(Application app) {
        return map.put(app.hashCode(), app);
    }

    public Collection<Application> findAll() {
        return map.values();
    }
}
