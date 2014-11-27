package com.rest.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.rest.model.Track;

public class TrackDAO {
	private static final String tableName = "track";
	private static final String INSERT_TRACK = "INSERT INTO " + tableName
			+ " (trackid,albumid,artistid,genreid) values (?,?,?,?)";
	private static final String GET_ALL_TRACK= "select trackid,albumid,artistid,genreid From " + tableName;
	private static final String File_Path = "C:/euphonyDataSet/track1/track1.txt";

	public void insertTrack() {
		Connection conn = null;
		try {

			conn = DBOperation.getConnection();
			BufferedReader br = new BufferedReader(new FileReader(File_Path));
			String line;

			while ((line = br.readLine()) != null) {
				StringBuffer sb = new StringBuffer();
				String[] values = line.split("\\|", -1);

				for (int i = 3; i < values.length; i++) {
					sb.append(values[i]);
					if (i < (values.length - 1))
						sb.append(",");
				}

				PreparedStatement prepStmt = conn
						.prepareStatement(INSERT_TRACK);

				prepStmt.setInt(1, Integer.valueOf(values[0]));
				prepStmt.setString(2, values[1]);
				prepStmt.setString(3, values[2]);
				if (sb.toString().equalsIgnoreCase(" ")) {
					prepStmt.setNull(4, java.sql.Types.NULL);
				} else {
					prepStmt.setString(4, sb.toString());
				}
				prepStmt.executeUpdate();

			}
			br.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<Track> getAllTrack() {
		List<Track> trackList = new ArrayList<Track>();
		Connection conn = null;
		try {
			//
			conn = DBOperation.getConnection();
			PreparedStatement prepStmt = conn.prepareStatement(GET_ALL_TRACK);
			ResultSet rs = prepStmt.executeQuery();
			while (rs.next()) {
				trackList.add(setTrackBeanValues(rs));
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return trackList;
	}

	public Track setTrackBeanValues(ResultSet rs) {
		try {
			if (rs.next()) {
				Track track = new Track();
				track.setTrackid(rs.getInt(1));
				track.setAlbumid(rs.getInt(2));
				track.setArtist(rs.getInt(3));
				track.setGenre(rs.getString(4));
				return track;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String args[]) throws IOException {
		TrackDAO trackDAO = new TrackDAO();
		trackDAO.insertTrack();
	}

}
