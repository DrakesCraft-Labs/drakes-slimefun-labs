package me.voper.slimeframe.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.annotation.Nonnull;

import org.bukkit.inventory.ItemStack;

import org.apache.commons.lang.Validate;
import dev.drake.dough.common.CommonPatterns;
import dev.drake.dough.skins.PlayerHead;
import dev.drake.dough.skins.PlayerSkin;

import lombok.experimental.UtilityClass;

@UtilityClass
public class HeadTextures {

    @Nonnull
    public static ItemStack getSkull(String texture) {
        PlayerSkin skin = PlayerSkin.fromBase64(getTexture(texture));
        return PlayerHead.getItemStack(skin);
    }

    private static String getTexture(String texture) {
        Validate.notNull(texture, "The texture cannot be null");
        if (texture.startsWith("ey")) {
            return texture;
        } else if (CommonPatterns.HEXADECIMAL.matcher(texture).matches()) {
            String value = "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/" + texture + "\"}}}";
            return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
        } else {
            throw new IllegalArgumentException("The provided texture does not seem to be a valid texture String!");
        }
    }

    // https://minecraft-heads.com/custom-heads/humanoid/19390-excalibur-umbra
    public static final String MAIN_GROUP = "9b00d20a4f3eee0ca06bcf99a5cbfafaedfa0f5528432e72c0bd48e2e75938b";
    // https://minecraft-heads.com/custom-heads/miscellaneous/43797-error-cube
    public static final String MULTIBLOCKS = "9f13197c6a7cf52570fe564aab9944a9043bdf7d957479311ece0225e1d38df4";
    // https://minecraft-heads.com/custom-heads/miscellaneous/45543-moon-relic
    public static final String LITH_RELIC = "725bcb769f4dab17e6f5fe7412cdc57aa0326671686ff818aaed9e0957a11e0f";
    // https://minecraft-heads.com/custom-heads/miscellaneous/45544-sun-relic
    public static final String MESO_RELIC = "19a92e4cc9a85b4b0a83a3e7e14a19dd3814dae623e00cf9a37e8db4ddc9b757";
    // https://minecraft-heads.com/custom-heads/miscellaneous/45545-emerald-relic
    public static final String NEO_RELIC = "49f613d20b46f0cb89ec374faf4f4730fa5dc1090a0f52dffbc3c0cd59462894";
    // https://minecraft-heads.com/custom-heads/miscellaneous/45542-warped-relic
    public static final String AXI_RELIC = "e6aaef67c1436465d62a4ccb76a8716e58a9bf7d6df7ece77dae8f8c306ec3d2";
    public static final String CUBIC_DIODES = "f854cf58f3ba6a538906e0c44fbd50b54f56152c77354d947a8f895c72436820";
    // https://minecraft-heads.com/custom-heads/alphabet/10317-gray-plus
    public static final String GRAY_PLUS = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTBjOTdlNGI2OGFhYWFlODQ3MmUzNDFiMWQ4NzJiOTNiMzZkNGViNmVhODllY2VjMjZhNjZlNmM0ZTE3OCJ9fX0=";
    // https://minecraft-heads.com/custom-heads/alphabet/10323-gray-minus
    public static final String GRAY_MINUS = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzY3NGI2ODJiMTc5ZDFkNGZhNzkxZjkyY2RhZjFjMmYzN2ZhZDRlYWRiYzdkYmRjMDYwZTgxNTRkMzUxNDgyIn19fQ==";
    // https://minecraft-heads.com/custom-heads/decoration/58672-core-blue
    public static final String MACHINE_CORE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZThmNWRjOTI3OTIzMjc5MTI3YTlkMmFkZTg2NDMyZjk4Nzc2MDljYjlmODM4NTRhNWI4OTJiZjdjYWQ5ZGYyZiJ9fX0=";
    // https://minecraft-heads.com/custom-heads/decoration/30286-power-orb
    public static final String MACHINE_POWER_CELL = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2E3Y2RhOTAwNGZjMTk3ZDY2YWZiYzJiMDAzYTViOWVmMTNjZjQ2MDBiMWZjNzQ5MDA2NzU5MGYwNDcxODFlIn19fQ==";
    // https://minecraft-heads.com/custom-heads/decoration/55749-control-monitor
    public static final String MACHINE_CONTROL = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWQ2Yjg1ODRlOTM4YzgwNWRhNTViMjRiYTJmMDI3ZjNiY2JkMWI1NDM0ZWY2NDliODU4MzkzNGZhMWI2NGI0NCJ9fX0=";

    // https://minecraft-heads.com/custom-heads/decoration/14324-void-orb
    public static final String VOID_SHARD_ESSENCE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjIwMWFlMWE4YTA0ZGY1MjY1NmY1ZTQ4MTNlMWZiY2Y5Nzg3N2RiYmZiYzQyNjhkMDQzMTZkNmY5Zjc1MyJ9fX0=";
    // https://minecraft-heads.com/custom-heads/decoration/56477-clock
    public static final String TEMPORAL_COGWHEEL = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTQyYTI0MTUyYWExM2FjMzE1YjI3MWQ2OThiODJiNGE3YTE3ZjEwZjE3ZjBlOGM0OTVmYjZmOGNiMzljNTU2NCJ9fX0=";
    // https://minecraft-heads.com/custom-heads/miscellaneous/57905-fancy-cube
    public static final String NEURAL_NEXUS_CORE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjI2ZGFkNzRiMmJhYjEwNWNiNjhjOTRlYjNiZTMyZjVkYmRhNDJlYWI5NDRiNmVkOWU4MDMxMzZmOGY2MTliYyJ9fX0=";
}
