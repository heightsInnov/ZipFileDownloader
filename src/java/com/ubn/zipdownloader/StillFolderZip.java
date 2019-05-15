/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubn.zipdownloader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import oracle.jdbc.internal.OracleTypes;

/**
 *
 * @author aojinadu
 */
public class StillFolderZip {

	private List<String> fileList;
	private static String OUTPUT_ZIP_FILE = "";
	private static String SOURCE_FOLDER = ""; // SourceFolder path

	public StillFolderZip() {
		fileList = new ArrayList< String>();
	}

	public File createFolder(String staffid) {
		Connection con = null;
		CallableStatement cs = null;
		File directory = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@//10.8.64.117:1521/EBSOCT1", "apps", "apps123");
			cs = con.prepareCall("{call HMOFILEDOWNLOAD.GETPATH(?,?)}");
			cs.setString(1, staffid);
			cs.registerOutParameter(2, OracleTypes.VARCHAR);

			if (cs.executeUpdate() != Statement.EXECUTE_FAILED) {
				String path = (String) cs.getString(2);
				if (!path.equals("NONE")) {
					directory = new File("C:\\" + path + "\\");
					if (!directory.exists()) {
						directory.mkdir();
						System.out.println(directory.getAbsolutePath());
					}
				} else {
					throw new RuntimeException("Staff or path not found!");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException ex) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ex) {
				}
			}
		}
		return directory;
	}

	public void populateFolder(String staffid, String hmoName) {
		System.out.println("Started service.....");
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		try {
			File directory = createFolder(staffid);
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@//10.8.64.117:1521/EBSOCT1", "apps", "apps123");
			cs = con.prepareCall("{call HMOFILEDOWNLOAD.GETHMODETAILS(?,?,?)}");
			cs.setString(1, staffid);
			cs.registerOutParameter(2, OracleTypes.CURSOR);
			cs.registerOutParameter(3, OracleTypes.VARCHAR);

			if (cs.executeUpdate() != Statement.EXECUTE_FAILED) {

				String response = (String) cs.getString(3);
				if (response == null) {
					rs = (ResultSet) cs.getObject(2);
					while (rs.next()) {
						System.out.println("Writing to file " + directory.getAbsolutePath());
						System.out.println("Root of Path [" + directory.getPath() + "]");

						if (rs.getBinaryStream("STAFF_IMAGE") != null) {
							FileOutputStream output = new FileOutputStream(directory + "/" + rs.getString("IMAGE_FILE_NAME"));
							InputStream staff = rs.getBinaryStream("STAFF_IMAGE");
							byte[] buffer = new byte[1024];
							while (staff.read(buffer) > 0) {
								output.write(buffer);
							}
						}

						if (rs.getBinaryStream("SPOUSE_IMAGE") != null) {
							FileOutputStream output = new FileOutputStream(directory + "/" + rs.getString("SPOUSE_FILE_NAME"));
							InputStream spouse = rs.getBinaryStream("SPOUSE_IMAGE");
							byte[] buffer1 = new byte[1024];
							while (spouse.read(buffer1) > 0) {
								output.write(buffer1);
							}
						}

						if (rs.getBinaryStream("DEP1_IMAGE") != null) {
							FileOutputStream output = new FileOutputStream(directory + "/" + rs.getString("DEP1_FILE_NAME"));
							InputStream dep1 = rs.getBinaryStream("DEP1_IMAGE");
							byte[] buffer2 = new byte[1024];
							while (dep1.read(buffer2) > 0) {
								output.write(buffer2);
							}
						}

						if (rs.getBinaryStream("DEP2_IMAGE") != null) {
							FileOutputStream output = new FileOutputStream(directory + "/" + rs.getString("DEP2_FILE_NAME"));
							InputStream dep2 = rs.getBinaryStream("DEP2_IMAGE");
							byte[] buffer3 = new byte[1024];
							while (dep2.read(buffer3) > 0) {
								output.write(buffer3);
							}
						}

						if (rs.getBinaryStream("DEP3_IMAGE") != null) {
							FileOutputStream output = new FileOutputStream(directory + "/" + rs.getString("DEP3_FILE_NAME"));
							InputStream dep3 = rs.getBinaryStream("DEP3_IMAGE");
							byte[] buffer4 = new byte[1024];
							while (dep3.read(buffer4) > 0) {
								output.write(buffer4);
							}
						}
					}
				}
			}
			String str = directory.getPath();
			int index = str.lastIndexOf("\\");
			this.SOURCE_FOLDER = str.substring(0, index);
			this.OUTPUT_ZIP_FILE = "C:\\HMOFiles\\" + hmoName + ".zip";
			System.out.println("This trial gives >> " + str.substring(0, index));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException ex) {
				}
			}
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException ex) {
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException ex) {
				}
			}
		}
	}

	public void zipIt(String zipFile) {
		byte[] buffer = new byte[1024];
		String source = new File(SOURCE_FOLDER).getName();
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + zipFile);
			FileInputStream in = null;

			for (String file : this.fileList) {
				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(source + File.separator + file);
				zos.putNextEntry(ze);
				try {
					in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} finally {
					in.close();
				}
			}

			zos.closeEntry();
			System.out.println("Folder successfully compressed");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void generateFileList(File node) {
		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.toString()));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
	}

	private String generateZipEntry(String file) {
		return file.substring(SOURCE_FOLDER.length() + 1, file.length());
	}

	public static void main(String[] args) {
		StillFolderZip appZip = new StillFolderZip();
		appZip.populateFolder("5429772", "RODING");
		appZip.generateFileList(new File(SOURCE_FOLDER));
		appZip.zipIt(OUTPUT_ZIP_FILE);
	}
//	public static void main(String[] args) {
//		StillFolderZip a = new StillFolderZip();
//		System.out.println("What is wrong here???");
//		a.createFolder("5429324");
//		a.populateFolder("5429324");
//		a.doProc();
//	}
}
