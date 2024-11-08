package store;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import store.constants.ErrorConstants;
import store.constants.FilePathConstants;
import store.model.dto.ProductDto;

public class StoreService {
    String NEWLINE_DELIMITER = "\n";
    String COMMA_DELIMITER = ",";
    String NULL_DELIMITER = "null";

    public List<ProductDto> loadItem() throws Exception {
        Path path = Paths.get(FilePathConstants.ITEM_FILE_PATH.getPath());
        String content = getFileData(path);

        List<List<String>> parsedContent = separateCsvData(content, NEWLINE_DELIMITER).stream()
                .skip(1)
                .map((line)->separateCsvData(line, COMMA_DELIMITER))
                .toList();

        return ParseToProduct(parsedContent);
    }

    private String getFileData(Path path) throws Exception {
        try {
            return Files.readString(path);
        } catch (Exception e) {
            throw ErrorConstants.INVALID_FILE_IO.getException();
        }
    }

    private List<String> separateCsvData (String content, String delimiter) {
        return List.of(content.split(delimiter));
    }

    private List<ProductDto> ParseToProduct(List<List<String>> content) throws Exception {
        List<ProductDto> parsedData = new ArrayList<>();
        for (List<String> line : content) {
            // 예외처리 해야함 (null handling 포함)
            String name = line.getFirst();
            int price = Integer.parseInt(line.get(1));
            int amount = Integer.parseInt(line.get(2));
            String promotion = line.get(3);
            if (promotion.equals(NULL_DELIMITER))
                promotion = null;
            parsedData.add(new ProductDto(name, price, amount, promotion));
        }
        parsedData.forEach((data)->System.out.println(data.toString()));
        return parsedData;
    }
}
