package eu.trentorise.smartcampus.bikesharing.feedback;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
		String name = UUID.randomUUID().toString();
		try
		{
			BufferedOutputStream outFile = new BufferedOutputStream(new FileOutputStream(new File(path + name + "." + format)));
			outFile.write(file);
			outFile.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return name;
	}
}
