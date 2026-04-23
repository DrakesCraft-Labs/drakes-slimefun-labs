package com.github.relativobr.supreme.machine.tech;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItemStack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MobTechMutationGeneric {

    SlimefunItemStack input1;
    SlimefunItemStack input2;
    int chance;
    SlimefunItemStack output;

}
