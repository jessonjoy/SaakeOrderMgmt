/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.saake.invoicer.reports;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataSource;

/**
 *
 * @author jn
 */
public class ByteArrayDataSource implements DataSource{
	private final String m_strName;
	private final String m_strType;

	private byte[] m_baData;
		
	private class MyByteArrayOutputStream extends ByteArrayOutputStream
	{
		MyByteArrayOutputStream(byte[] baData)
		{
			buf = baData;
		}
	}
		
	ByteArrayDataSource(String strName, byte[] baData, String strType)
	{
		m_strType = strType;
		m_baData = baData;
		m_strName = strName;
	}

	ByteArrayDataSource(String strName, String strData, String strType)
	{
		m_strType = strType;
		m_baData = strData.getBytes();
		m_strName = strName;
	}

	/* (non-Javadoc)
	 * @see javax.activation.DataSource#getContentType()
	 */
	public String getContentType()
	{
		return m_strType;
	}

	/* (non-Javadoc)
	 * @see javax.activation.DataSource#getInputStream()
	 */
	public InputStream getInputStream() throws IOException
	{
		return new ByteArrayInputStream(m_baData);
	}

	/* (non-Javadoc)
	 * @see javax.activation.DataSource#getName()
	 */
	public String getName()
	{
		return m_strName;
	}

	/* (non-Javadoc)
	 * @see javax.activation.DataSource#getOutputStream()
	 */
	public OutputStream getOutputStream() throws IOException
	{
		return new MyByteArrayOutputStream(m_baData);
	}
		
}