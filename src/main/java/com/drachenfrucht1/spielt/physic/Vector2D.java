package com.drachenfrucht1.spielt.physic;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Dominik on 25.01.2018.
 * Version: 0.0.1
 * Project: particles
 */
public class Vector2D {

  protected  @Setter @Getter float x;
  protected  @Setter @Getter float y;

  public Vector2D(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public void add(Vector2D v) {
    x += v.x;
    y += v.y;
  }

  public void multiply(float f) {
    x *= f;
    y *= f;
  }

  public float getValue() {
    return (float) Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2)));
  }

  public static Vector2D getVectorFromVectorAndValue(Vector2D v , float value) {
    float n = (float) Math.sqrt(Math.pow(value, 2) / (Math.pow(v.x, 2) + Math.pow(v.y, 2)));

    return new Vector2D(v.x * n, v.y * n);
  }

}
