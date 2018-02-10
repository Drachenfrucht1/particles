package com.drachenfrucht1.spielt.physic;

import com.drachenfrucht1.spielt.Settings;
import com.drachenfrucht1.spielt.Settings.Mode;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Dominik on 25.01.2018.
 * Version: 0.0.1
 * Project: particles
 */
public class Simulation {

  private @Getter CopyOnWriteArrayList<PhysicsObject> objects = new CopyOnWriteArrayList<>();
  private ZonedDateTime lastReset;

  public Simulation() {
    createObjects(Settings.OBJECT_COUNT);
  }

  public void startTimer() {
    lastReset = ZonedDateTime.now();
    //Timer for physics calculations
    TimerTask task = new TimerTask() {
      @Override
      public void run() {
        if(Settings.MODE == Mode.web) {
          if(ChronoUnit.MINUTES.between(lastReset, ZonedDateTime.now()) >= 5) {
            objects.clear();
            createObjects(Settings.OBJECT_COUNT);
            lastReset = ZonedDateTime.now();
          }
        }
        if(objects.size() <= 0) {
          createObjects(Settings.OBJECT_COUNT);
          lastReset = ZonedDateTime.now();
        }
        calculations();
      }
    };

    Timer timer = new Timer();
    timer.schedule(task, 2000, (long)(1000/Settings.CALCULATIONS_PER_SECOND));
  }

  public void calculations() {
    objects.forEach(o -> o.gravitation(objects));
    objects.forEach(PhysicsObject::calculateSpeed);
    test();
    objects.forEach(o -> {
      if(o.isDead()) {
        objects.remove(o);
      }
      o.move();
    });
    for (PhysicsObject o : objects) {
      if(o.getX() < 0 || o.getY() < 0 || o.getX() > Settings.PLAYGROUND_WIDTH || o.getY() > Settings.PLAYGROUND_HEIGHT) {
        o.setDead(true);
        objects.remove(o);
        System.out.println("Removed object");
      }
    }
    System.gc();
  }

  public void test() {
    List<PhysicsObject> phObjetcs = new ArrayList<>();
    phObjetcs.addAll(objects);
    while(phObjetcs.size() > 1) {
      PhysicsObject obj1 = phObjetcs.get(0);
      for(int i = 1; i < phObjetcs.size(); i++) {
        PhysicsObject obj2 = phObjetcs.get(i);
        boolean obj1Match = false;
        boolean obj2Match = false;

        float m1 = obj1.getSpeed().getY() / obj1.getSpeed().getX();
        float c1 = obj1.getY() - m1 * obj1.getX();

        float m2 = obj2.getSpeed().getY() / obj2.getSpeed().getX();
        float c2 = obj2.getY() - m2 * obj2.getX();

        float x = 0;
        try {
           x = (c2-c1) / (m1-m2);
        } catch (Exception e) {
          continue;
        }
        float y = m1 * x + c1;

        if(obj1.getX() < (obj1.getSpeed().getX() + obj1.getX())) {
          if(obj1.getX() < x && x < (obj1.getSpeed().getX() + obj1.getX())) {
            obj1Match = true;
          }
        } else {
          if(obj1.getX() > x && x > (obj1.getSpeed().getX() + obj1.getX())) {
            obj1Match = true;
          }
        }

        if(obj2.getX() < (obj2.getSpeed().getX() + obj2.getX())) {
          if(obj2.getX() < x && x < (obj2.getSpeed().getX() + obj2.getX())) {
            obj2Match = true;
          }
        } else {
          if(obj2.getX() > x && x > (obj2.getSpeed().getX() + obj2.getX())) {
            obj2Match = true;
          }
        }

        if(obj1Match && obj2Match) {
          obj2.setDead(true);
          phObjetcs.remove(obj2);
          objects.remove(obj2);

          obj1.setMass(obj1.getMass() + obj2.getMass());

          float valueF1 = obj1.getAcceleration() * obj1.getMass();
          float valueF2 = obj2.getAcceleration() * obj2.getMass();

          Force f1 = Force.getForceFromVectorAndValue(obj1.getSpeed().getX(),
                  obj1.getSpeed().getY(),
                  (int) x,
                  (int) y,
                  valueF1);

          Force f2 = Force.getForceFromVectorAndValue(obj2.getSpeed().getX(),
                  obj2.getSpeed().getY(),
                  (int) x,
                  (int) y,
                  valueF2);

          Force resulting = Force.getResulting((int) x, (int) y, new ArrayList<>(Arrays.asList(f1, f2)));

          obj1.getForces().add(resulting);
        }
      }
      phObjetcs.remove(0);
    }
  }

  public void createObjects(int count) {
    Random r = new Random();
    for(int i = 0; i< count; i++) {
      objects.add(new PhysicsObject(r.nextInt(1000) + 10, r.nextInt(Settings.PLAYGROUND_WIDTH), r.nextInt(Settings.PLAYGROUND_HEIGHT)));
    }
  }
}
