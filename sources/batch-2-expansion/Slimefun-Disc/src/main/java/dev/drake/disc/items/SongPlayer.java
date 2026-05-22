package dev.drake.disc.items;

import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import dev.drake.disc.NBSParser.NBSong;
import dev.drake.disc.NBSParser.Note;

public class SongPlayer {

    private static final Sound[] INSTRUMENTS = {
        Sound.BLOCK_NOTE_BLOCK_HARP,
        Sound.BLOCK_NOTE_BLOCK_BASS,
        Sound.BLOCK_NOTE_BLOCK_BASEDRUM,
        Sound.BLOCK_NOTE_BLOCK_SNARE,
        Sound.BLOCK_NOTE_BLOCK_HAT,
        Sound.BLOCK_NOTE_BLOCK_GUITAR,
        Sound.BLOCK_NOTE_BLOCK_FLUTE,
        Sound.BLOCK_NOTE_BLOCK_BELL,
        Sound.BLOCK_NOTE_BLOCK_CHIME,
        Sound.BLOCK_NOTE_BLOCK_XYLOPHONE,
        Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE,
        Sound.BLOCK_NOTE_BLOCK_COW_BELL,
        Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO,
        Sound.BLOCK_NOTE_BLOCK_BIT,
        Sound.BLOCK_NOTE_BLOCK_BANJO,
        Sound.BLOCK_NOTE_BLOCK_PLING
    };

    private final Location location;
    private final NBSong song;
    private BukkitRunnable task;
    private int currentTick = 0;
    private final int maxTick;

    public SongPlayer(Location location, NBSong song) {
        this.location = location;
        this.song = song;
        this.maxTick = song.getTickNotes().keySet().stream().max(Integer::compareTo).orElse(0);
    }

    public void start(JavaPlugin plugin) {
        long intervalTicks = Math.max(1, (long)(20f / song.getTempo()));

        task = new BukkitRunnable() {
            @Override
            public void run() {
                List<Note> notes = song.getTickNotes().get(currentTick);

                if (notes != null) {
                    for (Note note : notes) {
                        playSound(note);
                    }
                }

                currentTick++;
                if (currentTick > maxTick) {
                    cleanup();
                }
            }
        };

        task.runTaskTimer(plugin, 0, intervalTicks);
    }

    private void playSound(Note note) {
        int rawNote = note.getNoteKey();
        int instrument = note.getInstrument();
        int velocity = note.getVelocity();

        Sound sound = getInstrument(instrument);
        float pitch = keyToPitch(rawNote);
        float volume = Math.max(0.5f, velocity / 100.0f);

        location.getWorld().playSound(location, sound, volume, pitch);
    }

    private float keyToPitch(int key) {
        int click;
        if (key > 24) {
            click = key - 33;
        } else {
            click = key;
        }
        click = Math.max(0, Math.min(24, click));
        return (float) Math.pow(2.0, (click - 12) / 12.0);
    }

    private Sound getInstrument(int index) {
        if (index >= 0 && index < INSTRUMENTS.length) {
            return INSTRUMENTS[index];
        }
        return Sound.BLOCK_NOTE_BLOCK_HARP;
    }

    public void stop() {
        cleanup();
    }

    private void cleanup() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        currentTick = 0;
    }

    public boolean isPlaying() {
        return task != null;
    }
}