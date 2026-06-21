package com.mindshift.service;

import com.mindshift.dto.request.TrackerRequest;
import com.mindshift.dto.response.TrackerResponse;
import com.mindshift.model.User;

import java.util.Map;

public interface TrackerService {
    TrackerResponse save(User user, TrackerRequest request);
    Map<String, TrackerResponse> getAll(User user);
}
