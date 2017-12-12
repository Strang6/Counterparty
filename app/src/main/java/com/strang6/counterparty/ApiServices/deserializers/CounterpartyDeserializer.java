package com.strang6.counterparty.ApiServices.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.strang6.counterparty.Counterparty;

import java.lang.reflect.Type;

/**
 * Created by Strang6 on 21.11.2017.
 */

public class CounterpartyDeserializer implements JsonDeserializer<Counterparty> {

    @Override
    public Counterparty deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Counterparty counterparty = null;
        if (json.isJsonObject()) {
            JsonObject data = json.getAsJsonObject().getAsJsonObject("data");
            String name = data.getAsJsonObject("name").get("full").getAsString();
            String opf = data.getAsJsonObject("opf").get("short").getAsString();
            String address = data.getAsJsonObject("address").get("value").getAsString();
            String inn = data.get("inn").getAsString();
            String kpp = data.get("kpp").getAsString();
            String ogrn = data.get("ogrn").getAsString();
            int branchCount = data.get("branch_count").getAsInt();
            Counterparty.BranchType branchType = Counterparty.BranchType
                    .valueOf(data.get("branch_type").getAsString());
            Counterparty.OrganizationType type = Counterparty.OrganizationType.valueOf(data.get("type").getAsString());
            counterparty =
                    new Counterparty(name, opf, address, inn, kpp, ogrn, branchCount, branchType, type);
        }
        return counterparty;
    }
}
