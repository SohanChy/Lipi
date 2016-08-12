package tests;

import model.hugo.HMdFileProcessor;
import model.hugo.Hugo;
import model.toml.TomlUtils;
import model.utility.FileHandler;
import model.utility.Ipc;
import model.utility.Pandoc;

import java.util.ArrayList;
import java.util.List;

class RunTests {
    private static final String testsFolder = "tests-res";

    public static void main(String[] args) {

        System.out.println("This is the class to run tests on features.");

        testHugo();
        testHMdFileProcessor();

        testTomlParser();
    }

    private static void testTomlParser() {
        String inp;
    /*
    inp = testsFolder + "/tomlParserTest.md";
    HMdFileProcessor mdObject = new HMdFileProcessor(inp);
    mdObject.readHMdFile();

    System.out.println(TomlUtils.readToml(mdObject.getFrontMatter()));*/

        inp = testsFolder + "/tomlConfigTest.toml";

        System.out.println(TomlUtils.toToml(TomlUtils.readToml(FileHandler.readFile(inp))));
    }

    private static void testHugo() {

        Hugo testing = new Hugo("/home/sohan/Sandbox/Java/blog");
        testing.startHugoServer();

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Slept 3000 ms");
        testing.stopHugoServer();

        //System.out.println(testing.getHugoOut());
    }


    private static void testHMdFileProcessor() {

        HMdFileProcessor mdObject = new HMdFileProcessor(testsFolder + "/test.md");


        mdObject.readHMdFile();
        //mdObject.setPostContent((mdObject.getPostContent() + "-----TEST-----"));

        mdObject.writeHMdFile(true);

        //         System.out.println(mdObject.getFrontMatter());
        //         System.out.println(mdObject.getPostContent());

    }

    public static void testIpc() {

        Ipc pandoc = new Ipc("pandoc");

        List<String> params = new ArrayList<String>();

        params.add("test.html");
        params.add("-o");
        params.add("test.md");

        pandoc.setProgArgs(params);

        pandoc.runProc();
    }

    public static void testPandoc() {
        Pandoc PandocParser = new Pandoc();

        //System.out.println( PandocParser.htmlToMd( FileHandler.readFile("test.html") ) );
        System.out.println(PandocParser.htmlToMd(FileHandler.readFile(testsFolder + "/test.html")));
    }

}
