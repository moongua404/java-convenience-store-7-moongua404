package store.model.dto;

import java.time.LocalDateTime;
import store.constants.ExceptionConstants;
import store.constants.MessageConstants;

//name,buy,get,start_date,end_date
public class PromotionDto {
    private final String name;
    private final int buy;
    private final int get;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;

    public PromotionDto(
            String name, int buy, int get, LocalDateTime startDate, LocalDateTime endDate) throws Exception {
        validatePromotionDto(name, buy, get, startDate, endDate);
        this.name = name;
        this.buy = buy;
        this.get = get;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validatePromotionDto(String name, int buy, int get,
                                      LocalDateTime start, LocalDateTime end) throws Exception {
        if (name == null || start == null || end == null || buy <= 0 || get <= 0) {
            throw ExceptionConstants.INVALID_PROMOTION.getException();
        }
    }

    @Override
    public String toString() {
        return String.format(
                MessageConstants.PROMOTE_MESSAGE.getMessage(),
                name, buy, get, startDate.toString(), endDate.toString());
    }

    public String getName() {
        return name;
    }

    public int getBuy() {
        return buy;
    }

    public int getGet() {
        return get;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }
}
