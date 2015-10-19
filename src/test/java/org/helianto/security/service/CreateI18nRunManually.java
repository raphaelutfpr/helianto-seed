package org.helianto.security.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * Serviço para criar e/ou renomear linguagens.
 * 
 * @author Eldevan Nery Junior
 */
public class CreateI18nRunManually {

	private static final String FOLDER_URL = "src/main/resources/assets";
	
	private static final String[] LANGS = new String[]{"pt_BR", "en_US"};

	@Test
	public void create() throws IOException {
		File folder = new File(FOLDER_URL);
		listFilesForFolder(folder);
	}

	public void listFilesForFolder(File folder) throws IOException{
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				createI18N(fileEntry);
			} else {
			}
		}
	}	

	public void createI18N(File folder) throws IOException{
		boolean canCreate = true;
		File i18nFolder = null;
		for (final File fileEntry : folder.listFiles()) {
			if(fileEntry.getName().contains("i18n")){
				i18nFolder = fileEntry;
				canCreate = false;
			}
		}
		if(canCreate){
			i18nFolder = new File(folder, "i18n");
			i18nFolder.mkdir();
		}
		String prefix = FOLDER_URL+"/"+folder.getName()+"/i18n/"+ folder.getName();

		for (String string : LANGS) {
			File oldFile = new File(prefix+"_"+string+".js");
			File newFile = new File(prefix+"_"+string.toLowerCase()+".js");
			createAndDelete(oldFile, newFile);
		}

	}

	public void createAndDelete(File oldFile, File newFile) throws IOException{
		if (oldFile.exists() && !newFile.exists()) {
			renameAndCreate(oldFile, newFile);
		}else if(!oldFile.exists() && !newFile.exists()) {
			FileUtils.writeStringToFile(newFile, getStringToFile());
			newFile.createNewFile();
		}
		if (oldFile.exists()){
			oldFile.delete();
		}
	}

	public boolean renameAndCreate(File oldFile, File newFile) throws IOException{
		oldFile.renameTo(newFile);
		return oldFile.createNewFile();
	}

	public static final String getStringToFile(){
		return "angular.module('app.services').	value('lang', {_getLocalizationKeys: function() {var keys = {};for (var k in this) {keys[k] = k;}return keys;}});";
	}

}
