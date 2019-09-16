package com.core.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Properties;


public class FileUtil {
	
	
	 /** 
     * 根据byte数组，生成文件 
     */  
    public static String getPath()
    {
    	//windows环境下面的资源文件路径
    	String ccoopfs_windows  ="D:/document";
    	//linux环境下面的资源文件路径
    	String ccoopfs_linux  ="/document";
    
    	if( isWindows() )
    	{
    		return ccoopfs_windows;
    		
    	}
    	
    	return ccoopfs_linux;
    }
    
    public static boolean isWindows() {
        Properties prop = System.getProperties();

        String os = prop.getProperty("os.name");
        if (os != null && os.toLowerCase().contains("windows")) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 创建资源文件
     *
     */
    public static void copyResources( String path)
	{
		InputStream stream = FileUtil.class.getResourceAsStream(path);
		File targetFile = new File(FileUtil.getPath() + path);
		try {
			if(stream !=null)
			FileUtils.copyInputStreamToFile(stream, targetFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    /**
     * 创建目录
     * @param prifix 目录前缀
     * @return
     */
    public static String createRandomDir(String prifix) {
    	
		String filepath = getPath() +prifix +"/"+ SeriesUtils.generateSeries("HT");
		
		File dir = new File(filepath);
        if (dir.exists()) {    
            deleteDirectory(filepath);
        } 
        if (!filepath.endsWith(File.separator)) {
        	filepath = filepath + File.separator;
        }  
        //创建目录  
        if (dir.mkdirs()) {   
            return filepath;  
        } 
        return ""; 
    }  
   
    /** 
     * 删除单个文件 
     * @param   sPath    被删除文件的文件名 
     * @return 单个文件删除成功返回true，否则返回false 
     */  
    public static boolean deleteFile(String sPath) {
       boolean flag = false;  
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除  
        if (file.isFile() && file.exists()) {  
            file.delete();  
            flag = true;  
        }  
        return flag;  
    }  
    /** 
     * 删除目录（文件夹）以及目录下的文件 
     * @param   sPath 被删除目录的文件路径 
     * @return  目录删除成功返回true，否则返回false 
     */  
    public static boolean deleteDirectory(String sPath) {
        //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }  
        File dirFile = new File(sPath);
        //如果dir对应的文件不存在，或者不是一个目录，则退出  
        if (!dirFile.exists() || !dirFile.isDirectory()) {  
            return false;  
        }  
        boolean  flag = true;  
        //删除文件夹下的所有文件(包括子目录)  
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {  
            //删除子文件  
            if (files[i].isFile()) {  
                flag = deleteFile(files[i].getAbsolutePath());  
                if (!flag) break;  
            } //删除子目录  
            else {  
                flag = deleteDirectory(files[i].getAbsolutePath());  
                if (!flag) break;  
            }  
        }  
        if (!flag) return false;  
        //删除当前目录  
        if (dirFile.delete()) {  
            return true;  
        } else {  
            return false;  
        }  
    }  
	 /** 
     * 根据byte数组，生成文件 
     */  
    public static boolean saveFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {  
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在  
                dir.mkdirs();  
            }  
            file = new File(filePath+"/"+fileName);
            //linux 先删除再保存
            if(file.exists())
            {
            	file.delete();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);  
        } catch (Exception e) {
            e.printStackTrace();  
            return false;
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {
                    e1.printStackTrace();  
                    return false;
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {
                    e1.printStackTrace();  
                    return false;
                }  
            }  
        }
        
        return true;
    }
    /**
     * 文件转化成字节数组
     * 
     * @param filePath
     * @return
     */
    public static byte[] File2byte(String filePath)
	{
		byte[] buffer = null;
		try
		{
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1)
			{
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return buffer;
	}

    public static void main(String[] args) throws Exception {
    	
    	String path = FileUtil.getPath();
    	System.out.println("===");
    	
    	FileUtil.saveFile(null, path, "t.xls");
    }

}
