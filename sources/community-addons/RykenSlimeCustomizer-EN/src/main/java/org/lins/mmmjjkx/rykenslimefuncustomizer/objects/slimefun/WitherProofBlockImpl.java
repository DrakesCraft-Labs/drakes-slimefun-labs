package org.lins.mmmjjkx.rykenslimefuncustomizer.objects.slimefun;

import com.github.drakescraft_labs.slimefun4.core.attributes.WitherProof;
import javax.annotation.Nonnull;
import org.bukkit.block.Block;
import org.bukkit.entity.Wither;

public interface WitherProofBlockImpl extends WitherProof {
    default void onAttack(@Nonnull Block var1, @Nonnull Wither var2) {}
}
