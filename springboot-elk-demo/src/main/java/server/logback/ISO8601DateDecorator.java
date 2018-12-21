package server.logback;

import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import net.logstash.logback.decorate.JsonFactoryDecorator;

/**
 * @author zacconding
 * @Date 2018-12-21
 * @GitHub : https://github.com/zacscoding
 */
public class ISO8601DateDecorator implements JsonFactoryDecorator {

    @Override
    public MappingJsonFactory decorate(MappingJsonFactory mappingJsonFactory) {
        ObjectMapper codec = mappingJsonFactory.getCodec();
        codec.setDateFormat(new ISO8601DateFormat());
        return mappingJsonFactory;
    }
}
