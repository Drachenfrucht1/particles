package com.drachenfrucht1.spielt.physic;


import lombok.Getter;
import lombok.Setter;

/**
 * Created by Dominik on 25.01.2018.
 * Version: 0.0.1
 * Project: particles
 */
public class Point2D {

  private @Getter @Setter int x;
  private @Getter @Setter int y;

  public Point2D(int x, int y) {
    this.x = x;
    this.y = y;
  }
}
