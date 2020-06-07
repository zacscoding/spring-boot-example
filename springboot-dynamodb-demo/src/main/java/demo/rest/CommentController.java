package demo.rest;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.domain.Comment;
import demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    @GetMapping("/comments")
    public ResponseEntity<Iterable<Comment>> getComments(@RequestParam(name = "mentionId") Integer mentionId) {
        return ResponseEntity.ok(commentRepository.findAllByMentionIdOrderByCreatedAtAsc(mentionId));
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable("id") String id) {
        final Optional<Comment> commentOptional = commentRepository.findById(id);
        return commentOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setId(null);
        comment = commentRepository.save(comment);
        return ResponseEntity.ok(comment);
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable("id") String id,
                                                 @RequestBody Comment update) {
        final Optional<Comment> commentOptional = commentRepository.findById(id);

        if (!commentOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        final Comment saved = commentOptional.get();
        saved.setContent(update.getContent());

        return ResponseEntity.ok(commentRepository.save(saved));
    }
}
