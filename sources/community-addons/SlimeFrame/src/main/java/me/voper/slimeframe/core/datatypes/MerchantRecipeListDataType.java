package me.voper.slimeframe.core.datatypes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import com.github.drakescraft-labs.slimefun4.api.items.SlimefunItem;

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class MerchantRecipeListDataType implements PersistentDataType<byte[], List<MerchantRecipe>> {

    @Nonnull
    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Nonnull
    @Override
    public Class<List<MerchantRecipe>> getComplexType() {
        return (Class<List<MerchantRecipe>>) (Class<?>) List.class;
    }

    @Nonnull
    @Override
    public byte[] toPrimitive(@Nonnull List<MerchantRecipe> merchantRecipes, @Nonnull PersistentDataAdapterContext context) {
        if (merchantRecipes.isEmpty()) {
            return new byte[]{};
        }

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeInt(merchantRecipes.size());
            for (MerchantRecipe recipe : merchantRecipes) {
                SerializableMerchantRecipe serializableRecipe = new SerializableMerchantRecipe(recipe);
                objectStream.writeObject(serializableRecipe);
            }
            objectStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[]{};
        }

        return byteStream.toByteArray();
    }

    @Nonnull
    @Override
    public List<MerchantRecipe> fromPrimitive(@Nonnull byte[] bytes, @Nonnull PersistentDataAdapterContext context) {
        if (bytes.length == 0) {
            return new ArrayList<>();
        }

        List<MerchantRecipe> merchantRecipes = new ArrayList<>();

        try (ObjectInputStream objectStream = new CustomObjectInputStream(new ByteArrayInputStream(bytes))) {
            int recipeCount = objectStream.readInt();
            for (int i = 0; i < recipeCount; i++) {
                SerializableMerchantRecipe serializableRecipe = (SerializableMerchantRecipe) objectStream.readObject();
                MerchantRecipe recipe = serializableRecipe.toMerchantRecipe(context);
                merchantRecipes.add(recipe);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return merchantRecipes;
    }

    private static class SerializableMerchantRecipe implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        private ItemStack result;
        private ItemStack[] ingredients;
        private int uses;
        private int maxUses;
        private boolean experienceReward;

        public SerializableMerchantRecipe(@Nonnull MerchantRecipe recipe) {
            this.result = recipe.getResult();
            this.ingredients = recipe.getIngredients().toArray(new ItemStack[0]);
            this.uses = recipe.getUses();
            this.maxUses = recipe.getMaxUses();
            this.experienceReward = recipe.hasExperienceReward();
        }

        @Nonnull
        public MerchantRecipe toMerchantRecipe(PersistentDataAdapterContext context) {
            MerchantRecipe recipe = new MerchantRecipe(result, uses, maxUses, experienceReward);
            for (ItemStack ingredient : ingredients) {
                recipe.addIngredient(ingredient);
            }
            return recipe;
        }

        @Serial
        private void writeObject(@Nonnull ObjectOutputStream out) throws IOException {
            out.writeInt(uses);
            out.writeInt(maxUses);
            out.writeBoolean(experienceReward);
            out.writeObject(itemStackArrayToBase64(new ItemStack[]{result}));
            out.writeObject(itemStackArrayToBase64(ingredients));
        }

        @Serial
        private void readObject(@Nonnull ObjectInputStream in) throws IOException, ClassNotFoundException {
            uses = in.readInt();
            maxUses = in.readInt();
            experienceReward = in.readBoolean();
            result = itemStackArrayFromBase64(String.valueOf(in.readObject()))[0];
            ingredients = itemStackArrayFromBase64((String) in.readObject());
        }

        @Nonnull
        private static String itemStackArrayToBase64(ItemStack[] items) throws IllegalStateException {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

                // Write the size of the array
                dataOutput.writeInt(items.length);

                // Save every element in the list
                for (int i = 0; i < items.length; i++) {
                    SlimefunItem sfItem = SlimefunItem.getByItem(items[i]);
                    if (sfItem != null) {
                        ItemStack clone = sfItem.getItem().clone();
                        clone.setAmount(items[i].getAmount());
                        SerializableSlimefunItemStack serializableSfStack = new SerializableSlimefunItemStack(clone);
                        dataOutput.writeObject(serializableSfStack);
                    } else {
                        dataOutput.writeObject(items[i]);
                    }
                }

                // Serialize that array
                dataOutput.close();
                return Base64Coder.encodeLines(outputStream.toByteArray());
            } catch (Exception e) {
                throw new IllegalStateException("Unable to save item stacks.", e);
            }
        }

        @Nonnull
        private static ItemStack[] itemStackArrayFromBase64(String data) throws IOException {
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
                BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
                ItemStack[] items = new ItemStack[dataInput.readInt()];

                // Read the serialized string
                for (int i = 0; i < items.length; i++) {
                    Object o = dataInput.readObject();
                    if (o instanceof SerializableSlimefunItemStack sfStack) {
                        items[i] = sfStack.itemStack;
                    } else if (o instanceof ItemStack itemStack) {
                        items[i] = itemStack;
                    }
                }

                dataInput.close();
                return items;
            } catch (ClassNotFoundException e) {
                throw new IOException("Unable to decode class type.", e);
            }
        }

    }

    @SerializableAs("SerializableSlimefunItemStack")
    public static class SerializableSlimefunItemStack implements ConfigurationSerializable {

        private final ItemStack itemStack;

        SerializableSlimefunItemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        @Nonnull
        @Override
        public Map<String, Object> serialize() {
            return itemStack.serialize();
        }

        @Nonnull
        public static SerializableSlimefunItemStack deserialize(Map<String, Object> args) {
            return new SerializableSlimefunItemStack(ItemStack.deserialize(args));
        }

    }

    public static class CustomObjectInputStream extends ObjectInputStream {

        public CustomObjectInputStream(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
            ObjectStreamClass osc = super.readClassDescriptor();

            if (osc.getName().equals("me.voper.slimeframe.slimefun.datatypes.MerchantRecipeListDataType$SerializableMerchantRecipe")) {
                osc = ObjectStreamClass.lookup(MerchantRecipeListDataType.SerializableMerchantRecipe.class);
            }

            return osc;
        }
    }

}
