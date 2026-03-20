import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import javax.sound.sampled.*;

class TimerWork implements Runnable{
    private int duration;
    private final Scanner scanner;
    TimerWork(int duration, Scanner scanner){
        this.duration = duration;
        this.scanner = scanner;
    }
    @Override
    public void run(){
        while (duration > 0){
            try {
                int hrs = duration / 3600;
                int mins = (duration % 3600) / 60;
                int secs = duration % 60;

                System.out.printf("\r%02d:%02d:%02d", hrs, mins, secs);

                Thread.sleep(1000);
                duration--;
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        playSound();
    }

    private void playSound() {
        File audio = new File("alarm Clock.wav");
        try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(audio)) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);

            System.out.print("\nTime's up! Press Enter to stop...");
            scanner.nextLine();

            clip.stop();
            scanner.close();
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}

public class Timer {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter timer duration in seconds: ");
        int duration = scanner.nextInt();
        scanner.nextLine();
        Thread thread = new Thread(new TimerWork(duration, scanner));
        thread.start();
    }
}
