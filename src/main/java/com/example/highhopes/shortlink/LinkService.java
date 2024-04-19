package com.example.highhopes.shortlink;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkService {

    private final LinkRepository linkRepository;

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }


    public List<Link> listAll() {
        return linkRepository.findAll();
    }


    public Link add(Link link) {

        return linkRepository.save(link);
    }


    public Boolean deleteById(long id) {
        linkRepository.deleteById(id);
        return true;
    }


    public Link update(Link link) {
        if (linkRepository.existsById(link.getId())) {
            linkRepository.save(link);
            return link;
        } else {
            return null;
        }
    }

    public Link getById(long id) {
        return linkRepository.findById(id).orElse(null);
    }
}