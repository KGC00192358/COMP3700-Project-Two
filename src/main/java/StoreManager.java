import javax.swing.*;

public class StoreManager {
    public static final String DBMS_SQ_LITE = "SQLite";
    public static final String DB_FILE = "C:\\Users\\Kevin\\IdeaProjects\\ProjectTwo\\Data\\store.db";

    IDataAdapter adapter = null;
    SQLiteDataAdapter sqlAdapter = null;
    WebHelper helper = null;
    private static StoreManager instance = null;


    public static StoreManager getInstance() {
        if (instance == null) {

            String dbfile = DB_FILE;
            if (dbfile.length() == 0) {
                JFileChooser fc = new JFileChooser();
                if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
                    dbfile = fc.getSelectedFile().getAbsolutePath();
            }
            instance = new StoreManager(DBMS_SQ_LITE, dbfile);
        }
        return instance;
    }

    private StoreManager(String dbms, String dbfile) {
        if (dbms.equals("Oracle"))
            adapter = new OracleDataAdapter();
        else
        if (dbms.equals("SQLite"))
            adapter = new SQLiteDataAdapter();
            sqlAdapter = (SQLiteDataAdapter) adapter;

        helper = new WebHelper();
        adapter.connect(dbfile);
    }

    public IDataAdapter getDataAdapter() {
        return adapter;
    }
    public SQLiteDataAdapter getSQLDataAdapter() {
        return sqlAdapter;
    }

    public void setDataAdapter(IDataAdapter a) {
        adapter = a;
    }


    public void run() {
        MainUI ui = new MainUI();
        ui.view.setVisible(true);
    }

    public static void main(String[] args) {
        System.out.println("Hello class!");
//        StoreManager.getInstance().init();
        StoreManager.getInstance().run();
    }

}
