package stream;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class Client {
    public static void main(String[] args) {

        AudioFormat format = getAudioFormat();
        DataLine.Info info = getInfo(format);
        if (info == null) return;

        InetAddress addr;
        TargetDataLine line;
        DatagramPacket dgp;

        try {
            line = (TargetDataLine) AudioSystem.getLine(info);

            int buffSize = line.getBufferSize() / 5;
            buffSize += 512;

            line.open(format);

            line.start();

            int numBytesRead;
            byte[] data = new byte[buffSize];
//            {
//                final int λ = 16;
//                ByteBuffer buffer = ByteBuffer.allocate(λ * 2 * 8);
//                for (int j = 0; j < 2; j++) {
//                    for (double i = 0.0; i < λ; i++) {
//                        System.out.println(j + " " + i);
//                        //once for each sample
//                        buffer.putShort((short) (Math.sin(Math.PI * (λ / i)) * Short.MAX_VALUE));
//                        buffer.putShort((short) (Math.sin(Math.PI * (λ / i)) * Short.MAX_VALUE));
//                    }
//                }
//
//                data = buffer.array();
//            }
            addr = InetAddress.getByName("192.168.2.72");
            DatagramSocket socket = new DatagramSocket();
            while (true) {
                // Read the next chunk of data from the TargetDataLine.
                numBytesRead = line.read(data, 0, data.length);
                System.out.println("numBytesRead - " + numBytesRead);
                // Save this chunk of data.
                dgp = new DatagramPacket(data, data.length, addr, 50005);

                socket.send(dgp);
            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            // TODO: handle exception
        } catch (SocketException e) {
            // TODO: handle exception
        } catch (IOException e2) {
            // TODO: handle exception
        }
    }

    private static DataLine.Info getInfo(AudioFormat format) {
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line matching " + info + " not supported.");
            return null;
        }
        return info;
    }

    private static AudioFormat getAudioFormat() {
        AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
        float rate = 44100.0f;
        int sampleSize = 16;
        int channels = 2;
        boolean bigEndian = true;
        InetAddress addr;
        return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);
    }
}