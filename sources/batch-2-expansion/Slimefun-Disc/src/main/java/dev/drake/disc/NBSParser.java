package dev.drake.disc;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class NBSParser {

    private NBSParser() {}

    public static NBSong parse(File file) throws IOException {
        try (DataInputStream in = new DataInputStream(new FileInputStream(file))) {
            short first = readShortLE(in);
            int version = 0;

            if (first == 0) {
                version = in.readUnsignedByte();
                in.readUnsignedByte();
            }

            short length;
            if (first != 0) {
                length = first;
            } else if (version >= 3) {
                length = readShortLE(in);
            } else {
                length = 0;
            }

            short layers = readShortLE(in);
            String name = readNBSString(in);
            String author = readNBSString(in);
            String originalAuthor = readNBSString(in);
            String description = readNBSString(in);

            float tempo = readShortLE(in) / 100f;
            if (tempo <= 0) tempo = 10f;

            in.readBoolean();
            in.readByte();
            in.readByte();

            readIntLE(in);
            readIntLE(in);
            readIntLE(in);
            readIntLE(in);
            readIntLE(in);
            readNBSString(in);

            if (version >= 4) {
                in.readByte();
                in.readByte();
                readShortLE(in);
            }

            Map<Integer, List<Note>> tickNotes = new HashMap<>();
            Map<Integer, InstrumentDef> instruments = new HashMap<>();
            int tick = 0;
            while (true) {
                short jumpTicks = readShortLE(in);
                if (jumpTicks <= 0) break;
                tick += jumpTicks;

                List<Note> notesAtTick = new ArrayList<>();
                while (true) {
                    short jumpLayers = readShortLE(in);
                    if (jumpLayers <= 0) break;

                    int instrument = in.readByte() & 0xFF;
                    int noteKey = in.readByte() & 0xFF;
                    int velocity = 100;

                    if (version >= 4) {
                        velocity = in.readByte() & 0xFF;       // velocity (0-100)
                        in.readByte();                         // panning
                        readShortLE(in);                       // fine pitch
                    }

                    notesAtTick.add(new Note(tick, noteKey, instrument, velocity));
                }
                tickNotes.put(tick, notesAtTick);
            }

            for (int i = 0; i < layers; i++) {
                readNBSString(in);
                if (version >= 4) {
                    in.readByte();
                }
                in.readByte();
                if (version >= 2) {
                    in.readByte();
                }
            }

            int instrumentCount = in.readByte() & 0xFF;
            for (int i = 0; i < instrumentCount; i++) {
                String instName = readNBSString(in);
                String soundFile = readNBSString(in);
                int pitch = in.readByte() & 0xFF;
                in.readByte();
                instruments.put(i, new InstrumentDef(instName, soundFile, pitch));
            }

            int totalTicks = tickNotes.keySet().stream().max(Integer::compareTo).orElse(0);
            float durationSeconds = totalTicks / (tempo * 2f);

            return new NBSong(name, author, originalAuthor, description, tempo, durationSeconds, tickNotes);
        }
    }

    public static final class NBSong {
        private final String name;
        private final String author;
        private final String originalAuthor;
        private final String description;
        private final float tempo;
        private final float durationSeconds;
        private final Map<Integer, List<Note>> tickNotes;

        NBSong(String name, String author, String originalAuthor, String description,
               float tempo, float durationSeconds, Map<Integer, List<Note>> tickNotes) {
            this.name = name;
            this.author = author;
            this.originalAuthor = originalAuthor;
            this.description = description;
            this.tempo = tempo;
            this.durationSeconds = durationSeconds;
            this.tickNotes = tickNotes;
        }

        public String getName() { return name; }
        public String getAuthor() { return author; }
        public float getTempo() { return tempo; }
        public float getDurationSeconds() { return durationSeconds; }
        public Map<Integer, List<Note>> getTickNotes() { return tickNotes; }
    }

    public static final class Note {
        private final int tick;
        private final int noteKey;
        private final int instrument;
        private final int velocity;

        Note(int tick, int noteKey, int instrument, int velocity) {
            this.tick = tick;
            this.noteKey = noteKey;
            this.instrument = instrument;
            this.velocity = velocity;
        }

        public int getTick() { return tick; }
        public int getNoteKey() { return noteKey; }
        public int getInstrument() { return instrument; }
        public int getVelocity() { return velocity; }
    }

    private static final class InstrumentDef {
        private final String name;
        private final String soundFile;
        private final int pitch;

        InstrumentDef(String name, String soundFile, int pitch) {
            this.name = name;
            this.soundFile = soundFile;
            this.pitch = pitch;
        }
    }

    private static String readNBSString(DataInputStream in) throws IOException {
        int len = readIntLE(in);
        if (len <= 0) return "";
        byte[] bytes = new byte[len];
        in.readFully(bytes);
        return new String(bytes, "UTF-8");
    }

    private static short readShortLE(DataInputStream in) throws IOException {
        int ch1 = in.readUnsignedByte();
        int ch2 = in.readUnsignedByte();
        return (short) ((ch2 << 8) | ch1);
    }

    private static int readIntLE(DataInputStream in) throws IOException {
        int ch1 = in.readUnsignedByte();
        int ch2 = in.readUnsignedByte();
        int ch3 = in.readUnsignedByte();
        int ch4 = in.readUnsignedByte();
        return (ch4 << 24) | (ch3 << 16) | (ch2 << 8) | ch1;
    }
}