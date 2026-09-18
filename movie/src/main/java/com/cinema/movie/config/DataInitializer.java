package com.cinema.movie.config;

import com.cinema.movie.entity.MovieEntity;
import com.cinema.movie.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final MovieRepository movieRepository;

    public DataInitializer(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public void run(String... args) {
        if (movieRepository.count() > 0) {
            log.info("Database already contains movie data. Skipping initialization.");
            return;
        }

        List<String> titles = List.of(
          
            "Interstellar", "Inception", "The Matrix", 
            "Blade Runner 2049", "Dune: Part Two", "WALL-E",
            
            // Action & Superhero
            "The Dark Knight", "The Dark Knight Rises", "Avengers: Infinity War", "Avengers: Endgame", 
            "Spider-Man: Into the Spider-Verse", "Gladiator II", "Top Gun: Maverick", 
            "Mad Max: Fury Road", "Die Hard",
            
            // Anime & Animation Masterpieces
            "Spirited Away", "Princess Mononoke", "Your Name.", "Grave of the Fireflies", 
            "Toy Story", "Coco", "The Lion King",

            // Expanded Anime Mappings
            "Dragon Ball Super: Broly", "Hunter x Hunter: Phantom Rouge", "Inazuma Eleven: The Movie", 
            "Demon Slayer: Mugen Train", "Jujutsu Kaisen 0", "Naruto Shippuden the Movie: Road to Ninja", 
            "One Piece Film: Red", "Attack on Titan: Chronicle", "Bleach: Memories of Nobody", 
            "My Hero Academia: Two Heroes", "Fullmetal Alchemist: Brotherhood", "Steins;Gate", 
            "Cowboy Bebop: The Movie", "Akira", "Paprika", "Perfect Blue", "A Silent Voice", 
            "Weathering with You", "Tokyo Godfathers", "Howl's Moving Castle", "My Neighbor Totoro",

            // Expanded Korean & Japanese Live-Action Mappings
            "Train to Busan", "The Handmaiden", "Memories of Murder", "I Saw the Devil", 
            "The Wailing", "A Taxi Driver", "Minari", "Decision to Leave", "Broker", 
            "Audition", "Seven Samurai", "Rashomon", "Tokyo Sonata", "Shoplifters", "Drive My Car",

            // Expanded American Classics & Blockbusters
            "The Godfather Part II", "Goodfellas: Director's Cut", "Apocalypse Now Redux", 
            "The Lord of the Rings: The Fellowship of the Ring", "The Lord of the Rings: The Two Towers", 
            "The Lord of the Rings: The Return of the King", "Titanic", "Avatar",

            // Drama, Crime & Thrillers
            "The Shawshank Redemption", "The Godfather", "Pulp Fiction", "Fight Club", 
            "Forrest Gump", "Joker", "Oppenheimer", "Parasite", "The Truman Show", "Shutter Island"
        );

        List<List<String>> genrePool = List.of(
            List.of("Drama", "Crime"), List.of("Action", "Thriller"), List.of("Sci-Fi", "Adventure"),
            List.of("Comedy", "Romance"), List.of("Animation", "Family"), List.of("Horror", "Mystery")
        );

        List<String> directors = List.of(
            "Christopher Nolan", "Quentin Tarantino", "Martin Scorsese", "Steven Spielberg", 
            "David Fincher", "Ridley Scott", "Denis Villeneuve", "Francis Ford Coppola", "Hayao Miyazaki", "Bong Joon-ho"
        );

        Random random = new Random();
        List<MovieEntity> movies = new ArrayList<>();

        for (int i = 0; i < titles.size(); i++) {
            String movieId = "MOV-" + String.format("%03d", i + 1);
            String title = titles.get(i);
            List<String> genres = genrePool.get(random.nextInt(genrePool.size()));
            int releaseYear = 1970 + random.nextInt(55);
            double rating = Math.round((3.5 + random.nextDouble() * 1.5) * 10.0) / 10.0;
            
            String posterUrl = getSpecificPoster(title);
            String bannerUrl = getSpecificBanner(title);
            String director = directors.get(random.nextInt(directors.size()));
            String tagline = "Experience the unforgettable journey of " + title + ".";
            String duration = (95 + random.nextInt(50)) + "m";
            String description = title + " is an acclaimed cinematic masterpiece exploring deep thematic elements across " + genres.get(0) + " and " + genres.get(1) + ". Released in " + releaseYear + ", it has captivated audiences worldwide.";

            movies.add(MovieEntity.builder()
                    .movieId(movieId)
                    .title(title)
                    .tagline(tagline)
                    .description(description)
                    .genres(genres)
                    .releaseYear(releaseYear)
                    .averageRating(rating)
                    .posterUrl(posterUrl)
                    .bannerUrl(bannerUrl)
                    .duration(duration)
                    .director(director)
                    .build());
        }

        movieRepository.saveAll(movies);
        log.info("Successfully initialized PostgreSQL database with {} movies having specific assets.", movies.size());
    }

    private String getSpecificPoster(String title) {
        return switch (title) {
            // Sci-Fi & Space
            case "Interstellar", "Interstellar IMAX Edition" -> "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg";
            case "Inception" -> "https://image.tmdb.org/t/p/w500/edv5CZvWj09upOsy2Y6IwDhK8bt.jpg";
            case "The Matrix" -> "https://image.tmdb.org/t/p/w500/f89U3ADr1oiB1s9GkdPOEpXUk5H.jpg";
            case "Blade Runner 2049", "Blade Runner" -> "https://image.tmdb.org/t/p/w1280/gajva2L0rPYkEWjzgFlBXCAVBE5.jpg";
            case "Dune: Part Two", "Dune: Part One" -> "https://image.tmdb.org/t/p/w1280/6izwz7rsy95ARzTR3poZ8H6c5pp.jpg";
            case "WALL-E" -> "https://image.tmdb.org/t/p/w1280/hbhFnRzzg6ZDmm8YAmxBnQpQIPh.jpg";
            
            // Action & Superhero
            case "The Dark Knight" -> "https://image.tmdb.org/t/p/w500/qJ2tW6WMUDux911r6m7haRef0WH.jpg";
            case "The Dark Knight Rises" -> "https://image.tmdb.org/t/p/w500/hr0L2aueqlP2BYUblTTjmtn0hw4.jpg";
            case "Avengers: Infinity War" -> "https://image.tmdb.org/t/p/w500/7WsyChQLEftFiDOVTGkv3hFpyyt.jpg";
            case "Avengers: Endgame" -> "https://image.tmdb.org/t/p/w500/or06FN3Dka5tukK1e9sl16pB3iy.jpg";
            case "Spider-Man: Into the Spider-Verse" -> "https://image.tmdb.org/t/p/w500/iiZZdoQBEYBv6id8su7ImL0oCbD.jpg";
            case "Gladiator", "Gladiator II" -> "https://image.tmdb.org/t/p/w500/ty8TGRuvJLPUmAR1H1nRIsgwvim.jpg";
            case "Top Gun: Maverick" -> "https://image.tmdb.org/t/p/w500/62HCnUTziyWcpDaBO2i1DX17ljH.jpg";
            case "Mad Max: Fury Road" -> "https://image.tmdb.org/t/p/w500/hA2ple9q4qnwxp3hKVNhroipsir.jpg";
            case "Die Hard" -> "https://image.tmdb.org/t/p/w500/yFihWxQcmqcaBR31QM6Y8gT6aYV.jpg";
            
            // Anime & Animation Masterpieces
            case "Spirited Away" -> "https://image.tmdb.org/t/p/w500/39wmItIWsg5sZMyRUHLkWBcuVCM.jpg";
            case "Princess Mononoke" -> "https://image.tmdb.org/t/p/w1280/cMYCDADoLKLbB83g4WnJegaZimC.jpg";
            case "Your Name." -> "https://image.tmdb.org/t/p/w500/q719jXXEzOoYaps6babgKnONONX.jpg";
            case "Grave of the Fireflies" -> "https://image.tmdb.org/t/p/w1280/j7C9HpfZiIHFupkI55nuSK8qP8x.jpg";
            case "Toy Story" -> "https://image.tmdb.org/t/p/w500/uXDfjJbdP4ijW5hWSBrPrlKpxab.jpg";
            case "Coco" -> "https://image.tmdb.org/t/p/w1280/6Ryitt95xrO8KXuqRGm1fUuNwqF.jpg";
            case "The Lion King" -> "https://image.tmdb.org/t/p/w500/sKCr78MXSLixwmZ8DyJLrpMsd15.jpg";

            // Expanded Anime Mappings
            case "Dragon Ball Super: Broly" -> "https://image.tmdb.org/t/p/w1280/uMEgkyiPznZP5AiMSWAk2jsj5gC.jpg";
            case "Hunter x Hunter: Phantom Rouge" -> "https://image.tmdb.org/t/p/w1280/tBbr1LJn5rrP08Y16jJAKwLEECf.jpg";
            case "Inazuma Eleven: The Movie" -> "https://image.tmdb.org/t/p/w1280/vdhnDISpEAvZA5RvNdONkFM6L0j.jpg";
            case "Demon Slayer: Mugen Train" -> "https://image.tmdb.org/t/p/w500/h8Rb9gBr48ODIwYUttZNYeMWeUU.jpg";
            case "Jujutsu Kaisen 0" -> "https://image.tmdb.org/t/p/w1280/23oJaeBh0FDk2mQ2P240PU9Xxfh.jpg";
            case "Naruto Shippuden the Movie: Road to Ninja" -> "https://image.tmdb.org/t/p/w1280/xLal6fXNtiJN6Zw6qk21xAtdOeN.jpg";
            case "One Piece Film: Red" -> "https://image.tmdb.org/t/p/w1280/8ibfhe4P7rhmn3lrPhOZzIJHA2B.jpg";
            case "Attack on Titan: Chronicle" -> "https://image.tmdb.org/t/p/w1280/kXUSsxQ2J3QVGkG1thmhI1FadKd.jpg";
            case "Bleach: Memories of Nobody" -> "https://image.tmdb.org/t/p/w1280/1QXGWJ0bsmbMUDBXXfS1RnFadkS.jpg";
            case "My Hero Academia: Two Heroes" -> "https://image.tmdb.org/t/p/w1280/hC4nTxdhXqFWzgqynGvvXVMiMNp.jpg";
            case "Fullmetal Alchemist: Brotherhood" -> "https://image.tmdb.org/t/p/w1280/5ZFUEOULaVml7pQuXxhpR2SmVUw.jpg";
            case "Steins;Gate" -> "https://image.tmdb.org/t/p/w1280/96R4bV7dB8ramaWceNKsxvJgCUd.jpg";
            case "Cowboy Bebop: The Movie" -> "https://image.tmdb.org/t/p/w1280/34H5bsNc0EPILVr49TfOYXj50qV.jpg";
            case "Akira" -> "https://image.tmdb.org/t/p/w1280/neZ0ykEsPqxamsX6o5QNUFILQrz.jpg";
            case "Paprika" -> "https://image.tmdb.org/t/p/w1280/nHJljo2Pi7XimYEgV9hvRchQWmg.jpg";
            case "Perfect Blue" -> "https://image.tmdb.org/t/p/w1280/6WTiOCfDPP8XV4jqfloiVWf7KHq.jpg";
            case "A Silent Voice" -> "https://image.tmdb.org/t/p/w1280/tuFaWiqX0TXoWu7DGNcmX3UW7sT.jpg";
            case "Weathering with You" -> "https://image.tmdb.org/t/p/w1280/qgrk7r1fV4IjuoeiGS5HOhXNdLJ.jpg";
            case "Tokyo Godfathers" -> "https://image.tmdb.org/t/p/w1280/sPC66btzQlzuRdPKiSDYZ5Hvxgc.jpg";
            case "Howl's Moving Castle" -> "https://image.tmdb.org/t/p/w1280/13kOl2v0nD2OLbVSHnHk8GUFEhO.jpg";
            case "My Neighbor Totoro" -> "https://image.tmdb.org/t/p/w1280/rtGDOeG9LzoerkDGZF9dnVeLppL.jpg";

            // Expanded Korean & Japanese Live-Action Mappings
            case "Train to Busan" -> "https://image.tmdb.org/t/p/w1280/vNVFt6dtcqnI7hqa6LFBUibuFiw.jpg";
            case "The Handmaiden" -> "https://image.tmdb.org/t/p/w1280/dLlH4aNHdnmf62umnInL8xPlPzw.jpg";
            case "Memories of Murder" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/jcgUjx1QcupGzjntTVlnQ15lHqy.jpg";
            case "I Saw the Devil" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/zp5NrmYp80axIGiEiYPmm1CW6uH.jpg";
            case "The Wailing" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/lWE9ih9qgjx8HatYboP7fG0nri.jpg";
            case "A Taxi Driver" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/iXVaWbxmyPk4KZGZk5GGDGFieMX.jpg";
            case "Minari" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/6mPNdmjdbVKPITv3LLCmQoKs9Zw.jpg";
            case "Decision to Leave" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/zI8KZ4EdLUymWKX1YEkpZ0gtPUa.jpg";
            case "Broker" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/x86xaUnxU31JYiwlO35corDEV1i.jpg";
            case "Audition" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/zwGaUMm0wAqi0wkO7LJDlwoA5LP.jpg";
            case "Seven Samurai" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/lOMGc8bnSwQhS4XyE1S99uH8NXf.jpg";
            case "Rashomon" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/ijWibsAU1iBcCD8tuIZfTmDzMVE.jpg";
            case "Tokyo Sonata" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/6Y0ZRyh5eH7WLY4fcF92swhtgUz.jpg";
            case "Shoplifters" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/4nfRUOv3LX5zLn98WS1WqVBk9E9.jpg";
            case "Drive My Car" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/znXps7wPyYq8UDCfeyO2vfEIeRS.jpg";

            // Expanded American Classics Mappings
            case "The Godfather Part II" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/sSuQTCZwqKrNBNIsksO9IAUoWP9.jpg";
            case "Goodfellas: Director's Cut" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/9OkCLM73MIU2CrKZbqiT8Ln1wY2.jpg";
            case "Apocalypse Now Redux" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/gQB8Y5RCMkv2zwzFHbUJX3kAhvA.jpg";
            case "The Lord of the Rings: The Fellowship of the Ring" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/6oom5QYQ2yQTMJIbnvbkBL9cHo6.jpg";
            case "The Lord of the Rings: The Two Towers" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/5VTN0pR8gcqV3EPUHHfMGnJYN9L.jpg";
            case "The Lord of the Rings: The Return of the King" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/rCzpDGLbOoPwLjy3OAm5NUPOTrC.jpg";
            case "Titanic" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/9xjZS2rlVxm8SFx8kPC3aIGCOYQ.jpg";
            case "Avatar" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/gKY6q7SjCkAU6FqvqWybDYgUKIF.jpg";

            // Drama, Crime & Thrillers
            case "The Shawshank Redemption" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/9cqNxx0GxF0bflZmeSMuL5tnGzr.jpg";
            case "The Godfather" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/3bhkrj58Vtu7enYsRolD1fZdja1.jpg";
            case "Pulp Fiction" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/d5iIlFn5s0ImszYzBPb8JPIfbXD.jpg";
            case "Fight Club" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg";
            case "Forrest Gump" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/arw2vcBveWOVZr6pxd9XTd1TdQa.jpg";
            case "Joker" -> "https://media.themoviedb.org/t/p/w600_and_h900_face/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg";
            case "Oppenheimer" -> "https://image.tmdb.org/t/p/w500/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg";
            case "Parasite" -> "https://image.tmdb.org/t/p/w500/7IiTTgloJzvGI1TAYymCfbfl3vT.jpg";
            case "The Truman Show" -> "https://image.tmdb.org/t/p/w1280/vuza0WqY239yBXOadKlGwJsZJFE.jpg";
            case "Shutter Island" -> "https://image.tmdb.org/t/p/w1280/nrmXQ0zcZUL8jFLrakWc90IR8z9.jpg";
            
            default -> null; // Handled to ensure strict mapping
        };
    }

    private String getSpecificBanner(String title) {
        return switch (title) {
            // Sci-Fi & Space
            case "Interstellar", "Interstellar IMAX Edition" -> "https://image.tmdb.org/t/p/w1280/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg";
            case "Inception" -> "https://image.tmdb.org/t/p/w1280/s3TBrRGB1iav7gFOCNx3H31MoES.jpg";
            case "The Matrix" -> "https://image.tmdb.org/t/p/w1280/ncEsesgOJDNrTUED89hYbA117wo.jpg";
            case "Blade Runner 2049", "Blade Runner" -> "https://media.themoviedb.org/t/p/w1920_and_h800_multi_faces/gNdLJU9TxrpGx4dkZidjys3fyy0.jpg";
            case "Dune: Part Two", "Dune: Part One" -> "https://image.tmdb.org/t/p/w1280/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg";
            case "WALL-E" -> "https://image.tmdb.org/t/p/w1280/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg";
            
            // Action & Superhero
            case "The Dark Knight" -> "https://image.tmdb.org/t/p/w1280/hkBaDkMWbLaf8B1lsWsKX7Ew3Xq.jpg";
            case "The Dark Knight Rises" -> "https://image.tmdb.org/t/p/w1280/cOWfmriWv88OirVECL7Udb9g26X.jpg";
            case "Avengers: Infinity War" -> "https://image.tmdb.org/t/p/w1280/bOGkgRGdhrBYJSLpXaxhXVstddV.jpg";
            case "Avengers: Endgame" -> "https://image.tmdb.org/t/p/w1280/7RyHsO4yDXtBv1zUU3mTpHeQ0dZ.jpg";
            case "Spider-Man: Into the Spider-Verse" -> "https://image.tmdb.org/t/p/w1280/wqnLdwVXoBjKibFRR5U3y0aDUhs.jpg";
            case "Gladiator", "Gladiator II" -> "https://image.tmdb.org/t/p/w1280/feU1DWV5zMvXUHYSUN4T3pCznoF.jpg";
            case "Top Gun: Maverick" -> "https://image.tmdb.org/t/p/w1280/AaV1YIdWKnjAIAOe8UUKBFm327v.jpg";
            case "Mad Max: Fury Road" -> "https://image.tmdb.org/t/p/w1280/nlCHUWjY9XWbuEUQauCBgnY8ymF.jpg";
            case "Die Hard" -> "https://image.tmdb.org/t/p/w1280/eadXb90nS8xV13o1F0kYQnS0o3f.jpg";
            
            // Anime & Animation Masterpieces
            case "Spirited Away" -> "https://image.tmdb.org/t/p/w1280/AbMqlN2C1G1pMhC6tF1564YvJ3T.jpg";
            case "Princess Mononoke" -> "https://image.tmdb.org/t/p/w1280/mZiaGEIwknIIrA1O5U9yHwL8G6Q.jpg";
            case "Your Name." -> "https://image.tmdb.org/t/p/w1280/nvwcwc1qUeB/77114vW5Q9Q1bQo.jpg";
            case "Grave of the Fireflies" -> "https://image.tmdb.org/t/p/w1280/f7h0t2w7GvQ0tJgDk25o8n3rUqZ.jpg";
            case "Toy Story" -> "https://image.tmdb.org/t/p/w1280/9K9s1ZueyPNWn6lXg1jNqC4d2lK.jpg";
            case "Coco" -> "https://image.tmdb.org/t/p/w1280/askg3SMGQqgaA0G7w56G3l6Z77z.jpg";
            case "The Lion King" -> "https://image.tmdb.org/t/p/w1280/wXuE26n4cW7aH6a55pQ5c5Yv2hN.jpg";

            // Expanded Anime Banners
            case "Dragon Ball Super: Broly" -> "https://image.tmdb.org/t/p/w1280/suaEOtk1N1sgg2MTM7oZd2cfVp3.jpg";
            case "Hunter x Hunter: Phantom Rouge" -> "https://image.tmdb.org/t/p/w1280/n6bUvigpRFqSwmPp1m2YADdbRBc.jpg";
            case "Inazuma Eleven: The Movie" -> "https://image.tmdb.org/t/p/w1280/fm6KqXpk3M2HVveHwCrBSSBaO0V.jpg";
            case "Demon Slayer: Mugen Train" -> "https://image.tmdb.org/t/p/w1280/hiKmpZMGZsrkA3cdce8a7Dpos1j.jpg";
            case "Jujutsu Kaisen 0" -> "https://image.tmdb.org/t/p/w1280/xOMo8BRK7PfcJv9JCnx7s5hj0PX.jpg";
            case "Naruto Shippuden the Movie: Road to Ninja" -> "https://image.tmdb.org/t/p/w1280/rAiYTfKGqDCRIIqo664sY9XZIvQ.jpg";
            case "One Piece Film: Red" -> "https://image.tmdb.org/t/p/w1280/s3TBrRGB1iav7gFOCNx3H31MoES.jpg";
            case "Attack on Titan: Chronicle" -> "https://image.tmdb.org/t/p/w1280/ncEsesgOJDNrTUED89hYbA117wo.jpg";
            case "Fullmetal Alchemist: Brotherhood" -> "https://image.tmdb.org/t/p/w1280/feU1DWV5zMvXUHYSUN4T3pCznoF.jpg";
            case "Howl's Moving Castle" -> "https://image.tmdb.org/t/p/w1280/mZiaGEIwknIIrA1O5U9yHwL8G6Q.jpg";
            case "My Neighbor Totoro" -> "https://image.tmdb.org/t/p/w1280/AbMqlN2C1G1pMhC6tF1564YvJ3T.jpg";

            // Expanded Korean & Japanese Banners
            case "Train to Busan" -> "https://image.tmdb.org/t/p/w1280/tmU7GeKVybMWFButWEGl2M4GeiP.jpg";
            case "The Handmaiden" -> "https://image.tmdb.org/t/p/w1280/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg";
            case "Memories of Murder" -> "https://image.tmdb.org/t/p/w1280/hZkgoQYus5vegHoetLkCJzb17zJ.jpg";
            case "I Saw the Devil" -> "https://image.tmdb.org/t/p/w1280/qdIMHd4sEfJSckdVJfKQvisL02m.jpg";
            case "Minari" -> "https://image.tmdb.org/t/p/w1280/askg3SMGQqgaA0G7w56G3l6Z77z.jpg";
            case "Decision to Leave" -> "https://image.tmdb.org/t/p/w1280/9K9s1ZueyPNWn6lXg1jNqC4d2lK.jpg";
            case "Seven Samurai" -> "https://image.tmdb.org/t/p/w1280/cOWfmriWv88OirVECL7Udb9g26X.jpg";
            case "Rashomon" -> "https://image.tmdb.org/t/p/w1280/hkBaDkMWbLaf8B1lsWsKX7Ew3Xq.jpg";
            case "Drive My Car" -> "https://image.tmdb.org/t/p/w1280/bOGkgRGdhrBYJSLpXaxhXVstddV.jpg";

            // Expanded American Classics Banners
            case "The Godfather Part II" -> "https://image.tmdb.org/t/p/w1280/poo9qUNauS2bUfS4g8q618h9xM1.jpg";
            case "The Lord of the Rings: The Fellowship of the Ring" -> "https://image.tmdb.org/t/p/w1280/vRQnzOn4HjIMX4LBq9nHnFXbsSu.jpg";
            case "The Lord of the Rings: The Two Towers" -> "https://image.tmdb.org/t/p/w1280/kjQBypv0g8k8R8m86w3nB2rAiYT.jpg";
            case "The Lord of the Rings: The Return of the King" -> "https://image.tmdb.org/t/p/w1280/cckcYUVkswWl9fN05LqBvH2a0.jpg";
            case "Titanic" -> "https://image.tmdb.org/t/p/w1280/yDI6D5DJu675APSRmCaHkK0O7z9.jpg";
            case "Avatar" -> "https://image.tmdb.org/t/p/w1280/ogEN5jGuJ815l1mO0c6z9s7Z7yY.jpg";

            // Drama, Crime & Thrillers
            case "The Shawshank Redemption" -> "https://image.tmdb.org/t/p/w1280/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg";
            case "The Godfather" -> "https://image.tmdb.org/t/p/w1280/tmU7GeKVybMWFButWEGl2M4GeiP.jpg";
            case "Pulp Fiction" -> "https://image.tmdb.org/t/p/w1280/suaEOtk1N1sgg2MTM7oZd2cfVp3.jpg";
            case "Fight Club" -> "https://image.tmdb.org/t/p/w1280/hZkgoQYus5vegHoetLkCJzb17zJ.jpg";
            case "Forrest Gump" -> "https://image.tmdb.org/t/p/w1280/qdIMHd4sEfJSckdVJfKQvisL02m.jpg";
            case "Joker" -> "https://image.tmdb.org/t/p/w1280/n6bUvigpRFqSwmPp1m2YADdbRBc.jpg";
            case "Oppenheimer" -> "https://image.tmdb.org/t/p/w1280/fm6KqXpk3M2HVveHwCrBSSBaO0V.jpg";
            case "Parasite" -> "https://image.tmdb.org/t/p/w1280/hiKmpZMGZsrkA3cdce8a7Dpos1j.jpg";
            case "The Truman Show" -> "https://media.themoviedb.org/t/p/w1920_and_h800_multi_faces/rmiG2uwcNoGFmBKMoa1pIcf514L.jpg";
            case "Shutter Island" -> "https://media.themoviedb.org/t/p/w1920_and_h800_multi_faces/rbZvGN1A1QyZuoKzhCw8QPmf2q0.jpg";
            
            default -> "https://images.unsplash.com/photo-1574375927938-d5a98e8ffe85?auto=format&fit=crop&w=1200&q=80";
        };
    }
}