package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Rating to determine if the worker is available on the start date
 * A match will be permitted if the worker is not available on the start date,
 * but a penalty is applied
 */
@Component
public class AvailableOnStartDayRating implements MatchRating {

    private final double weightValue;

    public AvailableOnStartDayRating(@Value("${engine.rating.availableOnStartDay.weight}") double weightValue) {
        this.weightValue = weightValue;
    }

    @Override
    public double determineRating(WorkerDto worker, JobDto job) {
        // Swipejobs uses day index starting from Monday = 1 while DayOfWeek starts from Monday = 0
        // Note: I'm assuming that the timezone in the job startDate is the correct timezone to
        // evaluate this logic against. That may not be a good assumption - ideal would be to
        // know the worker's timezone and convert the job startDate into that TZ.
        final int requiredDayIndex = job.getStartDate().getDayOfWeek().ordinal() + 1;
        if (worker.getAvailability().stream()
                .filter(Objects::nonNull).anyMatch(a -> a.getDayIndex() == requiredDayIndex)) {
            return weightValue;
        } else {
            return 0;
        }
    }
}
