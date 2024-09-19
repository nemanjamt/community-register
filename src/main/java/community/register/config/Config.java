package community.register.config;

public class Config {
    public static String resourcePath = "src/main/resources";

    private static boolean isRunningFromJar() {
        String classpath = Config.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        return classpath.endsWith(".jar");
    }

    public static void loadConfig(){
        if (isRunningFromJar()) {
            Config.resourcePath = "resources";
        } else {
            Config.resourcePath = "src/main/resources";
        }
    }
}
