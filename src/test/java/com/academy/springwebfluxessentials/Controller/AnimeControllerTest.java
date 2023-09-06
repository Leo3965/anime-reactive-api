package com.academy.springwebfluxessentials.Controller;

import com.academy.springwebfluxessentials.domain.Anime;
import com.academy.springwebfluxessentials.service.AnimeService;
import com.academy.springwebfluxessentials.util.AnimeCreator;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
class AnimeControllerTest {
    @InjectMocks
    private AnimeController controller;
    @Mock
    private AnimeService service;

    private final Anime anime = AnimeCreator.createValidAnime();

    @BeforeAll
    public static void blockHoundSetUp() {
        BlockHound.install();
    }

    @Test
    public void blockHoundWorks() {
        try {
            FutureTask<?> task = new FutureTask<>(() -> {
                Thread.sleep(0);
                return "";
            });
            Schedulers.parallel().schedule(task);

            task.get(10, TimeUnit.SECONDS);
            Assertions.fail("should fail");
        } catch (Exception e) {
            Assertions.assertTrue(e.getCause() instanceof BlockingOperationError);
        }
    }

    @BeforeEach
    void setUp() {
        BDDMockito.when(service.findAll())
                .thenReturn(Flux.just(anime));

        BDDMockito.when(service.findById(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(service.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(Mono.just(anime));

        BDDMockito.when(service.delete(ArgumentMatchers.anyInt()))
                .thenReturn(Mono.empty());

        BDDMockito.when(service.update(ArgumentMatchers.any(Anime.class)))
                .thenReturn(Mono.empty());
    }

    @Test
    @DisplayName("Find all returns a flux of anime")
    void findAll() {
        StepVerifier.create(controller.listAll())
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("Find by ID returns a mono with anime, when exists")
    void findById() {
        StepVerifier.create(controller.findById(1))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("save an anime when successful")
    void save() {
        var toBeSaved = AnimeCreator.createAnimeTonBeSaved();

        StepVerifier.create(controller.save(toBeSaved))
                .expectSubscription()
                .expectNext(anime)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete an anime when successful")
    void delete() {
        StepVerifier.create(controller.delete(1))
                .expectSubscription()
                .verifyComplete();
    }


    @Test
    @DisplayName("update an anime and returns empty mono when successful")
    void update() {
        StepVerifier.create(controller.update(1, AnimeCreator.createValidAnime()))
                .expectSubscription()
                .verifyComplete();
    }


}