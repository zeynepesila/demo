package com.blog.demo.model;

import com.blog.demo.model.Post;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Comment {

    @Id
    @GeneratedValue
    private UUID commentId;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

    @Column(columnDefinition = "TEXT")
    private String comment;


    public UUID getCommentId() {
        return commentId;
    }

    public void setCommentId(UUID commentId) {
        this.commentId = commentId;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}



