package com.swipejobs.matcher.engine;

import com.swipejobs.matcher.dto.JobDto;
import com.swipejobs.matcher.dto.WorkerDto;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static junit.framework.TestCase.assertEquals;

public class HasRequiredCertificatesRatingUnitTest {

    private static final double RATING_INCREMENT = 0.8;

    private HasRequiredCertificatesRating hasRequiredCertificatesRating;

    @Before
    public void constructRating() {
        hasRequiredCertificatesRating = new HasRequiredCertificatesRating(1);
    }

    /**
     * Success case for identifying that a worker is missing a certificate
     * Expects -1 to be returned
     */
    @Test
    public void workerIsMissingCertificateTest() {
        WorkerDto worker = new WorkerDto();
        worker.setCertificates(Collections.emptyList());
        JobDto job = new JobDto();
        job.setRequiredCertificates(Collections.singletonList("certificate0"));
        assertEquals(-1d, hasRequiredCertificatesRating.determineRating(worker, job));
    }

    /**
     * Success case for job with no certificate requirements
     * Expects 0 to be returned
     */
    @Test
    public void noCertificatesAreRequiredTest() {
        WorkerDto worker = new WorkerDto();
        worker.setCertificates(Collections.emptyList());
        JobDto job = new JobDto();
        job.setRequiredCertificates(Collections.emptyList());
        assertEquals(0d, hasRequiredCertificatesRating.determineRating(worker, job));
    }

    /**
     * Success case for identifying that a worker has a certificate
     * Expects 1 to be returned
     */
    @Test
    public void workerHasCertificateTest() {
        WorkerDto worker = new WorkerDto();
        worker.setCertificates(Collections.singletonList("certificate0"));
        JobDto job = new JobDto();
        job.setRequiredCertificates(Collections.singletonList("certificate0"));
        assertEquals(1d, hasRequiredCertificatesRating.determineRating(worker, job));
    }

    /**
     * Success case for worker having multiple required certificates
     * Expects rating to increment by a reduced amount for each matching certificate
     */
    @Test
    public void workerHasMultipleCertificatesTest() {
        WorkerDto worker = new WorkerDto();
        worker.setCertificates(new ArrayList<>());
        JobDto job = new JobDto();
        job.setRequiredCertificates(new ArrayList<>());
        double expectRating = 0;
        for (int i = 0; i < 5; i++) {
            assertEquals(expectRating, hasRequiredCertificatesRating.determineRating(worker, job));
            expectRating += Math.pow(RATING_INCREMENT, i);
            worker.getCertificates().add(String.format("certificate%s", i));
            job.getRequiredCertificates().add(String.format("certificate%s", i));
        }
    }

    /**
     * Success case for worker having more certificates than job requires
     * Expects rating value to only reflect certificates required by job
     */
    @Test
    public void workerHasExtraCertificatesTest() {
        WorkerDto worker = new WorkerDto();
        worker.setCertificates(Arrays.asList(
                "certificate0",
                "certificate1",
                "certificate2",
                "certificate3",
                "certificate4"
        ));
        JobDto job = new JobDto();
        job.setRequiredCertificates(new ArrayList<>());
        double expectRating = 0;
        for (int i = 0; i < 5; i++) {
            assertEquals(expectRating, hasRequiredCertificatesRating.determineRating(worker, job));
            expectRating += Math.pow(RATING_INCREMENT, i);
            job.getRequiredCertificates().add(String.format("certificate%s", i));
        }
    }
}
