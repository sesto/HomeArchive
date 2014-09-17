package be.ordina.sest.homearchive.model;

import org.springframework.web.multipart.MultipartFile;

public class DbDocument {

	private MultipartFile file;

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
}
