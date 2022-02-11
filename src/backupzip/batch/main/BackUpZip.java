package backupzip.batch.main;

public final class BackUpZip extends AbstractBackUpZipBase{

    public static void main(String[] args) {
        
        // 起動引数チェック
        if (args.length != 1) {
            System.err.println("起動引数の数が1ではありません。");
            System.exit(1);
        }
        
        System.exit(new BackUpZip().doBatch(args[0]));
    }

}
