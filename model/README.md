# easy-gpa
Automatically exported from code.google.com/p/easy-gpa

# Easy-GPA-Android
## CRUD API(Create, read, update and delete)
### Google Persistence API

[![Donate To CharityWate Now Help a good cause](https://media1.tenor.com/images/5da95f8d44b36a5b55f2bc91eb0ac767/tenor.gif?itemid=14309950)](https://my.charitywater.org/charisma_on_command/charisma-on-command-fundraiser)
[![Donate To CharityWate Now Help a good cause](https://cdn.streamlabs.com/users/22097922/library/2ecn0nk.gif)](https://my.charitywater.org/charisma_on_command/charisma-on-command-fundraiser)
**CLICK TO DONATE \o/ **

**IRC** #easy-gpa<br> 
**Group** http://groups.google.com/group/easy-gpa<br>
<br>
**Easy-gpa-android user guide** <br>
## How to use?<br>
First of all, <br>
Create a new android project in eclipse and put on classpath the easy-gpa-android.jar <br>
Set the easy-gpa-android.jar to work with sqlite database’s. <br>
To set the database instance to your project using easy-gpa-android.<br>
In easy-gpa-android.jar library has a class named Entity <br>
which you can make your regular beans extends in order to use easy-gpa functionalities.<br>
Create a regular class with a suggestive name representing a database table, <br>
make it extends Entity class (from easy-gpa.jar) and annotate it with annonation. 
```JAVA 
@GPAEntity(name="table_name")
``` 
After that, you have to annotate your class attributes’ getters and setters according to your database table’s fields. <br>
The first thing to consider is the primary key. <br>
Make sure to annotate the table’s id with annotation.
```JAVA 
@GPAPrimaryKey(name="id_name", ignore=true)
```
The other regular fields must be annotated with annotation.
```JAVA 
@GPAField(name="field_name")
``` 
**Example**: The following example was made using this sqlite script: <br>
```SQL
CREATE TABLE [tb_usuario] (
  [id] integer primary key autoincrement NOT NULL, 
  [nome_completo] VARCHAR NOT NULL, 
  [nome_usuario] VARCHAR NOT NULL, 
  [senha] VARCHAR NOT NULL);
```
After this, a class with the table characterizes was created, extending Entity class to use it’s methods.<br>
The following example shows how the class Usuario (user) implements the easy-gpa-android functionalities.:<br>


```JAVA
import br.com.slv.database.dao.entity.Entity;
import br.com.slv.database.dao.entity.annotation.GPAEntity;
import br.com.slv.database.dao.entity.annotation.GPAField;
import br.com.slv.database.dao.entity.annotation.GPAPrimaryKey;

@GPAEntity(name="tb_usuario")
public class Usuario extends Entity{
	@GPAPrimaryKey(name="id", ignore=true)
	private int id;
	@GPAField(name="nome_completo")
	private String nomeCompleto;
	@GPAField(name="nome_usuario")
	private String nomeUsuario;
	@GPAField(name="senha")
	private String senha;

	/**
	 * Used by insert without id, database are responsible 
	 * to generate a sequential id to primary key 
	 * @param nomeCompleto
	 * @param nomeUsuario
	 * @param senhaCharacter
	 * @param idDevice
	 */
	public Usuario(String nomeCompleto, String nomeUsuario,
			String senhaCharacter) {
		super();
		this.nomeCompleto = nomeCompleto;
		this.nomeUsuario = nomeUsuario;
		this.senha = senhaCharacter;
	}
	/**
	 * in this case the entity are instantied with the primary key information
	 * @param id
	 * @param nomeCompleto
	 * @param nomeUsuario
	 * @param senhaCharacter
	 * @param idDevice
	 */
	public Usuario(int id, String nomeCompleto, String nomeUsuario,
			String senhaCharacter) {
		super();
		this.id = id;
		this.nomeCompleto = nomeCompleto;
		this.nomeUsuario = nomeUsuario;
		this.senha = senhaCharacter;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomeCompleto() {
		return nomeCompleto;
	}
	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}
	public String getNomeUsuario() {
		return nomeUsuario;
	}
	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
}

```
With this entity created, you can try this android test code 
```JAVA
import java.util.List;
import android.test.ActivityInstrumentationTestCase2;
import br.com.jro.gpa.MainActivity;
import br.com.slv.database.Repositorio;
import br.com.slv.database.dao.entity.Entity;
import br.com.slv.database.dao.model.TransferObject;

public class EntityTest extends
ActivityInstrumentationTestCase2<MainActivity>{
	
	private MainActivity mActivity;

	public EntityTest() {
		super("br.com.jro.gpa", MainActivity.class);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();	
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
			}
		});

		getInstrumentation().waitForIdleSync();
	}
	public void testCreateScriptAccept1(){
		try {
			Entity e = new Usuario(3,"test","test","test", 1);
			Repositorio repository = new Repositorio(mActivity, Usuario.class,"teste",1);
			String result = repository.createScript();
			assertNotNull(result);
			assertEquals("create table tb_usuario" +
					"(senha text,nome_usuario text," +
					"nome_completo text," +
					"id_device integer," +
					"id integer primary key autoincrement );", result);			
		} catch (Exception e) {
			fail("not pass here " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	public void testDeleteScriptAccept1(){
		try {
			Entity e = new Usuario(3,"test","test","test", 1);
			Repositorio repository = new Repositorio(mActivity, Usuario.class,"teste",1);
			String result = repository.deleteScript();
			assertNotNull(result);
			assertEquals("drop table if exists tb_usuario;", result);			
		} catch (Exception e) {
			fail("not pass here " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	public void testInsertAccept1(){
		try {
			Entity entity = new Usuario(3,"test","test","test", 1);
			Repositorio repository = new Repositorio(mActivity, Usuario.class,"teste",1);
			long result = repository.insert(entity);
			assertTrue(result>0);
		} catch (Exception e) {
			fail("not pass here " + e.getLocalizedMessage());
			e.printStackTrace();
		}

	}
	
	public void testUpdateAccept1(){
		try {
			Repositorio repository = new Repositorio(mActivity, Usuario.class,"teste",1);
			TransferObject to = repository.selectMax("TB_USUARIO", "id");
			Integer maxId = to.getInteger("max");
			Entity e = new Usuario(maxId, "test", "test", "test", 1);
			long result = repository.update(e);
			assertNotNull(result);
			assertEquals(1, result);
		} catch (Exception e) {
			fail("not pass here " + e.getLocalizedMessage());
			e.printStackTrace();
		}

	}
	
	public void testDeleteAccept1(){
		try {
			Repositorio repository = new Repositorio(mActivity, Usuario.class,"teste",1);
			TransferObject to = repository.selectMax("TB_USUARIO", "id");
			Integer maxId = to.getInteger("max");
			Entity e = new Usuario( maxId, "test", "test", "test", 1);
			long result = repository.delete(e);
			assertNotNull(result);
			assertEquals(1, result);
		} catch (Exception e) {
			fail("not pass here " + e.getLocalizedMessage());
			e.printStackTrace();
		}

	}
	
	public void testSelectAccept1(){
		try {
			Repositorio repository = new Repositorio(mActivity, Usuario.class,"teste",1);
			List<TransferObject> list = repository.select("TB_USUARIO", "nome_completo = 'test'");
			assertNotNull(list);
			assertTrue(list.size()>0);
		} catch (Exception e) {
			fail("not pass here " + e.getLocalizedMessage());
			e.printStackTrace();
		}

	}	
}
```
By doing these tests, you will be creating an Entity instance to transact information from/to the configured database, <br>
using a regular DBMS operations (insert update and delete).<br>
This is an easy, simple and elegant form to manipulate/transact statements. No more hard DAO implementations. <br>

## Youtube Code History

[![History of coding of EASY-GPA API - Credits the Gource AROUND incredible functionality, thanks](https://img.youtube.com/vi/_lfHKMuI1JI/0.jpg)](https://www.youtube.com/watch?v=_lfHKMuI1JI)

## Youtube Playlist
[https://www.youtube.com/playlist?list=PLLAE7O8grxj-V3W1D284SG1WNc99fgxH2]


## Old Versions, see to:
[http://code.google.com/p/easy-gpa-spatial/]

## Guide authors: <br>
**Jairo de Almeida Since: v2 - 02/08/2011** <br>
**Willian da Costa Chimura : v3 - 06/09/2011** <br>
**Jairo de Almeida Since: v4 - 17/11/2011** <br>
**Jairo de Almeida Since: v5 - 17/06/2014** <br>
