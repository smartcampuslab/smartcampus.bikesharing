package eu.trentorise.smartcampus.bikesharing.feedback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FeedbackFileManager
{
	@Value("${filesPath}")
	private String path;
	
	@Value("${filesFormat}")
	private String format;
	
	public FeedbackFileManager()
	{
		super();
	}
	
	public String storeNewFile(byte[] file)
	{
		String name = "";
		if(file != null)
		{
			name = path + UUID.randomUUID().toString() + "." + format;
			
			try
			{
				BufferedOutputStream outFile = new BufferedOutputStream(new FileOutputStream(new File(name)));
				outFile.write(file);
				outFile.close();
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
				name = "";
			}
		}
		return name;
	}
}
