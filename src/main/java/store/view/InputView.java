package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.constants.MessageConstants;

public class InputView {
    public String readItem() {
        System.out.println(MessageConstants.PURCHASE_GUIDE_MESSAGE.getMessage());
        return Console.readLine();
    }
}
