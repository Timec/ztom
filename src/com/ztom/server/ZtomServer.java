package com.ztom.server;

import ch.qos.logback.classic.Logger;
import com.ztom.container.ZtomContainer;
import com.ztom.container.factory.ZtomFactory;
import com.ztom.vo.ConfigVO;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ZtomServer {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(ZtomServer.class);
    private static final int NUM_THREADS = 50;
    private static ZtomFactory ztomFactory;

    public ZtomServer() {
    }

    public void initiate() {
        ztomFactory = new ZtomFactory();
        logger.info("Ztom Configuration setting completed");
    }

    public void start() throws NumberFormatException, IOException {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        ConfigVO config = ztomFactory.getConfig().getConfigVO();
        logger.debug("Ztom binding port:" + config.getConnector().getPort());
        logger.info("Ztom started");

        try (ServerSocket server = new ServerSocket(Integer.valueOf(config.getConnector().getPort()))) {
            while (true) {
                try {
                    Socket request = server.accept();
                    Runnable r = new ZtomContainer(request, ztomFactory);
                    pool.submit(r);
                } catch (IOException e) {
                    logger.warn("Error accepting connection", e);
                }
            }
        }
    }

    public static void main(String[] args) {
        ZtomServer server = new ZtomServer();
        server.initiate();
        try {
            server.start();
        } catch (IOException ex) {
            logger.warn("Server could not start", ex);
        }
    }
}
