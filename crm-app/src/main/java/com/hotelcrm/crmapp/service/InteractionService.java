package com.hotelcrm.crmapp.service;

import com.hotelcrm.crmapp.dto.interaction.request.InteractionCompleteRequest;
import com.hotelcrm.crmapp.dto.interaction.request.InteractionCreateRequest;
import com.hotelcrm.crmapp.dto.interaction.request.InteractionFilterRequest;
import com.hotelcrm.crmapp.dto.interaction.request.InteractionUpdateRequest;
import com.hotelcrm.crmapp.dto.interaction.response.InteractionResponse;
import com.hotelcrm.crmapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InteractionService {

    InteractionResponse schedule(InteractionCreateRequest req, User currentUser);

    InteractionResponse complete(Long id, InteractionCompleteRequest req, User currentUser);

    InteractionResponse update(Long id, InteractionUpdateRequest req, User currentUser);

    Page<InteractionResponse> getFiltered(InteractionFilterRequest filter, Pageable pageable, Long userId);

    Page<InteractionResponse> calendar(Long userId, LocalDateTime from, LocalDateTime to, Pageable pageable);

    List<InteractionResponse> upcomingFollowUps(Long userId);

    Optional<InteractionResponse> getById(Long id, User currentUser);

    Long delete(Long id, User currentUser);
}
