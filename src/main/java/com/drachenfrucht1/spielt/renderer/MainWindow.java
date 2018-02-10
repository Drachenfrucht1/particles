package com.drachenfrucht1.spielt.renderer;

import com.drachenfrucht1.spielt.GsonAdapter;
import com.drachenfrucht1.spielt.Settings;
import com.drachenfrucht1.spielt.Settings.Mode;
import com.drachenfrucht1.spielt.physic.PhysicsObject;
import com.drachenfrucht1.spielt.physic.Simulation;
import com.drachenfrucht1.spielt.web.WebServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Dominik on 25.01.2018.
 * Version: 0.0.1
 * Project: particles
 */
public class MainWindow extends Application {

  private Stage window;
  private Pane root;
  private Pane bg;
  private double prevX;
  private double prevY;

  public static boolean NOGUI = false;
  public static Simulation sim;
  private WebServer webServer;

  private int frameNumber = 0;

  @Override
  public void start(Stage primaryStage) {
    if(Settings.MODE != Mode.web) {
      window = primaryStage;

      root = new Pane();

      Rectangle r = new Rectangle(0, 0, Settings.PLAYGROUND_WIDTH, Settings.PLAYGROUND_HEIGHT);
      r.setFill(Color.BLACK);
      bg = new Pane();
      bg.getChildren().addAll(r, root);


      Scene scene = new Scene(bg, Settings.WIDTH, Settings.HEIGHT);
      window.setScene(scene);

      scene.setOnMousePressed(e -> {
        prevX = e.getX();
        prevY = e.getY();
      });

      scene.setOnMouseDragged(e -> {
        if (prevX < e.getX() && prevY < e.getY()) {
          double a = e.getX() - prevX;
          double b = e.getY() - prevY;
          bg.setTranslateX(bg.getTranslateX() + a);
          bg.setTranslateY(bg.getTranslateY() + b);
        } else if (prevX > e.getX() && prevY > e.getY()) {
          double a = prevX - e.getX();
          double b = prevY - e.getY();
          bg.setTranslateX(bg.getTranslateX() - a);
          bg.setTranslateY(bg.getTranslateY() - b);
        } else if (prevX > e.getX() && prevY < e.getY()) {
          double a = prevX - e.getX();
          double b = e.getY() - prevY;
          bg.setTranslateX(bg.getTranslateX() - a);
          bg.setTranslateY(bg.getTranslateY() + b);
        } else if (prevX < e.getX() && prevY > e.getY()) {
          double a = e.getX() - prevX;
          double b = prevY - e.getY();
          bg.setTranslateX(bg.getTranslateX() + a);
          bg.setTranslateY(bg.getTranslateY() - b);
        }
        prevX = e.getX();
        prevY = e.getY();
      });

      scene.setOnScroll(e -> {
        if (e.getDeltaY() < 0) {
          bg.setScaleX(bg.getScaleX() - 0.1);
          bg.setScaleY(bg.getScaleY() - 0.1);
        } else {
          bg.setScaleX(bg.getScaleX() + 0.1);
          bg.setScaleY(bg.getScaleY() + 0.1);
        }
      });

      window.setOnCloseRequest(e -> {
        e.consume();
        System.exit(0);
      });

      window.setTitle("Gravitation Simulation - Drachenfrucht1");
      window.setResizable(true);
      window.show();

      if (Settings.MODE == Mode.realtime) {
        startAnimation();
        sim.startTimer();
      } else {
        for (int i = 0; i < Settings.FRAME_COUNT; i++) {
          sim.calculations();
          draw();
          saveFrame();
        }
        System.exit(0);
      }
    } else {
      webServer = new WebServer(4000);
      GsonBuilder gsonBuilder = new GsonBuilder();
      gsonBuilder.registerTypeAdapter(Simulation.class, new GsonAdapter());
      Gson gson  = gsonBuilder.create();

      sim.startTimer();

      TimerTask t = new TimerTask() {
        @Override
        public void run() {
          String json = gson.toJson(sim);
          webServer.getConnections().forEach(c -> c.send(json));
        }
      };

      Timer timer = new Timer();
      timer.schedule(t, 0, (long) 1000/30);
    }
  }

  private void startAnimation() {
    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        draw();
      }
    };
    timer.start();
  }

  private void draw() {
    root.getChildren().removeAll(root.getChildren());
    for(PhysicsObject obj : sim.getObjects()) {
      int width = (int) obj.getMass()/100000;
      int height = (int) obj.getMass()/100000;
      Rectangle r = new Rectangle(obj.getX() - width/2, obj.getY() - height/2, width, height);
      r.setFill(Color.RED);

      root.getChildren().add(r);
    }
  }

  private void saveFrame() {
    WritableImage img = root.snapshot(null, null);

    File file = new File("./output/frame" + frameNumber + ".png");

    try {
      ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
    } catch (Exception e) {
      e.printStackTrace();
    }
    frameNumber++;
  }

}
