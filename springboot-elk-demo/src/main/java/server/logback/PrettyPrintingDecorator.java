package server.logback;

import com.fasterxml.jackson.core.JsonGenerator;
import net.logstash.logback.decorate.JsonGeneratorDecorator;

/**
 * @author zacconding
 * @Date 2018-12-21
 * @GitHub : https://github.com/zacscoding
 */
public class PrettyPrintingDecorator implements JsonGeneratorDecorator {

    @Override
    public JsonGenerator decorate(JsonGenerator jsonGenerator) {
        return jsonGenerator.useDefaultPrettyPrinter();
    }
}
