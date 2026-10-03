package com.cinema.user.service;

import com.cinema.user.dto.shared_movies.ShareMovieRequest;
import com.cinema.user.dto.shared_movies.ShareMovieResponse;
import com.cinema.user.dto.shared_movies.ReceivedMovieShareResponse;
import com.cinema.user.exceptions.ResourceNotFoundException;
import com.cinema.user.models.MovieShare;
import com.cinema.user.models.User;
import com.cinema.user.repository.MovieShareRepository;
import com.cinema.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieShareService {

    private final MovieShareRepository movieShareRepository;
    private final UserRepository userRepository;

    @Transactional
    public ShareMovieResponse shareMovie(Long senderUserId, ShareMovieRequest request) {
        if (!userRepository.existsById(senderUserId)) {
            throw new ResourceNotFoundException("Authenticated user was not found.");
        }
        
        String friendEmail = request.friendEmail().trim();
        User recipient = userRepository.findByEmailIgnoreCase(friendEmail)
            .orElseThrow(() -> new ResourceNotFoundException("No user exists with that email address."));
        
        if (movieShareRepository.existsBySenderUserIdAndRecipientUserIdAndMovieId(senderUserId, recipient.getId(), request.movieId())) {
            throw new IllegalArgumentException("You have already shared this movie with this friend.");
        }
        MovieShare share = new MovieShare(
                request.movieId(),
                senderUserId,
            recipient.getId(),
            recipient.getEmail());
        MovieShare savedShare = movieShareRepository.save(share);

        return new ShareMovieResponse("Movie shared successfully", savedShare.getId());
    }

        @Transactional
        public List<ReceivedMovieShareResponse> getReceivedShares(Long recipientUserId, String recipientEmail) {
            return movieShareRepository.findReceivedShares(recipientUserId, recipientEmail)
            .stream()
                    .map(share -> {
                        if (share.getRecipientUserId() == null) {
                            share.setRecipientUserId(recipientUserId);
                        }
                        return new ReceivedMovieShareResponse(
                                share.getId(),
                                share.getMovieId(),
                                share.getSenderUserId(),
                                share.getFriendEmail(),
                                share.getCreatedAt());
                    })
            .toList();
        }
}