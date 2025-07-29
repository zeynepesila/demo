package com.blog.demo.Service;

import com.blog.demo.model.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAllPosts();
    Post getPostById(UUID id);
    Post createPost(Post post);
    void deletePost(UUID id);
}


