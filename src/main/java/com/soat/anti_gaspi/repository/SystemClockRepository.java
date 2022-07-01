package com.soat.anti_gaspi.repository;

import org.springframework.stereotype.Repository;

import java.time.Clock;
import java.time.LocalDate;

@Repository
public class SystemClockRepository implements ClockRepository{

    private Clock clock;

    public SystemClockRepository(Clock clock) {
        this.clock = clock;
    }

    @Override
    public LocalDate now() {
        return LocalDate.now(clock);
    }
}
