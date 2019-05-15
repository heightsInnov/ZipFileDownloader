/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ubn.zipdownloader.model;

import oracle.sql.BLOB;


/**
 *
 * @author aojinadu
 */
public class imageModel {
	private String file_name;
	private String mime_type;
	private BLOB image_file;

	public String getFile_name() {
		return file_name;
	}

	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}

	public String getMime_type() {
		return mime_type;
	}

	public void setMime_type(String mime_type) {
		this.mime_type = mime_type;
	}

	public BLOB getImage_file() {
		return image_file;
	}

	public void setImage_file(BLOB image_file) {
		this.image_file = image_file;
	}
}
