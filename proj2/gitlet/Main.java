package gitlet;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static gitlet.Utils.*;
import static gitlet.Repository.*;

/**
 * Driver class for Gitlet, a subset of the Git version-control system.
 *
 * @author TODO
 */
public class Main {

    /**
     * Usage: java gitlet.Main ARGS, where ARGS contains
     * <COMMAND> <OPERAND1> <OPERAND2> ...
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }
        switch (args[0]) {
            case "init":
                Repository.init();
                break;
            case "add":
                initCheck();
                validateNumArgs(args, 2);
                Repository.add(args[1]);
                break;
            case "commit":
                initCheck();
                validateNumArgs(args, 2);
                Repository.commit(args[1]);
                break;
            case "rm":
                initCheck();
                validateNumArgs(args, 2);
                Repository.rm(args[1]);
                break;
            case "log":
                initCheck();
                Repository.log();
                break;
            case "global-log":
                initCheck();
                Repository.globalLog();
                break;
            case "find":
                initCheck();
                validateNumArgs(args, 2);
                find(args[1]);
                break;
            case "status":
                initCheck();
                Repository.status();
                break;
            case "checkout": {
                initCheck();
                Repository.checkout(args);
            }

            case "branch":
                initCheck();
                validateNumArgs(args, 2);
                Repository.branch(args[1]);

            case "rm-branch":
                initCheck();
                validateNumArgs(args, 2);
                rmBranch(args[1]);
                break;
            case "reset":
                initCheck();
                validateNumArgs(args, 2);
                Repository.reset(args[1]);
                break;
            case "merge":
                break;
            default:
                System.out.println("No command with that name exists. ");
                break;

        }
    }


    /**
     * Checks the .gitlet folder exists
     */
    private static void initCheck() {
        if (!GITLET_DIR.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }

    /**
     * Checks the number of arguments versus the expected number,
     * call System.exit(0) if they do not match.
     *
     * @param args Argument array from command line
     * @param n    Number of expected arguments
     */
    public static void validateNumArgs(String[] args, int n) {
        if (args.length != n) {
            System.out.println("Incorrect operands.");
            System.exit(0);
        }
    }


}
