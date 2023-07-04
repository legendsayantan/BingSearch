package com.legendsayantan.bingsearch;

import java.io.IOException;

public class BingSearch {
    int c, d;
    String p;
    boolean cT, dB;



    public BingSearch(int count, int delay, String path, boolean closeTabs, boolean defaultBrowser) {
        c = count;
        d = delay;
        p = path;
        cT = closeTabs;
        dB = defaultBrowser;
    }

    public Thread createThread() {
        return new Thread(() -> {
            for (int i = 0; i < c; i++) {
                try {
                    dispatchSearch();
                    onProgress(i +1);
                } catch (Exception e) {
                    if(e instanceof InterruptedException)return;
                    else onError(e);
                }
            }
        });
    }

    public void onProgress(int progress){}
    public void onError(Exception e){}
    private void dispatchSearch() throws InterruptedException, IOException {
        //generate a random word
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < Math.random()*10; i++) {
            word.append((char) (Math.random() * 26 + 97));
        }
        System.out.println("Searching for " + word);
        String url = "https://www.bing.com/search?q=" + word;
        String os = System.getProperty("os.name").toLowerCase();
        Process browserProcess;
        ProcessBuilder processBuilder;
        if (dB) {
            if (os.contains("win")) {
                // Windows
                processBuilder = new ProcessBuilder("cmd", "/c", "start", url);
            } else if (os.contains("mac")) {
                // macOS
                processBuilder = new ProcessBuilder("open", url);
            } else if (os.contains("nix") || os.contains("nux") || os.contains("bsd")) {
                // Linux/Unix/BSD
                processBuilder = new ProcessBuilder("xdg-open", url);
            } else {
                throw new UnsupportedOperationException("Unsupported os: " + os);
            }
        }else{
            processBuilder = new ProcessBuilder(p, url);
        }
        //use ProcessBuilder to open browser
        browserProcess = processBuilder.start();

        Thread.sleep(d * 1000L);
        if (cT) {
            browserProcess.destroy();
            browserProcess.waitFor();
        }
    }
}
