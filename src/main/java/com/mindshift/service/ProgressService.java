package com.mindshift.service;

import com.mindshift.dto.response.ProgressResponse;
import com.mindshift.model.User;

public interface ProgressService {
    ProgressResponse get(User user);
    ProgressResponse completeDay(User user, int day);
}
