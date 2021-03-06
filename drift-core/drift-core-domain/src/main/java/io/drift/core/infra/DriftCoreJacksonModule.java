package io.drift.core.infra;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.drift.core.system.EnvironmentKey;
import io.drift.core.system.SubSystemEnvironmentKey;
import io.drift.core.system.SubSystemEnvironmentKeyDeserializer;
import io.drift.core.system.SubSystemKey;

import java.io.IOException;

public class DriftCoreJacksonModule extends SimpleModule {

    class EnvironmentKeySerializer extends StdSerializer<EnvironmentKey> {

        EnvironmentKeySerializer() {
            super(EnvironmentKey.class);
        }

        @Override
        public void serialize(EnvironmentKey environmentKey, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(environmentKey.getName());
        }
    }

    class SubSystemKeySerializer extends StdSerializer<SubSystemKey> {

        SubSystemKeySerializer() {
            super(SubSystemKey.class);
        }

        @Override
        public void serialize(SubSystemKey subSystemKey, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(subSystemKey.getName());
        }
    }


    class EnvironmentKeyDeserializer extends StdDeserializer<EnvironmentKey> {

        EnvironmentKeyDeserializer() {
            super(EnvironmentKey.class);
        }

        @Override
        public EnvironmentKey deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return new EnvironmentKey(jsonParser.getValueAsString());
        }
    }

    class SubSystemKeyDeserializer extends StdDeserializer<SubSystemKey> {

        SubSystemKeyDeserializer() {
            super(SubSystemKey.class);
        }

        @Override
        public SubSystemKey deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
            return new SubSystemKey(jsonParser.getValueAsString());
        }
    }



    public DriftCoreJacksonModule() {
        super();
        addKeyDeserializer(SubSystemEnvironmentKey.class, new SubSystemEnvironmentKeyDeserializer());

        addSerializer(new EnvironmentKeySerializer());
        addDeserializer(EnvironmentKey.class, new EnvironmentKeyDeserializer());

        addSerializer(new SubSystemKeySerializer());
        addDeserializer(SubSystemKey.class, new SubSystemKeyDeserializer());
    }


}
