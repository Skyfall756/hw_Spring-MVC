package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class PostRepository {

    private AtomicLong counterID;
    private ConcurrentHashMap<Long, Post> posts;

    public PostRepository() {
        this.posts = new ConcurrentHashMap<>();
        this.counterID = new AtomicLong(0);
    }

    public List<Post> all() {
        return new ArrayList<>((posts.values().stream()
                .filter(post -> !post.isRemoved()))
                .collect(Collectors.toList()));
    }


    public Optional<Post> getById(long id) {

        if (posts.containsKey(id) && !posts.get(id).isRemoved()){
            return Optional.of(posts.get(id));
        } else return Optional.empty();
    }

    public Post save(Post post) {

        long postId = post.getId();
        if (posts.containsKey(postId)) {
            if (!posts.get(postId).isRemoved()) {
                posts.replace(postId, post);
            } else {
                throw new NotFoundException();
            }
        } else {
            post.setId(counterID.getAndIncrement());
            posts.put(post.getId(), post);
        }
    return post;
    }

    public void removeById(long id) {

        posts.get(id).setRemoved(true);
    }
}
