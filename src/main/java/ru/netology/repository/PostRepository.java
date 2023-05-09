package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {

    private AtomicLong counterID;
    private ConcurrentHashMap<Long, Post> posts;

    public PostRepository() {
        this.posts = new ConcurrentHashMap<>();
        this.counterID = new AtomicLong(0);
    }

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }


    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.getOrDefault(id, null));
    }

    public Post save(Post post) {
        if (posts.containsKey(post.getId())) {
            posts.replace(post.getId(), post);
        } else {
            post.setId(counterID.getAndIncrement());
            posts.put(post.getId(), post);
        }
        return post;
    }

    public void removeById(long id) {
        posts.remove(id);
    }
}
