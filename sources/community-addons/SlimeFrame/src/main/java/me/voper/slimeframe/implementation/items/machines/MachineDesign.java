package me.voper.slimeframe.implementation.items.machines;

import lombok.*;
import lombok.experimental.Accessors;

// Crédito: diseño de layout de máquina (línea Mooy1 / InfinityLib); port en el lab Drake.

@Setter
@Getter
@With
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true)
public final class MachineDesign {

    private int[] inputBorder;
    private int[] inputSlots;
    private int[] outputBorder;
    private int[] outputSlots;
    private int[] background;
    private int statusSlot;
    private int selectorSlot;

    public static final MachineDesign CRAFTING_MACHINE = new MachineDesign()
            .background(new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 13, 31, 36, 37, 38, 39, 40, 41, 42, 43, 44})
            .inputBorder(new int[]{9, 10, 11, 12, 18, 21, 27, 28, 29, 30})
            .inputSlots(new int[]{19, 20})
            .outputBorder(new int[]{14, 15, 16, 17, 23, 26, 32, 33, 34, 35})
            .outputSlots(new int[]{24, 25})
            .statusSlot(22);

    public static final MachineDesign SELECTOR_MACHINE = CRAFTING_MACHINE
            .withSelectorSlot(4);

}
