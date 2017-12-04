package com.strang6.counterparty;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Strang6 on 04.12.2017.
 */

public class GeocodingDeserializer implements JsonDeserializer<LatLng> {
    @Override
    public LatLng deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        LatLng latLng = null;
        if (json.isJsonObject()) {
            JsonArray results = json.getAsJsonObject().get("results").getAsJsonArray();
            JsonObject geometry = results.get(0).getAsJsonObject().getAsJsonObject("geometry");
            JsonObject location = geometry.getAsJsonObject("location");
            Double lat = location.get("lat").getAsDouble();
            Double lng = location.get("lng").getAsDouble();
            latLng = new LatLng(lat, lng);
        }
        return latLng;
    }
}
