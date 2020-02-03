package com.gvr.datahub.watch;

import com.orpak.fho.service.FileCharsetConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashSet;
import java.util.Set;

@Service
public class FileWatch {

    private static final Logger logger = LoggerFactory.getLogger(FileWatch.class);

    private static WatchService watchService;
    private static String filePath;

    @PostConstruct
    public void watch() {
         logger.info("start file watch");
        //目录全路径
        filePath = "C:\\HongJianWork\\HJOldPc\\fho\\";

        try {
            // 获取文件系统的WatchService对象
            watchService = FileSystems.getDefault().newWatchService();
            // 监听filePath目录下文件是否修改或增加；register()方法后面监听事件种类还可以增加、删除。
            Paths.get(filePath).register(watchService,  StandardWatchEventKinds.ENTRY_CREATE); //监听修改、添加
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置后台线程
        Thread watchThread = new Thread(new Runnable() {

            private Set<String> fileNameSet = new HashSet<>(); //Set去重，当添加文件的时候，修改时间也会被监听执行

            @Override
            public void run() {
                while (true) {
                    try {
                        // 获取下一个文件改动事件
                        WatchKey key = watchService.take();
                        for (WatchEvent<?> event : key.pollEvents()) {
                            logger.info("file event {}",event.kind());
                            fileNameSet.add(event.context() + "");
                        }
                        String lastFile = "";
                        for (String name : fileNameSet) {
                            lastFile = filePath+"\\"+name;
                            logger.info("File: " + filePath + "" + name);
                        }
                        File file = new File(lastFile);
                        if(file.exists()){
                            Thread.sleep(1000);
                            FileCharsetConverter.addBom(lastFile);
                        }
                        // 重设WatchKey
                        boolean valid = key.reset();
                        fileNameSet.clear();

                        // 如果重设失败，退出监听
                        if (!valid) {
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // 设置为后台守护线程
        watchThread.setDaemon(true);
        watchThread.start();

    }

    /**
     * 模拟测试
     */
    public static void main(String[] args) throws Exception {

        // 开启监听程序，如有改动，则更新对象
        FileWatch w = new FileWatch();
        w.watch();

        // 假装做一些事情，延迟。
      //  Thread.sleep(8000000000000l);

    }
}
