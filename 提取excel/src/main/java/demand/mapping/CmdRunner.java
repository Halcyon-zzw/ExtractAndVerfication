package demand.mapping;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * TODO
 *
 * @Author: zhuzw
 * @Date: 2019/12/3 17:16
 * @Version: 1.0
 */
@Component
public class CmdRunner implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        Options options = new Options();
        // 直接增加方式，参数1为短选项，2为长选项，3为是否选项后有参数，
        // 如-i 2之类。4为注释，在HelpFormatter打印中有用。
        options.addOption("h", "help", false, "显示帮助。");
        options.addOption("t", "type", true, "操作类型。");
        options.addOption("p", "path", true, "源语料路径。");

        CommandLineParser parser = new DefaultParser();

        CommandLine cl = parser.parse(options, args, true);
        if (cl.hasOption("h")) {
            System.out.println("aaa");
        }
    }
}
