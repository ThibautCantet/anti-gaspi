package com.soat.anti_gaspi;

import com.soat.anti_gaspi.repository.ClockRepository;

import java.time.LocalDate;

public class FakeClockRepository implements ClockRepository {

    private LocalDate now;

    public FakeClockRepository(LocalDate now) {

        this.now = now;
    }

    @Override
    public LocalDate now() {
        return now;
    }
}
