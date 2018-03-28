package com.drachenfrucht1.spielt;

/**
 * Created by Dominik on 25.01.2018.
 * Version: 0.0.1
 * Project: particles
 */
public class Settings {

  public final static int WIDTH = 800;
  public final static int HEIGHT = 800;

  public final static float GRAVITATION_CONSTANT = 6.674F * (float) Math.pow(10, -11);

  public final static int CALCULATIONS_PER_SECOND = 60; //up to 120 in realtime mode

  public final static float RESISTANCE = 0; //value which will be added to the acceleration of the objects every operation

  public static int PLAYGROUND_WIDTH = 7500;
  public static int PLAYGROUND_HEIGHT = 7500;

  public final static int OBJECT_COUNT = 10;

  public static float WEIGHT_MULTIPLIER = 10000;

  public final static Mode MODE = Mode.realtime;
  public final static int FRAME_COUNT = 60*Settings.CALCULATIONS_PER_SECOND;//number of frames that should be rendered

  public enum Mode {
    web,
    realtime,
    render;
  }
}
