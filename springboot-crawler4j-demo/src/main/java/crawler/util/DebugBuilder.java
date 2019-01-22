package crawler.util;

/**
 * @author zacconding
 * @Date 2019-01-22
 * @GitHub : https://github.com/zacscoding
 */
public class DebugBuilder {

    private StringBuilder builder;

    public DebugBuilder() {
        this.builder = new StringBuilder();
        builder.chars();
    }

    public DebugBuilder(int capacity) {
        this.builder = new StringBuilder(capacity);
    }

    public DebugBuilder appendMessage(String title, Object content) {
        builder.append(title).append(content).append("\n");
        return this;
    }

    public StringBuilder getStringBuilder() {
        return builder;
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
