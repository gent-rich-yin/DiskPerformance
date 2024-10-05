package io.javago.diskperformance;

import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.stream.IntStream;

@Service //@Todo Service vs. Component?
@Slf4j
public class DiskWriter {
    private final List<String> messages;
    private final static int N_MESSAGES = 1_000_000;

    public DiskWriter() {
        messages = generateFakeMessages();
    }

    public void write(String filePath) {
        long stime = System.currentTimeMillis();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String m : messages) {
                writer.write(m + System.lineSeparator());
//                writer.flush();
            }
        } catch(IOException e) {
            log.error(e.getMessage(), e);
        }
        long ftime = System.currentTimeMillis();
        log.info("Took: " + (ftime - stime) + "ms, " + (((double)N_MESSAGES) * 1000 / (ftime - stime)) + " messages per second");
    }

    private List<String> generateFakeMessages() {
        var shakespeare = new Faker().shakespeare();
        return IntStream.range(0, N_MESSAGES).mapToObj(i -> shakespeare.romeoAndJulietQuote()).toList();
    }
}
