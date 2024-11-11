package store;

import store.controller.StoreController;
import store.model.Store;
import store.service.StoreService;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) throws Exception {
        // TODO: 프로그램 구현
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        StoreService storeService = new StoreService();
        Store store = new Store();
        StoreController storeController = new StoreController(
                inputView,
                outputView,
                storeService
        );

        try {
            storeController.run();
        } catch (Exception exception) {
            return;
        }
    }
}
