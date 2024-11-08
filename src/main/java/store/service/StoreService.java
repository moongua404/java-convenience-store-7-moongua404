package store.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.constants.ExceptionConstants;
import store.constants.FilePathConstants;
import store.model.dto.ProductDto;
import store.model.dto.PromotionDto;
import store.model.dto.PurchaseDto;

public class StoreService {
    String NEWLINE_DELIMITER = "\n";
    String COMMA_DELIMITER = ",";
    String NULL_DELIMITER = "null";

    public List<ProductDto> loadItem() throws Exception {
        Path path = Paths.get(FilePathConstants.ITEM_FILE_PATH.getPath());
        String content = getFileData(path);

        List<List<String>> parsedContent = separateData(content, NEWLINE_DELIMITER).stream()
                .skip(1)
                .map((line) -> separateData(line, COMMA_DELIMITER))
                .toList();

        return ParseToProduct(parsedContent);
    }

    public List<PromotionDto> loadPromotion() throws Exception {
        Path path = Paths.get(FilePathConstants.PROMOTION_FILE_PATH.getPath());
        String content = getFileData(path);

        List<List<String>> parsedContent = separateData(content, NEWLINE_DELIMITER).stream()
                .skip(1)
                .map((line) -> separateData(line, COMMA_DELIMITER))
                .toList();

        return ParseToPromotion(parsedContent);
    }

    public List<PurchaseDto> parsePurchaseDto(String response) throws Exception {
        List<String> items = separateData(response, COMMA_DELIMITER);
        List<PurchaseDto> parsedData = new ArrayList<>();
        for (String item : items) {
            PurchaseDto productPurchaseDto = parseProductPurchaseDto(item);
            parsedData.add(productPurchaseDto);
        }
        return parsedData;
    }

    private PurchaseDto parseProductPurchaseDto(String itemInfo) throws Exception {
        Pattern pattern = Pattern.compile("^\\[([\\\\w가-힣]+)-(\\d+)\\]");
        Matcher matcher = pattern.matcher(itemInfo);
        try {
            if (matcher.matches()) {
                String name = matcher.group(1);
                int amount = parseInt(matcher.group(2));
                return new PurchaseDto(name, amount);
            }
            throw new Exception();
        } catch (Exception exception) {
            throw ExceptionConstants.INVALID_INPUT.getException();
        }
    }

    private String getFileData(Path path) throws Exception {
        try {
            return Files.readString(path);
        } catch (Exception exception) {
            throw ExceptionConstants.INVALID_FILE_IO.getException();
        }
    }

    private List<String> separateData(String content, String delimiter) {
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
            throw ExceptionConstants.INVALID_NUMBER_FORMAT.getException();
        }
    }

    private LocalDateTime parseDate(String token) throws Exception {
        try {
            return LocalDate.parse(token).atStartOfDay();
        } catch (Exception exception) {
            throw ExceptionConstants.INVALID_DATE_FORMAT.getException();
        }
    }
}
