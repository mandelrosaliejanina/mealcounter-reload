package at.htl.mealcounter.entity;

import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.List;

public class JsonMessageEncoder implements Encoder.Text<List<Consumation>> {



    Jsonb jsonb = JsonbBuilder.create();

    @Override
    public String encode(List<Consumation> consumations) throws EncodeException {

        return jsonb.toJson(consumations);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
