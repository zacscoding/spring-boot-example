package demo.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import demo.domain.Comment;

public interface CommentRepository extends PagingAndSortingRepository<Comment, String> {

    List<Comment> findAllByMentionIdOrderByCreatedAtAsc(Integer mentionId);
}
