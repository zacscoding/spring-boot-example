package demo.basic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.GlobalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

/**
 * Create table / Create item / Get item / Update item / Delete item / Drop table.
 */
public class SdkBasicUsage extends AbstractDynamoBasic {

    @Test
    @Disabled
    public void runTests() {
        runCreateTable();
        runCreateItem();
        runGetItem();
        runUpdateItem();
        runDeleteItem();
    }

    private void runCreateTable() {
        System.out.println("## Create table");
        final CreateTableRequest createTableRequest = new CreateTableRequest()
                .withAttributeDefinitions(
                        new AttributeDefinition("id", ScalarAttributeType.S),
                        new AttributeDefinition("mentionId", ScalarAttributeType.N),
                        new AttributeDefinition("createdAt", ScalarAttributeType.S)
                ).withTableName("Comment").withKeySchema(
                        new KeySchemaElement("id", KeyType.HASH)
                ).withGlobalSecondaryIndexes(
                        (new GlobalSecondaryIndex())
                                .withIndexName("byMentionId")
                                .withKeySchema(
                                        new KeySchemaElement("mentionId", KeyType.HASH),
                                        new KeySchemaElement("createdAt", KeyType.RANGE))
                                .withProjection(
                                        (new Projection()).withProjectionType(ProjectionType.ALL))
                                .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
                ).withProvisionedThroughput(
                        new ProvisionedThroughput(1L, 1L)
                );

        final boolean result = TableUtils.createTableIfNotExists(client, createTableRequest);
        assertThat(result).isTrue();
    }

    private void runCreateItem() {
        System.out.println("## Create item");
        final Map<String, AttributeValue> item = new HashMap<>();

        item.put("id", new AttributeValue().withS("uuid"));
        item.put("name", new AttributeValue().withS("comment name"));
        item.put("mentionId", new AttributeValue().withN("1"));
        item.put("content", new AttributeValue().withS("comment content"));
        item.put("deleted", new AttributeValue().withBOOL(false));
        item.put("createdAt", new AttributeValue().withS("1836-03-07T02:21:30.536Z"));

        final PutItemRequest request = new PutItemRequest()
                .withTableName("Comment")
                .withItem(item);
        final PutItemResult result = client.putItem(request);
        assertThat(result.getSdkHttpMetadata().getHttpStatusCode()).isEqualTo(200);
    }

    private void runGetItem() {
        System.out.println("## Read item");
        final Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue().withS("uuid"));

        final GetItemRequest request = new GetItemRequest()
                .withTableName("Comment")
                .withKey(key);

        final GetItemResult result = client.getItem(request);
        final Map<String, AttributeValue> item = result.getItem();
        assertThat(item.get("id").getS()).isEqualTo("uuid");
        System.out.println(item);
    }

    private void runUpdateItem() {
        System.out.println("## Update item");
        final String updateContent = "modified comment content";
        final Map<String, AttributeValue> item = new HashMap<>();

        item.put("id", new AttributeValue().withS("uuid"));
        item.put("name", new AttributeValue().withS("comment name"));
        item.put("mentionId", new AttributeValue().withN("1"));
        item.put("content", new AttributeValue().withS(updateContent));
        item.put("deleted", new AttributeValue().withBOOL(false));
        item.put("createdAt", new AttributeValue().withS("1836-03-07T02:21:30.536Z"));

        final PutItemRequest request = new PutItemRequest()
                .withTableName("Comment")
                .withItem(item);
        final PutItemResult result = client.putItem(request);
        assertThat(result.getSdkHttpMetadata().getHttpStatusCode()).isEqualTo(200);
        runGetItem();
    }

    private void runDeleteItem() {
        final Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", new AttributeValue().withS("uuid"));

        final DeleteItemRequest request = new DeleteItemRequest()
                .withTableName("Comment")
                .withKey(key);
        final DeleteItemResult result = client.deleteItem(request);
        assertThat(result.getSdkHttpMetadata().getHttpStatusCode()).isEqualTo(200);

        final GetItemRequest getItemRequest = new GetItemRequest()
                .withTableName("Comment")
                .withKey(ImmutableMap.of("id", new AttributeValue().withS("uuid")));
        assertThat(client.getItem(getItemRequest).getItem()).isNull();
    }
}
