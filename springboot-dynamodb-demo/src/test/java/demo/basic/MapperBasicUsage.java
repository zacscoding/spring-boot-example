package demo.basic;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;

import demo.domain.Comment;

/**
 * Create table / Create item / Get item / Update item / Delete item / Drop table
 */
public class MapperBasicUsage extends AbstractDynamoBasic {

    String id;

    @Test
    @Disabled
    public void runTests() {
        runCreateTable();
        runCreateItem();
        runGetItem();
        runUpdateItem();
        runDeleteItem();
        runDropTable();
    }

    private void runCreateTable() {
        System.out.println("## Create table");
        final CreateTableRequest request = mapper.generateCreateTableRequest(Comment.class)
                                                 .withProvisionedThroughput(new ProvisionedThroughput(1L, 1L));

        request.getGlobalSecondaryIndexes()
               .forEach(idx -> idx.withProvisionedThroughput(new ProvisionedThroughput(1L, 1L))
                                  .withProjection(new Projection().withProjectionType("ALL")));
        assertThat(TableUtils.createTableIfNotExists(client, request)).isTrue();
    }

    private void runCreateItem() {
        System.out.println("## Create item");
        final Comment comment = Comment.builder()
                                       .name("comment name")
                                       .mentionId(1)
                                       .content("comment content")
                                       .build();
        assertThat(comment.getId()).isNull();
        mapper.save(comment);
        assertThat(comment.getId()).isNotNull();
        id = comment.getId();
    }

    private void runGetItem() {
        System.out.println("## Get item");
        final Comment comment = mapper.load(Comment.class, id);
        System.out.println(comment.toString());
        assertThat(comment.getId()).isEqualTo(id);
    }

    private void runUpdateItem() {
        System.out.println("## Update item");
        final String updatedContent = "modified comment content";
        final Comment comment = mapper.load(Comment.class, id);

        comment.setContent(updatedContent);
        mapper.save(comment);

        final Comment find = mapper.load(Comment.class, id);
        assertThat(find.getContent()).isEqualTo(updatedContent);
    }

    private void runDeleteItem() {
        mapper.delete(Comment.builder().id(id).build());

        final Comment find = mapper.load(Comment.class, id);
        assertThat(find).isNull();
    }

    private void runDropTable() {
        final boolean result = TableUtils.deleteTableIfExists(client, mapper.generateDeleteTableRequest(Comment.class));
        assertThat(result).isTrue();
    }
}
