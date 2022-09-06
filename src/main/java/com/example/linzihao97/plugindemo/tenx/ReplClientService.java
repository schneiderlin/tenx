package com.example.linzihao97.plugindemo.tenx;

public class ReplClientService {
    private final ReplClient client = new ReplClient();
    public void start() {
        client.start(7888);
    }
}
