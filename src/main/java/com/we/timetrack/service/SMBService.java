package com.we.timetrack.service;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

@Service
public class SMBService {

	private final static Logger logger = LoggerFactory.getLogger(SMBService.class);

	@Value("${ldap.user}")
	private String ldapUser;
	@Value("${ldap.password}")
	private String ldapPassword;
	@Value("${ldap.domain}")
	private String ldapDomain;
	@Value("${export.path}")
	private String exportPath;

	public OutputStream getOutputStream(String filename) throws SmbException, MalformedURLException, UnknownHostException, IllegalStateException, UnsupportedEncodingException {
		// Create authentication object
        NtlmPasswordAuthentication auth =
          new NtlmPasswordAuthentication(ldapDomain, ldapUser, ldapPassword);
        
        // Resolve base dir
        String path = new String(exportPath.getBytes("ISO-8859-1"), "UTF-8") + filename;
        SmbFile file = new SmbFile(path);
        SmbFile dir = new SmbFile(file.getParent(), auth);
        if (!dir.exists()) {
        	logger.debug("Try to create dir '{}'", dir.getPath());
        	dir.mkdirs();
        }
        // Create outputstream
        logger.debug("Writing file '{}'", path.toString());
        SmbFileOutputStream outputStream = new SmbFileOutputStream(
          new SmbFile(path.toString(), auth));
        return outputStream;
	}
	
	public String getPath() throws UnsupportedEncodingException {
		return new String(exportPath.getBytes("ISO-8859-1"), "UTF-8");
	}
}
