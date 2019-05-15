/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubn.zipdownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import oracle.jdbc.internal.OracleTypes;

/**
 *
 * @author aojinadu
 */
public class FileDownloader extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		doProcess("HMO1", "5429324");
		//String Rowref=pageContext.getParameter(EVENT_SOURCE_ROW_REFERENCE);
		//OtherReportRequestVORowImpl row=(OtherReportRequestVORowImpl)am.findRowByRef(Rowref);
	}

	private void doProcess(String saveName, String Staffid) {
		System.out.println("Started service.....");
		Connection con = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		String MimeType = null, FileName = null;
		try {
			File directory = new File("C:\\HMOFiles\\");
			if (!directory.exists()) {
				directory.mkdir();
			}
			File f = new File("C:\\HMOFiles\\" + saveName + ".zip");
//			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(f));
//			String Query = "SELECT 'image/jpeg' mime_type, image_file, staff_id||'_'||id||'_'||COALESCE(first_name,middle_name,last_name)||'.jpg' file_name"
//					+ " FROM xxubn_hmo_dependants"
//					+ " WHERE staff_id = '5429028' AND status = 'A'";
			Class.forName("oracle.jdbc.driver.OracleDriver");
			con = DriverManager.getConnection("jdbc:oracle:thin:@//10.8.64.117:1521/EBSOCT1", "apps", "apps123");
			cs = con.prepareCall("{call HMOFILEDOWNLOAD.GETHMODETAILS(?,?,?)}");
			cs.setString(1, Staffid);
			cs.registerOutParameter(2, OracleTypes.CURSOR);
			cs.registerOutParameter(3, OracleTypes.VARCHAR);

			if (cs.executeUpdate() != Statement.EXECUTE_FAILED) {

				String response = (String) cs.getString(3);
				if (response == null) {
					rs = (ResultSet) cs.getObject(2);
					while (rs.next()) {
						System.out.println("Writing to file " + directory.getAbsolutePath());

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

//			while (rs.next()) {
//				InputStream is = rs.getBinaryStream(1);
//				java.sql.Blob blob;
//				blob = rs.getBlob(2);
//				int iLength = (int) blob.length();
//				byte[] blobAsBytes = blob.getBytes(1, iLength);
//				File file = new File("C:\\HMOFiles\\"+rs.getString(3));
//				ZipEntry e = new ZipEntry(rs.getString(3));
//				out.putNextEntry(e);
//				out.write(blobAsBytes, 0, iLength);
//
//				out.closeEntry();
//			}
//			out.close();
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

	public static void main(String[] args) {
		FileDownloader a = new FileDownloader();
		System.out.println("What is wrong here???");
		a.doProcess("HMO1", "5429324");
//		a.doProc();
	}

	// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
