/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #11
 * 1 - 5026231018 - Izzudin Hamadi Faiz
 * 2 - 5026231091 - Bagas Rafi Dewantara
 * 3 - 5026231116 - I Putu Febryan Khrisyantara
 */

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SoundPlayer {
    public static void playSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            System.err.println("Error playing sound: " + e.getMessage());
        }
    }
}


