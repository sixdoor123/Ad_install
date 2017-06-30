package com.baiyi.install;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BaseHttp
{
	public ExecutorService executorService = null;
	//request type
	public static final String Methed_Post = "POST";
	public static final String Methed_Get = "GET";

	//Content-DataType
	public static final String Content_Form = "application/x-www-form-urlencoded";
	public static final String Application_Json = "application/json";
	public static final String Application_Json_UTF8 = "application/json;charset=UTF-8";
	public static final String Octet_Stream = "application/octet-stream";
	public static final String Multipart_Form_Data ="multipart/form-data";

	public static String Charset = "UTF-8";
	//body data
	private String BodyData;
	//riquest mothed
	private String mothed;
	//url
	private String url;
	//Content_type
	private String contentType;

	private String charset;


	public BaseHttp()
	{
		if (executorService == null)
		{
			executorService = Executors.newFixedThreadPool(3); // 固定三个线程来执行任务
		}
	}


	public String getBodyData()
	{
		return BodyData;
	}

	public void setBodyData(String bodyData)
	{
		BodyData = bodyData;
	}

	public String getMothed()
	{
		return mothed;
	}

	public void setMothed(String mothed)
	{
		this.mothed = mothed;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getContentType()
	{
		return contentType;
	}

	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

}
