package com.drachenfrucht1.spielt;

import com.drachenfrucht1.spielt.physic.Simulation;
import com.drachenfrucht1.spielt.renderer.MainWindow;
import javafx.application.Application;

/**
 * Created by Dominik on 25.01.2018.
 * Version: 0.0.1
 * Project: particles
 */
public class Main {

  public static void main(String args[]) {
    MainWindow.sim = new Simulation();
    Application.launch(MainWindow.class, args);
  }
}
