package com.drachenfrucht1.spielt;

import com.drachenfrucht1.spielt.physic.Simulation;
import com.drachenfrucht1.spielt.renderer.MainWindow;
import javafx.application.Application;
import java.util.Timer;
import java.util.TimerTask;
import com.drachenfrucht1.spielt.physic.Simulation;
import com.drachenfrucht1.spielt.web.WebServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Dominik on 25.01.2018.
 * Version: 0.0.1
 * Project: particles
 */
public class Main {

  public static void main(String args[]) {

    for(String s : args) {
      if(s.equals("nogui")) {
        MainWindow.NOGUI = true;
        Settings.PLAYGROUND_WIDTH = 1000 * 4;
        Settings.PLAYGROUND_HEIGHT = 410 * 4;
        //Settings.WEIGHT_MULTIPLIER = 0;
        startNoGui();
        return;
      }
    }
    MainWindow.sim = new Simulation();
    Application.launch(MainWindow.class, args);
  }

  private static void startNoGui() {
    WebServer webServer = new WebServer(4000);
    Simulation sim = new Simulation();
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
