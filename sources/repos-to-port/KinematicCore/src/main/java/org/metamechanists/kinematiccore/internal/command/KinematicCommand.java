package org.metamechanists.kinematiccore.internal.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.metamechanists.kinematiccore.KinematicCore;
import org.metamechanists.kinematiccore.api.entity.KinematicEntitySchema;
import org.metamechanists.kinematiccore.test.MainTester;


@SuppressWarnings("unused")
@CommandAlias("ki|kinematic")
public class KinematicCommand extends BaseCommand {
    private static final TextColor HEADING_COLOR = TextColor.color(100, 255, 100);

    @HelpCommand
    @Description("Show this help screen")
    public static void help(@NotNull CommandSender sender, @NotNull CommandHelp help) {
        sender.sendMessage(Component.text("[[ Kinematic Core ]]").color(HEADING_COLOR).decorate(TextDecoration.BOLD));
        help.showHelp();
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    @Subcommand("dev")
    @Description("Commands to help with Kinematic development. DO NOT EVER use this command outside of a test server unless you know exactly what you're doing!")
    public class KinematicDevCommand extends BaseCommand {

        @Subcommand("schemas")
        @Description("List all loaded schemas")
        public static void schemas(@NotNull Player player) {
            player.sendMessage(String.join(", ", KinematicEntitySchema.registeredSchemas()));
        }

        @Subcommand("test")
        @Description("Run Kinematic tests.")
        public class KinematicTestCommand extends BaseCommand {

            @Subcommand("all")
            @Description("Run all tests.")
            public static void test(@NotNull Player player) {
                // Tests must be run async
                Bukkit.getScheduler().runTaskAsynchronously(KinematicCore.instance(), () -> {
                    MainTester.TestResult result = new MainTester(player.getLocation().getWorld()).all();

                    player.sendMessage(ChatColor.GRAY + "[ "
                            + ChatColor.GREEN + result.passed() + ChatColor.WHITE + " passed"
                            + ChatColor.GRAY + " | "
                            + ChatColor.RED + result.failed() + ChatColor.WHITE + " failed"
                            + ChatColor.GRAY + " | "
                            + ChatColor.BLUE + result.total() + ChatColor.WHITE + " total"
                            + ChatColor.GRAY + " ]"
                    );

                    for (String failure : result.failures()) {
                        player.sendMessage(ChatColor.DARK_GRAY + "- " + ChatColor.DARK_RED + failure);
                    }
                });
            }
        }
    }
}

