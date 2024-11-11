package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.Objects;
import store.constants.ExceptionConstants;
import store.constants.MessageConstants;

public class InputView {
    public String readItem() {
        System.out.println(MessageConstants.PURCHASE_GUIDE_MESSAGE.getMessage());
        return Console.readLine();
    }

    public boolean readYorN(MessageConstants message, String name, int amount) throws Exception {
        System.out.println(String.format(message.getMessage(), name, amount));
        String line = Console.readLine();
        if (Objects.equals(line, "Y")) {
            return true;
        }
        if (Objects.equals(line, "N")) {
            return false;
        }
        throw ExceptionConstants.INVALID_INPUT.getException();
    }
}
