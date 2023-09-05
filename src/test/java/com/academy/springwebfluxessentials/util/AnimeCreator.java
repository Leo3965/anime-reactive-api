package com.academy.springwebfluxessentials.util;

import com.academy.springwebfluxessentials.domain.Anime;

public class AnimeCreator {
    public static Anime createAnimeTonBeSaved() {
        return Anime.builder()
                .name("Naruto  Uzumaki")
                .build();
    }

    public static Anime createValidAnime() {
        return Anime.builder()
                .id(1)
                .name("Naruto  Uzumaki")
                .build();
    }

    public static Anime createValidUpdatedAnime() {
        return Anime.builder()
                .id(1)
                .name("Naruto  Uzumaki 2")
                .build();
    }

}
