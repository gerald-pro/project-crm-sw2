package com.jonatan.dev.crm_sales.services;

import com.jonatan.dev.crm_sales.domains.Activity;
import com.jonatan.dev.crm_sales.domains.input.ActivityInput;
import com.jonatan.dev.crm_sales.domains.input.UpdateActivityInput;
import com.jonatan.dev.crm_sales.repositories.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;

    @Autowired
    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    public Page<Activity> findAll(Pageable pageable) {
        return activityRepository.findAll(pageable);
    }

    public List<Activity> findByLead(String leadId) {
        return activityRepository.findByLeadId(leadId);
    }

    public Optional<Activity> findById(String id) {
        return activityRepository.findById(id);
    }

    public Activity create(ActivityInput input) {
        Activity newActivity = new Activity(input);
        return activityRepository.save(newActivity);
    }

    public Activity update(UpdateActivityInput updatedActivity) {
        Activity activity = findById(updatedActivity.getId()).orElseThrow(() -> new NoSuchElementException("Activity con id " + updatedActivity.getId() + " no encontrado."));
        if (updatedActivity.getTitle() != null) {
            activity.setTitle(updatedActivity.getTitle());
        }
        if (updatedActivity.getDescription() != null) {
            activity.setDescription(updatedActivity.getDescription());
        }
        if (updatedActivity.getType() != null) {
            activity.setType(updatedActivity.getType());
        }
        if (updatedActivity.getLocation() != null) {
            activity.setLocation(updatedActivity.getLocation());
        }
        if (updatedActivity.getActivityDate() != null) {
            activity.setActivityDate(updatedActivity.getActivityDate());
        }

        return activityRepository.save(activity);
    }

    public void delete(String id) {
        activityRepository.deleteById(id);
    }
}
