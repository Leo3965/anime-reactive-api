package com.academy.springwebfluxessentials.service;

import com.academy.springwebfluxessentials.domain.Anime;
import com.academy.springwebfluxessentials.repository.AnimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AnimeService {
    private final AnimeRepository repository;

    public Flux<Anime> findAll() {
        return repository.findAll();
    }

    public Mono<Anime> findById(int id) {
        return repository.findById(id)
                .switchIfEmpty(monoResponseStatusNotFoundException());
    }

    public <T> Mono<T> monoResponseStatusNotFoundException() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));
    }

    public Mono<Anime> save(Anime anime) {
        return repository.save(anime);
    }

    public Mono<Void> update(Anime anime) {
        return findById(anime.getId())
                .map(animeFound -> anime.withId(animeFound.getId()))
                .flatMap(repository::save)
                .then();
    }

    public Mono<Void> delete(int id) {
        return repository.findById(id)
                .flatMap(repository::delete)
                .then();
    }
}
