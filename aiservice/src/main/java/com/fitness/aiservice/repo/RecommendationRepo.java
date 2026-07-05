package com.fitness.aiservice.repo;

import com.fitness.aiservice.models.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepo extends MongoRepository<Recommendation,String> {
    List<Recommendation> findByUserId(String userId); // non primary filed ko list doge ko multiple
    // nahi to single return krega ( primary -uniequ always single return;)

    Optional<Recommendation> findByActivityId(String activityId);
}
