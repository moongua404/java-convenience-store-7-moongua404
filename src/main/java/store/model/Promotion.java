package store.model;

import java.time.LocalDateTime;
import store.model.dto.PromotionDto;

public class Promotion {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public Promotion(PromotionDto promotionDto) {
        this.name = promotionDto.getName();
        this.buy = promotionDto.getBuy();
        this.get = promotionDto.getGet();
        this.startDate = promotionDto.getStartDate();
        this.endDate = promotionDto.getEndDate();
    }

    public Promotion(String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getName() {
        return name;
    }

    public int getGet() {
        return get;
    }

    public int getBuy() {
        return buy;
    }

    public int getBundle() {
        return get + buy;
    }

    public boolean isValidOn(LocalDateTime time) {
        return !time.isBefore(startDate) && !time.isAfter(endDate);
    }
}
