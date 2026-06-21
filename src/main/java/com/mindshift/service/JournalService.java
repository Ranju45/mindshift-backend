package com.mindshift.service;

import com.mindshift.dto.request.JournalRequest;
import com.mindshift.dto.response.JournalResponse;
import com.mindshift.model.User;

import java.util.List;

public interface JournalService {
    JournalResponse create(User user, JournalRequest request);
    List<JournalResponse> getAll(User user);
    void delete(User user, Long entryId);
}
