package store;

import camp.nextstep.edu.missionutils.DateTimes;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import store.constants.ExceptionConstants;

public class Utils {
    public <T> void validateEmpty(List<T> items, Exception exception) throws Exception {
        if (items.isEmpty()) {
            throw exception;
        }
    }

    public void validatePositiveNumber(int item, Exception exception) throws Exception {
        if (item <= 0) {
            throw exception;
        }
    }

    public <T, R> void validateDuplicate(List<T> items, Function<T, R> keyExtractor) throws Exception {
        Set<R> seenKeys = new HashSet<>();
        for (T item : items) {
            R key = keyExtractor.apply(item);
            if (seenKeys.contains(key)) {
                throw ExceptionConstants.DUPLICATED_ITEM.getException();
            }
            seenKeys.add(key);
        }
    }

    public LocalDateTime getToday() {
        return DateTimes.now();
    }
}
