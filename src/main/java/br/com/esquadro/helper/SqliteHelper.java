package br.com.esquadro.helper;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import br.com.esquadro.sqlite.entity.BancoDadosEntity;

public class SqliteHelper {

	public static ConnectionSource connectionSource;

	public static void init() throws SQLException, IOException {

		File file = new File("bd/esquadro.db");
		if (!file.exists()) {
			file.createNewFile();
		}

		String databaseUrl = "jdbc:sqlite:" + file.getAbsolutePath();

		// create a connection source to our database
		connectionSource = new JdbcConnectionSource(databaseUrl);

		TableUtils.createTableIfNotExists(connectionSource, BancoDadosEntity.class);

	}

}
