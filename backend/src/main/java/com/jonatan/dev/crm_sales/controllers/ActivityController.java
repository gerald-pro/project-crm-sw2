package com.jonatan.dev.crm_sales.controllers;

import com.jonatan.dev.crm_sales.domains.Activity;
import com.jonatan.dev.crm_sales.domains.input.ActivityInput;
import com.jonatan.dev.crm_sales.domains.input.UpdateActivityInput;
import com.jonatan.dev.crm_sales.services.ActivityService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class ActivityController implements GraphQLMutationResolver {
    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Activity> getAllActivities() {
        return activityService.findAll();
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Optional<Activity> getActivity(@Argument String id) {
        return activityService.findById(id);
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Activity> getActivitiesByLead(@Argument String id) {
        return activityService.findByLead(id);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Activity createActivity(@Argument ActivityInput input) {
        return activityService.create(input);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public Activity updateActivity(@Argument UpdateActivityInput input) {
        return activityService.update(input);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteActivity(@Argument String id) {
        if (activityService.findById(id).isEmpty()) {
            throw new NoSuchElementException("No se encontró el activity con id: " + id);
        } else {
            activityService.delete(id);
        }
    }
}
