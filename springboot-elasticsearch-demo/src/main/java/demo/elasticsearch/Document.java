package demo.elasticsearch;

/**
 * @author zacconding
 * @Date 2018-12-28
 * @GitHub : https://github.com/zacscoding
 */
public interface Document {

    String getIndex();

    String getSettings();

    String getMappings();
}
