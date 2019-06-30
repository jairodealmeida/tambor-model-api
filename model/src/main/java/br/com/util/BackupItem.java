package br.com.util;

import java.io.File;
import java.util.Date;

import br.com.database.dao.entity.Entity;


public class BackupItem {
	private boolean checked;
	private File backupBancoEmbarcado;
	private File backupBancoServidor;
	private String backupBancoServidorPath;
	public BackupItem(File file){
		this.backupBancoEmbarcado = file;
	}
	public BackupItem(){
		
	}
	public String getFileNameBackupBancoEmbarcado() {
		return (backupBancoEmbarcado!=null)?backupBancoEmbarcado.getName():null;
	}
	
	public Date getDataBackupBancoEmbarcado(String databaseName) {
		try {
			return BackupUtil.getBackupDateFromFile(backupBancoEmbarcado,databaseName);
		} catch (Exception e) {
			Log.e(e.getLocalizedMessage());
		}
		return null;
	}
	public File getBackupBancoEmbarcado() {
		return backupBancoEmbarcado;
	}
	public File getBackupBancoServidor() {
		return backupBancoServidor;
	}
	public void setBackupBancoServidor(File backupBancoServidor) {
		this.backupBancoServidor = backupBancoServidor;
	}
	

	public BackupMobile sintetizarPojo(Long codigo, String databaseName){
		Long backupCodigo = Entity.getIdRandomico();
		
		BackupMobile sintese = new BackupMobile(
				backupCodigo,
				(this.getDataBackupBancoEmbarcado(databaseName)!=null)?DateUtil.parseString(this.getDataBackupBancoEmbarcado(databaseName),DateUtil.DD_MM_YYYY_HH_MM_SS):null,
				DateUtil.parseString(new Date(System.currentTimeMillis()),DateUtil.DD_MM_YYYY_HH_MM_SS),
				codigo,
				(this.getBackupBancoEmbarcado()!=null)?this.getBackupBancoEmbarcado().getAbsolutePath():null,
				(this.getBackupBancoServidorPath()!=null)?this.getBackupBancoServidorPath():null);
		return sintese;
		
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public String getBackupBancoServidorPath() {
		return backupBancoServidorPath;
	}
	public void setBackupBancoServidorPath(String backupBancoServidorPath) {
		this.backupBancoServidorPath = backupBancoServidorPath;
	}
}