package store;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import store.constants.ErrorConstants;
import store.constants.FilePathConstants;
import store.model.dto.ProductDto;
import store.model.dto.PromotionDto;

public class StoreService {
    String NEWLINE_DELIMITER = "\n";
    String COMMA_DELIMITER = ",";
    String NULL_DELIMITER = "null";

    public List<ProductDto> loadItem() throws Exception {
        Path path = Paths.get(FilePathConstants.ITEM_FILE_PATH.getPath());
        String content = getFileData(path);

        List<List<String>> parsedContent = separateCsvData(content, NEWLINE_DELIMITER).stream()
                .skip(1)
                .map((line) -> separateCsvData(line, COMMA_DELIMITER))
                .toList();

        return ParseToProduct(parsedContent);
    }

    public List<PromotionDto> loadPromotion() throws Exception {
        Path path = Paths.get(FilePathConstants.PROMOTION_FILE_PATH.getPath());
        String content = getFileData(path);

        List<List<String>> parsedContent = separateCsvData(content, NEWLINE_DELIMITER).stream()
                .skip(1)
                .map((line) -> separateCsvData(line, COMMA_DELIMITER))
                .toList();

        return ParseToPromotion(parsedContent);
    }

    private String getFileData(Path path) throws Exception {
        try {
            return Files.readString(path);
        } catch (Exception e) {
            throw ErrorConstants.INVALID_FILE_IO.getException();
        }
    }

    private List<String> separateCsvData(String content, String delimiter) {
        return List.of(content.split(delimiter));
    }

    private List<ProductDto> ParseToProduct(List<List<String>> content) throws Exception {
        List<ProductDto> parsedData = new ArrayList<>();
        for (List<String> line : content) {
            String name = line.getFirst();
            int price = parseInt(line.get(1));
            int amount = parseInt(line.get(2));
            String promotion = line.get(3);
            if (promotion.equals(NULL_DELIMITER)) {
                promotion = null;
            }
            parsedData.add(new ProductDto(name, price, amount, promotion));
        }
        return parsedData;
    }

    private List<PromotionDto> ParseToPromotion(List<List<String>> content) throws Exception {
        List<PromotionDto> parsedData = new ArrayList<>();
        for (List<String> line : content) {
            String name = line.get(0);
            int buy = parseInt(line.get(1));
            int get = parseInt(line.get(2));
            LocalDateTime startTime = parseDate(line.get(3));
            LocalDateTime endTime = parseDate(line.get(4));
            parsedData.add(new PromotionDto(name, buy, get, startTime, endTime));
        }
        return parsedData;
    }

    private int parseInt(String token) throws Exception {
        try {
            return Integer.parseInt(token);
        } catch (Exception exception) {
            throw ErrorConstants.INVALID_NUMBER_FORMAT.getException();
        }
    }

    private LocalDateTime parseDate(String token) throws Exception {
        try {
            return LocalDate.parse(token).atStartOfDay();
        } catch (Exception exception) {
            throw ErrorConstants.INVALID_DATE_FORMAT.getException();
        }
    }
}
