package info.cameronlund.autonplanner.zones;

import info.cameronlund.autonplanner.gameobjects.GameObject;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.List;

public abstract class ScoringZone {
    private String name;
    private List<GameObject> scoredObjects = new ArrayList<>();

    public ScoringZone(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void paint(ImageObserver observer, Graphics g);

    public void scoreGameObject(GameObject object) {
        scoredObjects.add(object);
    }

    public List<GameObject> getScoredObjects() {
        return scoredObjects;
    }

    public void clear() {
        scoredObjects = new ArrayList<>();
    }

    public void transferObjects(ScoringZone zone) {
        zone.getScoredObjects().addAll(getScoredObjects());
        clear();
    }
}
