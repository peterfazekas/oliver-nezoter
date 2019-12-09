package hu.auditorium.controller;

import hu.auditorium.model.domain.Chair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChairService {

    private static final int MAX_NUMBER = 20;
    private static final int MAX_ROW = 15;

    private final List<Chair> chairs;

    public ChairService(List<Chair> chairs) {
        this.chairs = chairs;
    }

    public long getOccupiedChairCount() {
        return chairs.stream()
                .filter(Chair::isOccupied)
                .count();
    }

    public String getOccupiedChairPercent() {
        double percent = getOccupiedChairCount() * 100.0 / chairs.size();
        return String.format("%2.0f%%", percent);
    }

    public boolean isGivenChairOccupied(int row, int number) {
        return getChair(row, number)
                .map(Chair::isOccupied)
                .orElse(true);
    }

    public int countTotalIncome() {
        return chairs.stream()
                .filter(Chair::isOccupied)
                .mapToInt(Chair::getPrice)
                .sum();
    }

    private Optional<Chair> getChair(int row, int number) {
        return chairs.stream()
                .filter(i -> i.findChair(row, number))
                .findAny();
    }

    public List<String> getAuditoriumStatus() {
        String auditoriumStatusInRow = getAuditoriumStatusInRow();
        return IntStream.range(0, MAX_ROW)
                .mapToObj(row -> auditoriumStatusInRow.substring(row * MAX_NUMBER, row * MAX_NUMBER + (MAX_NUMBER - 1)))
                .collect(Collectors.toList());
    }

    private String getAuditoriumStatusInRow() {
        return chairs.stream()
                .map(Chair::toString)
                .collect(Collectors.joining());
    }

}
