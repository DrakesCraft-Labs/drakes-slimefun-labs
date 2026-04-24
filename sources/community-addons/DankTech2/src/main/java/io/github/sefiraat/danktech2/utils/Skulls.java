package io.github.sefiraat.danktech2.utils;

import dev.drake.dough.skins.PlayerHead;
import dev.drake.dough.skins.PlayerSkin;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

public enum Skulls {

    MAIN_HEAD("32697929465950ccd3a4923a5e6eb15b4fe2ba2b28715f4a7996785d706158"),

    PACK_1("407940711108966031f5e86128528d362736cac1514593531008a3c6026192b2"),
    PACK_2("dda260fdc05722eebaab390aebc2f9392134440f3b1da5f30e5fe671238a8abb"),
    PACK_3("982bada50efa8cbd8758c226c7712158c6fca03eaa87e19c5c231d4feda8a2c6"),
    PACK_4("5f1d806d1c4cd0b1ddd0bea39f48e8d7475069a6d79d8e06becc12a93a3f3512"),
    PACK_5("561c46bf6e5d5ebc31654dfc4fef2f6bfb4981bef8c1008a7e7ab425b9c49813"),
    PACK_6("6fcb6c6d342d96b8fc6e50be2ee8f3bfffb57779e100fc0bf46e0b03b17d0681"),
    PACK_7("39b8bee816fead9ef1ea70ba580327734f94c58b231d57737ca75a9dcdee10d9"),
    PACK_8("c2a2459c8c2bed9e1d54b9d2d918a4e0d7384b3cd90bd657aa728c80ec4bfed0"),
    PACK_9("a89b4c5e119a61773dd52d36b572bc0f9548d984ce0841417a301a65351a768d"),

    TRASH_1("1e03bf5133bde0e6b8c629975215c28b0048120c40e3d739122ba09cd1778cea"),
    TRASH_2("32518d04f9c06c95dd0edad617abb93d3d8657f01e659079d330cca6f65bccf7"),
    TRASH_3("1811917c644f04f56b68c9c71b33ac082c2601c24409be78c2063d4df2a86525"),
    TRASH_4("94c28ffb9063e63037c7c9c3a99975333254946ddfa0018996151740a5c66842"),
    TRASH_5("8d6a5e43f598bcc1ac2205c2b3017e409feeb1d4c42e4719f540b8764e9fa996"),
    TRASH_6("bc761ff1b56ddb3906e5146e6bd7df0922f2ba6c3e3024803f794a719da184a3"),
    TRASH_7("df361075b779ec6504758a5ee3d4dbe3bc54e713cb1a5add86749eaa19877046"),
    TRASH_8("c02154af4fdf893e1b65d74463c853c66bf7f05b1a2efac510448c59d335dc0d"),
    TRASH_9("b465f80bf02b408885987b00957ca5e9eb874c3fa88305099597a333a336ee15"),

    CELL_1("caf039bec1fc1fb75196092b26e631f37a87dff143fc18297798d47c5eaaf"),
    CELL_2("145fd2c3736af4bd2811296661e0cb49bab2cfa65f5c9e598aa43bebfa1ea368"),
    CELL_3("c607cffcd7864ff27c78b29a2c5955131a677bef6371f88988d3dcc37ef8873"),
    CELL_4("85484f4b6367b95bb16288398f1c8dd6c61de988f3a8356d4c3ae73ea38a42"),
    CELL_5("4f26ea93d5fd19a3808a5e5885fc29612657d83dfab9bff527279ccbd6f16"),
    CELL_6("d1137b9bf435c4b6b88faeaf2e41d8fd04e1d9663d6f63ed3c68cc16fc724"),
    CELL_7("c54deeeab3b9750c77642ec95e37fe2bdf9adc555e0826dedd769bedd10"),
    CELL_8("3dd0115e7d84b11b67a4c6176521a0e79d8ba7628587ae38159e828852bfd"),
    CELL_9("533a5bfc8a2a3a152d646a5bea694a425ab79db694b214f156c37c7183aa"),

    GUI_WITHDRAW("61e1e730c77279c8e2e15d8b271a117e5e2ca93d25c8be3a00cc92a00cc0bb85"),
    GUI_INFO("16439d2e306b225516aa9a6d007a7e75edd2d5015d113b42f44be62a517e574f"),
    GUI_EMPTY("3ed1aba73f639f4bc42bd48196c715197be2712c3b962c97ebf9e9ed8efa025");

    @Getter
    protected static final Skulls[] cachedValues = values();

    @Getter
    private final String hash;

    @ParametersAreNonnullByDefault
    Skulls(String hash) {
        this.hash = hash;
    }

    public ItemStack getPlayerHead() {
        return PlayerHead.getItemStack(PlayerSkin.fromHashCode(hash));
    }

}
