package com.strang6.counterparty.ApiServices.deserializers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.strang6.counterparty.Counterparty;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Strang6 on 21.11.2017.
 */

public class CounterpartyListDeserializer implements JsonDeserializer<List<Counterparty>> {
    @Override
    public List<Counterparty> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Counterparty> counterparties = new ArrayList<>();
        if (json.isJsonObject()) {
            JsonArray suggestions = json.getAsJsonObject().getAsJsonArray("suggestions");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Counterparty.class, new CounterpartyDeserializer())
                    .create();
            Counterparty counterparty;
            JsonObject object;
            for (int i = 0; i < suggestions.size(); i++) {
                object = suggestions.get(i).getAsJsonObject();
                counterparty = gson.fromJson(object, Counterparty.class);
                counterparties.add(counterparty);
            }
        }
        return counterparties;
    }
}

