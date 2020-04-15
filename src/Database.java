import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.sqlite.SQLiteDataSource;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;

public class Database {

	public Connection connection;
	//Database Git update-objects
	public Git git;
	public Repository liveRepository;
	private CredentialsProvider cp;
	String databaseLocation = "nasa.db";
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
        //connection to local repository
        try {
			liveRepository = new FileRepositoryBuilder()
					.findGitDir()
					.build();
		} catch (IOException e) {
			System.err.print("Unable to locate local repository");
			e.printStackTrace();
		}
	}
	
	public void connect() throws SQLException {
		connection = DriverManager.getConnection("jdbc:sqlite:nasa.db");
		git = new Git(liveRepository);
		
	}
	
	public void disconnect() throws SQLException {
		connection.close();
	}
	
	public ResultSet runQuery(String query) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet results = stmt.executeQuery();
		return results;
	}
}