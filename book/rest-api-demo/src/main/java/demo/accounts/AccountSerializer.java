package demo.accounts;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

/**
 * https://github.com/keesun/study/tree/master/rest-api-with-spring
 */
public class AccountSerializer extends JsonSerializer<Account> {

    @Override
    public void serialize(Account account, JsonGenerator generator, SerializerProvider serializers) throws IOException {
        generator.writeStartObject();
        generator.writeNumberField("id", account.getId());
        generator.writeEndObject();
    }
}
