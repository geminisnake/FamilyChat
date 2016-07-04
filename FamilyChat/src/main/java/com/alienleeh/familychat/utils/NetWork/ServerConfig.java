package com.alienleeh.familychat.utils.NetWork;

final class ServerConfig {

    public enum ServerEnv {
        TEST("t"),
        PRE_REL("p"),
        REL("r"),

        ;
        String tag;

        ServerEnv(String tag) {
            this.tag = tag;
        }
    }


    public static boolean testServer() {
        return ServerEnvs.SERVER == ServerEnv.TEST;
    }

    static final class ServerEnvs {

        //
        // ENVs
        // DEFAULT Env.REL
        //

        static final ServerConfig.ServerEnv SERVER = ServerConfig.ServerEnv.REL;

    }
}
