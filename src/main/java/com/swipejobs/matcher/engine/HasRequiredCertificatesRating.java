package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Rating to determine if worker has all required certificates
 * If worker is missing any certificates, rating will be -1
 * Otherwise, a higher rating is given for the more certificates that match
 */
@Component
public class HasRequiredCertificatesRating implements MatchRating {

    private static final double RATING_INCREMENT = 0.8;

    private final double weightValue;

    public HasRequiredCertificatesRating(@Value("${engine.rating.hasRequiredCertificates.weight}") double weightValue) {
        this.weightValue = weightValue;
    }

    @Override
    public double determineRating(WorkerDto worker, JobDto job) {
        double rating = 0;
        int matchedCount = 0;
        for (String requiredCertificate : job.getRequiredCertificates()) {
            if (!StringUtils.isEmpty(requiredCertificate)) {
                if (worker.getCertificates().contains(requiredCertificate)) {
                    // The more matching certificates the better
                    // However, there is a reduced benefit to each successive match
                    rating += Math.pow(RATING_INCREMENT, matchedCount);
                    matchedCount++;
                } else {
                    return -1;
                }
            }
        }

        return weightValue * rating;
    }
}
