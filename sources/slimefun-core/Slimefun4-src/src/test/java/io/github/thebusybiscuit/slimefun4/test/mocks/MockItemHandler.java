package com.github.drakescraft_labs.slimefun4.test.mocks;

import com.github.drakescraft_labs.slimefun4.api.items.ItemHandler;

public class MockItemHandler implements ItemHandler {

    @Override
    public Class<? extends ItemHandler> getIdentifier() {
        return getClass();
    }

}
