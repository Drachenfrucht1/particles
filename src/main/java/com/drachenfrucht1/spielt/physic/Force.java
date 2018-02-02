package com.drachenfrucht1.spielt.physic;

import lombok.Getter;

import java.util.List;

/**
 * Created by Dominik on 25.01.2018.
 * Version: 0.0.1
 * Project: particles
 * simple 2dVector class
 */
public class Force extends Vector2D {

  private @Getter int originX;
  private @Getter int originY;

  public Force(int originX, int originY, float x, float y) {
    super(x, y);
    this.originX = originX;
    this.originY = originY;
  }

  public void add(Force f) {
    x += f.x;
    y += f.y;
  }

  public void negate(float a) {
    x *= a;
    y *= a;
  }

  public static Force getForceFromVectorAndValue(float x, float y,int originX, int originY, float value) {
    Force f;

    Vector2D v = Vector2D.getVectorFromVectorAndValue(new Vector2D(x, y), value);

    f = new Force(originX, originY, v.x, v.y);

    return f;
  }

  /**
   * get the resulting
   * @param forces
   */
  public static Force getResulting(int originX, int originY, List<Force> forces) {
    Force resulting = new Force(originX, originY, 0, 0);
    for(Force f : forces) {
      if(f.originX == originX && f.originY == originY) {
        resulting.add(f);
      }
    }
    return resulting;
  }
}
