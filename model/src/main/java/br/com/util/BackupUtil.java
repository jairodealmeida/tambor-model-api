package br.com.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;

import br.com.database.dao.entity.Entity;


/**
 * @class BackupUtil Classe utilitaria para backup do banco sqlite embarcado em memoria
 * No maximo serao permitido no numero, NUM_BACKUPS no sdcard
 * @author Jairo
 * 
 */
public class BackupUtil {

	public static final String BACKUP_DATE_FORMAT = "yyyyMMdd_HHmmss_SSS";
	public static final int NUM_BACKUPS = 5;

	/**
	 * Metodo que realiza a limpeza do diretorio onde esta a base de dados
	 * utilizada pela aplicacao. Verifica o nome do banco utilizado pela
	 * aplicacao.
	 */
	public static void clearDiretoryDatabase(Context context, String databaseName) {

		File directory = getExternalStoragePublicDirectory(context);
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.getName().startsWith(databaseName)) {
					file.delete();
				}
			}
		}
	}
	public static int countDiretoryDatabase(Context context, String databaseName) {
		int count = 0;
		File directory = getExternalStoragePublicDirectory(context);
		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.getName().startsWith(databaseName)) {
					count = count + 1;
				}
			}
		}
		return count;
	}
	public static void executeBackupDoBanco(Context context, String databaseName) {
		try {
			String arquivoBackup = getOutputBackupFile(context,databaseName);
			Log.i(context, "Processando arquivo de backup ..: " + arquivoBackup);
			executeBackupDoBanco(arquivoBackup,context,databaseName);
			int count = countDiretoryDatabase(context, databaseName);
			Log.i(context, count +" ..: Arquivos de backup gerados.");
			if(count>NUM_BACKUPS){
				Log.i(context, " Limit de "+NUM_BACKUPS+"arquivos atingido.");
				removeOldDatabase(context,databaseName);	
			}
			Log.i(context, "Processo de backup finalizado com sucesso.");
		} catch (Exception e) {
			Log.e(context, e.getLocalizedMessage(),e);
		}
	}
	
	public static File getExternalStoragePublicDirectory(Context context) {
		File externamStorageDir = Environment
				.getExternalStorageDirectory();
		File directory = new File(externamStorageDir, context.getPackageName());
		return directory;
	}
	public static Date getBackupDateFromFile(File file, String databaseName) throws ParseException{
		String subDate = file.getName().replace(databaseName, "");
		Date timeStamp = new SimpleDateFormat(BACKUP_DATE_FORMAT, Locale.UK).parse(subDate);
		return timeStamp;
	}
	/**
	 * Metodo que realiza a limpeza do diretorio onde esta a base de dados
	 * utilizada pela aplicacao. Verifica o nome do banco utilizado pela
	 * aplicacao.
	 */
	public static void removeOldDatabase(Context context, String databaseName) {
		try {
			File oldFile = null;
			Date timeStampAux = null; 
			File directory = getExternalStoragePublicDirectory(context);
			File[] files = directory.listFiles();
			
			if (files != null) {
				for (File file : files) {
					if (file.getName().startsWith(databaseName)) {
						Date timeStamp = getBackupDateFromFile(file,databaseName);
						if(timeStampAux==null){
							timeStampAux = timeStamp;
							oldFile = file;
						}else{
							if(timeStamp==null) {
								throw new NullPointerException("Arquivo backup invalido");
							}
							if(timeStamp.before(timeStampAux)){
								timeStampAux = timeStamp;
								oldFile = file;
							}else{
								continue;
							}
						}
					}
				}
			}
			if(oldFile!=null){
				oldFile.delete();
				Log.i(context, "Backup antigo ..: " + oldFile.getAbsolutePath() + " removido com sucesso!");
			}			
		} catch (Exception e) {
			Log.e(e.getLocalizedMessage());
		}
	}

	/**
	 * Metodo que gera o nome do arquivo backup
	 * 
	 * @param context
	 * @return
	 */
	private static String getOutputBackupFile(Context context,String databaseName) {
		File directory = getExternalStoragePublicDirectory(context);
		if (!directory.exists()) {
			if (!directory.mkdirs()) {
				Log.e("Failed to create storage directory.");
				return null;
			}
		}
		String timeStamp = new SimpleDateFormat(BACKUP_DATE_FORMAT, Locale.UK)
				.format(new Date());
		return directory.getPath() + File.separator + databaseName
				+ timeStamp;
	}
	/**
	 * Pega caminho do banco de dados sqlite embarcado
	 * @param context
	 * @return
	 */
	private static File getAndroidDatabaseFile( Context context,String databaseName){
		
		String baseDiretorio = "/data/data/" + context.getPackageName() + "/databases";
		if (new File(baseDiretorio).exists()) {
			String databaseNamePth = baseDiretorio + "/"
					+ databaseName;
			File f1 = context.getDatabasePath(databaseNamePth);
			return f1;
		}else{
			Log.e("Diretorio " + baseDiretorio	+ " de banco de dados nao encontrado!");
			return null;
		}
	}
	/**
	 * Executa o procedimento de backup do banco embarcado, no sdcard 
	 * @param filePath
	 * @param context
	 * @throws IOException
	 */
	private static void executeBackupDoBanco(String filePath, Context context,String databaseName) throws IOException {
		File arquivoBanco = getAndroidDatabaseFile(context,databaseName);
			if (arquivoBanco.exists()) {
				InputStream arquivoLeitura = null;
				OutputStream arquivoEscrita = null;
				try {
					arquivoLeitura = new FileInputStream(arquivoBanco);
					arquivoEscrita = new FileOutputStream(new File(filePath));
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = arquivoLeitura.read(buffer)) > 0) {
						arquivoEscrita.write(buffer, 0, len);
					}
					Log.i("Arquivo de backup do banco " + filePath	+ " gravado corretamente!");
				} catch (FileNotFoundException ex) {
					Log.e(ex.getLocalizedMessage());
				} catch (IOException e) {
					Log.e(e.getLocalizedMessage());
				} finally {
					if (arquivoLeitura != null) {
						arquivoLeitura.close();
					}
					if (arquivoEscrita != null) {
						arquivoEscrita.close();
					}
				}
			} else {
				Log.e("Arquivo " + arquivoBanco + " de banco de dados nao encontrado!");
			}
	
	}
	public static void sintetizarPojos(List<BackupItem> backupitens, List<Entity> sintetizados, Long codigo, String databaseName){
		for (BackupItem backupiten : backupitens) {
			sintetizados.add( backupiten.sintetizarPojo(codigo, databaseName) );
		}
	}
	//public static void sintetizarItens(List<Entity> backupObjs,List<BackupItem> sintetizados){
	//	for (Entity backupiten : backupObjs) {
	//		BackupMobile b = ((BackupMobile)backupiten);
			//BackupItem item = new BackupItem();
			//item.se
			//sintetizados.add( backupiten.sintetizarPojo() );
			
			//BackupMobile sintese = new BackupMobile(
			//		Entity.getIdRandomico(),
			//		(this.getDataBackupBancoEmbarcado()!=null)?new Long( this.getDataBackupBancoEmbarcado().getTime()):null,
			//		new Long(new Date(System.currentTimeMillis()).getTime()),
			//		(EntrarSessaoBackgroundTask.getLogin()!=null)?EntrarSessaoBackgroundTask.getLogin().getUsuariocodigo():null,
			//		(this.getBackupBancoEmbarcado()!=null)?this.getBackupBancoEmbarcado().getAbsolutePath():null,
			//		(this.getBackupBancoServidorPath()!=null)?this.getBackupBancoServidorPath():null
			//		);
			//return sintese;
			
			
	//	}
	//}
	public static void getSortedBackupItensByDate(List<BackupItem> itens, Context context, final String databaseName) {
		File directory = getExternalStoragePublicDirectory(context);
		File[] files = directory.listFiles();
		//List<BackupItem> itens = new ArrayList<BackupItem>();
		if (files != null) {
			for (File file : files) {
				if (file.getName().startsWith(databaseName)) {
					itens.add(new BackupItem( file ));
				}
			}
		}
		Collections.sort(itens, new Comparator<BackupItem>(){
	           public int compare (BackupItem m1, BackupItem m2){
	               return m1.getDataBackupBancoEmbarcado(databaseName).compareTo(m2.getDataBackupBancoEmbarcado(databaseName));
	           }
        });
	}
	public static File getLastBackupFile(Context context,String databaseName) {
		File directory = getExternalStoragePublicDirectory(context);
		File[] files = directory.listFiles();
		if(files!=null && files.length>0){
			double dateAux = 0.0;
			File fileAux = null;
			for (File file : files) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Log.i("Backup date : " + sdf.format(file.lastModified()));
				if(file.getName().startsWith(databaseName) && file.lastModified()>dateAux){
					dateAux =  file.lastModified();
					fileAux = file; 
				}
			}
			return fileAux;
		}else{
			return null;
		}
		
		
	}
	
	/*
	public static Date getDataBackupBancoEmbarcado(BackupItem item) {
		try {
			return BackupUtil.getBackupDateFromFile(item.getBackupBancoEmbarcado());
		} catch (Exception e) {
			Log.e("ERROR", e.getLocalizedMessage());
		}
		return null;
	}
	public static Date getDataBackupBancoEmbarcado(BackupMobile item) {
		try {
			return BackupUtil.getBackupDateFromFile(new File(item.getBackupmobilecaminhooarquivolocal()));
		} catch (Exception e) {
			Log.e("ERROR", e.getLocalizedMessage());
		}
		return null;
	}*/
	
}
