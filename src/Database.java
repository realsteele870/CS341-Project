import java.sql.Connection;
import java.sql.SQLException;

import org.sqlite.SQLiteDataSource;

public class Database {

	public Database() {
        SQLiteDataSource ds = null;

        try {
            ds = new SQLiteDataSource();
            ds.setUrl("jdbc:sqlite:nasa.db");
        } catch ( Exception e ) {
            e.printStackTrace();
            System.exit(0);
        }
        System.out.println( "Opened database successfully" );

        try ( Connection conn = ds.getConnection() ) {
        } catch ( SQLException e ) {
            e.printStackTrace();
            System.exit( 0 );
        }
	}
}