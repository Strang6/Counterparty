package com.strang6.counterparty.ApiServices.deserializers;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.strang6.counterparty.Counterparty;
import com.strang6.counterparty.Logger;

import java.lang.reflect.Type;

/**
 * Created by Strang6 on 21.11.2017.
 */

public class CounterpartyDeserializer implements JsonDeserializer<Counterparty> {

    @Override
    public Counterparty deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Counterparty counterparty = null;
        if (json.isJsonObject()) {
            Logger.d("CounterpartyDeserializer: " + json.toString());
            JsonObject data = json.getAsJsonObject().getAsJsonObject("data");
            String name = data.getAsJsonObject("name").get("full").getAsString();
            String opf = "-";
            if (! data.get("opf").isJsonNull()) {
                opf = data.getAsJsonObject("opf").get("short").getAsString();
            }
            JsonObject jsonAddress = data.getAsJsonObject("address");
            String address = jsonAddress.get("value").getAsString();
            String inn = "-";
            if (! data.get("inn").isJsonNull()) {
                inn = data.get("inn").getAsString();
            }
            String kpp = "-";
            if (data.has("kpp") && !data.get("kpp").isJsonNull()) {
                kpp = data.get("kpp").getAsString();
            }
            String ogrn = data.get("ogrn").getAsString();
            int branchCount = -1;
            if (data.has("branch_count")) {
                branchCount = data.get("branch_count").getAsInt();
            }
            Counterparty.BranchType branchType;
            if (data.has("branch_type")) {
                branchType = Counterparty.BranchType
                        .valueOf(data.get("branch_type").getAsString());
            } else
                branchType = Counterparty.BranchType.NULL;
            Counterparty.OrganizationType type = Counterparty.OrganizationType.valueOf(data.get("type").getAsString());
            LatLng latLng = null;
            if (! jsonAddress.get("data").isJsonNull()) {
                data = jsonAddress.getAsJsonObject("data");
                String lat = data.get("geo_lat").getAsString();
                String lng = data.get("geo_lon").getAsString();
                latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            }
            counterparty =
                    new Counterparty(name, opf, address, inn, kpp, ogrn, branchCount, branchType, type, latLng);
        }
        return counterparty;
    }
}
