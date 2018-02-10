package com.drachenfrucht1.spielt;

import com.drachenfrucht1.spielt.physic.PhysicsObject;
import com.drachenfrucht1.spielt.physic.Simulation;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by Dominik on 10.02.2018.
 * Version: 0.0.1
 * Project: particles
 */
public class GsonAdapter implements JsonSerializer<Simulation>, JsonDeserializer<Simulation> {

  @Override
  public Simulation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    JsonArray array = jsonElement.getAsJsonArray();
    for(JsonElement e : array) {
      JsonObject obj = e.getAsJsonObject();
      obj.get("mass");
    }
    return null;
  }

  @Override
  public JsonElement serialize(Simulation simulation, Type type, JsonSerializationContext jsonSerializationContext) {
    JsonArray array = new JsonArray();
    for(PhysicsObject obj : simulation.getObjects()) {
      JsonObject jObj = new JsonObject();
      jObj.add("mass", new JsonPrimitive(obj.getMass()));
      jObj.add("x", new JsonPrimitive(obj.getX()));
      jObj.add("y", new JsonPrimitive(obj.getY()));
      array.add(jObj);
    }
    return array;
  }
}
