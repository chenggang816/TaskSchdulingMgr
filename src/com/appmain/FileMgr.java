package com.appmain;

import java.io.File;

import com.tools.FileHelper;

public class FileMgr {
	/*
	 * 获取data文件夹的File对象
	 */
	public static File getDataDir(){
		return FileHelper.getDir("data");
	}

	/*
	 * 获取tasks.json的File对象
	 */
	public static File getTaskAllJsonFile(){
		return FileHelper.getFile(getDataDir(),"TaskAll.json");
	}

	/*
	 * 获取data/task目录的File对象
	 */
	public static File getTaskDir(){
		return FileHelper.getDir(getDataDir(),"task");
	}

	/*
	 * 获取config文件夹
	 */
	public static File getConfigDir(){
		return FileHelper.getDir("config");
	}
	/*
	 * 获取配置文件
	 */
	public static File getConfigFile(){
		return FileHelper.getFile(getConfigDir(),"app.conf");
	}
}
