package hu.auditorium.controller;

import hu.auditorium.model.domain.Chair;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ChairService {

    private static final int MAX_NUMBER = 20;
    private static final int MAX_ROW = 15;
    private static final String PERCENT_FORMAT = "%2.0f%%";

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
        return String.format(PERCENT_FORMAT, percent);
    }

    public boolean isGivenChairOccupied(int row, int number) {
        return chairs.stream()
                .filter(i -> i.findChair(row, number))
                .findAny()
                .map(Chair::isOccupied)
                .orElse(true);
    }

    public int getMostPopularChairCategory() {
        return getChairCategoryCountMap().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .get();
    }

    public int countTotalIncome() {
        return chairs.stream()
                .filter(Chair::isOccupied)
                .mapToInt(Chair::getPrice)
                .sum();
    }

    public long getSingleFreeChairCount() {
        return chairs.stream()
                .filter(this::isSingleFreeChair)
                .count();
    }

    public List<String> getAuditoriumStatus() {
        String auditoriumStatusInRow = getAuditoriumStatusInRow();
        return IntStream.range(0, MAX_ROW)
                .mapToObj(row -> printChairsInRow(auditoriumStatusInRow, row))
                .collect(Collectors.toList());
    }

    private Map<Integer, Long> getChairCategoryCountMap() {
        return chairs.stream()
                .filter(Chair::isOccupied)
                .collect(Collectors.groupingBy(Chair::getCategoryId, Collectors.counting()));
    }

    private boolean isSingleFreeChair(Chair chair) {
        int row = chair.getRow();
        int number = chair.getNumber();
        return !chair.isOccupied()
                && isGivenChairOccupied(row, number - 1)
                && isGivenChairOccupied(row, number + 1);
    }

    private String printChairsInRow(String auditoriumStatusInRow, int row) {
        return auditoriumStatusInRow.substring(row * MAX_NUMBER, row * MAX_NUMBER + (MAX_NUMBER - 1));
    }

    private String getAuditoriumStatusInRow() {
        return chairs.stream()
                .map(Chair::toString)
                .collect(Collectors.joining());
    }

}
