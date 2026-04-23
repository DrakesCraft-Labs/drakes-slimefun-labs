package me.voper.slimeframe.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.inventory.ItemStack;

import com.github.drakescraft-labs.slimefun4.libraries.commons.lang.Validate;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ParametersAreNonnullByDefault
public class RandomItemStacks<T extends ItemStack> {

    @Getter
    private final List<T> items = new ArrayList<>();

    public RandomItemStacks(T[] itemStacks) {
        this.items.addAll(List.of(itemStacks));
    }

    public RandomItemStacks(RandomItemStacks<T>[] randomItemStacksArray) {
        for (RandomItemStacks<T> randomItemStack : randomItemStacksArray) {
            this.items.addAll(randomItemStack.items);
        }
    }

    public T getRandom() {
        Validate.notEmpty(items, "Items list must not be empty.");
        return items.size() == 1 ? items.get(0) : items.get(ThreadLocalRandom.current().nextInt(items.size()));
    }

    public void add(T item) {
        this.items.add(item);
    }

}
