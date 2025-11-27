package com.clienttrack.api.controller;

import com.clienttrack.api.controller.dto.ActivityData;
import com.clienttrack.api.controller.dto.ActivityDto;
import com.clienttrack.api.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping
    public List<ActivityDto> getAllActivities() {
        return activityService.getAll()
                .stream()
                .map(ActivityDto::from)
                .toList();
    }

    @PostMapping
    public ActivityDto createActivity(@RequestBody ActivityData data) {
        return ActivityDto.from(activityService.create(data));
    }

    @PostMapping("/{id}/complete")
    public void setActivityComplete(@PathVariable("id")  java.util.UUID id) {
        activityService.setComplete(id);
    }
}
