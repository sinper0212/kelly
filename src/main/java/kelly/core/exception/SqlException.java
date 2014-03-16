package kelly.core.exception;

import java.sql.SQLException;

public class SqlException extends KellyException {

	public SqlException(SQLException cause) {
		super(cause);
	}

}
