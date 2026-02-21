import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

class Alarm implements Runnable{
    private final LocalTime alarmTime;
    private final Scanner scanner;
    Alarm(LocalTime alarmTime, Scanner scanner){
        this.alarmTime = alarmTime;
        this.scanner = scanner;
    }
    @Override
    public void run(){
        while(LocalTime.now().isBefore(alarmTime)){
            try {
                Thread.sleep(1000);
                LocalTime now = LocalTime.now();
                System.out.printf("\r%02d:%02d:%02d",now.getHour(),now.getMinute(),now.getSecond());
            } 
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        playSound();
    }
    private void playSound(){
        File audio = new File("alarm Clock.wav");
        try(AudioInputStream audioStream = AudioSystem.getAudioInputStream(audio)){
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.print("\nPress enter to STOP Alarm");
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
    
public class AlarmClock{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime alarmTime=null;
        while(alarmTime == null){
            try{
                System.out.print("Enter an alarm Time (HH:mm:ss): ");
                String alarm = scanner.nextLine();
                alarmTime = LocalTime.parse(alarm, formatter);
                System.out.println("Alarm Set for: "+alarm);
            }
            catch(DateTimeParseException e){
                System.out.println("Invalid Format. Please use HH:mm:ss");
            }
        }
        Thread thread = new Thread(new Alarm(alarmTime, scanner));
        thread.start();
    }
}