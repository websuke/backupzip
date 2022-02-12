package backupzip.batch.delete;

/**
 * 後処理削除エントリポイントクラス
 */
public class AfterDeleteMain {
    public static void main(String[] args) {
        
        // 起動引数チェック
        if (args.length != 1) {
            System.err.println("起動引数の数が1ではありません。");
            System.exit(1);
        }
        
        System.exit(new AfterDelete().doBatch(args[0]));

    }
}
