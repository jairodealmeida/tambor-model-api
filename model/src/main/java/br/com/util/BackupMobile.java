package br.com.util;


import br.com.database.dao.entity.Entity;
import br.com.database.dao.entity.annotation.GPAEntity;
import br.com.database.dao.entity.annotation.GPAField;

@GPAEntity(name = "backupsmobile")
public class BackupMobile extends Entity {

	private static final long serialVersionUID = 1L;

	@GPAField(name = "checked", type = Entity.BOOLEAN)
	private boolean checked;
	
	@GPAField(name = "codigo", type = Entity.LONG)
	private Long codigo;
	
	@GPAField(name = "data", type = Entity.VARCHAR)
	private String data;  
	
	@GPAField(name = "dataenvio", type = Entity.VARCHAR)
	private String dataenvio;
	
	
	@GPAField(name = "usuarioid", type = Entity.LONG)
	private Long usuarioid;
	
	@GPAField(name = "caminhooarquivolocal", type = Entity.VARCHAR)
	private String  caminhooarquivolocal; 
	
	@GPAField(name = "caminhooarquivoservidor", type = Entity.VARCHAR)
	private String  caminhooarquivoservidor;
	
	public BackupMobile(){
		super();
	}
	
	
	public BackupMobile( Long codigo, String data, String dataenvio,
			Long usuarioid, String caminhooarquivolocal, String caminhooarquivoservidor) {
		super();
	
		this.codigo = codigo;
		this.data = data;
		this.dataenvio = dataenvio;
		this.usuarioid = usuarioid;
		this.caminhooarquivolocal = caminhooarquivolocal;
		this.caminhooarquivoservidor = caminhooarquivoservidor;
	}


	

	public Long getCodigo() {
		return codigo;
	}


	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public String getDataenvio() {
		return dataenvio;
	}


	public void setDataenvio(String dataenvio) {
		this.dataenvio = dataenvio;
	}


	

	public Long getUsuarioid() {
		return usuarioid;
	}


	public void setUsuarioid(Long usuarioid) {
		this.usuarioid = usuarioid;
	}


	public String getCaminhooarquivolocal() {
		return caminhooarquivolocal;
	}


	public void setCaminhooarquivolocal(String caminhooarquivolocal) {
		this.caminhooarquivolocal = caminhooarquivolocal;
	}


	public String getCaminhooarquivoservidor() {
		return caminhooarquivoservidor;
	}


	public void setCaminhooarquivoservidor(String caminhooarquivoservidor) {
		this.caminhooarquivoservidor = caminhooarquivoservidor;
	}


	@Override
	public void setId(Long id) {
		codigo = id;
		
	}
	@Override
	public Long getId() {
		return codigo;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
}
