package com.venkat.blog.service.implementations;

import com.venkat.blog.model.Tag;
import com.venkat.blog.repository.PostRepository;
import com.venkat.blog.repository.TagRepository;
import com.venkat.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {
    TagRepository tagRepository;
    PostRepository postRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void save(Tag tag) {
        tagRepository.save(tag);
    }

    @Override
    public Tag findByName(String tagName) {
        Tag tag = tagRepository.findByName(tagName);
        if (tag == null) {
            Tag newTag = new Tag();
            newTag.setName(tagName);
            return newTag;
        }

        return tag;
    }

}
