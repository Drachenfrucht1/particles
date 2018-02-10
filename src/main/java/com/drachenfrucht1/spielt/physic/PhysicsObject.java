package com.drachenfrucht1.spielt.physic;

import com.drachenfrucht1.spielt.Settings;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 25.01.2018.
 * Version: 0.0.1
 * Project: particles
 */
public class PhysicsObject extends Point2D {

  private @Getter @Setter float mass;
  private @Getter List<Force> forces = new ArrayList<>();
  private @Getter Vector2D speed = new Vector2D(0,0);
  private @Getter float acceleration;

  private @Getter @Setter boolean dead = false;

  public PhysicsObject(float mass, int x, int y) {
    super(x, y);
    this.mass = mass * Settings.WEIGHT_MULTIPLIER;
  }

  public void calculateSpeed() {
    Force resulting = Force.getResulting(getX(), getY(), forces);
    float acceleration = resulting.getValue() / mass;
    Vector2D direction = speed;
    direction.add(resulting);

    this.acceleration = acceleration/Settings.CALCULATIONS_PER_SECOND;

    float speedValue =  speed.getValue() + this.acceleration + Settings.RESISTANCE;

    speed = Vector2D.getVectorFromVectorAndValue(direction, speedValue);

    forces.clear();
  }

  public void move() {
    setX(getX() + (int) speed.x);
    setY(getY() + (int) speed.y);
  }

  public void gravitation(List<PhysicsObject> physicsObjects) {
    if(dead) return;
    for(PhysicsObject obj : physicsObjects) {
      if(!obj.equals(this) && !obj.isDead()) {
        float vecX = obj.getX() - getX();
        float vecY = obj.getY() - getY();
        float b = (float) Math.sqrt((Math.pow(vecX, 2) + Math.pow(vecY, 2)));
        float value = (float) ((Settings.GRAVITATION_CONSTANT * mass * obj.mass) / Math.pow(b, 2));

        Force f1 = Force.getForceFromVectorAndValue(vecX, vecY,  getX(),  getY(), value);
        forces.add(f1);
      }
    }
  }

  @Override
  public String toString() {
    return "Mass: " + mass + "\n" + "X: " + getX() + " - Y: " + getY();
  }
}
