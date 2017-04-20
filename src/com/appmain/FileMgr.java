package com.appmain;

import java.io.File;

import com.tools.FileHelper;

public class FileMgr {
	/*
	 * ��ȡdata�ļ��е�File����
	 */
	public static File getDataDir(){
		return FileHelper.getDir("data");
	}

	/*
	 * ��ȡtasks.json��File����
	 */
	public static File getTaskAllJsonFile(){
		return FileHelper.getFile(getDataDir(),"TaskAll.json");
	}

	/*
	 * ��ȡdata/taskĿ¼��File����
	 */
	public static File getTaskDir(){
		return FileHelper.getDir(getDataDir(),"task");
	}

	/*
	 * ��ȡconfig�ļ���
	 */
	public static File getConfigDir(){
		return FileHelper.getDir("config");
	}
	/*
	 * ��ȡ�����ļ�
	 */
	public static File getConfigFile(){
		return FileHelper.getFile(getConfigDir(),"app.conf");
	}
}
