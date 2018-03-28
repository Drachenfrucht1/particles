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
  private @Getter @Setter float v0;
  private @Getter @Setter float vE;
  private @Getter @Setter Vector2D Richtungv0;
  private @Getter @Setter boolean dead = false;
  private @Getter @Setter Point2D Anfangspunkt;

  public PhysicsObject(float mass, int x, int y) {
    super(x, y);
    this.mass = mass * Settings.WEIGHT_MULTIPLIER;
  }

  public void calculateSpeed() {
    Anfangspunkt.setX(getX());
    Anfangspunkt.setY(getY());
    Force resulting = Force.getResulting(getX(), getY(), forces);
    float acceleration = resulting.getValue() / mass;
    //a braucht richtugn der resulting forces
    setV0(speed.getValue());//Betrag v0
    Vector2D direction = speed;
    direction.add(resulting);
    Richtungv0=speed;
    this.acceleration = acceleration/Settings.CALCULATIONS_PER_SECOND;

    float speedValue =  speed.getValue() + this.acceleration + Settings.RESISTANCE;

    speed = Vector2D.getVectorFromVectorAndValue(direction, speedValue);//gesamter Vektor für diese Zeiteinheit, mit a miteinbezogen
    //eig falsch, da hier Beträge simpel addiert wurden, unbeachtet ihrer richtungsverhältnise und dann der neue betrag auf die Richtung gelegt wurde, welche aber auch falsch ist, da diese
    //ahängig von den beträgen in die jeweilige richtung ist
    setVE(speed.getValue());
    forces.clear();
    v0Mess();//setzt v0 auf Anfangsv relativ zur bewegungsrichtung
  }

  public void move() {
    setX(getX() + (int) speed.x);
    setY(getY() + (int) speed.y);
  }
  public void v0Mess(){
    float x=(float)Math.acos((Richtungv0.getX()+Richtungv0.getY())*(speed.getX()+speed.getY())/Richtungv0.getValue()*speed.getValue());//Winkel zwischen den Geraden
    v0=((float)Math.cos(x)*v0);

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
