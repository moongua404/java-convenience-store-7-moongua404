package store.model;

import java.time.LocalDateTime;
import store.model.dto.PromotionDto;

public class Promotion {
    private String name;
    private int buy;
    private int get;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

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
}
