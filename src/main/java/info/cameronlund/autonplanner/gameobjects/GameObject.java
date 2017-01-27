package info.cameronlund.autonplanner.gameobjects;

import java.awt.*;
import java.awt.image.ImageObserver;

public abstract class GameObject {
    private String globalId;
    private int typeId;
    private String type;
    private int restingX, restingY, posX, posY;
    private boolean isOnField = true;
    private boolean isSkillsOnly = false;

    public GameObject(String type, int typeId) {
        this.typeId = typeId;
        this.type = type;
        this.globalId = type+"_"+typeId;
    }

    public String toString() {
        return globalId;
    }

    public int getTypeId() {
        return typeId;
    }

    public String getType() {
        return type;
    }

    public String getGlobalId() {
        return globalId;
    }

    public void setResting(int restingX, int restingY) {
        this.restingX = restingX;
        this.restingY = restingY;
    }

    public void setRestingReturn(int restingX, int restingY) {
        setResting(restingX,restingY);
        returnToResting();
    }

    public void returnToResting() {
        posX = restingX;
        posY = restingY;
    }

    public void setPosition(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void draw(ImageObserver observer, Graphics g) {
        draw(observer, g, posX, posY);
    }

    public abstract void draw(ImageObserver observer, Graphics g, int centerX, int centerY);

    public boolean isOnField() {
        return isOnField;
    }

    public void setOnField(boolean onField) {
        isOnField = onField;
    }

    public boolean isSkillsOnly() {
        return isSkillsOnly;
    }

    public void setSkillsOnly(boolean skillsOnly) {
        isSkillsOnly = skillsOnly;
    }
}
