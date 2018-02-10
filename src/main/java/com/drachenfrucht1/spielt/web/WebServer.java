package com.drachenfrucht1.spielt.web;

import com.drachenfrucht1.spielt.physic.Simulation;
import lombok.Getter;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebServers;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 10.02.2018.
 * Version: 0.0.1
 * Project: particles
 */
public class WebServer extends BaseWebSocketHandler {

  private org.webbitserver.WebServer webServer;       /**GLOBAL webserver instance*/
  private @Getter List<WebSocketConnection> connections = new ArrayList<>(); /**open connections*/


  /**
   * FUNCTION: WebServer()
   *
   * PURPOSE: create a new webserver instance on which webclints can connect
   * @param sim
   */
  public WebServer(int port) {
    webServer = WebServers.createWebServer(port);
    webServer.add("/ws", this);

    webServer.start();

    System.out.println("WebServer started...");
  }

  /**
   * FUNCTION: stop()
   *
   * PURPOSE: stop the webserver
   */
  public void stop() {
    webServer.stop();
  }

  @Override
  public void onOpen(WebSocketConnection con) {
    connections.add(con);
  }

  @Override
  public void onClose(WebSocketConnection con) {
    connections.remove(con);
  }

  @Override
  public void onMessage(WebSocketConnection con, String msg) {
    //do nothing
  }
}
