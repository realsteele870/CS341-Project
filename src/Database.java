import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.sqlite.SQLiteDataSource;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public class Database {

	public Connection connection;
	//Database Git update-objects
	Git git;
	private Repository liveRepository;
	//private CredentialsProvider cp = new UsernamePasswordCredentialsProvider("benlamasney", "");
	private String databaseLocation = "nasa.db";
	private Config config;
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
        config = liveRepository.getConfig();
	}
	
	public void connect() throws SQLException {
		connection = DriverManager.getConnection("jdbc:sqlite:nasa.db");
		git = new Git(liveRepository);
		
		//config.setString("user", NULL, , value);
	}
	
	public void disconnect() throws SQLException {
		connection.close();
	}
	
	public ResultSet runQuery(String query) throws SQLException {
		PreparedStatement stmt = connection.prepareStatement(query);
		ResultSet results = stmt.executeQuery();
		return results;
	}

	
	public void updateDatabase(String commitMessage) {
		String defaultMessage = "Auto-Update";
		if("".equals(commitMessage)) {
			commitDatabase(defaultMessage);
		} else {
			commitDatabase(commitMessage);
		}
		pushDatabase();
		pullDatabase();
	}	
	public void commitDatabase(String message) {
		try {
			git.commit().setMessage(message).call();
		} catch (NoHeadException e) {
			System.err.println("No HEAD for repository was found.");
			e.printStackTrace();
		} catch (NoMessageException e) {
			System.err.println("No commit message was created.");
			e.printStackTrace();
		} catch (UnmergedPathsException e) {
			System.err.println("File discpencies between local and git servers exist; manually merge.");
			e.printStackTrace();
		} catch (ConcurrentRefUpdateException e) {
			System.err.println("Concurrent updates were attempted. Please try again soon.");
			e.printStackTrace();
		} catch (WrongRepositoryStateException e) {
			System.err.println("An error in repository permissions prohibits your current action.");
			e.printStackTrace();
		} catch (AbortedByHookException e) {
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
	}
	public void pushDatabase() {
		try {
			git.push().call();
		} catch (InvalidRemoteException e) {
			System.err.println("Fetch was performed on an invalid remote location/repository.");
			e.printStackTrace();
		} catch (TransportException e) {
			System.err.println("Transport Operation Failed.");
			e.printStackTrace();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}	
	}
	public void pullDatabase() {
		try {
			git.pull().call();
		} catch (WrongRepositoryStateException e) {
			System.err.println("An error in repository permissions prohibits your current action.");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			System.err.println("Action failed due to invalid repo/git configuration.");
			e.printStackTrace();
		} catch (InvalidRemoteException e) {
			System.err.println("Fetch was performed on an invalid remote location/repository.");
			e.printStackTrace();
		} catch (CanceledException e) {
			System.err.println("Operation was cancelled");
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			System.err.println("Git reference was not found.");
			e.printStackTrace();
		} catch (RefNotAdvertisedException e) {
			System.err.println("Git reference was not found in advertised refs.");
			e.printStackTrace();
		} catch (NoHeadException e) {
			System.err.println("No HEAD for repository was found.");
			e.printStackTrace();
		} catch (TransportException e) {
			System.err.println("Transport Operation Failed.");
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void fetchDatabase() {
		try {
			git.fetch().call();
		} catch (InvalidRemoteException e) {
			System.err.println("Fetch was performed on an invalid remote location/repository.");
			e.printStackTrace();
		} catch (TransportException e) {
			System.err.println("Transport Operation Failed.");
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}